import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main extends Canvas{

    public static void main(String[] args){
        //setup menu window
        //create menu buttons
        JFrame f      = new JFrame("Menu");
        JFrame frame  = new JFrame();
        JFrame frame3 = new JFrame("Create/Edit Maze");
        JFrame frame4 = new JFrame("New Maze");
        
        JButton b  = new JButton("Create New Maze");
        JButton b2 = new JButton("Edit Maze");
        JButton b3 = new JButton("AutoGenerate Maze");
        JButton b4 = new JButton("Back");
        JButton b5 = new JButton("Back");
        JButton b6 = new JButton("Draw");
        JButton toMakeSure = new JButton("Sure");
        
        JLabel row1 = new JLabel("rowNumber");
        JLabel cols = new JLabel("columnNumber");
        JTextField number = new JTextField();
        JTextField number2 = new JTextField();   
        

        b.setBounds(90,70,170,30);
        b2.setBounds(90,100,170,30);
        b3.setBounds(90,130,170,30);
        b4.setBounds(0,400,120,30);
        b5.setBounds(150,700,100,50);
        b6.setBounds(50,700,100,50);
        row1.setBounds(325,0,200,50);
        cols.setBounds(325,100,200,50);
        number.setBounds(450,10,150,30);
        number2.setBounds(450,110,150,30);
        toMakeSure.setBounds(425, 200, 100, 50);
        
        //add buttons to frame
        f.add(b);
        f.add(b2);
        f.add(b3);
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // auto generate -> frame
        
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame3.add(row1);
        frame3.add(cols);
        frame3.add(number);
        frame3.add(number2);
        frame3.add(toMakeSure);
        frame3.setSize(1000,1000);
        frame3.setLayout(null);
        frame3.add(b5);
        frame3.add(b6);
        frame4.setSize(1000,1000);

        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame3.setVisible(true);
                f.setVisible(false);
            }
        });
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame3.setVisible(true);
                f.setVisible(false);
            }
        });

        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setVisible(true);
                f.setVisible(false);
                
                Maze maze = new Maze(10, 10);
                frame.add(b4);  
                frame.getContentPane().add(maze);
                Coordinate StartC = new Coordinate (3,3);
                maze.traverse(StartC);
                //System.out.println(maze.route1);
            }
        });
        b4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame.setVisible(false);
                f.setVisible(true);
            }
        });
        b5.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                frame3.setVisible(false);
                f.setVisible(true);
            }
        });
        b6.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                frame3.setVisible(false);
                f.setVisible(false);
                frame4.setVisible(true);
                
                int colNumber  =    Integer.parseInt( number.getText()); 
                int row1Number  =    Integer.parseInt( number2.getText());

                Maze maze = new Maze(colNumber, row1Number);
                frame4.add(b4);  
                frame4.getContentPane().add(maze);
                Coordinate StartC = new Coordinate (0,0);
                maze.traverse(StartC);
                
            }
        });


    }
}
