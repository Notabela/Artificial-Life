/**
 * Herbivore.java
 * Purpose: defines Herbivore class
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 1.0 3/31/2017
 */
public class Herbivore extends Animal
{

    /**
     * Herbivore Constructor
     * @param x x-cor of herbivore
     * @param y y-cor of herbivore
     */
    public Herbivore(int x, int y) { super(x, y); }

    /**
     * @return string representation of Herbivore '&'
     */
    @Override
    public String toString() { return "&"; }

    /**
     * checks if entity is this animal's prey
     * @param entity the entity to check for
     * @return true if entity is this animal's prey
     */
    @Override
    public boolean isPrey(Entity entity)
    {
        return (entity instanceof Plant);
    }

    /**
     * Herbivore attempts to feed on prey
     * @param entity the organism that animal should try to feed on
     * @return true if animal successfully fed on the organism
     */
    @Override
    public boolean feedOn(Entity entity)
    {
        if (entity instanceof Plant && energy < MAX_ENERGY)
        {
           energy += ((Plant) entity).energy;
           int newX = entity.getX(), newY = entity.getY();
           ((Plant) entity).die();
           feedCount++;
           moveTo(newX, newY);
           return true;
        }

        return false;
    }

    /**
     * herbivore gives birth at (x, y)
     * @param x x-cor of child
     * @param y y-cor of child
     */
    @Override
    public void giveBirthAt(int x, int y)
    {
        if ( earth.getEntityAt(x,y) != null ) return;
        energy -= INITIAL_ENERGY;
        earth.set(x, y, new Herbivore(x,y));
    }
}
