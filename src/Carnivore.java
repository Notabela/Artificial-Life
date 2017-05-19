/**
 * Carnivore.java
 * Purpose: defines Carnivore class
 *
 * @author Daniel Obeng
 * @version 1.0 3/31/2017
 */
public class Carnivore extends Animal
{
    public Carnivore(int x, int y) { super(x, y); }

    /**
     * @return string representation of Plant '*'
     */
    @Override
    public String toString() { return "@"; }

    /**
     * Method to check if organism is this animal's prey
     * @param entity refers to the organism to checked for
     * @return true if organism is this animal's prey
     */
    @Override
    public boolean isPrey(Entity entity)
    {
        return (entity instanceof Herbivore);
    }

    /**
     * Method to cause animal to try to feed on an organism
     * @param entity refers to the organism that animal should try to feed on
     * @return true if animal successfully fed on the organism
     */
    @Override
    public boolean feedOn(Entity entity)
    {
        if (entity instanceof Herbivore && energy < maxEnergy)
        {
            energy += ((Herbivore) entity).energy;
            int newX = entity.getX(), newY = entity.getY();
            ((Herbivore) entity).die();
            feedCount++;
            moveTo(newX, newY);
            return true;
        }
        return false;
    }

    /**
     * Abstract method to make Carnivore give birth at x,y
     * @param x represents x location for new child to live at
     * @param y represents y location for new child to live at
     */
    @Override
    public void giveBirthAt(int x, int y)
    {
        if (earth.getEntityAt(x,y) != null) return;
        energy -= initialEnergy;
        earth.set( x, y, new Carnivore(x,y) );
    }
}
