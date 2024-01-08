import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.net.*;

public class DeviceCardsApp extends JFrame {
    private HashMap<String, String> devices;
    JPanel mainp = new JPanel(new FlowLayout(0, 10, 10));

    public DeviceCardsApp(HashMap<String, String> devices) {
        super();
        this.devices = devices;
        initializeUI();
    }

    public void initializeUI() {
        setTitle("Device Cards");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));
        mainp.removeAll();
        for (HashMap.Entry<String, String> entry : devices.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            JButton cardButton = new JButton(key);
            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        DatagramSocket socket = new DatagramSocket();
                        byte[] data = "CONNECT".getBytes();
                        DatagramPacket pack = new DatagramPacket(data, data.length,
                                InetAddress.getByName(devices.get(e.getActionCommand())), 8080);
                        socket.send(pack);
                        socket.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            JPanel cardPanel = new JPanel(new BorderLayout());
            cardPanel.add(cardButton, BorderLayout.CENTER);
            cardPanel.add(new JLabel("IP: " + value), BorderLayout.SOUTH);
            mainp.add(cardPanel);
            setSize(200, 200);
        }
        add(mainp);
        pack();
        setLocationRelativeTo(null);
    }

    public void addNew(JPanel panel) {
        add(panel);
    }
}