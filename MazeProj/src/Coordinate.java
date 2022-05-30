public class Coordinate {
    private int x;
    private int y;
    //initialize constructor
    public Coordinate(int givenx, int giveny){
        x = givenx;
        y = giveny;
    }
    //return x coordinate
    public int getx (){
        return x;
    }
    //return y coordinate
    public int gety (){
        return y;
    }
    //reset x and y for coordinate
    public void SetCoordinate (int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    //display x and y for coordinate
    public void display()
    {
        System.out.println("X= "+x+" Y= "+y);
    }

}
