import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.Key;
import java.util.Base64;
import java.util.Scanner;

public class Client {
    private static Key aesKey;
    private static boolean hasFullPermissions;
    private static DataInputStream input;
    private static DataOutputStream output;
    private static String clientName;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter Server IP Address:");
            String serverIp = scanner.nextLine();
            System.out.println("Enter Server Port:");
            int port = scanner.nextInt();
            scanner.nextLine();

            Socket socket = new Socket(serverIp, port);
            System.out.println("Connected to server.");

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            aesKey = generateAESKey();

            hasFullPermissions = input.readBoolean();
            System.out.println("Full permissions: " + hasFullPermissions);

            System.out.print("Enter your name: ");
            clientName = scanner.nextLine();
            String encryptedName = encrypt(clientName, aesKey);
            output.writeUTF(encryptedName);
            System.out.println("Client name sent to server.");

            while (true) {
                System.out.print("Enter command (list_files, read_file [filename], write_file [filename], execute [filename], delete_file [filename], exit): ");
                String command = scanner.nextLine();
                sendMessage(command);

                if (command.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting and notifying server.");
                    break;
                } else if (command.equalsIgnoreCase("list_files")) {
                    String response = receiveMessage();
                    System.out.println("Server Response:\n" + response);
                } else if (command.startsWith("read_file")) {
                    String response = receiveMessage();
                    System.out.println("Server Response:\n" + response);
                } else if (command.startsWith("write_file ")) {
                    if (hasFullPermissions) {
                        System.out.println("Notifying server of write_file command.");
                        String prompt = receiveMessage();
                        System.out.println(prompt);
                        String content = scanner.nextLine();
                        sendMessage(content);
                        String response = receiveMessage();
                        System.out.println("Server Response: " + response);
                    } else {
                        System.out.println("Error: You do not have permission to write to the folder.");
                    }
                } else if (command.startsWith("execute ")) {
                    if (hasFullPermissions) {
                        System.out.println("Notifying server of execute command.");
                        String response = receiveMessage();
                        System.out.println("Server Response:\n" + response);
                    } else {
                        System.out.println("Error: You do not have permission to execute files.");
                    }
                } else if (command.startsWith("delete_file ")) {
                    if (hasFullPermissions) {
                        System.out.println("Notifying server of delete_file command.");
                        String response = receiveMessage();
                        System.out.println("Server Response:\n" + response);
                    } else {
                        System.out.println("Error: You do not have permission to delete files.");
                    }
                } else {
                    System.out.println("Unknown command.");
                }
            }

            input.close();
            output.close();
            socket.close();
            System.out.println("Disconnected from server.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Key generateAESKey() {
        byte[] key = "1234567890123456".getBytes();
        return new SecretKeySpec(key, "AES");
    }
    private static String encrypt(String message, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    private static String decrypt(String encryptedMessage, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
        return new String(cipher.doFinal(decodedBytes), "UTF-8");
    }

    private static void sendMessage(String message) throws Exception {
        String encryptedMessage = encrypt(message, aesKey);
        output.writeUTF(encryptedMessage);
    }
    private static String receiveMessage() throws Exception {
        String encryptedMessage = input.readUTF();
        return decrypt(encryptedMessage, aesKey);
    }
}
