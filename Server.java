import java.io.*;
import java.net.*;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Server {

    private static List<ClientHandler> connections = new ArrayList<>();
    private static ExecutorService pool = Executors.newCachedThreadPool();
    private static Key aesKey; // Encryption key
    private static final String folderPath = "shared_folder"; // Folder for client access
    private static String fullPermissionIpAddress; // IP Address with full permissions

    public static void main(String[] args) {
        try {
            // Set variables: IP address and port number
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter IP Address:");
            String ipAddress = scanner.nextLine();
            System.out.println("Enter Port Number:");
            int port = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Prompt to choose IP address for full permissions
            System.out.println("Enter the IP address of the client that will have full permissions:");
            fullPermissionIpAddress = scanner.nextLine();

            // Generate AES encryption key
            aesKey = generateAESKey();

            // Create the server socket
            ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ipAddress));
            System.out.println("Server started on IP " + ipAddress + " and port " + port);

            // Ensure the shared folder exists
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            // Listen for connections
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

        // Generate AES key for encryption
    private static Key generateAESKey() {
        // Fixed 16-byte (128-bit) key for testing purposes
        byte[] key = "1234567890123456".getBytes(); // Simple 16-byte key
        return new SecretKeySpec(key, "AES");
    }

}

class ClientHandler implements Runnable {

    private Socket clientSocket;
    private Key aesKey;
    private String folderPath;
    private DataInputStream input;
    private DataOutputStream output;
    private boolean hasFullPermissions; // true if client has full access
    private String clientName;

    
    @Override
    public void run() {
        try {
            input = new DataInputStream(clientSocket.getInputStream());
            output = new DataOutputStream(clientSocket.getOutputStream());

            // Inform client about their permissions
            output.writeBoolean(hasFullPermissions);

            // Receive client name
            String encryptedName = input.readUTF();
            clientName = decrypt(encryptedName, aesKey);
            System.out.println(clientName + " connected from " + clientSocket.getRemoteSocketAddress());

            // Process client requests
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
                } else if (message.startsWith("write_file")) {
                    if (hasFullPermissions) {
                        writeFile(message.substring(11));
                    } else {
                        sendMessage("Error: You do not have write permissions");
                    }
                } else if (message.startsWith("execute ")) {
                    String filename = message.substring(8);
                    if (hasFullPermissions) {
                        executeFile(filename);
                    } else {
                        sendMessage("Error: You do not have execute permissions");
                    }
                } else {
                    sendMessage("Unknown command");
                }
            }

            // Close connections
            input.close();
            output.close();
            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
    // Send encrypted message to client
private void sendMessage(String message) throws Exception {
    String encryptedMessage = encrypt(message, aesKey);
    output.writeUTF(encryptedMessage);
}

// Method to list files in the shared folder
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

// Method to read a file in the shared folder
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
