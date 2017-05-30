import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Animal.java
 * Purpose: defines abstract Animal class to represent Animals on the earth
 *
 * @author Daniel Obeng and Socratis Katehis
 * @version 3.0 4/15/2017
 */
public abstract class Animal extends Organism implements Movable, Serializable
{
    private static SecureRandom rand = new SecureRandom(); //random numbers generator

    //Tuning parameters for Organism Class
    private static final double ENERGY_LOST_PER_MOVE = 0.2; // energy animal loses per move
    static final int MAX_ENERGY = 30 + rand.nextInt(60 - 30 + 1);  // energy at which animal can no longer feed
    private static final int CAN_BIRTH_ENERGY = 6 + rand.nextInt(10 - 6 + 1);   // energy at which animal can start giving birth
    private static final int LOW_CAN_BIRTH_AGE = 10 + rand.nextInt(15 - 10 + 1);  // lowest age at which animal can give birth
    private static final int HIGH_CAN_BIRTH_AGE = 50 + rand.nextInt(100 - 50 + 1);  // highest age at which animal can still give birth

    //helper bool to keep track of if animal was moved during the current iteration
    //This will ensure that organism isn't moved twice in the earth array at each iteration
    boolean isActive = false;
    int feedCount = 0;  //amount of times animal has fed

    /**
     * Constructor of Animal for subclasses
     * @param pair (x, y) pair of animal's coordinates
     */
    protected Animal(Pair<Integer,Integer> pair) { super(pair); }

    /**
     * Animal feeds on given entity
     * Pre-condition: this.isPrey() is true
     * @param entity the entity that animal should try to feed on
     * @return true if the animal successfully fed on the entity
     */
    public abstract boolean feedOn(Entity entity);

    /**
     * Checks if entity is this animal's prey
     * @param entity the entity to checked for
     * @return true if entity is this animal's prey
     */
    public abstract boolean isPrey(Entity entity);

    /**
     * Animal gives birth at (x, y)
     * @param pair (x, y) pair of coordinates where child will live at
     */
    public abstract void giveBirthAt(Pair<Integer,Integer> pair);

    /**
     * Checks if animal meets the minimum birth requirements
     * @return true if animal satisfies the minimum birth requirements
     */
    private boolean canGiveBirth()
    {
        return( (energy >= CAN_BIRTH_ENERGY) && (age >= LOW_CAN_BIRTH_AGE && age <= HIGH_CAN_BIRTH_AGE) );
    }

    /**
     * Check sif organism has reached its life Expectancy
     * @return true if age of organism >= LIFE_EXPECTANCY or its energy is <= 0
     */
    @Override
    public boolean shouldDie() { return age >= LIFE_EXPECTANCY || energy <= 0; }

    /**
     * Animal moves around earth, feed and die if necessary
     */
    public void beginAction()
    {
        ArrayList<int[]> locations = earth.getLocationsAround(this);
        Collections.shuffle(locations); //randomize

        boolean fedOnPrey = false;

        // If animal's energy is less than its initial energy, it should feed if possible
        if (energy <= INITIAL_ENERGY)
        {
            for (int[] location : locations)
            {
                Entity entity = earth.getEntityAt(location[0], location[1]);
                if ( isPrey(entity) )
                {
                    fedOnPrey = feedOn(entity);
                    break;
                }
            }
        }

        //Try to move to a location as long as animal didn't feed yet (animal moves when it feeds)
        if( !fedOnPrey )
        {
            for (int[] location : locations)
            {
                Entity entity = earth.getEntityAt(location[0], location[1]);
                if (entity == null)
                {
                    Pair<Integer,Integer> defaultPair = new Pair<>(location[0],location[1]);
                    moveTo(defaultPair);
                    break;
                } else if ( feedOn(entity) ) break;
            }
        }

        if ( canGiveBirth() )
        {
            //Put baby at a random free location around animal
            //If no such free location exist, animal doesn't give birth
            ArrayList<int[]> adjacentLocations = earth.getLocationsAround(this);
            Collections.shuffle(adjacentLocations);

            for (int[] adjacentLocation : adjacentLocations) {
                int x = adjacentLocation[0], y = adjacentLocation[1];
                Pair<Integer,Integer> pair = new Pair<>(x,y);
                if (earth.getEntityAt(pair) == null) {
                    giveBirthAt(pair);
                    break;
                }
            }
        }

        if ( shouldDie() ) die();
    }

    /**
     * Moves animal to pair(x, y)
     * @param pair new (x,y) location of animal
     */
    @Override
    public void moveTo(Pair<Integer,Integer> pair)
    {
        isActive = true;
        energy -= ENERGY_LOST_PER_MOVE;
        earth.set(getX(), getY(), null);
        locationX = pair.getXCoord(); locationY = pair.getYCoord();
        earth.set(pair.getXCoord(), pair.getYCoord(),this);
    }

    /**
     * Increases animal's age by 1
     */
    void increaseAge() { age++; }

    /**
     * Helper method: Resets animal's moved var. Check variable isActive's comment for more details
     */
    void endAction() { isActive = false; }
}
