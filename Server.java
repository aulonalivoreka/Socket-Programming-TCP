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
