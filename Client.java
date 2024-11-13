import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.Key;
import java.util.Base64;
import java.util.Scanner;

public class Client {
    private static Key aesKey; // Encryption key
    private static boolean hasFullPermissions; // Indicates if the client has full permissions
    private static DataInputStream input;
    private static DataOutputStream output;
    private static String clientName;
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Input server IP and port
            System.out.println("Enter Server IP Address:");
            String serverIp = scanner.nextLine();
            System.out.println("Enter Server Port:");
            int port = scanner.nextInt();
            scanner.nextLine();

            // Create a socket connection to the server
            Socket socket = new Socket(serverIp, port);
            System.out.println("Connected to server.");

            // Initialize input and output streams
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            // Generate AES key for encryption (should be same as server's key)
            aesKey = generateAESKey();

            // Receive permissions from the server
            hasFullPermissions = input.readBoolean();
            System.out.println("Full permissions: " + hasFullPermissions);

            // Send client name to server
            System.out.print("Enter your name: ");
            clientName = scanner.nextLine();
            String encryptedName = encrypt(clientName, aesKey);
            output.writeUTF(encryptedName);


            // Main interaction loop with the server
            while (true) {
                System.out.print("Enter command (list_files, read_file [filename], write_file [filename], execute [filename], exit): ");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("exit")) {
                    sendMessage("exit");
                    break;
                } else if (command.equalsIgnoreCase("list_files")) {
                    sendMessage("list_files");
                    String response = receiveMessage();
                    System.out.println("Server Response:\n" + response);
                } else if (command.startsWith("read_file")) {
                    sendMessage(command);
                    String response = receiveMessage();
                    System.out.println("Server Response:\n" + response);
                } else if (command.startsWith("write_file")) {
                    if (hasFullPermissions) {
                        sendMessage(command);
                        // Wait for server to prompt for content
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
                    // Execute command for specific file
                    sendMessage(command);
                    String response = receiveMessage();
                    System.out.println("Server Response:\n" + response);
                } else {
                    System.out.println("Unknown command.");
                }
            }

            // Close connections
            input.close();
            output.close();
            socket.close();
            System.out.println("Disconnected from server.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Generate AES key for encryption
    private static Key generateAESKey() {
        // The key should match the server's key. For simplicity, we use a fixed key here.
        byte[] key = "1234567890123456".getBytes(); // Simple 16-byte key for testing
        return new SecretKeySpec(key, "AES");
    }
    // Encrypt a message using AES
    private static String encrypt(String message, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    // Decrypt a message using AES
    private static String decrypt(String encryptedMessage, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedMessage);
        return new String(cipher.doFinal(decodedBytes), "UTF-8");
    }
    // Send encrypted message to the server
    private static void sendMessage(String message) throws Exception {
        String encryptedMessage = encrypt(message, aesKey);
        output.writeUTF(encryptedMessage);
    }
    // Receive and decrypt message from the server
    private static String receiveMessage() throws Exception {
        String encryptedMessage = input.readUTF();
        return decrypt(encryptedMessage, aesKey);
    }
}





