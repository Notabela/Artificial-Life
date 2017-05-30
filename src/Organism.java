import java.io.Serializable;
import java.security.SecureRandom;

/**
 * Organism.java
 * Purpose: defines abstract Organism class which represents any living object on the earth
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 3.0 4/18/2017
 */
public abstract class Organism extends Entity implements Serializable
{
    private static final SecureRandom rand = new SecureRandom(); //random variable
    protected static final Earth earth = Earth.getInstance();    //earth reference

    //Tuning parameters for Organism Class
    protected static final int LIFE_EXPECTANCY = 50 + rand.nextInt(100 - 50 + 1);
    protected static final int INITIAL_ENERGY = 3;   // initial energy of animal

    //Member variables
    protected double energy = INITIAL_ENERGY;
    protected int age = 0;

    /**
     * constructor of organism
     * @param pair (x, y) pair of coordinate of obstacle on earth
     */
    protected Organism(Pair<Integer,Integer> pair) { super(pair); }

    /**
     * Checks if organism has reached its life Expectancy
     * @return true if age of organism >= LIFE_EXPECTANCY
     */
    public boolean shouldDie() { return age >= LIFE_EXPECTANCY; }

    /**
     * Kills the Organism, that is removes organism's reference from earth grid
     */
    public void die() { earth.set( getX(), getY(), null ); }

    /**
     * Causes the Organism to age, feed, grow etc. based on its properties
     */
    public abstract void beginAction();

}
