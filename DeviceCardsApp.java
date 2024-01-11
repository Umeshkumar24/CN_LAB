import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.net.*;

public class DeviceCardsApp extends JFrame {
    private HashMap<String, String> devices;
    JPanel mainp = new JPanel(new FlowLayout(0, 10, 10));
    
    public DeviceCardsApp(HashMap<String, String> devices) {
        super();
        getContentPane().setBackground(Color.YELLOW);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        this.devices = devices;
        initializeUI();
    }

    public static ImageIcon resize(ImageIcon image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image.getImage(), 0, 0, width, height, null);
        g2d.dispose();
        return new ImageIcon(bi);
    }

    public void initializeUI() {
        setTitle("Device Cards");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));
        mainp.removeAll();
        for (HashMap.Entry<String, String> entry : devices.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Icon icon = resize(new ImageIcon("monitor.png"), 50, 50);
            JButton cardButton = new JButton(key, icon);
            cardButton.setVerticalAlignment(SwingConstants.BOTTOM);
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
        }
        // setMinimumSize(600, 400);
setMinimumSize(new Dimension(600,400));
        add(mainp);
        pack();
        setLocationRelativeTo(null);
    }

    public void addNew(JPanel panel) {
        add(panel);
    }
}