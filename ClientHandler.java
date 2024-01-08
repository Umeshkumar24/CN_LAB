import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends Thread {
	Socket client = null;
	JDesktopPane desktop = null;
	JFrame jf = null;
	String clientIP;
	JTextArea properties;
	int port = 5555;
	JInternalFrame iframe = new JInternalFrame("Client screen", true, true);
	JPanel panel = new JPanel();
	boolean c = true;

	public ClientHandler(Socket client, JDesktopPane desktop, int port) {
		this.client = client;
		this.desktop = desktop;
		this.port = port;
		clientIP = (((InetSocketAddress) client.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
		System.out.println(clientIP + "\n" + port);
		start();
	}

	public void run() {
		ServerMainFrame();
		Rectangle clientScreenDim = null;
		ObjectInputStream ois = null;
		drawGUI();
		try {
			ois = new ObjectInputStream(client.getInputStream());
			clientScreenDim = (Rectangle) ois.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, String> props = (HashMap<String, String>) ois.readObject();
			for (Map.Entry<String, String> p : props.entrySet()) {
				properties.append("\n" + p.getKey() + " : " + p.getValue());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		new ScreenReceiver(ois, panel);
		new CommandSender(client, panel, clientScreenDim);
	}

	public void drawGUI() {
		iframe.setLayout(new BorderLayout());
		iframe.getContentPane().add(panel, BorderLayout.CENTER);
		iframe.setSize(100, 100);
		desktop.add(iframe);
		try {
			iframe.setMaximum(true);
		} catch (PropertyVetoException ex) {
			ex.printStackTrace();
		}
		panel.setFocusable(true);
		iframe.setVisible(true);
	}

	public void ServerMainFrame() {
		jf = new JFrame("This is Server Window");
		jf.setSize(800, 820);
		JTabbedPane jtp = new JTabbedPane();
		jtp.addTab("System Properties", new SystemProperties());
		jf.add(jtp);
		jf.setVisible(true);
	}

	private class SystemProperties extends JPanel {
		public SystemProperties() {
			properties = new JTextArea(40, 50);
			properties.setFont(new Font("Monaco", Font.PLAIN, 15));
			properties.setMargin(new Insets(12, 12, 12, 12));
			properties.setCaretPosition(0);
			JScrollPane jsp = new JScrollPane(properties);
			add(jsp);
		}
	}
}
