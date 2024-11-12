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
 private void handleFullAccessRequest(String decryptedMessage) throws Exception {
            String providedKey = decryptedMessage.split(" ")[1];
            if (providedKey.equals(ACCESS_KEY) && !isFullAccessGranted) {
                isAuthorized = true;
                isFullAccessGranted = true;
                sendEncryptedMessage("Full access granted. You can list, create, and delete files.");
            } else if (isFullAccessGranted) {
                sendEncryptedMessage("Full access already granted to another client.");
            } else {
                sendEncryptedMessage("Incorrect access key.");
            }
        }

        private void provideLimitedAccess() throws Exception {
            sendEncryptedMessage("Limited access granted. You can view files only.");
            listFilesInFolder();
        }
