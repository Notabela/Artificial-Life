/**
 * Movable.java
 * purpose: defines Movable interface - requires implementors to be a movable entity
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 1.0 4/20/2017
 */
public interface Movable
{
    /**
     * Object moves to a new (x, y) location
     * @param x x-cor of new location
     * @param y y-cor of new location
     */
    void moveTo(Pair<Integer,Integer> pair);
}
