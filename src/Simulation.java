/**
 * AnimalSimulation.java
 * Purpose: defines a game involving animals and plants on an earth
 *
 * @author Daniel Obeng
 * @version 2.0 3/31/2017
 */

import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.util.Scanner;

public class Simulation
{
    public static void main(String[] args)
    {

        String firstText = JOptionPane.showInputDialog("Enter desired Earth width (>= 5)");
        String secondText = JOptionPane.showInputDialog("Enter desired Earth height (>= 5)");
        //String thirdText = JOptionPane.showInputDialog("Enter number of iterations");

        int userWidth = Integer.parseInt(firstText);
        int userHeight = Integer.parseInt(secondText);
        //int maxIterations = Integer.parseInt(thirdText);

        int maxIterations = Integer.MAX_VALUE;

        Earth.initialize(userWidth, userHeight);
        GridInterface frame = new GridInterface();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addComponents(frame.getContentPane());
        frame.setVisible(true);

        //Game ends when iterations = maxIterations
        for(int iteration = 1; iteration <= maxIterations; iteration++)
        {
            Earth.simulate(iteration);
            System.out.println(Earth.getInstance());
            //TODO: Call a function that refreshes GUI with latest instance
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