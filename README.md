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
- [Authors](#authors)

## Components
The system includes two main components:

- **Server**: Manages client connections, processes commands, and handles file operations.
- **Client**: Connects to the server and sends commands for file management operations.

## Detailed Component Description

### Server
The server is responsible for:

- **Listening for Connections**: Opens a server socket on a specified port to listen for incoming connections from clients.
- **Managing Client Connections**:
  - Allows up to 4 clients to connect simultaneously.
  - Handles requests from each client independently.
- **File Management Commands**:
  - `READ`: Reads and sends the content of a specified file to the client.
  - `WRITE`: Writes data to a specified file, creating it if it doesn't exist.
  - `LIST`: Lists all files available in the server’s directory.
  - `DELETE`: Deletes a specified file.
- **Access Control**:
  - Provides full access to the first connected client, allowing complete file management.
  - Limits subsequent clients to basic interactions.
- **Logging**:
  - Logs each client's requests and maintains session logs in memory.
  - Tracks and displays client connection status in the server console.
- **Client**: Connects to the server and sends commands for file management operations.

## Detailed Component Description

### Server
The server is responsible for:

- **Listening for Connections**: Opens a server socket on a specified port to listen for incoming connections from clients.
- **Managing Client Connections**:
  - Allows up to 4 clients to connect simultaneously.
  - Handles requests from each client independently.
- **File Management Commands**:
  - `READ`: Reads and sends the content of a specified file to the client.
  - `WRITE`: Writes data to a specified file, creating it if it doesn't exist.
  - `LIST`: Lists all files available in the server’s directory.
  - `DELETE`: Deletes a specified file.
- **Access Control**:
  - Provides full access to the first connected client, allowing complete file management.
  - Limits subsequent clients to basic interactions.
- **Logging**:
  - Logs each client's requests and maintains session logs in memory.
  - Tracks and displays client connection status in the server console.

### Client
The client is responsible for:

- **Connecting to the Server**: Establishes a socket connection with the server on a specified host and port.
- **User Interaction**:
  - Prompts the user to enter commands for file management, such as `READ`, `WRITE`, `LIST`, and `DELETE`.
- **Sending Data to Server**: Sends user-entered commands to the server for execution.
- **Receiving Server Response**: Displays the server's responses, such as file contents or confirmation messages.

## Prerequisites
- [Java Development Kit (JDK) 8 or higher](https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)
- A compatible IDE, such as:
  - [IntelliJ IDEA](https://www.jetbrains.com/idea/)
  - [Eclipse](https://www.eclipse.org/)
  - [Visual Studio Code](https://code.visualstudio.com/)

## Configuration
Ensure that the following parameters are configured as required in the server code:

- **PORT**: Port number that the server will listen to, currently set to `10000`.
- **MAX_CLIENTS**: Maximum number of concurrent clients, set to `4`.
- **TIMEOUT**: Inactivity timeout in milliseconds, set to `10 minutes`.

## Running the Application

### Setting Up
1. Clone or download the project repository.
2. Open the project in your preferred IDE.

### Compiling the Java Files
Navigate to the project directory and compile the server and client code:

```bash
cd Projekti2
javac TCPServer.java
javac TCPClient.java

