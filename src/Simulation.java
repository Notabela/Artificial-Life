/**
 * AnimalSimulation.java
 * Purpose: defines a game involving animals and plants on an earth
 *
 * @author Daniel Obeng
 * @version 2.0 3/31/2017
 */

import javax.swing.*;

public class Simulation
{

    public static void main(String[] args)
    {
        IntroInterface intro = new IntroInterface();
        intro.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        intro.setVisible(true);
    }

     public static void newInitialization() {
        String firstText = JOptionPane.showInputDialog("Enter desired Earth width (>= 5)");
        String secondText = JOptionPane.showInputDialog("Enter desired Earth height (>= 5)");

        int userWidth = Integer.parseInt(firstText);
        int userHeight = Integer.parseInt(secondText);

        Earth.initialize(userWidth, userHeight);

        beginSimulation();
    }


    public static void beginSimulation() {
        GridInterface frame = new GridInterface();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addComponents(frame.getContentPane());
        frame.setVisible(true);

        //Game ends when iterations = maxIterations
        int maxIterations = Integer.MAX_VALUE;
        for(int iteration = 1; iteration <= maxIterations; iteration++)
        {
            Earth.simulate(iteration);
            System.out.println(Earth.getInstance());

            frame.renew();
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
