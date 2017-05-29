import java.io.Serializable;

/**
 * Obstacle.java
 * purpose: defines Obstacle class which represents an obstacle on the earth
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 1.0 4/20/2017
 */
public class Obstacle extends Entity implements Serializable
{
    /**
     * Constructor of obstacle
     * @param x x-location of obstacle on earth
     * @param y y-location of obstacle on earth
     */
    Obstacle(Pair<Integer,Integer> pair) { super(pair); }

    /**
     * override object toString method
     * @return string representation of Obstacle '*'
     */
    @Override
    public String toString() { return "O"; }
}
