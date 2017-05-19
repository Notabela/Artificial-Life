import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by socra_000 on 5/12/2017.
 */
public class GridInterface extends JFrame {

    JButton saveButton = new JButton("Save");
    JButton loadButton = new JButton("Load");
    JButton exitButton = new JButton("Exit");

    //Add more images for obstacles. Maximum size 50px x 50px
    private static final String[] object = {
            "media/herbivore.gif", "media/carnivore.gif", "media/plant.gif",
            "media/free.gif"};
    private final Icon[] icons = {
            new ImageIcon(getClass().getResource(object[0])),
            new ImageIcon(getClass().getResource(object[1])),
            new ImageIcon(getClass().getResource(object[2])),
            new ImageIcon(getClass().getResource(object[3]))
    };

    JPanel grid = new JPanel();
    JPanel panel = new JPanel();
    JButton[] buttons = new JButton[Earth.height *Earth.width];

    public GridInterface()
    {
        super("Earth");
        setSize(Earth.height*50,Earth.width*68);
    }

    public void addComponents(final Container pane) {

        grid.setLayout(new GridLayout(Earth.width, Earth.height));
        panel.setLayout(new GridLayout(0,3));

        addObjects(grid);

        //TODO: Add buttons to start and pause.
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(exitButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Save the game
                Earth.saveGameState();
            }
        });

        //TODO: Get this method to load state onto GUI
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Load the game
                Earth.LoadGameState();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        pane.add(grid, BorderLayout.NORTH);
        pane.add(new JSeparator(), BorderLayout.CENTER);
        pane.add(panel, BorderLayout.SOUTH);
    }

    private void addObjects(JPanel frame) {
        for (int i = 0; i < Earth.width; i++) {
            for (int j = 0; j < Earth.height; j++) {

                Entity entity = Organism.earth.getEntityAt(i, j);

                //TODO: Create GUI that shows data of object (Energy and Age)
                if (entity instanceof Herbivore) {
                    buttons[i] = new JButton((icons[0]));
                    frame.add(buttons[i]);

                    buttons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(null, "Energy: " + ((Herbivore) entity).energy + "\n"
                                    + "Age: " + ((Herbivore) entity).age + "\n" + "Fed: " + ((Herbivore) entity).feedCount + " times \n\n" );
                        }
                    });
                }

                else if (entity instanceof Carnivore) {
                    buttons[i] = new JButton((icons[1]));
                    frame.add(buttons[i]);

                    buttons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(null, "Energy: " + ((Carnivore) entity).energy + "\n"
                                    + "Age: " + ((Carnivore) entity).age + "\n" + "Fed: " + ((Carnivore) entity).feedCount + " times \n\n" );
                        }
                    });
                }

                else if (entity instanceof Plant) {
                    buttons[i] = new JButton((icons[2]));
                    frame.add(buttons[i]);

                    buttons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(null, "Energy: N/A \n"
                                    + "Age: " + ((Plant) entity).age + "\n\n");
                        }
                    });
                }

                else if (entity == null) {
                    buttons[i] = new JButton((icons[3]));
                    frame.add(buttons[i]);

                    buttons[i].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JOptionPane.showMessageDialog(null, "Energy: N/A \n"
                                    + "Age: N/A \n\n");
                        }
                    });
                }
            }
        }
    }

    public void renew() {

        this.grid.removeAll();

        addObjects(grid);

        this.revalidate();
        this.repaint();

    }
}
