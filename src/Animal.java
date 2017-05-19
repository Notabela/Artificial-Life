import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Animal.java
 * Purpose: defines abstract Animal class to represent Animals on the earth
 *
 * @author Daniel Obeng
 * @version 1.0 3/31/2017
 */
public abstract class Animal extends Organism implements Movable
{
    private static SecureRandom rand = new SecureRandom(); //random numbers generator

    //constants for animal implementation purposes
    //these constants are made available here to make changing how an animal behaves quick and easy
    protected static final int initialEnergy      = 3;   // initial energy of animal
    private static final double energyLostPerMove = 0.2; // energy animal loses per move

    protected static final int maxEnergy          = 5 + rand.nextInt(10 - 5 + 1);  // energy at which animal can no longer feed
    //protected static final int energyGainPerFeed  = 2 + rand.nextInt(4 - 2 + 1); // energy animal gains per feed
    private static final int energyCanGiveBirth   = 4 + rand.nextInt(6 - 4 + 1);   // energy at which animal can start giving birth
    private static final int lowestCanBirthAge    = 20 + rand.nextInt(30 - 5 + 1);  // lowest age at which animal can give birth
    private static final int highestCanBirthAge   = 50 + rand.nextInt(100 - 30 + 1);  // highest age at which animal can still give birth

    protected int feedCount = 0;             //amount of times animal has fed

    //helper bool to keep track of if animal was moved during the current iteration
    //This will ensure that organism isn't moved twice in the earth array at each iteration
    public boolean isActive = false;

    //Protected constructor for subclasses
    protected Animal(int x, int y)
    {
        super(x, y);
        energy = initialEnergy;
    }

    /**
     * Method to cause animal to try to feed on an organism
     * @param entity refers to the organism that animal should try to feed on
     * @return true if animal successfully fed on the organism
     */
    public abstract boolean feedOn(Entity entity);

    /**
     * Method to check if organism is this animal's prey
     * @param entity refers to the entity to checked for
     * @return true if organism is this animal's prey
     */
    public abstract boolean isPrey(Entity entity);

    /**
     * Abstract method to make animal give birth at x,y
     * @param x represents x location for new child to live at
     * @param y represents y location for new child to live at
     */
    public abstract void giveBirthAt(int x, int y);

    /**
     * Method to check if animal is able to giveBirth
     * @return true if animal satisfies the minimum birth requirements
     */
    public boolean canGiveBirth()
    {
        return( (energy >= energyCanGiveBirth) && (age >= lowestCanBirthAge && age <= highestCanBirthAge) );
    }

    /**
     * Method to check if organism has reached its life Expectancy
     * @return true if age of organism >= lifeExpectancy or its energy is <= 0
     */
    @Override
    public boolean shouldDie() { return age >= lifeExpectancy || energy <= 0; }

    /**
     * Method to make animal move around earth and feed if necessary
     */
    public void activate()
    {
        //MARK: Get the top, down, left and right locations around animal
        ArrayList<int[]> locations = earth.getLocationsAround(this);
        Collections.shuffle(locations);

        boolean fedOnPrey = false;

        // If animal's energy is less than its initial energy, it should feed if possible
        if (energy <= initialEnergy)
        {
            for (int[] location : locations)
            {
                Entity entity = earth.getEntityAt(location[0], location[1]);
                if ( isPrey(entity) )
                {
                    //if feedOn(organism) returns false, then animals has too much energy
                    //i.e we already ensured organism is prey of this
                    if ( feedOn(entity) )
                    {
                        fedOnPrey = true;
                        break;
                    }
                    else break;
                }
            }
        }

        //Animal couldn't feed, lets try to move to a location
        if(!fedOnPrey)
        {
            for (int[] location : locations)
            {
                Entity entity = earth.getEntityAt(location[0], location[1]);
                if (entity == null)
                {
                    moveTo(location[0], location[1]);
                    break;
                } else if (feedOn(entity)) break;
            }
        }

        if ( canGiveBirth() )
        {
            //Put baby at a random free location around animal
            //If no such free location exist, animal doesn't give birth
            ArrayList<int[]> adjacentLocations = earth.getLocationsAround(this);
            Collections.shuffle(adjacentLocations);

            for(int i = 0, j = adjacentLocations.size(); i < j; i ++)
            {
                int x = adjacentLocations.get(i)[0];
                int y = adjacentLocations.get(i)[1];
                if(earth.getEntityAt(x, y) == null)
                {
                    giveBirthAt(x, y);
                    break;
                }
            }
        }

        if ( shouldDie() ) die();
    }

    /**
     * Method to move animal to specified x, y
     * @param x represents x location animal should move to
     * @param y represents y location animal should move to
     */
    @Override
    public void moveTo(int x, int y)
    {
        isActive = true;
        energy -= energyLostPerMove;
        earth.set(getX(), getY(), null);
        locationX = x;
        locationY = y;
        earth.set(x, y,this);
    }

    /**
     * Method to increase the age of an organism
     */
    public void increaseAge() { age++; }

    /**
     * Helper method to reset animal's moved var. Check Moved var comment for more details
     */
    public void deactivate() { isActive = false; }
}
