import javax.swing.*;
import java.io.Serializable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Earth.java
 * Purpose: defines Earth on which Entities will sit (Designed as a singleton)
 *
 * @author Daniel Obeng & Socratis Katehis
 * @version 1.0 3/31/2017
 */
public class Earth implements Serializable
{
    private static SecureRandom rand = new SecureRandom(); //random number generator

    //Tuning parameters for Organism Class
    private static final int MIN_CARN_ITER = 2;  //min iterations after which carnivore moves
    private static final int MAX_CARN_ITER = 4;  //max iterations after which carnivore moves
    private static final int MIN_HERB_ITER = 1;  //min iterations after which herbivore moves
    private static final int MAX_HERB_ITER = 3;  //max iterations after which herbivore moves
    private static final int MIN_PLNT_ITER = 3;  //min iterations after which new plant grows
    private static final int MAX_PLNT_ITER = 5;  //max iterations after which new plant grows

    private enum EntityType { CARNIVORE, HERBIVORE, PLANT }

    //Iteration at which plant/herbivore/carnivore move
    private static int plantIter = MIN_PLNT_ITER + rand.nextInt(MAX_PLNT_ITER - MIN_PLNT_ITER + 1);
    private static int herbivoreIter = MIN_HERB_ITER + rand.nextInt(MAX_HERB_ITER - MIN_HERB_ITER + 1);
    private static int carnivoreIter = MIN_CARN_ITER + rand.nextInt(MAX_CARN_ITER - MIN_CARN_ITER + 1);

    static int width  = 20; // Width of earth - default size is 20
    static int height = 20; // Height of earth - default size is 20

    private static Earth instance; //reference to the only instance of earth in program

    private SaveGame save = new SaveGame(); //save game object to enable saving of current state of simulation
    private Entity[][] grid;

    /**
     * Private constructor to enforce Singleton design
     */
    private Earth() { grid = new Entity[height][width]; }

    /**
     * Private copy constructor to allow overwriting of earth
     */
    private Earth(Earth other) { grid = other.grid; }

    /**
     * Get the single Earth instance
     * @return the single earth object in the program
     */
    static Earth getInstance()
    {
        if (instance == null) instance = new Earth();
        return instance;
    }

    /**
     * Gets entity at location (x, y)
     * @param x x-cor of organism
     * @param y y-cor of organism
     * @return Organism at (x, y)
     */
    Entity getEntityAt(Pair<Integer,Integer> pair) { return grid[pair.getXCoord()][pair.getYCoord()]; }
    Entity getEntityAt(int x, int y) {return grid[x][y];}

    /**
     * Set (x, y) on earth grid to entity
     * @param x x-location
     * @param y y-location
     * @param entity entity to placed at (x, y)
     */
    void set(int x, int y, Entity entity)
    {
        grid[x][y] = entity;
    }

    /**
     * Overwrites Earth instance's width and height - can only be done when earth's instance is null
     * @param width initial width of earth
     * @param height initial height of earth
     */
    private static void setSize(int width, int height)
    {
        //trying to set earth size after earth instance has been created throws an exception
        //this should never happen
        if ( instance != null ) throw new SimulationError(SimulationError.ErrorType.SET_SIZE_ERROR);

        Earth.width = width;
        Earth.height = height;
    }

    /**
    *  Initializes earth and game states
    *  @param width width of the earth
    *  @param height height of the earth
    */
    static void initialize(int width, int height)
    {
        setSize(width, height); //set earth's width and height;
        instance = new Earth(); //create Earth instance

        //Note (width/2 + width/3 + width/2 + width + width + width/2) < width * height
        //Ensure this is the case by choosing the minimum of width and height
        //Minimum width/height should be 10 * 10
        int scalingParam = Math.min(width, height);
        createLake(scalingParam/2);  //earth contains one patch of lake
        placeObstacles(scalingParam/2);
        fillEarth(EntityType.CARNIVORE, scalingParam/2);
        fillEarth(EntityType.HERBIVORE, scalingParam);
        fillEarth(EntityType.PLANT, scalingParam + scalingParam/2);
    }

    /**
     * Begin Earth simulation
     * @param iteration the current iteration number
     */
    static void simulate(int iteration)
    {
        if (iteration % carnivoreIter == 0)
        {
            startActionOf(EntityType.CARNIVORE);
            carnivoreIter = MIN_CARN_ITER + rand.nextInt(MAX_CARN_ITER - MIN_CARN_ITER + 1);
        }
        if (iteration % herbivoreIter == 0)
        {
            startActionOf(EntityType.HERBIVORE);
            herbivoreIter = MIN_HERB_ITER + rand.nextInt(MAX_HERB_ITER - MIN_HERB_ITER + 1);
        }
        if (iteration % plantIter == 0)
        {
            startActionOf(EntityType.PLANT);
            growPlants();
            plantIter = MIN_PLNT_ITER + rand.nextInt(MAX_PLNT_ITER - MIN_PLNT_ITER + 1);
        }

        ageAndStopOrganismActivity();
    }

    /**
     * Fills earth with the amount of the specified entity Type
     * Precondition: ensure amount <= free
     * @param entityType type of Entity to fill earth with
     * @param amount the amount of entities to place on earth
     */
    private static void fillEarth(EntityType entityType, int amount)
    {
        //if freelocations < amount, this method gets stuck in an infinite loop
        if (instance.getFreeLocations().size() < amount)
            throw new SimulationError(SimulationError.ErrorType.NOT_ENOUGH_FREE_LOCATIONS);

        for(int i = 0; i < amount & i <= Earth.height*Earth.width; )
        {
            int randX = rand.nextInt(Earth.height);
            int randY = rand.nextInt(Earth.width);

            Pair<Integer,Integer> randPair = new Pair<Integer,Integer>(randX, randY);

            if(Earth.getInstance().getEntityAt(randPair) == null)
            {
                switch (entityType)
                {
                    case CARNIVORE: Earth.getInstance().set(randX, randY, new Carnivore(randPair)); break;
                    case HERBIVORE: Earth.getInstance().set(randX, randY, new Herbivore(randPair)); break;
                    case PLANT:     Earth.getInstance().set(randX, randY, new Plant(randPair)); break;
                }
                i++;
            }
        }
    }

    /**
     * Create a patch of lake objects on the earth
     * lake will be made up of amount tiles
     * @param num the number of spaces that will make up the lake
     */
    private static void createLake(int num)
    {
        if (instance.getFreeLocations().size() < num)
            throw new SimulationError(SimulationError.ErrorType.NOT_ENOUGH_FREE_LOCATIONS);

        boolean getNewLocation = true;
        int prevX = -1, prevY = -1;
        for(int i = 0; i < num; i++)
        {
            int x, y;
            if ( getNewLocation || prevX < 0)
            {
                do {
                    x = rand.nextInt(Earth.height);
                    y = rand.nextInt(Earth.width);

                } while (instance.getEntityAt(x, y) != null);

                Pair<Integer,Integer> pair = new Pair<>(x,y);
                instance.set(x, y, new Lake(pair));
                prevX = x; prevY = y;
                getNewLocation = false;
                continue;
            }

            Pair<Integer, Integer> newPair = new Pair<>(prevX, prevY);
            ArrayList<int[]> locations = instance.getFreeLocationsAround(newPair);
            if (locations.size() == 0) //no free locations around x,y - get a new random location
            {
                i--;
                getNewLocation = true;
                continue;
            }

            Collections.shuffle(locations);
            x = locations.get(0)[0]; y = locations.get(0)[1];
            Pair<Integer,Integer> pair = new Pair<>(x,y);
            instance.set(x, y, new Lake(pair));
            prevX = x; prevY = y;
        }

    }

    /**
\    * Places obstacles on the earth. Groups obstacles with probability 3/5
     * @param num the number of obstacles to place on the earth
     */
    private static void placeObstacles(int num)
    {
        if (instance.getFreeLocations().size() < num)
            throw new SimulationError(SimulationError.ErrorType.NOT_ENOUGH_FREE_LOCATIONS);

        int prevX = -1, prevY = -1;
        boolean keepRandom = false;

        for(int i = 0; i < num & i <= Earth.height*Earth.width; i++)
        {
            //we should pick a new location and start working there
            int x, y;
            if ( keepRandom || prevX < 0 || rand.nextInt(5) > 3 )
            {
                //since num < number of free spaces, we can keep randomizing until we get a free location
                do {
                    x = rand.nextInt(Earth.height);
                    y = rand.nextInt(Earth.width);
                } while( instance.getEntityAt(x, y) != null);

                Pair<Integer,Integer> pair = new Pair<>(x,y);
                instance.set(x, y, new Obstacle(pair));
                prevX = x; prevY = y;
                keepRandom = false;
            }
            else
            {
                Pair<Integer, Integer> newPair = new Pair<>(prevX, prevY);
                ArrayList<int[]> locations = instance.getFreeLocationsAround(newPair);
                //no free locations lets repeat this iteration and randomize
                if (locations.size() == 0)
                {
                    i--;
                    keepRandom = true;
                    continue;
                }

                Collections.shuffle(locations);
                x = locations.get(0)[0]; y = locations.get(0)[1];
                Pair<Integer,Integer> pair = new Pair<>(x,y);
                instance.set(x, y, new Obstacle(pair));
                prevX = x; prevY = y;
            }

        }
    }

    /**
     * Call entity to action - entity will perform its default action
     * @param entityType the type of entity to activate
     */
    private static void startActionOf(EntityType entityType)
    {
        for (int i = 0; i < Earth.height; i++)
        {
            for (int j = 0; j < Earth.width; j++)
            {
                Entity entity = Earth.getInstance().getEntityAt(i, j);
                switch(entityType)
                {
                    case HERBIVORE:
                        if (entity instanceof Herbivore && !((Herbivore) entity).isActive) ((Herbivore) entity).beginAction();
                        break;
                    case CARNIVORE:
                        if (entity instanceof Carnivore && !((Carnivore) entity).isActive) ((Carnivore) entity).beginAction();
                        break;
                    case PLANT:
                        if (entity instanceof Plant ) ((Plant) entity).beginAction();
                        break;
                }
            }
        }
    }

    /**
     * Grow new plants and kill plants as needed
     */
    private static void growPlants()
    {

        ArrayList<int[]> freeLocations = Earth.getInstance().getFreeLocations();
        if ( freeLocations.isEmpty() ) return; //no plants can grow at this iteration
        int index = rand.nextInt( freeLocations.size() ); //pick a random index in the freeLocations array
        int x = freeLocations.get(index)[0];
        int y = freeLocations.get(index)[1];

        Pair<Integer,Integer> pair = new Pair<>(x,y);

        Earth.getInstance().set(x, y, new Plant(pair));
    }

    /**
     * Increase all the ages of animals by 1
     * also stop animal's activity and prepare for next iteration
     */
    private static void ageAndStopOrganismActivity()
    {
        for (int i = 0; i < Earth.height; i++)
        {
            for (int j = 0; j < Earth.width; j++)
            {
                Entity entity = Earth.getInstance().getEntityAt(i, j);
                if (entity instanceof Plant) ((Plant) entity).grow();
                else if (entity instanceof Animal)
                {
                    ((Animal) entity).increaseAge();
                    ((Animal) entity).endAction();
                }
            }
        }
    }

    /**
     * Get list of all free spaces available on earth
     * @return list of 2 element integer arrays
     */
    private ArrayList<int[]> getFreeLocations()
    {
        ArrayList<int[]> freeLocations = new ArrayList<>();

        for(int m = 0; m < height; m++)
            for(int n = 0; n < width; n++)
                if (getEntityAt(m, n) == null) freeLocations.add(new int[]{m, n});

        return freeLocations;
    }

    /**
     * Returns a list of all free Locations around pair(x,y)
     * @param pair pair(x,y)
     * @return list of 2 element integer arrays
     */
    private ArrayList<int[]> getFreeLocationsAround(Pair<Integer, Integer> pair)
    {
        ArrayList<int[]> locations = new ArrayList<>();
        int x = pair.getXCoord(), y = pair.getYCoord();

        int topX = x - 1;
        int leftY = y - 1;
        int rightY = y + 1;
        int bottomX = x + 1;

        if (topX >= 0 && instance.getEntityAt(topX, y) == null)  locations.add(new int[]{topX, y});
        if (leftY >= 0 && instance.getEntityAt(x, leftY) == null) locations.add(new int[]{x, leftY});
        if (rightY < width && instance.getEntityAt(x, rightY) == null) locations.add(new int[]{x, rightY});
        if (bottomX < height && instance.getEntityAt(bottomX, y) == null) locations.add(new int[]{bottomX, y});

        return locations;
    }

    /**
     * Returns a list of Locations around entity
     * @param entity the entity
     * @return list of 2 element integer arrays
     */
    ArrayList<int[]> getLocationsAround(Entity entity)
    {
        int x = entity.getX();
        int y = entity.getY();
        ArrayList<int[]> locations = new ArrayList<>();

        int topX = x - 1;
        int leftY = y - 1;
        int rightY = y + 1;
        int bottomX = x + 1;

        if (topX >= 0)  locations.add(new int[]{topX, y});
        if (leftY >= 0) locations.add(new int[]{x, leftY});
        if (rightY < width) locations.add(new int[]{x, rightY});
        if (bottomX < height) locations.add(new int[]{bottomX, y});

        return locations;
    }

    /**
     * String representation of the earth
     */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Entity[] row : grid)
        {
            for (Entity entity : row)
            {
                if (entity == null) sb.append("." + " ");
                else sb.append(entity.toString()).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Pauses the movement of the earth
     */
    static void pauseGameState() {
        try {
            synchronized (getInstance()) {
                getInstance().wait();
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resumes the movement of entities on the earth
     */
    static void continueGameState() {
        synchronized (getInstance()) {
            getInstance().notify();
        }
    }

    /**
     * Saves current game State to a binary file
     */
    static void saveGameState()
    {
        getInstance().save.saveGameState();
    }

    /**
     * Reads current game State from a saved binary file
     */
    static boolean LoadGameState()
    {
        getInstance().save.loadFromSave();
        return getInstance().save.loadFromSave();
    }

    /**
     * static method to overwrite earth's single instance with another object
     * @param earth Earth object to overwrite default object with
     */
    private static void overwriteInstance(Earth earth)
    {
        instance = new Earth(earth);
    }

    /**
     * inner SaveGame class to enable saving earth's instance and necessary static variables for loading later
     */
    class SaveGame implements Serializable
    {
        private String fileName;
        private File file;

        SaveGame()
        {
            fileName = "save.bin";
            file = new File(fileName);
        }

        boolean loadFromSave()
        {
            try (FileInputStream fstream = new FileInputStream(file)) {
                ObjectInputStream istream = new ObjectInputStream(fstream);

                plantIter = istream.readInt();
                carnivoreIter = istream.readInt();
                herbivoreIter = istream.readInt();
                width = istream.readInt();
                height = istream.readInt();

                Earth.overwriteInstance((Earth)istream.readObject());
                istream.close();

                return true;
            } catch (FileNotFoundException e) {
                throw new SimulationError(SimulationError.ErrorType.BAD_SAVE_FILE);
            } catch (IOException e) {
                throw new SimulationError(SimulationError.ErrorType.BAD_SAVE_FILE);
            } catch (ClassNotFoundException e) {
                throw new SimulationError(SimulationError.ErrorType.BAD_SAVE_FILE);
            }
        }

        boolean saveGameState()
        {
            try ( FileOutputStream fstream = new FileOutputStream(file))
            {
               ObjectOutputStream ostream = new ObjectOutputStream(fstream);

               //Write Constants
                ostream.writeInt(plantIter);
                ostream.writeInt(carnivoreIter);
                ostream.writeInt(herbivoreIter);
                ostream.writeInt(width);
                ostream.writeInt(height);

                //then Write game objects
                ostream.writeObject(getInstance());

               ostream.close();
               return true;
            } catch (FileNotFoundException e)
            {
                throw new SimulationError(SimulationError.ErrorType.UNABLE_TO_SAVE);
            } catch (IOException e)
            {
                throw new SimulationError(SimulationError.ErrorType.UNABLE_TO_SAVE);
            }
        }
    }

}
