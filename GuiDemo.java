import javax.swing.*;
import java.awt.*;

/**
 * Created by socra_000 on 5/12/2017.
 */

//This class will be moved to Simulation class once ready.
public class GuiDemo {
    public static void main(String[] args) {
        GridInterface grid = new GridInterface();
        grid.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        grid.setSize(750,750);
        grid.setVisible(true);
    }
}
