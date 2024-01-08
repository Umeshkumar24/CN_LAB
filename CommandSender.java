import java.awt.Rectangle;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class CommandSender extends Thread implements KeyListener, MouseListener, MouseMotionListener {
    private Socket cSocket = null;
    private JPanel cPanel = null;
    private PrintWriter writer = null;
    private Rectangle clientScreenDim = null;

    CommandSender(Socket s, JPanel p, Rectangle r) {
        cSocket = s;
        cPanel = p;
        clientScreenDim = r;
        cPanel.addKeyListener(this);
        cPanel.addMouseListener(this);
        cPanel.addMouseMotionListener(this);
        try {
            writer = new PrintWriter(cSocket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void mouseMoved(MouseEvent e) {
        double xScale = clientScreenDim.getWidth() / cPanel.getWidth();
        double yScale = clientScreenDim.getHeight() / cPanel.getHeight();
        writer.println(EnumCommands.MOVE_MOUSE.getAbbrev());
        writer.println((int) (e.getX() * xScale));
        writer.println((int) (e.getY() * yScale));
        writer.flush();
    }

    public void mousePressed(MouseEvent e) {
         if (e.getButton() == MouseEvent.BUTTON1) {
            writer.println(EnumCommands.PRESS_MOUSE.getAbbrev());
            int button = e.getButton();
            writer.println(button);
            writer.flush();
        }

        if (e.getButton() == MouseEvent.BUTTON2) {
            writer.println(EnumCommands.PRESS_MOUSE.getAbbrev());
            int button = e.getButton();
            writer.println(button);
            writer.flush();
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            writer.println(EnumCommands.PRESS_MOUSE.getAbbrev());
            int button = e.getButton();
            writer.println(button);
            writer.flush();
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            writer.println(EnumCommands.RELEASE_MOUSE.getAbbrev());
            int button = e.getButton();
            writer.println(button);
            writer.flush();
        }

        if (e.getButton() == MouseEvent.BUTTON2) {
            writer.println(EnumCommands.RELEASE_MOUSE.getAbbrev());
            int button = e.getButton();
            writer.println(button);
            writer.flush();
        }

        if (e.getButton() == MouseEvent.BUTTON3) {
            writer.println(EnumCommands.RELEASE_MOUSE.getAbbrev());
            int button = e.getButton();
            writer.println(button);
            writer.flush();
        }
    }

    public void keyPressed(KeyEvent e) {
        writer.println(EnumCommands.PRESS_KEY.getAbbrev());
        writer.println(e.getKeyCode());
        writer.flush();
    }

    public void keyReleased(KeyEvent e) {
        writer.println(EnumCommands.RELEASE_KEY.getAbbrev());
        writer.println(e.getKeyCode());
        writer.flush();
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
}
