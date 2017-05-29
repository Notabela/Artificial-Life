/**
 * Plant.java
 * Purpose: defines Plant class to represent Plants on the earth
 *
 * @author Daniel Obeng and Socratis Katehis
 * @version 1.0 3/31/2017
 */
public class Plant extends Organism
{
    public Plant(Pair<Integer,Integer> pair) { super(pair); }

    /**
     * plants die if it meets the dying requirement
     */
    @Override
    public void beginAction()
    {
        if (shouldDie() ) die();
    }

    /**
     * @return string representation of Plant '*'
     */
    @Override
    public String toString() { return "*"; }

    /**
     * Plant grows: age increases and its energy increases
     */
    public void grow()
    {
        age++;
        energy += 0.2;
    }
}
