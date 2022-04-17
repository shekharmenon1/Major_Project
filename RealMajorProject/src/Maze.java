import javax.swing.*;
import javax.xml.validation.Validator;
import java.awt.*;
import java.util.*;
import java.lang.Math;

public class Maze extends Canvas
{

    //initialze variables
    private boolean [][] CoordinatesVisited;
    private String [][] CoordinateDirection;
    private int horizontal_size;
    private int vertical_size;
    private int startcellx, startcelly, finalcellx, finalcelly;
    private Stack MazeStack;
    private int countofvisitedcells =0;
    //horizontal and vertical margins
    private int hmargin = 10; //distance horizontally from canvas left border
    private int vmargin = 10; //distance vertically from canvas top border
    private int cellopening = 10; // Opening in the maze cell
    private int cellwidth = 40; // width of mze cell

    public Maze(int horizontalsize, int verticalsize) {

        horizontal_size = horizontalsize;
        vertical_size = verticalsize;

        // Create an array of Coodinate objects
        MazeStack = new Stack (horizontalsize,verticalsize);

        //Create a 2 dimension array to keep the list of Coordinates visited
        CoordinatesVisited =  new boolean[horizontalsize][verticalsize];


        // Create a 2 dimensional  array to keep the direction of the cell in the maze
        CoordinateDirection = new String [horizontal_size][vertical_size];

        //initialising all coordinates to null
        for (int i = 0; i < horizontal_size; i++)
            for (int j = 0; j < vertical_size; j++)
            {
                CoordinateDirection[i][j] = null;
                CoordinatesVisited[i][j] = false;
            }

    }

    public void traverse (Coordinate C)
    {
        //Traverse through the grid of the maze
        // Push the coordinate to the Stack or Pop the top Stack
        if (CoordinatesVisited[C.getx()][C.gety()] == false)
        {
            CoordinatesVisited[C.getx()][C.gety()] = true;
            if (countofvisitedcells == 0)
            {
                startcellx = C.getx();
                startcelly = C.gety();
                System.out.println("Start Cell = ("+startcellx+","+startcelly+")");
            }
            countofvisitedcells = countofvisitedcells + 1;
        }
        String Direction = this.NextCoordinate(C);

        if (Direction == "R" && countofvisitedcells < 81) {
            C = MazeStack.Pop();
            this.traverse (C);
        }
        else if (Direction != "R")
        {
            MazeStack.Push(C);
            //Keep the direction in the 2 dimensional array CoordinateDirection
            CoordinateDirection[C.getx()][C.gety()] = Direction;

            if (Direction  == "E")
            {
                C.SetCoordinate(C.getx()+1,C.gety());
            }
            else if (Direction == "W")
            {
                C.SetCoordinate(C.getx()-1,C.gety());
            }
            else if (Direction  == "N")
            {
                C.SetCoordinate(C.getx(),C.gety()-1);
            }
            else if (Direction == "S")
            {
                C.SetCoordinate(C.getx(),C.gety()+1);
            }
            this.traverse (C);
        }
        else if (countofvisitedcells == 81)
        {
            // Save the final cell
            finalcellx = C.getx();
            finalcelly = C.gety();
            System.out.println("Final Cell = ("+finalcellx+","+finalcelly+")");
        }
    }
    //getting next coordinate
    public String NextCoordinate (Coordinate C)
    {
        //save current coordinates in local variables
        int x = C.getx();
        int y = C.gety();

        // Return next coordinate randomly.

        Random Rand = new Random();
        Set <Integer> superset = new HashSet<Integer>();
        superset.add(0);
        superset.add(1);
        superset.add(2);
        superset.add(3);

        int randval = Rand.nextInt(4);
        while (!superset.isEmpty())
        {

            superset.remove(randval);
            //returning north west east south based on random number generator
            if (randval == 0 && (x > 0) && CoordinatesVisited [x-1][y] == false)
            {
                CoordinatesVisited [x-1][y] = true;
                countofvisitedcells = countofvisitedcells+1;
                return "W";
            }
            else if (randval == 1 && (x < horizontal_size-2) && CoordinatesVisited [x+1][y] == false)
            {
                CoordinatesVisited [x+1][y] = true;
                countofvisitedcells = countofvisitedcells+1;
                return "E";
            }
            else if (randval == 2 && (y > 0) && CoordinatesVisited [x][y-1] == false)
            {
                CoordinatesVisited [x][y-1] = true;
                countofvisitedcells = countofvisitedcells+1;
                return "N";
            }
            else if ((y < vertical_size-2) && CoordinatesVisited [x][y+1] == false)
            {
                CoordinatesVisited [x][y+1] = true;
                countofvisitedcells = countofvisitedcells+1;
                return "S";
            }
            randval = Rand.nextInt(4);
        }
        //return invalid direction of list empty
        return "R";

    }

    public void drawroute (Graphics g)
    {
        //Draw the route of the maze from the staring point to end point.
        g.setColor(Color.red);
        while (MazeStack.Size() > 0)
        {
            Coordinate C = MazeStack.Pop();
            if (CoordinateDirection[C.getx()][C.gety()] == "E") {
                g.drawLine(C.getx() * cellwidth + hmargin + 5, C.gety() * cellwidth + vmargin + 5, (C.getx()+1) * cellwidth + hmargin + 5, (C.gety()) * cellwidth + vmargin + 5);
            }
            else  if (CoordinateDirection[C.getx()][C.gety()] == "W") {
                g.drawLine(C.getx() * cellwidth + hmargin + 5, C.gety() * cellwidth + vmargin + 5, (C.getx()-1) * cellwidth + hmargin + 5, (C.gety()) * cellwidth + vmargin + 5);
            }
            else if (CoordinateDirection[C.getx()][C.gety()] == "N") {
                g.drawLine(C.getx() * cellwidth + hmargin + 5, C.gety() * cellwidth + vmargin + 5, (C.getx()) * cellwidth + hmargin + 5, (C.gety() - 1) * cellwidth + vmargin + 5);
            }
            else if (CoordinateDirection[C.getx()][C.gety()] == "S") {
                g.drawLine(C.getx() * cellwidth + hmargin + 5, C.gety() * cellwidth + vmargin + 5, (C.getx()) * cellwidth + hmargin + 5, (C.gety() + 1) * cellwidth + vmargin + 5);
            }
        }
    }

    public void paint(Graphics g){
        //Draw the cells of the maze
        for (int i=0; i < horizontal_size;i=i+1) {
            g.setColor(Color.black);
            g.drawLine((i) * cellwidth + hmargin, vmargin, i * cellwidth + hmargin, (vertical_size-1) * cellwidth + vmargin);
        }
        
        for (int j=0; j < vertical_size;j=j+1) {
            g.setColor(Color.black);
            g.drawLine(hmargin, j * cellwidth + vmargin, (horizontal_size-1) * cellwidth + hmargin, (j) * cellwidth + vmargin);
        }

        //draw the cell openings in the maze
        for (int i=0; i < horizontal_size-1;i=i+1)
        {
            for (int j=0;j<vertical_size-1;j=j+1)
            {
                //if 2 consecutive points are true draw from one to other with 20 for pixel size
                if (CoordinateDirection[i][j] == "E")
                {
                    g.setColor(Color.white);
                    g.drawLine((i+1)*cellwidth+hmargin, j*cellwidth+vmargin, (i+1)*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                    g.setColor(Color.black);
                    g.drawLine((i+1)*cellwidth+hmargin, j*cellwidth+vmargin+cellopening, (i+1)*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                }
                else if (CoordinateDirection[i][j] == "W")
                {
                    g.setColor(Color.white);
                    g.drawLine(i*cellwidth+hmargin, j*cellwidth+vmargin, i*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                    g.setColor(Color.black);
                    g.drawLine(i*cellwidth+hmargin, j*cellwidth+vmargin+cellopening, i*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                }
                else if (CoordinateDirection[i][j] == "N")
                {
                    g.setColor(Color.white);
                    g.drawLine(i*cellwidth+hmargin, j*cellwidth+vmargin, (i+1)*cellwidth+hmargin, j*cellwidth+vmargin);
                    g.setColor(Color.black);
                    g.drawLine(i*cellwidth+hmargin+cellopening, j*cellwidth+vmargin, (i+1)*cellwidth+hmargin, j*cellwidth+vmargin);
                }
                else if (CoordinateDirection[i][j] == "S")
                {
                    g.setColor(Color.white);
                    g.drawLine(i*cellwidth+hmargin, (j+1)*cellwidth+vmargin, (i+1)*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                    g.setColor(Color.black);
                    g.drawLine(i*cellwidth+hmargin+cellopening, (j+1)*cellwidth+vmargin, (i+1)*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                }
                // Draw the starting and ending cells
                g.setColor(Color.green);
                g.fillOval(startcellx*cellwidth+hmargin+5, startcelly*cellwidth+vmargin+5, 5,5);
                g.setColor(Color.blue);
                g.fillOval(finalcellx*cellwidth+hmargin+5, finalcelly*cellwidth+vmargin+5, 5,5);

                //Draw the route from starting to end point
                this.drawroute(g);
            }
        }
    }
}