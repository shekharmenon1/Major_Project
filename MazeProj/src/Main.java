import javax.swing.*;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.event.FocusListener;

public class Main extends Canvas{


    public static void main(String[] args){
        Mainframe(10, 10);
    }

    private static void ResizeGUI(int hor, int ver, JFrame MainFrame){
        //Specifications page
        JFrame Specifications = new JFrame("Resize Maze");
        Specifications.setVisible(true);
        Specifications.setSize(500,100);
        Specifications.setLayout(new GridLayout(2, 2));
        //Add User Entry Objects

        //Dimensions
        JPanel dimensions = new JPanel();
        dimensions.setLayout(new FlowLayout());

        JLabel rows = new JLabel("#Rows: ");
        JLabel cols = new JLabel("#Columns: ");
        JTextField row = new JTextField();
        row.setText(String.valueOf(hor));
        JTextField col = new JTextField();
        col.setText(String.valueOf(ver));
        JButton submitbutton = new JButton("Submit");
        //submitbutton.setBounds(10, 500, 10, 30);
        submitbutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int newhorizontal = Integer.parseInt(row.getText());
                int newvertical = Integer.parseInt(col.getText());
                Specifications.setVisible(false);
                System.out.println("hor: "+newhorizontal+", ver: "+ newvertical);
                Mainframe(newhorizontal, newvertical);
            }
        });

        dimensions.add(rows);
        dimensions.add(row);
        dimensions.add(cols);
        dimensions.add(col);
        dimensions.add(submitbutton);
        Specifications.add(dimensions);


        Specifications.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void Mainframe(int horizontal_value, int vertical_value){
        // Sets up the drawing canvas
        //Create new Window
        //Set Window Size
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(1000, 1000);
        Container cpane;
        cpane = frame.getContentPane();
        cpane.setLayout(new GridLayout());

        Maze maze = new Maze(horizontal_value,vertical_value, true);
        //Start traversing from (0,0)
        Coordinate StartC = new Coordinate (0,0);
        maze.traverse (StartC);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        //mainPanel.setBounds(500, 200, 30*horizontalsize, 30*verticalsize);

        JPanel panel = new JPanel(new GridLayout(0, horizontal_value));
        panel.setBackground(Color.blue);

        JPanel buttonspannel = new JPanel(new GridLayout(3,3));
        JButton savetodb = new JButton("Save To Database");
        JButton exportmaze = new JButton("Export Maze");
        JButton addlogo = new JButton("Add Logo");
        JButton solve = new JButton("Solve Maze");
        JButton resizegrid = new JButton("Resize Grid");
        JButton cleardesign = new JButton("Clear Design");
        JButton addthemes = new JButton("Add Themes");
        buttonspannel.add(savetodb);
        buttonspannel.add(exportmaze);
        buttonspannel.add(addlogo);
        buttonspannel.add(solve);
        buttonspannel.add(resizegrid);
        buttonspannel.add(cleardesign);
        buttonspannel.add(addthemes);
        List<JTextField> textFields = new ArrayList<JTextField>();

        for (int j=0; j<vertical_value; j = j+1) {
            for (int i=0;i<horizontal_value;i=i+1) {
                JTextField textField = new JTextField();
                textField.setBounds(i*5, j*5, 20, 20);
                textFields.add(textField);
                int arrayindex = (i + j * horizontal_value);

                panel.add(textFields.get(arrayindex));

                textFields.get(arrayindex).addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        maze.repaint();
                    }
                    @Override
                    public void focusLost(FocusEvent e) {
                        maze.setCoordinateDirection(arrayindex, textFields.get(arrayindex).getText().toString());
                        maze.repaint();
                    }
                });
            }
        }

        for (int i=0;i<horizontal_value;i=i+1) {
            for (int j=0; j<vertical_value; j = j+1) {
                //System.out.println("i: "+i+", j: "+j+", direction: "+ maze.getCoordinateDirection(i+j*horizontalsize));
                if (maze.getCoordinateDirection(i+j*horizontal_value) != null) {
                    textFields.get(i + j * horizontal_value).setText(maze.getCoordinateDirection(i+j*horizontal_value));
                }
                else {
                    textFields.get(i + j * vertical_value).setText("-");
                }

            }
        }

        solve.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (maze.getRouteflag() == true)
                    maze.setRouteflag(false);
                else
                    maze.setRouteflag(true);
                maze.repaint();
            }
        });

        cpane.add(mainPanel);

        GridLayout layout = new GridLayout(2, 2);
        layout.setHgap(20);
        layout.setVgap(20);
        mainPanel.setLayout(layout);

        mainPanel.add(maze);
        mainPanel.add (panel);
        mainPanel.add(buttonspannel);
        frame.setVisible(true);
        //Button to go to specifications page
        resizegrid.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                ResizeGUI(horizontal_value, vertical_value, frame);
            }
        });
        //Make Sure Program Ends when Window Exit Button is Clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
