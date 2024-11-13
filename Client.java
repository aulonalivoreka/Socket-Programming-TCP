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


            aesKey = generateAESKey();

            hasFullPermissions = input.readBoolean();
            System.out.println("Full permissions: " + hasFullPermissions);

            
            System.out.print("Enter your name: ");
            clientName = scanner.nextLine();
            String encryptedName = encrypt(clientName, aesKey);
            output.writeUTF(encryptedName);



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

                    sendMessage(command);
                    String response = receiveMessage();
                    System.out.println("Server Response:\n" + response);
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



}



    }
}
