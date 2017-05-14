import javax.swing.*;
import java.awt.*;

/**
 * Created by socra_000 on 5/12/2017.
 */
public class GridInterface extends JFrame {

    private final JButton[] buttons;
    private final JButton[] panelButtons;

    //Add more images for obstacles. Maximum size 50px x 50px
    private static final String[] names = {
            "herbivore.gif", "carnivore.gif", "plant.gif",
            "free.gif"};
    private final Icon[] icons = {
            new ImageIcon(getClass().getResource(names[0])),
            new ImageIcon(getClass().getResource(names[1])),
            new ImageIcon(getClass().getResource(names[2])),
            new ImageIcon(getClass().getResource(names[3]))
    };
    private final GridLayout gridLayout;
    private final Container container;
    private final JPanel panel;


    public GridInterface()
    {
        super("Earth");
        gridLayout = new GridLayout(Earth.width,Earth.height);
        container = getContentPane();
        setLayout(gridLayout);
        buttons = new JButton[Earth.height *Earth.width];
        panelButtons = new JButton[5];
        panel = new JPanel();
        panel.setLayout(new GridLayout(1,panelButtons.length));

        //TODO: Change the logic for our objects.
        for (int i = 0; i < Earth.width; i++) {
            for (int j = 0; j < Earth.height; j++) {

                Organism organismAt = Organism.earth.getOrganismAt(i, j);

                //TODO: Statement for herbivore
                if (organismAt instanceof Herbivore) {
                    buttons[i] = new JButton((icons[0]));
                    add(buttons[i]);
                }
                //TODO: Statement for carnivore
                else if (organismAt instanceof Carnivore) {
                    buttons[i] = new JButton((icons[1]));
                    add(buttons[i]);
                }
                //TODO: Statement for plant
                else if (organismAt instanceof Plant) {
                    buttons[i] = new JButton((icons[2]));
                    add(buttons[i]);
                }
                //TODO: Statement for free space
                else if (organismAt == null) {
                    buttons[i] = new JButton((icons[3]));
                    add(buttons[i]);
                }
            }
        }

        //This gets added to the grid, going to have to call seperately.
        for (int j = 0; j <panelButtons.length; j++) {
            panelButtons[j] = new JButton("Button" + (j+1));
            panel.add(panelButtons[j]);
        }

        add(panel, BorderLayout.SOUTH);
    }
}
