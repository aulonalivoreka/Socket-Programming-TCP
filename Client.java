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
        String line = "";
        while (!line.equals("Over")) {
            try {
                line = input.readLine();
                out.writeUTF(line);
            }
            catch (IOException i) {
                System.out.println(i);
            }
        }

        try {
            input.close();
            out.close();
            socket.close();
        }
        catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String args[])
    {
        Client client = new Client("127.0.0.1", 5000);
    }
}



