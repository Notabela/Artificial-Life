/**
 * Carnivore.java
 * Purpose: Defines Carnivore class
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 3.0 3/31/2017
 */
public class Carnivore extends Animal
{
    /**
     * Constructor of Carnivore
     * @param x x-location of obstacle on earth
     * @param y y-location of obstacle on earth
     */
    public Carnivore(Pair<Integer,Integer> pair) { super(pair); }

    /**
     * @return string representation of Plant '*'
     */
    @Override
    public String toString() { return "@"; }

    /**
     * Checks if entity is this animal's prey
     * @param entity the entity to be checked for
     * @return true if entity is this animal's prey
     */
    @Override
    public boolean isPrey(Entity entity)
    {
        return (entity instanceof Herbivore);
    }

    /**
     * Carnivore attempts to feed on prey
     * @param entity the organism that animal should try to feed on
     * @return true if animal successfully fed on the organism
     */
    @Override
    public boolean feedOn(Entity entity)
    {
        if (entity instanceof Herbivore && energy < MAX_ENERGY)
        {
            energy += ((Herbivore) entity).energy; //animal obtains prey's energy
            int newX = entity.getX(), newY = entity.getY();
            Pair<Integer,Integer> newPair = new Pair<Integer,Integer>(newX,newY);
            ((Herbivore) entity).die();
            feedCount++;
            moveTo(newPair);
            return true;
        }
        return false;
    }

    /**
     * Carnivore gives birth at (x, y)
     * @param x x-cor of child
     * @param y y-cor of child
     */
    @Override
    public void giveBirthAt(Pair<Integer,Integer> pair)
    {
        if (earth.getEntityAt(pair) != null) return;
        energy -= INITIAL_ENERGY;
        earth.set( pair.getXCoord(), pair.getYCoord(), new Carnivore(pair) );
    }
}
