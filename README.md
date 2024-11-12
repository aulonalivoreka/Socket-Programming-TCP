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
