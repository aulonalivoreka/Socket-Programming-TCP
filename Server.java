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
