import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
public class MazeCollection extends Canvas {
    List<Maze> Maze_col;
    Maze[] Maze_col_array;
    public MazeCollection(){

    }
    public void Add(Maze maze){
        //adding maze to list
        Maze_col.add(maze);
        //converting list to array and storing in array variable for ease of use
        Maze_col_array = Maze_col.toArray(Maze_col_array);
    }
    public void Display(){
        for (int i=0;i<Maze_col_array.length;i=i+1){
            System.out.println("Maze Collection element #"+(i+1)+" = "+Maze_col_array[i]);
        }
    }
}
