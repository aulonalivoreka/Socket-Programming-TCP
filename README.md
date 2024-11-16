# Programming with Sockets

The Programming with Sockets project is a TCP-based client-server application written in Java that supports multi-client connections, file management commands, and access control. The server maintains session logs, manages client requests independently, and restricts access based on client privileges.

## Table of Contents
- [Components](#components)
- [Detailed Component Description](#detailed-component-description)
  - [Server](#server)
  - [Client](#client)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
  - [Setting Up](#setting-up)
  - [Compiling the Java Files](#compiling-the-java-files)
- [Running the Server](#running-the-server)
- [Running the Client](#running-the-client)
- [Authors](#authors)
  
<br>.
## Components
The system includes two main components:

- **Server**:
  - Manages client connections.
  - Processes commands.
  - Handles file operations such as reading, writing, and deleting files.
- **Client**:
  - Connects to the server.
  - Sends commands to the server for file management operations.

    
<br>.
## Detailed Component Description

### Server
The server is responsible for the following tasks:

- **Listening for Connections**:
  - The server listens for incoming connections from clients via a server socket bound to a specific port (e.g., port 10000).
  - Upon receiving a connection request, it establishes a TCP connection with the client and spawns a new thread to handle that client’s requests independently, allowing for multi-client support.
- **Managing Client Connections**:
  - The server can manage up to 4 clients simultaneously. If more than 4 clients attempt to connect, the server will reject the connection, and the client will be informed of the maximum limit.
  - The server uses a thread pool or separate threads to handle client requests concurrently.
- **File Management Commands**: The server supports the following commands:
  - `READ`: Reads and sends the content of a specified file to the client.
  - `WRITE`: Writes data to a specified file, creating it if it doesn't exist.
  - `LIST`: Lists all files available in the server’s directory.
  - `DELETE`: Deletes a specified file.
- **Access Control**:
  - Provides full access to the first connected client, allowing complete file management.
  - Limits subsequent clients to basic interactions.They can only list files and read files, but cannot modify or delete files.
- **Logging**:
  - Logs each client's requests and maintains session logs in memory.
  - Tracks and displays client connection status in the server console.


### Client
The client is responsible for:

- **Connecting to the Server**: Establishes a socket connection with the server on a specified host and port.
- **User Interaction**:
  - Prompts the user to enter commands for file management, such as `READ`, `WRITE`, `LIST`, and `DELETE`.
- **Sending Data to Server**: Sends user-entered commands to the server for execution.
- **Receiving Server Response**:
  - The client listens for a response from the server. This may include:
    - The contents of a file (in case of the READ command).
    - Confirmation messages indicating that a file was created, written to, or deleted.
    - List of available files (in case of the LIST command).
    - Error messages (e.g., if a file does not exist, if a command is invalid, or if access is restricted).

<br>.
## Prerequisites
Before running the application, ensure that you have the following software:
 - [Java Development Kit (JDK) 8 or higher](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
 - A compatible IDE, such as:
   - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
   - [Eclipse](https://www.eclipse.org/)
   - [Visual Studio Code](https://code.visualstudio.com/)

<br>.
## Configuration
The server configuration includes several key parameters, which you can modify in the server's code or configuration file:

- **PORT**: Port number that the server will listen to, currently set to `10000`.
- **MAX_CLIENTS**: Maximum number of concurrent clients, set to `4`.
- **TIMEOUT**: Inactivity timeout in milliseconds, set to `10 minutes`.

<br>.
## Running the Application

### Setting Up
1. Clone or download the project repository.
   - You can clone the repository using Git or download the project files as a ZIP archive and extract them to a directory on your local machine.
   - If using Git, run the following command in your terminal: git clone <repository_url>
2.Open your IDE (e.g., IntelliJ IDEA, Eclipse, or Visual Studio Code) and import the project directory.

### Compiling the Java Files
To compile the Java files, follow these steps:
1. Navigate to the Project Directory:
   - Use your terminal or command prompt to navigate to the directory where the project files are located. For example: cd Projekti2

2. Compile the Server and Client Code:
   - In the project directory, compile the Java files for the server and client:
     javac TCPServer.java
     javac TCPClient.java
This will create the corresponding TCPServer.class and TCPClient.class files, which are the bytecode files that can be executed on the Java Virtual Machine (JVM).

<br>.
## Running the Server
The server is responsible for accepting incoming client connections, processing commands, and managing file operations (read, write, delete, list files). The server listens on a specific port and handles up to 4 concurrent clients.

1. Prepare the Server
Before running the server, ensure that you have the following:
   - The server code (TCPServer.java) is compiled into bytecode. If you haven't already compiled the server, do so by running:  javac TCPServer.java
    
2. Starting the Server
To run the server, follow these steps:
   1. Open a Terminal (or Command Prompt) Window:
      - Navigate to the directory where your compiled TCPServer.class file is located.
   2. Start the Server:
      - Run the following command in the terminal to start the server: java TCPServer
      - The server will now listen for incoming client connections on the specified port
      - Server Output: Once the server starts, you should see some output in the terminal indicating the server is listening for connections, such as:
Server is running and listening for client connections on port 10000...

3. Server Behavior After Starting
    - Listening for Client Connections:
      - The server opens a server socket on the configured port (e.g., 10000) and waits for clients to connect. It will display a message in the server console for every incoming connection.
      - Example output when a client connects:  Client 1 connected from [IP Address].
    - Handling Multiple Clients:
      - The server can handle up to 4 concurrent clients. For each client connection, the server creates a new thread to process that client’s requests. Each client can interact with the server independently without affecting other clients.
      - If more than 4 clients try to connect simultaneously, the server will reject the additional connections and display a message, such as: Maximum number of clients (4) reached. Connection attempt rejected from [IP Address].
    - Access Control:
      - The first connected client (Client 1) will have full access to all file management commands, including READ, WRITE, LIST, and DELETE.
      - Subsequent clients will only have limited access and will be able to use basic commands like READ and LIST.
    - Logging:
        - The server logs client connections, disconnections, and the commands they issue. The log information is displayed on the server console and is useful for monitoring the server’s activity.



<br>.
## Running the Client
The client interacts with the server, sending commands and receiving responses. You can run the client multiple times to simulate multiple users connecting to the server.

1. Prepare the Client
- Before running the client, ensure that the client code (TCPClient.java) has been compiled into bytecode. If you haven't already compiled it, run:  javac TCPClient.java

2. Starting the Client
- To run the client, follow these steps:
   1. Open Another Terminal Window:
      - You need to open a new terminal window (or use a separate terminal tab) for each client you want to run.
      - Navigate to the directory where the TCPClient.class file is located.
    2. Run the Client:
      - Run the client using the following command in the terminal:  java TCPClient
      - The client will try to connect to the server on the specified host and port
      - Client Output: Once the connection is successful, you will see a prompt in the client console to enter commands: Connected to the server.

3. Client Interaction with the Server
- The client will prompt the user for a command.
- Example: To read a file named example.txt, you would type: READ example.txt

4. Handling Multiple Clients
   - You can run multiple clients in separate terminal windows or tabs, simulating multiple users accessing the server.
     - Client 1: Full access to all commands (READ, WRITE, LIST, DELETE).
     - Client 2, 3, 4: Limited to READ and LIST commands.


<br>.
## Authors
This project was completed as part of the Computer Networks course at the Faculty of Electrical and Computer Engineering, University of "Hasan Prishtina".
- [Aulona Livoreka](https://github.com/aulonalivoreka)
- [Artina Qorrolli](https://github.com/ArtinaQorrolli)
- [Aulona Ramosaj](https://github.com/aulonaramosaj)
- [Arlinda Beqiraj](https://github.com/arlindabeqiraj)

