import java.io.*; 
import java.net.*; 
 
public class TcpServer { 
    public static void main(String[] args) { 
        final int PORT = 9876; 
 
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { 
            System.out.println("TCP Server is running on port " + PORT); 
 
            while (true) { 
                // Accept incoming client connections 
                Socket clientSocket = serverSocket.accept(); 
                System.out.println("Client connected: " + 
clientSocket.getInetAddress()); 
 
                // Handle client in a new thread 
                new Thread(() -> handleClient(clientSocket)).start(); 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
 
    private static void handleClient(Socket clientSocket) { 
        try (BufferedReader in = new BufferedReader(new 
InputStreamReader(clientSocket.getInputStream())); 
             PrintWriter out = new 
PrintWriter(clientSocket.getOutputStream(), true)) { 
 
            String inputLine; 
            while ((inputLine = in.readLine()) != null) { 
                System.out.println("Received: " + inputLine); 
                // Echo back the received message 
                out.println(inputLine); 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
            try { 
                clientSocket.close(); 
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 
} 
