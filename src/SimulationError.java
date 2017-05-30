/**
 * SimulationError.java
 * purpose: defines SimulationError class - represents all unique exceptions that
 * can occur in the simulation
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 1.0 4/20/2017
 */
public class SimulationError extends RuntimeException
{
    /**
     * Enum of different types of errors that can occur in the simulation
     */
    public enum ErrorType
    {
        //possible runtime errors than can occur in the simulation - we have ensured that these error will
        //not happen by passing the using the right parameters in our code
        SET_SIZE_ERROR("Cannot change size of earth after the earth instance has been created"),
        NOT_ENOUGH_FREE_LOCATIONS("Free locations on earth is less than the amount of entities being added"),

        //these errors can be triggered by the user
        BAD_SAVE_FILE("Unable to load saved game, please begin a new game"),
        UNABLE_TO_SAVE("Unable to save game, please try again later");

        private final String description;
        ErrorType(String description) { this.description = description; }
    }

    /**
     * SimulationError constructor
     * @param error the Error type of the error encountered
     */
    SimulationError(ErrorType error)
    {
        super(error.description);
    }
}
