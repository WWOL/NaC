package mccarthy.brian.dte.nac;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class handles the drawing of the "Control Area" as well as the 
 * event handling for the buttons
 *
 * @author Brian McCarthy
 */
public class ControlsPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1;

    // Keep a reference to the object so a new one isn't created every painting 
    private JButton reset = null;
    private JButton scores = null;
    private JButton step = null;
    private JComboBox<String> combo = null;
    private JButton help = null;
    private JButton exit = null;

    private boolean setup = false;

    public ControlsPanel() {
        setLocation(600, 0);
        setSize(200, 600);
        // Initialise all components
        setupComponents();
    }

    /*
     * This method is called whenever the OS needs to update the window
     * This can include when it is first drawn and when it is resized + more 
     */
    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("paint");
        //super.paintComponent(g);

        if (!setup) {
            /*
             * Add all the buttons to the panel, only once
             * NOTE: The buttons already have their position and size set
             * This is tidier than doing all that setup here
             */
            add(reset);
            add(scores);
            add(step);
            add(combo);
            add(help);
            add(exit);
            setup = true;

            g.setColor(Color.DARK_GRAY);
            // This makes the dark grey background
            g.fillRect(0, 0, 200, 600);

            // Draw "Control Area" at the top of the panel in the center
            g.setColor(Color.GREEN);
            Font font = new Font("Arial", Font.PLAIN, 30);
            g.setFont(font);
            int width = g.getFontMetrics().stringWidth("Control Area");
            g.drawString("Control Area", 100 - (width / 2), 35);
        }
    }

    private void setupComponents() {
        setupResetButton();
        setupScoresButton();
        setupStepButton();
        setupLogicComboBox();
        setupLogicSelectorOptions();
        setupHelpButton();
        setupExitButton();
    }

    private JButton setupResetButton() {
        reset = new JButton("Reset");
        reset.setLocation(10, 60);
        reset.setSize(180, 50);
        reset.setActionCommand("CONTROLS_RESET");
        reset.addActionListener(this);
        return reset;
    }

    private JButton setupScoresButton() {
        scores = new JButton("Scores");
        scores.setLocation(10, 120);
        scores.setSize(180, 50);
        scores.setActionCommand("CONTROLS_SCORES");
        scores.addActionListener(this);
        return scores;
    }

    private JButton setupStepButton() {
        step = new JButton("Step");
        step.setLocation(10, 180);
        step.setSize(180, 50);
        step.setActionCommand("CONTROLS_STEP");
        step.addActionListener(this);
        return step;
    }

    private JComboBox<String> setupLogicComboBox() {
        combo = new JComboBox<String>();
        combo.setLocation(10, 240);
        combo.setSize(180, 25);
        
        return combo;
    }

    private void setupLogicSelectorOptions() {
        List<GameLogic> logicList = GameRunner.getGameBoard().getLogicList();
        for (GameLogic logic : logicList) {
            System.out.println("Add Logic: " + logic);
            combo.addItem(logic.getId());
        }
        for (GameLogic logic : GameRunner.getGameBoard().getLogicList()) {
            if (logic.getId().equals(GameRunner.getGameBoard().getLogic().getId())) {
                combo.setSelectedItem(logic.getId());
            }
        }
        // NOTE: We do this here because otherwise an addItem updates the selection causing the current logic to be changed
        combo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Change the logic type
                    GameRunner.getGameBoard().setLogicType((String) e.getItem());
                }
            }
        });
    }

    private JButton setupHelpButton() {
        help = new JButton("Help / About");
        help.setLocation(10, 480);
        help.setSize(180, 50);
        help.setActionCommand("CONTROLS_HELP");
        help.addActionListener(this);
        help.setToolTipText("");
        return help;
    }

    private JButton setupExitButton() {
        exit = new JButton("Exit");
        exit.setLocation(10, 540);
        exit.setSize(180, 50);
        exit.setActionCommand("CONTROLS_EXIT");
        exit.addActionListener(this);
        exit.setToolTipText("Save and exit");
        return exit;
    }

    // This method responds to all button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CONTROLS_RESET")) {
            int result = JOptionPane.showConfirmDialog(null, "Reset scores too?", "Reset Scores?", JOptionPane.YES_NO_OPTION);
            GameRunner.getGameBoard().clear();
            GameRunner.getGameBoard().gamePlayed();
            GameRunner.repaint();
            if (result == JOptionPane.YES_OPTION) {
                GameRunner.getGameBoard().resetScores();
            }
        } else if (e.getActionCommand().equals("CONTROLS_SCORES")) {
            JOptionPane.showMessageDialog(null, GameRunner.getScoresInfo(), "Scores", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getActionCommand().equals("CONTROLS_STEP")) {
            if (GameRunner.getGameBoard().isPlayersTurn()) {
                JOptionPane.showMessageDialog(null, "It's your turn!", "Your turn.", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            GameRunner.getGameBoard().placeComputerPiece();
            GameRunner.getGameBoard().checkForWin();
        } else if (e.getActionCommand().equals("CONTROLS_HELP")) {
            JOptionPane.showMessageDialog(null, GameRunner.getHelpText() + GameRunner.getAboutText() + "Logic: " + GameRunner.getGameBoard().getLogic(), "Help / About", JOptionPane.PLAIN_MESSAGE);
        } else if (e.getActionCommand().equals("CONTROLS_EXIT")) {
            GameRunner.exit();
        }
    }

}
