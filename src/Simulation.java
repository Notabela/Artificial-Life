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
        newInitialization();
    }

     private static void newInitialization()
     {
         int dialogResult = JOptionPane.showConfirmDialog(null,
                 "If there is a previous save you'd like to continue from, choose YES",
                 "Artificial Life",JOptionPane.YES_NO_OPTION);

         if(dialogResult == JOptionPane.YES_OPTION)
         {
             boolean noError = false;
             try
             {
                 Earth.LoadGameState();
                 noError = true;
                 Simulation.beginSimulation();
             } catch (SimulationError error)
             {
                 JOptionPane.showMessageDialog(null,
                         error.toString(),
                         "File Error",
                         JOptionPane.ERROR_MESSAGE);
             }
             if (!noError) startNewGame();
         }
         else startNewGame();
    }

    static void startNewGame()
    {
        String firstText = JOptionPane.showInputDialog("Enter desired Earth width (>= 5)");
        if (firstText == null) System.exit(0);

        String secondText = JOptionPane.showInputDialog("Enter desired Earth height (>= 5)");
        if (secondText == null) System.exit(0);

        int userWidth = Integer.parseInt(firstText);
        int userHeight = Integer.parseInt(secondText);

        //Bad Input - Game will Exist
        if(userWidth < 5 || userHeight < 5)
        {
            JOptionPane.showMessageDialog(null,
                    "Width or Height < 5, Game will exit", "Fatal Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        Earth.initialize(userWidth, userHeight);

        beginSimulation();
    }


    static void beginSimulation()
    {
        GridInterface frame = new GridInterface();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addComponents(frame.getContentPane());
        frame.setVisible(true);

        //Game ends when iterations = maxIterations
        int maxIterations = Integer.MAX_VALUE;
        for(int iteration = 1; iteration <= maxIterations; iteration++)
        {
            Earth.simulate(iteration);
            frame.renew();
            //code to slow down game a bit
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
