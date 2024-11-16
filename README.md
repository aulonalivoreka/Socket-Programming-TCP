# Programming with Sockets
This project was completed by senior students from the Faculty of Electrical and Computer Engineering at the University of "Hasan Prishtina", under the expert guidance of Prof. Dr. Blerim Rexha and Ass. Mërgim Hoti, as part of our coursework in Computer Networks.

The Programming with Sockets project is a TCP-based client-server application written in Java that supports multi-client connections, file management commands, and access control. The server manages client requests independently, processes commands, and restricts access based on client privileges.

## Components
The system includes two main components:
- **Server**:
  - Manages client connections.
  - Processes commands.
  - Handles file operations such as reading, writing, deleting, and executing files.
- **Client**:
  - Connects to the server.
  - Sends commands to the server for file management operations.

## Detailed Component Description
### Server
The server is responsible for the following tasks:
- **Listening for Connections**:
  - The server listens for incoming connections from clients via a server socket bound to a specific IP address and port entered at runtime.
  - Upon receiving a connection request, it establishes a TCP connection with the client and spawns a new thread to handle that client’s requests independently, allowing for multi-client support.
- **Managing Client Connections**:
  - The server uses a thread pool to handle client requests concurrently, allowing for an unbounded number of clients.
- **File Management Commands**: The server supports the following commands:
  - `list_files`: Lists all files available in the server’s shared folder.
  - `read_file [filename]`: Reads and sends the content of a specified file to the client.
  - `write_file [filename]`: Writes data to a specified file, creating it if it doesn't exist (only for clients with full permissions).
  - `execute [filename]`: Executes the code in the specified file and shows the output (only for clients with full permissions).
  - `delete_file [filename]`: Deletes a specified file (only for clients with full permissions).
- **Access Control**:
  - Provides full access to the first client with a specific IP address, allowing complete file management.
  - Limits other clients to basic interactions. They can only list and read files but cannot modify, execute, or delete files.
- **Logging**:
  - Logs client connections, disconnections, and the commands they issue in the server console.

### Client
The client is responsible for:
- **Connecting to the Server**: Establishes a socket connection with the server on a specified host and port.
- **User Interaction**:
  - Prompts the user to enter commands for file management, such as `list_files`, `read_file [filename]`, `write_file [filename]`, `execute [filename]`, and `delete_file [filename]`.
- **Sending Data to Server**: Sends user-entered commands to the server for execution.
- **Receiving Server Response**:
  - The client listens for a response from the server. This may include:
    - The contents of a file (in case of the `read_file` command).
    - Confirmation messages indicating that a file was created, written to, deleted, or executed.
    - List of available files (in case of the `list_files` command).
    - Error messages (e.g., if a file does not exist, if a command is invalid, or if access is restricted).

## Prerequisites
Before running the application, ensure that you have the following software:
 - [Java Development Kit (JDK) 8 or higher](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
 - A compatible IDE, such as:
   - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
   - [Eclipse](https://www.eclipse.org/)
   - [Visual Studio Code](https://code.visualstudio.com/)

## Configuration
The server configuration includes several key parameters:
- **IP Address**: The IP address that the server will bind to, entered at runtime.
- **Port**: Port number that the server will listen to, entered at runtime.

## Running the Application
### Setting Up
1. Clone or download the project repository.
   - You can clone the repository using Git or download the project files as a ZIP archive and extract them to a directory on your local machine.
   - If using Git, run the following command in your terminal: `git clone <repository_url>`
2. Open your IDE (e.g., IntelliJ IDEA, Eclipse, or Visual Studio Code) and import the project directory.

### Compiling the Java Files
To compile the Java files, follow these steps:
1. Navigate to the Project Directory:
   - Use your terminal or command prompt to navigate to the directory where the project files are located.
2. Compile the Server and Client Code:
   - Run the following commands to compile the Java files:
     ```
     javac Server.java
     javac Client.java
     ```

## Running the Server
1. Prepare the Server:
   - Ensure the `Server.java` code is compiled into bytecode. If not, compile it using the steps above.
2. Starting the Server:
   - Open a terminal, navigate to the project directory, and run:
     ```
     java Server
     ```
   - Enter the IP address and port when prompted.
3. Server Behavior:
   - Logs each client's connection, disconnection, and issued commands in the terminal.

## Running the Client
1. Prepare the Client:
   - Ensure the `Client.java` code is compiled into bytecode. If not, compile it using the steps above.
2. Starting the Client:
   - Open a new terminal and run:
     ```
     java Client
     ```
   - Enter the server's IP address and port when prompted.
3. Client Commands:
   - Example commands:
     - `list_files`
     - `read_file example.txt`
     - `write_file example.txt`
     - `execute example.py`
     - `delete_file example.txt`
   - Use `exit` to disconnect from the server.

4. Handling Multiple Clients:
   - You can run multiple clients in separate terminal windows to simulate concurrent users.
     - The first client with the specified IP address has full access.
     - Other clients are restricted to listing and reading files.

## Authors
This project was completed as part of the Computer Networks course at the Faculty of Electrical and Computer Engineering, University of "Hasan Prishtina".
- [Aulona Livoreka](https://github.com/aulonalivoreka)
- [Artina Qorrolli](https://github.com/ArtinaQorrolli)
- [Aulona Ramosaj](https://github.com/aulonaramosaj)
- [Arlinda Beqiraj](https://github.com/arlindabeqiraj)
