import java.io.*;
import java.net.*;

public class UdpEmergencyAlertClient {
    private static final String SERVER_ADDRESS = "localhost"; // Change to server IP if needed
    private static final int SERVER_PORT = 6000;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) {
        new UdpEmergencyAlertClient().run();
    }

    public void run() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);  // Debugging
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            // Read the welcome message from server
            String serverMessage;
            while ((serverMessage = in.readLine()) != null) {
                System.out.println(serverMessage);  // Debugging
                break; // After reading the initial welcome messages, exit the loop.
            }

            String userCommand;
            while (true) {
                System.out.print("Enter command: ");
                userCommand = userInput.readLine();

                if (userCommand.equalsIgnoreCase("exit")) {
                    break; // Exit the client
                }

                // Send the command to the server
                out.println(userCommand);
                System.out.println("Command sent: " + userCommand);  // Debugging

                // Read and display any alert messages from the server
                if (in.ready()) {
                    String alertMessage = in.readLine();
                    if (alertMessage != null) {
                        System.out.println("Received: " + alertMessage);
                    }
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
        }
    }
}
