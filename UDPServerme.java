import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServerme {
    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(9876); 
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            System.out.println("Hi Tehreem, Server is waiting for a message...");

            while (true){

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());

                System.out.println("Received from client: " + clientMessage);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                String response = "Acknowledged: " + clientMessage;
                sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                serverSocket.send(sendPacket);

                System.out.println("Acknowledgment sent to client.\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
