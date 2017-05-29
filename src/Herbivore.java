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
    public Herbivore(Pair<Integer,Integer> pair) { super(pair); }

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
           Pair<Integer,Integer> newPair = new Pair<>(newX,newY);
           ((Plant) entity).die();
           feedCount++;
           moveTo(newPair);
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
    public void giveBirthAt(Pair<Integer,Integer> pair)
    {
        if ( earth.getEntityAt(pair) != null ) return;
        energy -= INITIAL_ENERGY;
        earth.set(pair.getXCoord(), pair.getYCoord(), new Herbivore(pair));
    }
}
