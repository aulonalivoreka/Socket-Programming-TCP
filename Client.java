import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.util.Base64;

public class Client {
    private static final String SECRET_KEY_STRING = "YOUR_BASE64_SECRET_KEY";
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader userInput;
    private SecretKey secretKey;

    public Client(String serverIp, int port, boolean fullAccess) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY_STRING);
            secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            clientSocket = new Socket(serverIp, port);
            System.out.println("Connected to server at " + serverIp + ":" + port);

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            userInput = new BufferedReader(new InputStreamReader(System.in));

            if (fullAccess) requestFullAccess();
            else requestReadAccess();

            readServerResponses();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void requestFullAccess() {
        System.out.print("Enter full access key: ");
        try {
            String accessKey = userInput.readLine();
            sendEncryptedMessage("FULL_ACCESS " + accessKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void requestReadAccess() {
        sendEncryptedMessage("REQUEST_ACCESS");
    }
     private void readServerResponses() {
        new Thread(() -> {
            String encryptedResponse;
            try {
                while ((encryptedResponse = in.readLine()) != null) {
                    String response = AESUtil.decrypt(encryptedResponse, secretKey);
                    System.out.println("Server: " + response);
                }
            } catch (Exception e) {
                System.out.println("Connection closed by server.");
            }
        }).start();
    }

     public void sendMessage() {
        System.out.print("Enter message: ");
        try {
            String message = userInput.readLine();
            sendEncryptedMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       
}



