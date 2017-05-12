import java.security.SecureRandom;
import java.util.ArrayList;
/**
 * Earth.java
 * Purpose: defines Earth on which Entities will sit (Designed as a singleton)
 *
 * @author Daniel Obeng
 * @version 1.0 3/31/2017
 */
public class Earth
{

    //these constants are made available here to make changing how the earth is quick and easy
    private static final int minCarnivoreIter = 2;  // iterations after which carnivore moves
    private static final int maxCarnivoreIter = 4;  // iterations after which carnivore moves
    private static final int minHerbivoreIter = 1;  // iterations after which carnivore moves
    private static final int maxHerbivoreIter = 3;  // iterations after which carnivore moves
    private static final int minPlantIter  = 3;  // min iterations after which new plant grows
    private static final int maxPlantIter  = 5;  // max iterations after which new plant grows
    private static SecureRandom randomNumbers = new SecureRandom(); //random number generator

    private static final int sleepTime = 1000;   // wait time of main loop to limit speed of game

    private static int initPlants;       // initial number of plants on earth
    private static int initHerbivore;    // initial number of herbivores on earth
    private static int initCarnivore;   // initial number of carnivores on earth

    public static int globalWidth;     //globally available width of earth instance
    public static int globalHeight;   //height of earth instance with default value

    private int width;    //width of an earth object
    private int height;   //height of an earth object

    private static Earth instance; //reference to the only instance of earth in program
    private Organism[][] grid;

    /**
     * Overwrite Earth instance width and height - can only be done when earth
     * instance has never been called
     */
    public static void setSize(int width, int height)
    {
        if ( instance != null ) return;

        globalWidth = width;
        globalHeight = height;
    }

    /**
     * Private constructor to enforce Singleton design
     */
    private Earth()
    {
        width = globalWidth;
        height = globalHeight;
        grid = new Organism[globalHeight][globalWidth];
    }

    /**
     * Private copy constructor to allow overwriting of earth
     */
    private Earth(Earth other)
    {
        globalWidth = width = other.width;
        globalHeight = height = other.height;
        grid = other.grid;
    }

    /**
     * Get organism at location x and y
     * @param x x-location of organism
     * @param y y-location of organism
     * @return Organism at x,y
     */
    public Organism getOrganismAt(int x, int y)
    {
        return grid[x][y];
    }

    /**
     * Set location x,y on earth grid to organism
     * @param x x-location
     * @param y y-location
     * @param organism organism to place at location x,y
     */
    public void set(int x, int y, Organism organism)
    {
        grid[x][y] = organism;
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

        setSize(width, height);   //set earth's width and size
        Earth earth = Earth.getInstance(); //get/create instance of earth

        //MARK: initialize Plants
        for(int i = 0; i < initPlants; )
        {
            int randX = randomNumbers.nextInt(globalHeight);
            int randY = randomNumbers.nextInt(globalWidth);

            if(earth.getOrganismAt(randX, randY) == null)
            {
                earth.set(randX, randY, new Plant(randX, randY));
                i++;
            }
        }

        //MARK: initialize Herbivores
        for(int i = 0; i < initHerbivore; )
        {
            int randX = randomNumbers.nextInt(globalHeight);
            int randY = randomNumbers.nextInt(globalWidth);

            if(earth.getOrganismAt(randX, randY) == null)
            {
                earth.set(randX, randY, new Herbivore(randX, randY));
                i++;
            }
        }

        //MARK: Initialize Carnivores
        for(int i = 0; i < initCarnivore; )
        {
            int randX = randomNumbers.nextInt(globalHeight);
            int randY = randomNumbers.nextInt(globalWidth);

            if(earth.getOrganismAt(randX, randY) == null)
            {
                earth.set(randX, randY, new Carnivore(randX, randY));
                i++;
            }
        }

    }

    /**
     * Method to activate each Carnivore
     */
    private static void activateCarnivores()
    {
        for (int i = 0; i < Earth.globalHeight; i++)
        {
            for (int j = 0; j < Earth.globalWidth; j++)
            {
                Organism organism = Earth.getInstance().getOrganismAt(i, j);
                if (organism instanceof Carnivore && !((Carnivore) organism).isActive) organism.activate();
            }
        }
    }

    /**
     * Method to activate all herbivores
     */
    private static void activateHerbivores()
    {
        for (int i = 0; i < Earth.globalHeight; i++)
        {
            for (int j = 0; j < Earth.globalWidth; j++)
            {
                Organism organism = Earth.getInstance().getOrganismAt(i, j);
                if (organism instanceof Herbivore && !((Herbivore) organism).isActive) organism.activate();
            }
        }
    }

    /**
     * Method to randomly grow and kill plants as needed
     */
    private static void activatePlants()
    {
        Earth earth = Earth.getInstance();

        for (int i = 0; i < Earth.globalHeight; i++)
        {
            for (int j = 0; j < Earth.globalWidth; j++)
            {
                Organism organism = earth.getOrganismAt(i, j);
                if (organism instanceof Plant) organism.activate();
            }
        }

        ArrayList<int[]> freeLocations = earth.getFreeLocations();
        if ( freeLocations.isEmpty() ) return; //no plants can grow at this iteration
        int index = randomNumbers.nextInt( freeLocations.size() ); //pick a random index in the freeLocations array
        int x = freeLocations.get(index)[0];
        int y = freeLocations.get(index)[1];

        earth.set(x, y, new Plant(x, y));
    }

    /**
     * Method to start the Earth Simulation
     */
    public static void beginSimulation(int maxIterations)
    {

        int iteration = 1;

        //iteration for organisms to increase randomness
        int plantIter = minPlantIter + randomNumbers.nextInt(maxPlantIter - minPlantIter + 1);
        int herbivoreIter = minHerbivoreIter + randomNumbers.nextInt(maxHerbivoreIter - minHerbivoreIter + 1);
        int carnivoreIter = minCarnivoreIter + randomNumbers.nextInt(maxCarnivoreIter - minCarnivoreIter + 1);

        //Game ends when iterations = maxIterations
        while (iteration <= maxIterations)
        {
            System.out.println(Earth.getInstance() + "\n");

            if (iteration % carnivoreIter == 0)
            {
                activateCarnivores();
                carnivoreIter = minCarnivoreIter + randomNumbers.nextInt(maxCarnivoreIter - minCarnivoreIter + 1);
            }
            if (iteration % herbivoreIter == 0)
            {
                activateHerbivores();
                herbivoreIter = minHerbivoreIter + randomNumbers.nextInt(maxHerbivoreIter - minHerbivoreIter + 1);
            }
            if (iteration % plantIter == 0)
            {
                activatePlants();
                plantIter = minPlantIter + randomNumbers.nextInt(maxPlantIter - minPlantIter + 1);
            }

            ageAnimals();
            deactivateAnimals();
            iteration++;

            //pause to prevent game from moving too fast
            try
            {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Increase all the ages of animals by 1
     */
    private static void ageAnimals()
    {
        for (int i = 0; i < Earth.globalHeight; i++)
        {
            for (int j = 0; j < Earth.globalWidth; j++)
            {
                Organism organism = Earth.getInstance().getOrganismAt(i, j);
                if (organism != null) organism.increaseAge();
            }
        }
    }

    /**
     * Reset the isActive Variables of the Animals to prepare for next Iteration
     */
    private static void deactivateAnimals()
    {
        for (int i = 0; i < Earth.globalHeight; i++)
        {
            for (int j = 0; j < Earth.globalWidth; j++)
            {
                Organism organism = Earth.getInstance().getOrganismAt(i, j);
                if (organism instanceof Animal) ((Animal) organism).deactivate();
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
                if (getOrganismAt(m, n) == null) freeLocations.add(new int[]{m, n});

        return freeLocations;
    }

    /**
     * Returns a list of Locations around organism
     * @param organism the organism
     * @return list of 2 element integer arrays
     */
    public ArrayList<int[]> getLocationsAround(Organism organism)
    {
        int x = organism.getX();
        int y = organism.getY();
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
        for (Organism[] row : grid)
        {
            for (Organism organism : row)
            {
                if (organism == null) sb.append("." + " ");
                else sb.append(organism.toString() + " ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

}
