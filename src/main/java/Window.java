import javax.swing.*;

public class Window extends JFrame {
    public static final int WINDOW_WIDTH = 700;
    public static final int WINDOW_HEIGHT = 500;


    public static void main(String[] args) {
        new Window();
    }

    public Window() {
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        WhatsappPanel mainPanel = new WhatsappPanel();
        this.add(mainPanel);
        this.setVisible(true);
    }
}