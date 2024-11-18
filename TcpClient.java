import java.io.*; 
import java.net.*; 
import java.util.Scanner; 
public class TcpClient { 
  public static void main(String[] args) { 
    final String SERVER_ADDRESS = "localhost"; // or use an IP address 
    final int SERVER_PORT = 9876; 

    try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT); 
         PrintWriter out = new PrintWriter(socket.getOutputStream(), 
true); 
         BufferedReader in = new BufferedReader(new 
InputStreamReader(socket.getInputStream())); 
         Scanner scanner = new Scanner(System.in)) { 
      String userInput; 
      while (true) { 
         System.out.print("Enter message to send (type 'exit' to 
quit): "); 
         userInput = scanner.nextLine(); 
         // Exit condition 
        if (userInput.equalsIgnoreCase("exit")) { 
            break; 
         } 
         // Send the message to the server 
          out.println(userInput); 
        
        // Receive the response from the server 
        String response = in.readLine(); 
        System.out.println("Server response: " + response); 
      } 
     } catch (IOException e) { 
      e.printStackTrace(); 
     } 
    } 
  }