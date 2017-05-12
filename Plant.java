/**
 * Plant.java
 * Purpose: defines Plant class to represent Plants on the earth
 *
 * @author Daniel Obeng
 * @version 1.0 3/31/2017
 */
public class Plant extends Organism
{
    public Plant(int x, int y) { super(x, y); }

    @Override
    public void activate()
    {
        if (shouldDie() ) die();
    }

    /**
     * @return string representation of Plant '*'
     */
    @Override
    public String toString() { return "*"; }
}
