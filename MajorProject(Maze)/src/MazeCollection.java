import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
public class MazeCollection extends Canvas {
    //initialize maze collection with limit to 100 mazes
    Maze[] maze_col;
    public MazeCollection(){

    }
    //adding maze to maze collection
    public void Add(Maze maze)
    {
        //increasing size of maze collection array
        maze_col = Arrays.copyOf(maze_col, maze_col.length + 1);
        maze_col[maze_col.length-1] = maze;
    }

    //display stored mazes
    public String Display()
    {
        String displaystr = "";
        for (int i=0;i<maze_col.length;i=i+1){
            displaystr = displaystr + "Maze #"+(i+1)+" = "+maze_col[i] + "\n";
        }
        return displaystr;
    }
}
