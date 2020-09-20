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
    private JLabel playerName;


    private JLabel labelNextColor, labelNextNumber, labelNextSymbol, labelNextPlayer;

    JPanel cardPanel, wildPanel;

    ArrayList<JButton> cardHolders = new ArrayList<>();

    JButton drawButton, endButton;

    JButton gameStateButton;

    int cardPlayed = -1;

    public MainWindow() {
        JPanel mainCont = new JPanel() {
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        mainCont.setLayout(new OverlayLayout(mainCont));

        notification = new JLabel("");
        mainCont.add(notification);

        wildPanel = initWildColorPicker();
        mainCont.add(wildPanel);

        JPanel myPanel = initContainer();
        mainCont.add(myPanel);

        add(mainCont, BorderLayout.CENTER);
        setVisible(true);
        setSize(1080, 720);
        setLocationRelativeTo(null);
    }

    public void addCallback(WindowAdapter w) {
        this.addWindowListener(w);
    }

    /*
    private JPanel initializeLRPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.RED);
        p.add(new JButton("LR"));
        return p;
    }

    private JPanel initializeTBPanel() {
        JPanel p = new JPanel();
        p.setBackground(Color.BLUE);
        p.add(new JButton("TB"));
        return p;
    }
    */

    private JPanel initializeCenter() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        return p;
    }

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

    public JPanel initializeGameStatePanel() {
        labelNextColor = new JLabel();
        labelNextNumber = new JLabel();
        labelNextSymbol = new JLabel();
        labelNextPlayer = new JLabel();
        JPanel p = new JPanel();
//        p.add(labelNextColor);
//        p.add(labelNextNumber);
//        p.add(labelNextSymbol);
//        p.add(labelNextPlayer);
        gameStateButton = new JButton();
        p.add(gameStateButton);
        return p;
    }

    private JPanel initContainer() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(500,500));
        p.setLayout(new BorderLayout());

        /*
        JPanel lp = initializeLRPanel();
        JPanel rp = initializeLRPanel();
        JPanel tp = initializeTBPanel();
         */
        JPanel bp = initializePlayerPanel();
        JPanel center = initializeCenter();
        center.add(initializeGameStatePanel());

        /*
        p.add(lp, BorderLayout.WEST);
        p.add(rp, BorderLayout.EAST);
        p.add(tp, BorderLayout.NORTH);
         */
        p.add(bp, BorderLayout.SOUTH);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

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

    public void setDisplayedText(String text) {
        if (text.length() == 0) {
            notification.setVisible(false);
            return;
        }
        notification.setText(text);
        notification.setVisible(true);
    }

    public void setDisplayedName(String name) {
        playerName.setText(name);

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

    public void setDisplayedCards(HashMap<String, Object> payload) {
        ArrayList<HashMap<String,Object>> cards = (ArrayList<HashMap<String, Object>>) payload.get("playerCards");
        cardPanel.removeAll();
        cardHolders.clear();

        for (int i=0; i<cards.size(); ++i) {
            HashMap<String, Object> c = cards.get(i);
            JButton button = displayCard(c);

            cardHolders.add(button);
            int finalI = i;

            button.addActionListener(e -> {
                cardPlayed = finalI;
                if (c.get("wildType").equals(true)) {
//                    setWildPlayer((String) payload.get("playerID"), finalI);
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


    private JButton displayCard(HashMap<String, Object> c) {
        JButton button = new JButton();

        String path = "src/resources/images/";

        if (c.get("effect").equals("WILD")) {
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
            System.out.println(e);
        }
        return button;
    }

    public void setDisplayedGameState(GameState gs) {

        String path = "src/resources/images/";

        if (String.valueOf(gs.getNextEffect()).equals("Wild")) {
            path += "Wild.png";
        } else if (String.valueOf(gs.getNextEffect()).equals("Wild4")) {
            path += "Wild4.png";
        } else if (String.valueOf(gs.getNextEffect()).equals("-")) {
            path += gs.getNextColor() + " " + String.valueOf(gs.getNextNumber()) + ".png";
        } else {
            path += gs.getNextColor() + " " + String.valueOf(gs.getNextEffect()) + ".png";
        }

        System.out.println(path);

        try {
            Image img = ImageIO.read(new File(path));
            Image resizedImage = img.getScaledInstance(50, 80, Image.SCALE_DEFAULT);
            gameStateButton.setIcon(new ImageIcon(resizedImage));
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
