import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPClientme {
    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName("localhost"); // Server address

            byte[] sendData;
            byte[] receiveData = new byte[1024];

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter message to send to server: ");
            String message = scanner.nextLine();
            sendData = message.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9876);
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());

            System.out.println("Server response: " + serverResponse);

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
