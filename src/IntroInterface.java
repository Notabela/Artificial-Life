
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by socra_000 on 5/27/2017.
 */
public class IntroInterface extends JFrame {

    public IntroInterface() {
        super("Artificial Life");
        setSize(500,250);
        addComponents();
    }

    private void addComponents() {

        JLabel label = new JLabel("Welcome to Artificial Life!");
        JLabel label2 = new JLabel("Press play to start a new game, " +
                "or browse for a loadable file and press load");
        JPanel panel = new JPanel();
        JPanel browse = new JPanel();
        JTextField filePath = new JTextField(25);
        JButton browseButton = new JButton("Browse");
        JButton exitButton = new JButton("Exit");
        JButton loadButton = new JButton("Load");
        JButton playButton = new JButton("Play");

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 28));

        browse.setLayout(new FlowLayout());
        browse.add(filePath);
        browse.add(browseButton);
        browse.add(label2);

        panel.setLayout(new GridLayout(0,3));
        panel.add(playButton);
        panel.add(loadButton);
        panel.add(exitButton);

        JFileChooser fileChooser = new JFileChooser();

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Continue the game
                Simulation.newInitialization();
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setAcceptAllFileFilterUsed(false);

                int rVal = fileChooser.showOpenDialog(null);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    filePath.setText(fileChooser.getSelectedFile().toString());
                }
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Load the game

                boolean noError = false;
                File inputFile = fileChooser.getSelectedFile();

                try {
                    Earth.LoadGameState(inputFile);
                    noError = true;
                }
                catch (NullPointerException noFile) {
                    JOptionPane.showMessageDialog(null,
                            "No file was selected to load!",
                            "File Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                if(noError && Earth.LoadGameState(inputFile)) {
                    Simulation.beginSimulation();
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        add(label, BorderLayout.NORTH);
        add(browse, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

    }
}
