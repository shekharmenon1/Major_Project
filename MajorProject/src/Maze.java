import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Maze extends JFrame implements ActionListener, Runnable {
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;
    public Maze(String title) throws HeadlessException {
        super(title);
        CreateGUI();
    }
    private void CreateGUI(){
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        headerLabel.setText("Control in action: Button");
        JButton okButton = new JButton("OK");
        JButton javaButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");
        repaint();
        setVisible(true);
    }

    private JPanel setupPanel(Color c) {
        JPanel temp = new JPanel();
        temp.setBackground(c);
        return temp;
    }
    public void actionPerformed(ActionEvent e) {

    }
    private void layoutButtonPanel(JPanel pnl) {
        GridBagLayout layout = new GridBagLayout();
        pnl.setLayout(layout);
        //Lots of layout code here
    }
    public void run() {

    }
}
