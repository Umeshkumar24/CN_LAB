import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.net.*;

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
		DeviceCardsApp device = new DeviceCardsApp(devices);
		DiscoveryHandler d = new DiscoveryHandler(devices, device);
		d.start();
		// JButton discoverButton = new JButton("Discover Devices");
		// discoverButton.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// }
		// });

		JPanel panel = new JPanel();
		// JButton refresh = new JButton("Refresh");
		// refresh.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// device.repaint();
		// SwingUtilities.invokeLater(() -> {
		// device.initializeUI();
		// });
		// }
		// });

		device.setVisible(true);
		// panel.add(discoverButton);
		// panel.add(refresh);
		device.addNew(panel);
		// device.setSize(300, 200);
	}
}