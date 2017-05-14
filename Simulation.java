/**
 * AnimalSimulation.java
 * Purpose: defines a game involving animals and plants on an earth
 *
 * @author Daniel Obeng
 * @version 2.0 3/31/2017
 */

import java.util.Scanner;

public class Simulation
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        //Get Earth width
        int userWidth, userHeight, maxIterations;
        do
        {
            System.out.print("Enter a Width >= 5: ");
            userWidth = scanner.nextInt();
        } while (userWidth < 5);

        //Get Earth height
        do
        {
            System.out.print("Enter a Height >= 5: ");
            userHeight = scanner.nextInt();
        } while (userHeight < 5);

        //Get number of iterations
        System.out.print("Enter the number of iterations: ");
        maxIterations = scanner.nextInt();

        Earth.initialize(userWidth, userHeight);

        //Game ends when iterations = maxIterations
        for(int iteration = 1; iteration <= maxIterations; iteration++)
        {
            Earth.simulate(iteration);
            System.out.println(Earth.getInstance());

            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
