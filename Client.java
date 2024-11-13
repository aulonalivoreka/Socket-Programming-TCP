import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.Key;
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

        }

    }
}
