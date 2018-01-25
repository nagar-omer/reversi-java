package ReversiLogic;

/*****************************************************************************
 * Inner Class Point: makes it easier to store x,y in a vector   (mutable)   *
 ****************************************************************************/
public class Point{
    private int x,y;
    // simple constructor and get functions
    public Point(int inX,int inY) {
        x = inX;
        y = inY;
    }
    public int getX() { return x; }
    public int getY() { return y; }
}
