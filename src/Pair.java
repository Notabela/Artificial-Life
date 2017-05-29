/**
 * Created by socra_000 on 5/29/2017.
 */
public class Pair<X, Y> {

    private X xCoord;
    private Y yCoord;

    public Pair(X xCoordinate, Y yCoordinate) {
        this.xCoord = xCoordinate;
        this.yCoord = yCoordinate;
    }


    public X getXCoord() {
        return xCoord;
    };
    public Y getYCoord() {
        return yCoord;
    }


}
