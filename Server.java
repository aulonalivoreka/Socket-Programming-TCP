import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Base64;

public class Server {
    private static final String ACCESS_KEY = "FULL_ACCESS_KEY"; // Full access key
    private static boolean isFullAccessGranted = false; // Track full access status
    private static final String SECRET_KEY_STRING = "YOUR_BASE64_SECRET_KEY"; // Base64 encoded AES key
    private static SecretKey secretKey;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Server <IP Address> <Port>");
            return;
        }

public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);

                String encryptedMessage;
                while ((encryptedMessage = in.readLine()) != null) {
                    String decryptedMessage = AESUtil.decrypt(encryptedMessage, secretKey);
                    System.out.println("Decrypted message from client: " + decryptedMessage);

                    // Process decrypted message
                    if (decryptedMessage.equalsIgnoreCase("REQUEST_ACCESS")) {
                        provideLimitedAccess();
                    } else if (decryptedMessage.startsWith("FULL_ACCESS ")) {
                        handleFullAccessRequest(decryptedMessage);
                    } else {
                        sendEncryptedMessage("Unauthorized or unknown command.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeResources();
            }
        }
 private void handleFullAccessRequest(String decryptedMessage) throws Exception {
            String providedKey = decryptedMessage.split(" ")[1];
            if (providedKey.equals(ACCESS_KEY) && !isFullAccessGranted) {
                isAuthorized = true;
                isFullAccessGranted = true;
                sendEncryptedMessage("Full access granted. You can list, create, and delete files.");
            } else if (isFullAccessGranted) {
                sendEncryptedMessage("Full access already granted to another client.");
            } else {
                sendEncryptedMessage("Incorrect access key.");
            }
        }

        private void provideLimitedAccess() throws Exception {
            sendEncryptedMessage("Limited access granted. You can view files only.");
            listFilesInFolder();
        }
 private void provideLimitedAccess() throws Exception {
            sendEncryptedMessage("Limited access granted. You can view files only.");
            listFilesInFolder();
        }

        private void listFilesInFolder() throws Exception {
            String folderPath = "./server_data";
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(folderPath))) {
                sendEncryptedMessage("Files in " + folderPath + ":");
                for (Path filePath : directoryStream) {
                    sendEncryptedMessage(filePath.getFileName().toString());
                }
            } catch (IOException e) {
                sendEncryptedMessage("Error accessing folder.");
                e.printStackTrace();
            }
        }

        private void sendEncryptedMessage(String message) throws Exception {
            out.println(AESUtil.encrypt(message, secretKey));
        }

        private void closeResources() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

