import javax.swing.*;
import javax.xml.validation.Validator;
import java.awt.*;
import java.util.*;
import java.lang.Math;
public class Menu {
    public Menu(){
        JFrame f=new JFrame("Button Example");
        JButton b=new JButton("Create New Maze");
        JButton b2=new JButton("Edit Maze");
        JButton b3=new JButton("AutoGenerate Maze");
        b.setBounds(90,70,140,30);
        b2.setBounds(90,100,140,30);
        b3.setBounds(90,130,140,30);
        f.add(b);
        f.add(b2);
        f.add(b3);
        f.setSize(400,400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
