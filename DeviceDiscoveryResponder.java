import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class DeviceDiscoveryResponder {
    private static final int PORT = 8888;
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] receiveData = new byte[1024];

            System.out.println("Device Discovery Responder is running...");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                if (message.equals("DISCOVER")) {
                    InetAddress clientAddress = receivePacket.getAddress();
                    int clientPort = receivePacket.getPort();

                    String responseMessage = "" + InetAddress.getLocalHost(); // Add your device information here

                    byte[] sendData = responseMessage.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                    socket.send(sendPacket);

                    System.out.println("Response sent to " + clientAddress.getHostAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


