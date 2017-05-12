import java.security.SecureRandom;

/**
 * Organism.java
 * Purpose: defines abstract Organism class which represents any living object on the earth
 *
 * @author Daniel Obeng
 * @version 1.0 3/31/2017
 */
public abstract class Organism
{
    //constants for animal implementation purposes
    //these constants are made available here to make changing how an animal behaves quick and easy

    private static SecureRandom randomNumbers = new SecureRandom();
    protected static final int lifeExpectancy = 10 + randomNumbers.nextInt(50 - 50 + 1);

    protected static Earth earth = Earth.getInstance();
    protected int age = 0;
    protected int locationX;
    protected int locationY;

    protected Organism(int x, int y)
    {
        locationX = x;
        locationY = y;
        Earth.getInstance().toString();
    }

    //Getters for Entities Location X and Y
    public int getX() { return locationX; }
    public int getY() { return locationY; }

    /**
     * Method to check if organism has reached its life Expectancy
     * @return true if age of organism >= lifeExpectancy
     */
    public boolean shouldDie() { return age >= lifeExpectancy; }

    /**
     * Method to kill an organism, removes organisms reference in earth grid
     * and points it to a free space
     */
    public void die() { earth.set( getX(), getY(), null ); }

    /**
     * Method to activate a specific Organism. Organism will age, feed, grow etc if it can.
     */
    public abstract void activate();

    /**
     * Method to increase the age of an organism
     */
    public void increaseAge() { age++; }

}
