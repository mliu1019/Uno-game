package MVC.Views;

import CardPackage.Card;
import MVC.Models.GameState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainWindow extends  JFrame {
    private JLabel notification;
    private JButton status;
    private JLabel playerName;

    JPanel cardPanel, wildPanel;

    ArrayList<JButton> cardHolders = new ArrayList<>();

    JButton drawButton, endButton;

    JButton gameStateButton;
    JButton drawPileButton;

    int cardPlayed = -1;

    private String playerID;

    /**
     * The main window for user interface.
     */
    public MainWindow() {
        JPanel mainCont = new JPanel() {
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        setSize(1600, 900);

        mainCont.setLayout(new OverlayLayout(mainCont));
        mainCont.setPreferredSize(new Dimension(1600, 900));
        mainCont.setBackground(Color.DARK_GRAY);

        wildPanel = initWildColorPicker();
        mainCont.add(wildPanel);

        JPanel info = infoPanel();
        mainCont.add(info);

        JPanel myPanel = initContainer();
        myPanel.setBackground(Color.DARK_GRAY);
        mainCont.add(myPanel);



        add(mainCont, BorderLayout.CENTER);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    public void addCallback(WindowAdapter w) {
        this.addWindowListener(w);
    }

    private JPanel initializeCenter() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        return p;
    }

    private JPanel infoPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setMaximumSize(new Dimension(200, 400));
//        p.setBackground(Color.WHITE);
        JPanel p2 = new JPanel();

        status = new JButton();
        status.setVisible(false);
        voidilizeJButton(status);

        p2.add(status);
        p.add(p2, BorderLayout.SOUTH);

        notification = new JLabel();
        p.add(notification, BorderLayout.CENTER);

        return p;
    }

    /**
     * Initialized player panel including card deck and other functioning buttons.
     * @return JPanel
     */
    private JPanel initializePlayerPanel() {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel lp = new JPanel();
        lp.setLayout(new BoxLayout(lp, BoxLayout.Y_AXIS));

        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout());

        JPanel rp = new JPanel();
        rp.setLayout(new BoxLayout(rp, BoxLayout.Y_AXIS));
        drawButton = new JButton("Draw Card");
        rp.add(drawButton);
        endButton = new JButton("End Turn");
        rp.add(endButton);

        playerName = new JLabel("display player name here");
        lp.add(playerName);

        p.add(lp);
        p.add(cardPanel);
        p.add(rp);

        return p;
    }

    /**
     * Make button unclickable and unfocusable
     * @param button JButton target
     */
    private void voidilizeJButton(JButton button) {
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFocusable(false);
        button.setBorderPainted( false );
        button.setFocusPainted( false );
        button.setContentAreaFilled(false);
    }


    /**
     * Initializes JPanel to display current game state, discardPile, and drawPile.
     * @return JPanel
     */
    public JPanel initializeGameStatePanel() {
        JPanel p = new JPanel();
        gameStateButton = new JButton();
        drawPileButton = new JButton();
        voidilizeJButton(gameStateButton);
        voidilizeJButton(drawPileButton);
        try {
            Image img = ImageIO.read(new File("src/resources/images/uno.png"));
            Image resizedImage = img.getScaledInstance(50, 80, Image.SCALE_DEFAULT);
            drawPileButton.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            System.out.println(e);
        }
        p.add(drawPileButton);
        p.add(gameStateButton);
        return p;
    }


    /**
     * Initializes the main container.
     */
    private JPanel initContainer() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(500,500));
        p.setLayout(new BorderLayout());

        JPanel bp = initializePlayerPanel();
        JPanel center = initializeCenter();
        center.add(initializeGameStatePanel(), BorderLayout.NORTH);

        p.add(bp, BorderLayout.SOUTH);
        p.add(center, BorderLayout.CENTER);
        return p;
    }


    /**
     * Displays a wild color picker.
     */
    private JPanel initWildColorPicker() {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(4,1));

        JButton rb = new JButton("RED"); rb.setBackground(Color.red);
        JButton gb = new JButton("GREEN"); gb.setBackground(Color.green);
        JButton bb = new JButton("BLUE"); bb.setBackground(Color.blue);
        JButton yb = new JButton("YELLOW"); yb.setBackground(Color.yellow);

        p.add(rb);
        p.add(gb);
        p.add(bb);
        p.add(yb);

        p.setMaximumSize(new Dimension(200, 200));
        p.setVisible(false);
        return p;
    }


    /**
     * Displays a text area to notify game states, eg. when game has ended.
     * @param text message to display
     */
    public void setDisplayedText(String text) {
        if (text.length() == 0) {
            notification.setVisible(false);
            return;
        }
        notification.setText(text);
        notification.setVisible(true);
    }


    /**
     * Displays userID.
     * @param name userID to display
     */
    public void setDisplayedName(String name) {
        playerID = name;
        playerName.setText(name.substring(0, 8));

        for (Component c : wildPanel.getComponents()) {
            JButton button = (JButton) c;
            Color background = c.getBackground();
            if (Color.RED.equals(background)) {
                button.addActionListener(e -> {
                    HTTPHandlers.setWildAndPlay(name, this.cardPlayed, Card.Color.RED);
                    wildPanel.setVisible(false);
                });
            } else if (Color.GREEN.equals(background)) {
                button.addActionListener(e -> {
                    HTTPHandlers.setWildAndPlay(name, this.cardPlayed, Card.Color.GREEN);
                    wildPanel.setVisible(false);
                });
            } else if (Color.BLUE.equals(background)) {
                button.addActionListener(e -> {
                    HTTPHandlers.setWildAndPlay(name, this.cardPlayed, Card.Color.BLUE);
                    wildPanel.setVisible(false);
                });
            } else if (Color.YELLOW.equals(background)) {
                button.addActionListener(e -> {
                    HTTPHandlers.setWildAndPlay(name, this.cardPlayed, Card.Color.YELLOW);
                    wildPanel.setVisible(false);
                });
            }
        }

        drawButton.addActionListener(e -> {
            HTTPHandlers.drawCard(name);
        });

        endButton.addActionListener(e -> {
            HTTPHandlers.endPlay(name);
        });
    }



    /**
     * Updates player deck GUI.
     * @param payload deck of the current player
     */
    public void setDisplayedCards(HashMap<String, Object> payload) {
        ArrayList<HashMap<String,Object>> cards = (ArrayList<HashMap<String, Object>>) payload.get("playerCards");
        System.out.println(cards);
        cardPanel.removeAll();
        cardHolders.clear();

        for (int i=0; i<cards.size(); ++i) {
            HashMap<String, Object> c = cards.get(i);
            JButton button = displayCard(c);
            if (c.get("playable").equals(false)) {
                voidilizeJButton(button);
            }
            cardHolders.add(button);
            int finalI = i;

            button.addActionListener(e -> {
                cardPlayed = finalI;
                if (c.get("wildType").equals(true)) {
                    wildPanel.setVisible(true);
                } else {
                    HTTPHandlers.playCard( (String) payload.get("playerID"), this.cardPlayed);
                }
            });
        }

        for (JButton button: cardHolders) {
            cardPanel.add(button);
        }

        cardPanel.updateUI();
    }


    /**
     * Displays cards as pictures.
     * @param c card information
     */
    private JButton displayCard(HashMap<String, Object> c) {
        JButton button = new JButton();

        String path = "src/resources/images/";

        if (c.get("effect").equals("DISARM")) {
            path += "Disarm.png";
        } else if (c.get("effect").equals("WILD")) {
            path += "Wild.png";
        } else if (c.get("effect").equals("WILD4")) {
            path += "Wild4.png";
        } else if (c.get("effect").equals("NONE")) {
            path += c.get("color") + " " + c.get("number") + ".png";
        } else {
            path += c.get("color") + " " + c.get("effect") + ".png";
        }

        try {
            Image img = ImageIO.read(new File(path));
            Image resizedImage = img.getScaledInstance(50, 80, Image.SCALE_DEFAULT);
            button.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            System.out.println(path);
            System.out.println(e);
        }
        return button;
    }


    /**
     * Displays the top of the discard pile.
     * @param gs current game state
     */
    public void setDisplayedGameState(GameState gs) {
        String path = "src/resources/images/";

        if (String.valueOf(gs.getNextEffect()).equals("Disarm")) {
            path += "Disarm.png";
        } else if (String.valueOf(gs.getNextEffect()).equals("Wild")) {
            path += "Wild.png";
        } else if (String.valueOf(gs.getNextEffect()).equals("Wild4")) {
            path += "Wild4.png";
        } else if (String.valueOf(gs.getNextEffect()).equals("-")) {
            path += gs.getNextColor() + " " + String.valueOf(gs.getNextNumber()) + ".png";
        } else {
            path += gs.getNextColor() + " " + String.valueOf(gs.getNextEffect()) + ".png";
        }

        try {
            Image img = ImageIO.read(new File(path));
            Image resizedImage = img.getScaledInstance(50, 80, Image.SCALE_DEFAULT);
            gameStateButton.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            System.out.println(e);
        }

        gameStateButton.setText(String.valueOf(gs.getDiscardPileSize()));
        drawPileButton.setText(String.valueOf(gs.getDrawPileSize()));

        if (gs.getNextPlayerID().equals(playerID)) {
            if (gs.getShouldSkip()) {
                setButtonImage(status, "skipped.jpg", 50, 50);
            } else {
                setButtonImage(status, "clock.jpg", 50, 50);
            }
        } else {
            status.setVisible(false);
        }
    }

    private void setButtonImage(JButton button, String file, int w, int h) {
        try {
            Image img = ImageIO.read(new File("src/resources/images/" + file));
            Image resizedImage = img.getScaledInstance(w, h, Image.SCALE_DEFAULT);
            button.setIcon(new ImageIcon(resizedImage));
            button.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
