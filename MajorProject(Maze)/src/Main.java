import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main extends Canvas{

    public static void main(String[] args){
        int rownumber = 5;
        int columnnumber = 5;
        int arrayindex = 0;
        int size = rownumber*columnnumber;

        //setup menu window
        //create menu buttons
        JFrame f=new JFrame("Menu");
        JButton b=new JButton("Create New Maze");
        JButton b2=new JButton("Edit Maze");
        JButton b3=new JButton("AutoGenerate Maze");
        //put menu button configs

        b.setBounds(90,70,170,30);
        b2.setBounds(90,100,170,30);
        b3.setBounds(90,130,170,30);

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
        JFrame frame3=new JFrame("Create/Edit Maze");
        frame3.setSize(1000,1000);
        frame3.setLayout(null);
        Maze createmaze = new Maze(rownumber+1, columnnumber+1);
        frame3.add(createmaze);
        List<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
        for (int i=0;i<rownumber;i=i+1) {
            for (int j=0; j<columnnumber; j = j+1) {
                System.out.println("Here");
                JCheckBox checkBox = new JCheckBox("Click");
                checkBox.setBounds(i*50, j*50, 50, 50);
                checkboxes.add(checkBox);
                arrayindex = (i * rownumber) + j;
                frame3.add(checkboxes.get(arrayindex));
                checkboxes.get(arrayindex).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //createmaze.paint_create(checkboxes, rownumber);
                        System.out.println("x = "+checkBox.getX()+", y = "+checkBox.getY());
                    }
                });
            }
        }


        //add back/draw button
        JButton b5=new JButton("Back");
        JButton b6=new JButton("Draw");
        //set configuration of back/draw button
        b5.setBounds(0,700,20,30);
        b6.setBounds(40,700,20,30);
        //add back/draw button
        frame3.add(b5);
        frame3.add(b6);
        System.out.println("Here");
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
