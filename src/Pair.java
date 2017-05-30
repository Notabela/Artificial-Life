/**
 * Pair.java
 * Purpose: defines a pair of objects
 *
 * @author Daniel Obeng and Socratis Katehis
 * @version 1.0 5/29/2017
 */
public class Pair <X, Y>
{
    private X xCoord;
    private Y yCoord;

    /**
     * Public Constructor for Piar
     * @param xCoordinate first object of the pair
     * @param yCoordinate second object of the pair
     */
    public Pair(X xCoordinate, Y yCoordinate)
    {
        this.xCoord = xCoordinate;
        this.yCoord = yCoordinate;
    }

    /**
     * Gets the first of the two pairs
     * @return the first of the two pairs
     */
    public X getXCoord() {
        return xCoord;
    }

    /**
     * Gets the second of the two pairs
     * @return the second the two pairs
     */
    public Y getYCoord() {
        return yCoord;
    }
}
