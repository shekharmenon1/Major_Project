import java.util.*;
public class Stack {
    private Coordinate[] CArray;
    private int top = 0;
    //initialize constructor
    public Stack(int h, int v)
    {
        CArray = new Coordinate [h*v];
    }
    //return last coordinate
    public Coordinate Pop()
    {
        Coordinate lastC;
        if (this.Size() > 0)
        {
            top = top -1;
            lastC = CArray[top];
            return lastC;
        }
        else
        {
            return null;
        }
    }
    //return size of coordinate list
    public int Size()
    {
        return top;
    }
    //push coordinates into stack
    public void Push(Coordinate C)
    {
        CArray[top] = new Coordinate(C.getx(),C.gety());
        top = top + 1;
    }
    //display visited coordinates
    public void Display ()
    {
        for (int i = 0; i< this.Size(); i++)
        {
            CArray[i].display();
        }
    }

}
