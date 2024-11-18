import java.io.*;
import java.net.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UdpEmergencyAlertServer {
    private static final int PORT = 5000;
    private static Set<ClientHandler> clients = new HashSet<>();
    private static Map<String, Set<ClientHandler>> categorySubscriptions = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Emergency Alert Server is running on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
     private static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String username;
        private Set<String> subscribedCategories = new HashSet<>();

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("Client connected: " + socket.getRemoteSocketAddress());  // Debugging

                // Ask for username
                out.println("Enter your username:");
                username = in.readLine();
                System.out.println("Client's username: " + username);  // Debugging

                // Send a welcome message
                out.println("Welcome, " + username + "! You can now send and receive alerts.");
                out.println("Available commands: SEND <category> <message>, SUBSCRIBE <category>, UNSUBSCRIBE <category>");

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received command: " + inputLine);  // Debugging

                    if (inputLine.startsWith("SEND ")) {
                        String[] parts = inputLine.split(" ", 3);
                        if (parts.length == 3) {
                            broadcastAlert(parts[1], parts[2], username);
                        }
                    } else if (inputLine.startsWith("SUBSCRIBE ")) {
                        String category = inputLine.split(" ", 2)[1];
                        subscribeToCategory(category);
                    } else if (inputLine.startsWith("UNSUBSCRIBE ")) {
                        String category = inputLine.split(" ", 2)[1];
                        unsubscribeFromCategory(category);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clients.remove(this);
                for (Set<ClientHandler> subscribers : categorySubscriptions.values()) {
                    subscribers.remove(this);
                }
            }
        }

        private void subscribeToCategory(String category) {
            subscribedCategories.add(category);
            categorySubscriptions.computeIfAbsent(category, k -> new HashSet<>()).add(this);
            out.println("Subscribed to category: " + category);
        }

        private void unsubscribeFromCategory(String category) {
            subscribedCategories.remove(category);
            Set<ClientHandler> subscribers = categorySubscriptions.get(category);
            if (subscribers != null) {
                subscribers.remove(this);
            }
            out.println("Unsubscribed from category: " + category);
        }

        private void sendAlert(String message) {
            out.println(message);
        }
    }

    private static void broadcastAlert(String category, String message, String sender) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String fullMessage = String.format("[%s] ALERT (%s) from %s: %s", timestamp, category, sender, message);
        
        Set<ClientHandler> subscribers = categorySubscriptions.get(category);
        if (subscribers != null) {
            for (ClientHandler client : subscribers) {
                client.sendAlert(fullMessage);
            }
        }
    }
}
