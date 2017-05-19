import java.io.Serializable;

/**
 * Created by daniel on 5/19/17.
 */
public abstract class Entity implements Serializable
{
    protected int locationX;
    protected int locationY;

    protected Entity(int x, int y)
    {
        locationX = x;
        locationY = y;
    }

    //Getters for Entities Location X and Y
    public int getX() { return locationX; }
    public int getY() { return locationY; }
}
