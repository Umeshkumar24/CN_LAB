import java.util.*;
import java.net.*;


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
