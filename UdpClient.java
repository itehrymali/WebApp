import java.io.*; 
import java.net.*; 
public class UdpClient { 
  public static void main(String args[]) throws Exception { 
// get server address and open socket 
InetAddress serverAdr = InetAddress.getByName(args[0]); 
DatagramSocket sock = new DatagramSocket(); 
// build packet addressed to server, then send it 
byte[] outBuf = args[1].getBytes("US-ASCII"); 
DatagramPacket outPkt = new DatagramPacket(outBuf,outBuf.length,serverAdr,9876); 
sock.send(outPkt); 
// create buffer and packet for reply, then receive it 
byte[] inBuf = new byte[1000]; 
DatagramPacket inPkt = new DatagramPacket(inBuf,inBuf.length); 
sock.receive(inPkt); 
// print buffer contents and close socket 
String reply = new String(inBuf,0,inPkt.getLength(),"US-ASCII"); 
System.out.println(reply); 
sock.close(); 
}   
}