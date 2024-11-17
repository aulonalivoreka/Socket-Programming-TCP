import java.io.*;
import java.net.*;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Server {

    private static List<ClientHandler> connections = new ArrayList<>();
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private static Key aesKey;
    private static final String folderPath = "shared_folder";
    private static String fullPermissionIpAddress;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter IP Address:");
            String ipAddress = scanner.nextLine();
            System.out.println("Enter Port Number:");
            int port = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter the IP address of the client that will have full permissions:");
            fullPermissionIpAddress = scanner.nextLine();

            aesKey = generateAESKey();

            ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ipAddress));
            System.out.println("Server started on IP " + ipAddress + " and port " + port);

            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, aesKey, folderPath, clientSocket.getInetAddress().getHostAddress().equals(fullPermissionIpAddress));
                connections.add(clientHandler);
                pool.execute(clientHandler);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Key generateAESKey() {
        byte[] key = "1234567890123456".getBytes();
        return new SecretKeySpec(key, "AES");
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Key aesKey;
    private String folderPath;
    private DataInputStream input;
    private DataOutputStream output;
    private boolean hasFullPermissions;
    private String clientName;

    public ClientHandler(Socket socket, Key aesKey, String folderPath, boolean hasFullPermissions) {
        this.clientSocket = socket;
        this.aesKey = aesKey;
        this.folderPath = folderPath;
        this.hasFullPermissions = hasFullPermissions;
    }

    @Override
    public void run() {
        try {
            input = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());

            output.writeBoolean(hasFullPermissions);

            String encryptedName = input.readUTF();
            clientName = decrypt(encryptedName, aesKey);
            System.out.println(clientName + " connected from " + clientSocket.getRemoteSocketAddress());

            String encryptedMessage;
            while ((encryptedMessage = input.readUTF()) != null) {
                String message = decrypt(encryptedMessage, aesKey);
                System.out.println(clientName + ": " + message);

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println(clientName + " disconnected.");
                    break;
                } else if (message.equalsIgnoreCase("list_files")) {
                    listFiles();
                } else if (message.startsWith("read_file")) {
                    readFile(message.substring(10));
                } else if (message.startsWith("write_file ")) {
                    if (hasFullPermissions) {
                        writeFile(message.substring(11).trim());
                    } else {
                        System.out.println("Unauthorized write attempt by " + clientName);
                        sendMessage("Error: You do not have write permissions");
                    }
                } else if (message.startsWith("execute ")) {
                    String filename = message.substring(8).trim();
                    if (hasFullPermissions) {
                        executeFile(filename);
                    } else {
                        System.out.println("Unauthorized execute attempt by " + clientName);
                        sendMessage("Error: You do not have execute permissions");
                    }
                } else if (message.startsWith("delete_file ")) {
                    String filename = message.substring(12).trim();
                    if (hasFullPermissions) {
                        deleteFile(filename);
                    } else {
                        System.out.println("Unauthorized delete attempt by " + clientName + " for file: " + filename);
                        sendMessage("Error: You do not have delete permissions");
                    }
                } else {
                    System.out.println("Unknown command attempt by " + clientName + ": " + message);
                    sendMessage("Unknown command");
                }
            }

            input.close();
            output.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) throws Exception {
        String encryptedMessage = encrypt(message, aesKey);
        output.writeUTF(encryptedMessage);
    }

    private void listFiles() throws Exception {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        StringBuilder fileList = new StringBuilder("Files:\n");
        if (files != null) {
            for (File file : files) {
                fileList.append(file.getName()).append("\n");
            }
        }
        sendMessage(fileList.toString());
    }

    private void readFile(String filename) throws Exception {
        File file = new File(folderPath + "/" + filename);
        if (file.exists() && file.isFile()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder fileContent = new StringBuilder("Content of " + filename + ":\n");
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            reader.close();
            sendMessage(fileContent.toString());
        } else {
            sendMessage("File not found: " + filename);
        }
    }

    private void writeFile(String filename) throws Exception {
        File file = new File(folderPath + "/" + filename);
        if (file.exists() && file.isFile()) {
            sendMessage("Enter content to write to " + filename + ":");
            String encryptedContent = input.readUTF();
            String content = decrypt(encryptedContent, aesKey);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(content);
            writer.newLine();
            writer.close();
            sendMessage("Content written to " + filename);
        } else {
            sendMessage("File not found: " + filename);
        }
    }

    private void executeFile(String filename) throws Exception {
        File file = new File(folderPath + "/" + filename);
        if (file.exists() && file.canExecute()) {
            String[] command;
            if (filename.endsWith(".sh")) {
                command = new String[] { "bash", file.getAbsolutePath() };
            } else if (filename.endsWith(".bat")) {
                command = new String[] { "cmd.exe", "/c", file.getAbsolutePath() };
            } else if (filename.endsWith(".py")) {
                command = new String[] { "python", file.getAbsolutePath() };
            } else if (filename.endsWith(".js")) {
                command = new String[] { "node", file.getAbsolutePath() };
            } else if (filename.endsWith(".exe")) {
                command = new String[] { file.getAbsolutePath() };
            } else {
                sendMessage("Unsupported file type for execution: " + filename);
                return;
            }
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder("Execution result of " + filename + ":\n");
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
            sendMessage(output.toString());
        } else {
            sendMessage("File is not executable or does not exist: " + filename);
        }
    }

    private void deleteFile(String filename) throws Exception {
        File file = new File(folderPath + "/" + filename);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                sendMessage("File " + filename + " deleted successfully.");
                System.out.println(clientName + " deleted file: " + filename);
            } else {
                sendMessage("Error: Could not delete the file: " + filename);
            }
        } else {
            sendMessage("File not found: " + filename);
        }
    }

    private String encrypt(String message, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private String decrypt(String encryptedMessage, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
        return new String(cipher.doFinal(decodedBytes), "UTF-8");
    }
}

