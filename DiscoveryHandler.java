import java.util.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class DiscoveryHandler extends Thread {
	HashMap<String, String> devices;
DeviceCardsApp device;

	public DiscoveryHandler(HashMap<String, String> devices, DeviceCardsApp device) {
		this.devices = devices;
		this.device = device;

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
				device.repaint();
				SwingUtilities.invokeLater(() -> {
					device.initializeUI();
				});
			}
		} catch (Exception ec) {
			System.out.println(ec);
		}
	}
}
