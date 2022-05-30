import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Color;


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
    public void Draw_Line(Graphics g){
        g.drawline(0, 0, 1, 1);
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
    public void Draw(){

    }
    public void Restart(){

    }
    public void Display_stored_mazes(){

    }
    public void Add_maze_to_database(){

    }
    public void convert_db_to_image(){

    }
    private void layoutButtonPanel(JPanel pnl) {
        GridBagLayout layout = new GridBagLayout();
        pnl.setLayout(layout);
        //Lots of layout code here
    }
    public void run() {

    }
}
