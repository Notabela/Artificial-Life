import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by socra_000 on 5/12/2017.
 */
public class GridInterface extends JFrame {

    private JButton[] buttons;
    JButton saveButton = new JButton("Save");
    JButton loadButton = new JButton("Load");
    JButton exitButton = new JButton("Exit");

    //Add more images for obstacles. Maximum size 50px x 50px
    private static final String[] object = {
            "herbivore.gif", "carnivore.gif", "plant.gif",
            "free.gif"};
    private final Icon[] icons = {
            new ImageIcon(getClass().getResource(object[0])),
            new ImageIcon(getClass().getResource(object[1])),
            new ImageIcon(getClass().getResource(object[2])),
            new ImageIcon(getClass().getResource(object[3]))
    };

    public GridInterface()
    {
        super("Earth");
    }

    public void addComponents(final Container pane) {
        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(Earth.width, Earth.height));
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,3));
        buttons = new JButton[Earth.height *Earth.width];

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

    private void addObjects(Container jPanel) {
        for (int i = 0; i < Earth.width; i++) {
            for (int j = 0; j < Earth.height; j++) {

                Organism organismAt = Organism.earth.getOrganismAt(i, j);

                if (organismAt instanceof Herbivore) {
                    buttons[i] = new JButton((icons[0]));
                    jPanel.add(buttons[i]);
                }

                else if (organismAt instanceof Carnivore) {
                    buttons[i] = new JButton((icons[1]));
                    jPanel.add(buttons[i]);
                }

                else if (organismAt instanceof Plant) {
                    buttons[i] = new JButton((icons[2]));
                    jPanel.add(buttons[i]);
                }

                else if (organismAt == null) {
                    buttons[i] = new JButton((icons[3]));
                    jPanel.add(buttons[i]);
                }
            }
        }
    }

    //TODO: Get this to refresh the GUI to latest instance
    public void renew() {

        this.revalidate();
        this.repaint();

    }
}
