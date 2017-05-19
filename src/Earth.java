import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Earth.java
 * Purpose: defines Earth on which Entities will sit (Designed as a singleton)
 *
 * @author Daniel Obeng
 * @version 1.0 3/31/2017
 */
public class Earth implements Serializable
{

    //these constants are made available here to make changing how the earth is quick and easy
    private static final int minCarnivoreIter = 2;  // iterations after which carnivore moves
    private static final int maxCarnivoreIter = 4;  // iterations after which carnivore moves
    private static final int minHerbivoreIter = 1;  // iterations after which carnivore moves
    private static final int maxHerbivoreIter = 3;  // iterations after which carnivore moves
    private static final int minPlantIter  = 3;  // min iterations after which new plant grows
    private static final int maxPlantIter  = 5;  // max iterations after which new plant grows

    private static SecureRandom randomNumbers = new SecureRandom(); //random number generator

    private enum EntityType { CARNIVORE, HERBIVORE, PLANT, OBSTACLE, LAKE }

    //Iteration at which plant/herbivore/carnivore move
    private static int plantIter = minPlantIter + randomNumbers.nextInt(maxPlantIter - minPlantIter + 1);
    private static int herbivoreIter = minHerbivoreIter + randomNumbers.nextInt(maxHerbivoreIter - minHerbivoreIter + 1);
    private static int carnivoreIter = minCarnivoreIter + randomNumbers.nextInt(maxCarnivoreIter - minCarnivoreIter + 1);

    private static int initPlants;       // initial number of plants on earth
    private static int initHerbivore;    // initial number of herbivores on earth
    private static int initCarnivore;   // initial number of carnivores on earth
    private static int initObstacles;   // initial number of obstacles
    private static int initLakes;       // initial number of lakes

    public static int width = 20;     //globally available width of earth instance S- 20 as default for testing
    public static int height = 20;   //height of earth instance with default value

    private static Earth instance; //reference to the only instance of earth in program

    SaveGame save = new SaveGame();
    private Entity[][] grid;

    /**
     * Overwrite Earth instance width and height - can only be done when earth
     * instance has never been called
     */
    public static void setSize(int width, int height)
    {
        if ( instance != null ) return;

        Earth.width = width;
        Earth.height = height;
    }

    /**
     * Private constructor to enforce Singleton design
     */
    private Earth() { grid = new Entity[height][width]; }

    /**
     * Private copy constructor to allow overwriting of earth
     */
    private Earth(Earth other) { grid = other.grid; }

    /**
     * Get organism at location x and y
     * @param x x-location of organism
     * @param y y-location of organism
     * @return Organism at x,y
     */
    public Entity getEntityAt(int x, int y)
    {
        return grid[x][y];
    }

    /**
     * Set location x,y on earth grid to entity
     * @param x x-location
     * @param y y-location
     * @param entity entity to place at location x,y
     */
    public void set(int x, int y, Entity entity)
    {
        grid[x][y] = entity;
    }

    /**
     * static method to get the single Earth instance
     * setsize should be called before getting the instance of the earth
     * @return the single earth object in the program
     */
    public static Earth getInstance()
    {
        if (instance == null) instance = new Earth();
        return instance;
    }

    /**
     * Save game //TODO
     */
    public static void saveGameState()
    {
        getInstance().save.saveGameState();
    }

    /**
     * Load game //TODO
     */
    public static void LoadGameState()
    {
        getInstance().save.loadFromSave();
    }

    /**
     * static method to overwrite earth's single instance with another object
     * @param earth Earth object to overwrite default object with
     */
    public static void overwriteInstance(Earth earth)
    {
        instance = new Earth(earth);
    }

    /**
    *  Initialize earth and game states
    *  @param width width of the earth
    *  @param height height of the earth
    */
    public static void initialize(int width, int height)
    {
        initPlants = width + width/2;
        initHerbivore = width;
        initCarnivore = width/2;
        initObstacles = width/3;

        setSize(width, height);   //set earth's width and size

        fillEarth(EntityType.CARNIVORE, initCarnivore);
        fillEarth(EntityType.HERBIVORE, initHerbivore);
        fillEarth(EntityType.PLANT, initPlants);
        fillEarth(EntityType.OBSTACLE, initLakes);
    }

    /**
     * Precondition: ensure amount <= earth.freelocations
     * @param entityType
     * @param amount
     */
    private static void fillEarth(EntityType entityType, int amount)
    {
        for(int i = 0; i < amount & i <= Earth.height*Earth.width; )
        {
            int randX = randomNumbers.nextInt(Earth.height);
            int randY = randomNumbers.nextInt(Earth.width);

            if(Earth.getInstance().getEntityAt(randX, randY) == null)
            {
                switch (entityType)
                {
                    case CARNIVORE:
                        Earth.getInstance().set(randX, randY, new Carnivore(randX, randY));
                        break;
                    case HERBIVORE:
                        Earth.getInstance().set(randX, randY, new Herbivore(randX, randY));
                        break;
                    case PLANT:
                        Earth.getInstance().set(randX, randY, new Plant(randX, randY));
                        break;
                    case LAKE:
                        Earth.getInstance().set(randX, randY, new Lake(randX, randY));
                        break;
                    case OBSTACLE:
                        Earth.getInstance().set(randX, randY, new Obstacle(randX, randY));
                        break;
                }
                i++;
            }
        }
    }

    private static void activateOrganism(EntityType entityType)
    {
        for (int i = 0; i < Earth.height; i++)
        {
            for (int j = 0; j < Earth.width; j++)
            {
                Entity entity = Earth.getInstance().getEntityAt(i, j);
                switch(entityType)
                {
                    case HERBIVORE:
                        if (entity instanceof Herbivore && !((Herbivore) entity).isActive) ((Herbivore) entity).activate();
                        break;
                    case CARNIVORE:
                        if (entity instanceof Carnivore && !((Carnivore) entity).isActive) ((Carnivore) entity).activate();
                        break;
                    case PLANT:
                        if (entity instanceof Plant ) ((Plant) entity).activate();
                        break;
                }
            }
        }
    }

    /**
     * Method to randomly grow and kill plants as needed
     */
    private static void growPlants()
    {

        ArrayList<int[]> freeLocations = Earth.getInstance().getFreeLocations();
        if ( freeLocations.isEmpty() ) return; //no plants can grow at this iteration
        int index = randomNumbers.nextInt( freeLocations.size() ); //pick a random index in the freeLocations array
        int x = freeLocations.get(index)[0];
        int y = freeLocations.get(index)[1];

        Earth.getInstance().set(x, y, new Plant(x, y));
    }

    /**
     * Method to start the Earth Simulation
     */
    public static void simulate(int iteration)
    {
            if (iteration % carnivoreIter == 0)
            {
                activateOrganism(EntityType.CARNIVORE);
                carnivoreIter = minCarnivoreIter + randomNumbers.nextInt(maxCarnivoreIter - minCarnivoreIter + 1);
            }
            if (iteration % herbivoreIter == 0)
            {
                activateOrganism(EntityType.HERBIVORE);
                herbivoreIter = minHerbivoreIter + randomNumbers.nextInt(maxHerbivoreIter - minHerbivoreIter + 1);
            }
            if (iteration % plantIter == 0)
            {
                activateOrganism(EntityType.PLANT);
                growPlants();
                plantIter = minPlantIter + randomNumbers.nextInt(maxPlantIter - minPlantIter + 1);
            }

            ageAndDeactivateAnimals();
    }

    /**
     * Increase all the ages of animals by 1
     * also deactivate animals and prepare for next iteration
     */
    private static void ageAndDeactivateAnimals()
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
                    ((Animal) entity).deactivate();
                }
            }
        }
    }

    /**
     * Returns a list of all free spaces available on earth
     * @return list of 2 element integer arrays
     */
    public ArrayList<int[]> getFreeLocations()
    {
        ArrayList<int[]> freeLocations = new ArrayList<>();

        for(int m = 0; m < height; m++)
            for(int n = 0; n < width; n++)
                if (getEntityAt(m, n) == null) freeLocations.add(new int[]{m, n});

        return freeLocations;
    }

    /**
     * Returns a list of Locations around entity
     * @param entity the entity
     * @return list of 2 element integer arrays
     */
    public ArrayList<int[]> getLocationsAround(Entity entity)
    {
        int x = entity.getX();
        int y = entity.getY();
        ArrayList<int[]> locations = new ArrayList<>();

        int topX = x - 1, topY = y;
        int leftX = x, leftY = y - 1;
        int rightX = x, rightY = y + 1;
        int bottomX = x + 1, bottomY = y;

        if (topX >= 0)  locations.add(new int[]{topX, topY});
        if (leftY >= 0) locations.add(new int[]{leftX, leftY});
        if (rightY < width) locations.add(new int[]{rightX, rightY});
        if (bottomX < height) locations.add(new int[]{bottomX, bottomY});

        return locations;
    }

    /**
     * Method to print the current state of the earth instance
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
                else sb.append(entity.toString() + " ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    class SaveGame implements Serializable
    {
        private String fileName;
        private File file;

        public SaveGame()
        {
            fileName = "save.bin";
            file = new File(fileName);
        }

        public boolean loadFromSave()
        {
            try (FileInputStream fstream = new FileInputStream(file)) {
                ObjectInputStream istream = new ObjectInputStream(fstream);

                initPlants = istream.readInt();
                initHerbivore = istream.readInt();
                initCarnivore = istream.readInt();
                plantIter = istream.readInt();
                carnivoreIter = istream.readInt();
                herbivoreIter = istream.readInt();
                width = istream.readInt();
                height = istream.readInt();

                Earth.overwriteInstance((Earth)istream.readObject());

                istream.close();

                return true;
            } catch (FileNotFoundException e) {
                System.out.println("Error: Unable to Load " + file.toString());
                ///GUI.displayError("Error: " + file.toString() + " not found")
                return false;
            } catch (IOException e) {
                System.out.println("Error: unable to load " + file.toString());
                ///GUI.displayError("Error: ");
                return false;
            } catch (ClassNotFoundException e) {
                System.out.println("Error: Corrupt Save File");
                throw( new RuntimeException()); //Game should Quit
            }
        }

        public boolean saveGameState()
        {
            try ( FileOutputStream fstream = new FileOutputStream(file))
            {
               ObjectOutputStream ostream = new ObjectOutputStream(fstream);

               //Write Constants
                ostream.writeInt(initPlants);
                ostream.writeInt(initHerbivore);
                ostream.writeInt(initCarnivore);
                ostream.writeInt(plantIter);
                ostream.writeInt(carnivoreIter);
                ostream.writeInt(herbivoreIter);
                ostream.writeInt(width);
                ostream.writeInt(height);

                //then Write game objects
                ostream.writeObject(getInstance());

               ostream.close();
            } catch (FileNotFoundException e)
            {
                System.out.println("Error: Unable to save GameState");
                ///GUI.displayError("Error: " + file.toString() + " not found")
                return false;
            } catch (IOException e)
            {
                System.out.println(e);
                System.out.println("Error: unable to save to" + file.toString());
                ///GUI.displayError("Error: ");
                return false;
            }
            return false;
        }
    }

}
