import java.io.Serializable;

/**
 * Lake.java
 * purpose: defines Lake class which represents an patch of water on earth
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 1.0 4/20/2017
 */
public class Lake extends Entity implements Serializable
{
    /**
     * Constructor of Lake
     * @param x x-location of lake on earth
     * @param y y-location of lake on earth
     */
    Lake(Pair<Integer,Integer> pair) { super(pair); }

    /**
     * override object toString method
     * @return string representation of Obstacle '^'
     */
    @Override
    public String toString() { return "^"; }
}
