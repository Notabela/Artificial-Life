import java.io.Serializable;

/**
 * Entity.java
 * Purpose: defines abstract Entity class which represents any object on the earth
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 1.0 4/28/2017
 */
public abstract class Entity implements Serializable
{
    //Member Variables
    protected int locationX;
    protected int locationY;

    /**
     * Protected Constructor of Entity for subclasses
     * @param x x-location of entity
     * @param y y-location of entity
     */
    protected Entity(int x, int y)
    {
        locationX = x;
        locationY = y;
    }

    /**
     * Returns x-location of entity
     * @return the x-location of entity
     */
    public int getX() { return locationX; }

    /**
     * Returns y-location of entity
     * @return the y-location of entity
     */
    public int getY() { return locationY; }
}