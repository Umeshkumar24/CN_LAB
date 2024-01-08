import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.net.*;

class DiscoveryHandler extends Thread {
	HashMap<String, String> devices;

	public DiscoveryHandler(HashMap<String, String> devices) {
		this.devices = devices;
	}

	public void run() {
		Discovery d = new Discovery(devices);
		try {
			while (true) {
				d.interrupt();
				Thread.sleep(5000);
				d = new Discovery(devices);
				d.start();
				System.out.println(devices);
			}
		} catch (Exception ec) {
			System.out.println(ec);
		}
	}
}

class Discovery extends Thread {
	private static final int PORT = 8080;
	static HashMap<String, String> devices;

	public Discovery(HashMap<String, String> device) {
		devices = device;
	}

	public void run() {
		try (DatagramSocket socket = new DatagramSocket()) {
			socket.setBroadcast(true);
			byte[] sendData = "DISCOVER".getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName("255.255.255.255"), PORT);
			socket.send(sendPacket);
			socket.setSoTimeout(1000);
			for (int i = 0; i < 100; i++) {
				byte[] recieved = new byte[1024];
				DatagramPacket rPacket = new DatagramPacket(recieved, recieved.length);
				socket.receive(rPacket);
				String s = new String(rPacket.getData(), 0, rPacket.getLength());
				for (int j = 0; j < s.length(); j++) {
					if (s.charAt(j) == '/') {
						devices.put(s.substring(0, j), s.substring(j + 1, s.length()));
						break;
					}
				}
			}
			socket.close();
		} catch (Exception e) {
		}
	}
}

public class Server {
	private static JFrame jFrame = null;
	private static JDesktopPane desktop = null;
	private static int port = 1234;
	static ServerSocket sc = null;
	static HashMap<String, String> devices = new HashMap<String, String>();

	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			createAndShowGUI();
		});
		initialize(port);
	}

	public static void initialize(int port) {
		try {
			sc = new ServerSocket(port);
			while (true) {
				Socket client = sc.accept();
				drawGUI();
				System.out.println("New client Connected to the server");
				new ClientHandler(client, desktop, port);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void drawGUI() {
		jFrame = new JFrame("Server Window");
		desktop = new JDesktopPane();
		jFrame.add(desktop, new BorderLayout().CENTER);
		jFrame.setExtendedState(jFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		jFrame.setVisible(true);
	}

	public static void createAndShowGUI() {
		JFrame frame = new JFrame("Network Administration Tool");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton discoverButton = new JButton("Discover Devices");
		discoverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DiscoveryHandler d = new DiscoveryHandler(devices);
				d.start();
			}
		});

		JPanel panel = new JPanel();
		JButton refresh = new JButton("Refresh");
		DeviceCardsApp device = new DeviceCardsApp(devices);
		device.setVisible(true);
		refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				device.repaint();
				SwingUtilities.invokeLater(() -> {
					device.initializeUI();
				});
			}
		});

		panel.add(discoverButton);
		panel.add(refresh);
		device.addNew(panel);
		device.setSize(300, 200);
	}
}