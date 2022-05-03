import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends Canvas{

    public static void main(String[] args){
        //setup menu window
        //create menu buttons
        JFrame f=new JFrame("Menu");
        JButton b=new JButton("Create New Maze");
        JButton b2=new JButton("Edit Maze");
        JButton b3=new JButton("AutoGenerate Maze");
        //put menu button configs
        b.setBounds(90,70,140,30);
        b2.setBounds(90,100,140,30);
        b3.setBounds(90,130,140,30);
        //add buttons to frame
        f.add(b);
        f.add(b2);
        f.add(b3);
        //setsize of menu frama
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
        // Sets up the drawing canvas
        //Create autogenerate Window
        Maze maze = new Maze(10,10);
        JFrame frame = new JFrame();
        //add back button
        JButton b4=new JButton("Back");
        //set configuration of back button
        b4.setBounds(0,400,120,30);
        //add back button
        frame.add(b4);
        //go to main page when click back
        b4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setVisible(false);
                f.setVisible(true);
            }
        });
        //Set Window Size
        frame.setSize(500, 500);
        //Make Sure Programs Ends when Window Exit Button is Clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Draw to Canvas
        frame.getContentPane().add(maze);
        //Show autogenerate maze Window on button click
        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setVisible(true);
                f.setVisible(false);
            }
        });
        //Start traversing from (0,0)
        Coordinate StartC = new Coordinate (0,0);
        maze.traverse (StartC);//create maze collection object
        //setup create maze window
        //create buttons
        JFrame frame3=new JFrame("Create/Edit Maze");
        JCheckBox checkBox1 = new JCheckBox("Draw here");
        checkBox1.setBounds(100,100, 50,50);
        JCheckBox checkBox2 = new JCheckBox("Draw Here", true);
        checkBox2.setBounds(100,150, 50,50);
        frame3.add(checkBox1);
        frame3.add(checkBox2);
        frame3.setSize(400,400);
        frame3.setLayout(null);
        //add back button
        JButton b5=new JButton("Back");
        //set configuration of back button
        b5.setBounds(0,200,120,30);
        //add back button
        frame3.add(b5);
        //go to create page when click create
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame3.setVisible(true);
                f.setVisible(false);
            }
        });
        //go to main page when click back
        b5.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame3.setVisible(false);
                f.setVisible(true);
            }
        });
    }
}
