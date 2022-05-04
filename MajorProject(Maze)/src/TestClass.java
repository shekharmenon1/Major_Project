public class TestClass {
    public static void main(String[] args){
        Maze maze = new Maze(10,10);
        MazeCollection maze_col = new MazeCollection();
        //append Maze Object to MazeCollection
        maze_col.Add(maze);
        //display maze collection
        String displaystr = maze_col.Display();
        System.out.println(displaystr);
    }
}
