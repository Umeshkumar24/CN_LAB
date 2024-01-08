import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Client {
	Socket client = null;

	public static void main(String args[]) {
		int PORT = 8080;
		try (DatagramSocket socket = new DatagramSocket(PORT)) {
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			socket.receive(receivePacket);
			InetAddress clientAddress;
			int clientPort;
			clientAddress = receivePacket.getAddress();
			clientPort = receivePacket.getPort();
			String ip = clientAddress.getHostAddress();
			// String port = JOptionPane.showInputDialog("Enter the port number");
			String port = "8080";
			receiveData = new byte[1024];

			System.out.println("Device Discovery Responder is running...");
			while (true) {
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				socket.receive(receivePacket);
				String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
				if (message.equals("DISCOVER")) {
					clientAddress = receivePacket.getAddress();
					clientPort = receivePacket.getPort();
					String responseMessage = "" + InetAddress.getLocalHost();
					byte[] sendData = responseMessage.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress,
							clientPort);
					socket.send(sendPacket);
					System.out.println("Response sent to " + clientAddress.getHostAddress());
				}
				if (message.equals("CONNECT")) {
					new Client().initialize(ip, Integer.parseInt(port));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initialize(String ip, int port) {
		Robot robot = null;
		Rectangle rec = null;
		System.out.println("Connecting to Server");
		try {
			client = new Socket(ip, port);
			System.out.println("Connection made ..");
			GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			rec = new Rectangle(dim);
			try {
				robot = new Robot(gDev);
				drawGUI();
				new ScreenRecorder(client, robot, rec);
				new CommandReceiver(client, robot);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawGUI() {
	}
}
