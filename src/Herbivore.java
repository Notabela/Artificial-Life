/**
 * Herbivore.java
 * Purpose: defines Herbivore class
 *
 * @author Daniel Obeng
 * @version 1.0 3/31/2017
 */
public class Herbivore extends Animal
{

    public Herbivore(int x, int y) { super(x, y); }

    /**
     * @return string representation of Plant '*'
     */
    @Override
    public String toString() { return "&"; }

    /**
     * Method to check if organism is this animal's prey
     * @param organism refers to the organism to checked for
     * @return true if organism is this animal's prey
     */
    @Override
    public boolean isPrey(Organism organism)
    {
        return (organism instanceof Plant);
    }

    /**
     * Method to cause animal to try to feed on an organism
     * @param organism refers to the organism that animal should try to feed on
     * @return true if animal successfully fed on the organism
     */
    @Override
    public boolean feedOn(Organism organism)
    {
        if (organism instanceof Plant && energy < maxEnergy)
        {
           energy += energyGainPerFeed;
           int newX = organism.getX(), newY = organism.getY();
           organism.die();
           feedCount++;
           moveTo(newX, newY);
           return true;
        }
        return false;
    }

    /**
     * Method to make herbivore give birth at x,y
     * @param x represents x location for new child to live at
     * @param y represents y location for new child to live at
     */
    @Override
    public void giveBirthAt(int x, int y)
    {
        if ( earth.getOrganismAt(x,y) != null ) return;
        energy -= initialEnergy;
        earth.set(x, y, new Herbivore(x,y));
    }
}
