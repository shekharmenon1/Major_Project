import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.List;
import java.util.*;



public class Maze<Maze_Name> extends Canvas
{

    //initialze variables
    private boolean [][] CoordinatesVisited;
    boolean is_manual = false;
    private List <String> CoordinateDirection = new ArrayList<String>();
    public int horizontal_size;
    public int vertical_size;
    public int Maze_ID;
    public String Maze_Name;
    public String Author_Name;
    public Date Create_Date;
    public Date Last_Modified_Date;
    public int startcellx, startcelly, finalcellx, finalcelly;
    private Stack MazeStack;
    private boolean routeflag = true;
    private int countofvisitedcells =0;
    //horizontal and vertical margins
    private int hmargin = 10; //distance horizontally from canvas left border
    private int vmargin = 10; //distance vertically from canvas top border
    private int cellopening = 25; // Opening in the maze cell
    private int cellwidth = 40; // width of mze cell

    private Graphics gr;

    public Maze(int horizontalsize, int verticalsize, boolean ismanual) {

        horizontal_size = horizontalsize;
        vertical_size = verticalsize;
        is_manual = ismanual;
        // Create an array of Coodinate objects
        MazeStack = new Stack(horizontalsize, verticalsize);

        //Create a 2 dimension array to keep the list of Coordinates visited
        CoordinatesVisited = new boolean[horizontalsize][verticalsize];

        //initialising all coordinates to null
        for (int i = 0; i < horizontal_size; i++)
        {
            for (int j = 0; j < vertical_size; j++) {
                CoordinatesVisited[i][j] = false;
                CoordinateDirection.add(i*verticalsize+j,"-");
            }
        }
    }
    public void setCoordinateDirection(int x, String direction){
          //  System.out.println("Inside setCoordianteDirection"+x+":"+direction.);

            if (direction.compareTo("S") == 0)
                CoordinateDirection.set(x,"S");
            else if (direction.compareTo("N") == 0)
                CoordinateDirection.set(x,"N");
            else if (direction.compareTo("W") == 0)
                CoordinateDirection.set(x,"W");
            else if (direction.compareTo("E") == 0)
                CoordinateDirection.set(x,"E");
            else if (direction.compareTo("-") == 0)
                CoordinateDirection.set(x,"-");
    }

    public String getCoordinateDirection(int i){
        return CoordinateDirection.get(i);
    }


    public void setRouteflag (boolean flag)
    {
        routeflag = flag;
    }

    public boolean getRouteflag ()
    {
        return (routeflag);
    }
    public void traverse (Coordinate C) {
        //Traverse through the grid of the maze
        // Push the coordinate to the Stack or Pop the top Stack
        if (CoordinatesVisited[C.getx()][C.gety()] == false) {
            CoordinatesVisited[C.getx()][C.gety()] = true;
            if (countofvisitedcells == 0) {
                startcellx = C.getx();
                startcelly = C.gety();

            }
            countofvisitedcells = countofvisitedcells + 1;
        }

        String Direction = this.NextCoordinate(C);

        if (Direction == "R" && countofvisitedcells < ((horizontal_size - 1) * (vertical_size - 1))) {
            C = MazeStack.Pop();
            this.traverse(C);
        }
        else if (Direction != "R") {
            MazeStack.Push(C);
            //Keep the direction in the 2 dimensional array CoordinateDirection
            CoordinateDirection.set(C.getx()+C.gety()*horizontal_size, Direction);

            if (Direction == "E") {
                C.SetCoordinate(C.getx() + 1, C.gety());
            }
            else if (Direction == "W") {
                    C.SetCoordinate(C.getx() - 1, C.gety());
            }
            else if (Direction == "N") {
                    C.SetCoordinate(C.getx(), C.gety() - 1);
            }
            else if (Direction == "S") {
                    C.SetCoordinate(C.getx(), C.gety() + 1);
            }
            this.traverse(C);
        } else if (countofvisitedcells == (horizontal_size - 1) * (vertical_size - 1)) {
                // Save the final cell
                finalcellx = C.getx();
                finalcelly = C.gety();

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

    public void drawroute ()
    {
        //Draw the route from starting to end point
        Graphics g = gr;
        g.setColor(Color.red);


        int count = 0;
        Coordinate C = new Coordinate (startcellx,startcelly);

        while ((C.getx() != finalcellx || C.gety() != finalcelly ) && (count < (horizontal_size * vertical_size)))
        {
           // System.out.print(C.getx()+":"+C.gety()+":"+CoordinateDirection.get(C.getx()+C.gety()*horizontal_size));
            
            count = count + 1;
            if (CoordinateDirection.get(C.getx()+C.gety()*horizontal_size) == "E") {
                g.drawLine(C.getx() * cellwidth + hmargin + 5, C.gety() * cellwidth + vmargin + 5, (C.getx()+1) * cellwidth + hmargin + 5, (C.gety()) * cellwidth + vmargin + 5);
                C.SetCoordinate(C.getx()+1,C.gety());
            }
            else  if (CoordinateDirection.get(C.getx()+C.gety()*horizontal_size)  == "W") {
                g.drawLine(C.getx() * cellwidth + hmargin + 5, C.gety() * cellwidth + vmargin + 5, (C.getx()-1) * cellwidth + hmargin + 5, (C.gety()) * cellwidth + vmargin + 5);
                C.SetCoordinate(C.getx()-1,C.gety());
            }
            else if (CoordinateDirection.get(C.getx()+C.gety()*horizontal_size) == "N") {
                g.drawLine(C.getx() * cellwidth + hmargin + 5, C.gety() * cellwidth + vmargin + 5, (C.getx()) * cellwidth + hmargin + 5, (C.gety() - 1) * cellwidth + vmargin + 5);
                C.SetCoordinate(C.getx(),C.gety()-1);
            }
            else if (CoordinateDirection.get(C.getx()+C.gety()*horizontal_size)  == "S") {
                g.drawLine(C.getx() * cellwidth + hmargin + 5, C.gety() * cellwidth + vmargin + 5, (C.getx()) * cellwidth + hmargin + 5, (C.gety() + 1) * cellwidth + vmargin + 5);
                C.SetCoordinate(C.getx(), C.gety() + 1);

            }
        }
        if (count == horizontal_size*vertical_size)
        {
            System.out.println(count+"Unsolvable" + startcellx + startcelly + finalcellx + finalcelly);
        }
        else
        {
            System.out.println("***");
        }

    }

    public void paint(Graphics g){
        gr = g;

        System.out.println("Inside Paint");

        for (int i = 0; i< horizontal_size*vertical_size;i++)
            System.out.print (CoordinateDirection.get(i));


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

                if (CoordinateDirection.get(i+j*horizontal_size) == null) continue;

                if (CoordinateDirection.get(i+j*horizontal_size).contains ("E"))
                {
                    g.setColor(Color.white);
                    g.drawLine((i+1)*cellwidth+hmargin, j*cellwidth+vmargin, (i+1)*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                    g.setColor(Color.black);
                    g.drawLine((i+1)*cellwidth+hmargin, j*cellwidth+vmargin+cellopening, (i+1)*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                }
                else if (CoordinateDirection.get(i+j*horizontal_size) .contains("W"))
                {
                    g.setColor(Color.white);
                    g.drawLine(i*cellwidth+hmargin, j*cellwidth+vmargin, i*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                    g.setColor(Color.black);
                    g.drawLine(i*cellwidth+hmargin, j*cellwidth+vmargin+cellopening, i*cellwidth+hmargin, (j+1)*cellwidth+vmargin);
                }
                else if (CoordinateDirection.get(i+j*horizontal_size) .contains("N"))
                {
                    g.setColor(Color.white);
                    g.drawLine(i*cellwidth+hmargin, j*cellwidth+vmargin, (i+1)*cellwidth+hmargin, j*cellwidth+vmargin);
                    g.setColor(Color.black);
                    g.drawLine(i*cellwidth+hmargin+cellopening, j*cellwidth+vmargin, (i+1)*cellwidth+hmargin, j*cellwidth+vmargin);
                }
                else if (CoordinateDirection.get(i+j*horizontal_size) .contains("S"))
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

            }
        }

        if (routeflag == true) {
            drawroute();
        }


    }


}