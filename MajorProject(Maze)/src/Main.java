import javax.swing.*;
import java.awt.*;
public class Main extends Canvas{

    public static void main(String[] args){
        // Sets up the drawing canvas
        //Create new Window
        Maze maze = new Maze(10,10);
        MazeCollection Maze_collection = new MazeCollection();
        Maze_collection.Add(maze);
        Maze_collection.Display();
        JFrame frame = new JFrame();
        //Set Window Size
        frame.setSize(500, 500);
        //Make Sure Program Ends when Window Exit Button is Clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Draw to Canvas
        frame.getContentPane().add(maze);
        //Show Window
        frame.setVisible(true);

        //Start traversing from (0,0)
        Coordinate StartC = new Coordinate (0,0);
        maze.traverse (StartC);

    }
}
