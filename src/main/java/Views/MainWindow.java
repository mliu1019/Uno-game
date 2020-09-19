package Views;

import CardPackage.Card;
import Models.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainWindow extends  JFrame {
    private JLabel notification;
    private JLabel playerName;


    private JLabel labelNextColor, labelNextNumber, labelNextSymbol;

    JPanel cardPanel, wildPanel;

    ArrayList<JButton> cardHolders = new ArrayList<>();

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
        lp.add(new JButton("PREV"));
        lp.add(new JButton("NEXT"));

        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout());

        JPanel rp = new JPanel();
        rp.setLayout(new BoxLayout(rp, BoxLayout.Y_AXIS));
        rp.add(new JButton("DRAW"));
        rp.add(new JButton("PLAY"));

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
        JPanel p = new JPanel();
        p.add(labelNextColor);
        p.add(labelNextNumber);
        p.add(labelNextSymbol);
        return p;
    }

    private JPanel initContainer() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(500,500));
        p.setLayout(new BorderLayout());

        JPanel lp = initializeLRPanel();
        JPanel rp = initializeLRPanel();
        JPanel tp = initializeTBPanel();
        JPanel bp = initializePlayerPanel();
        JPanel center = initializeCenter();
        center.add(initializeGameStatePanel());

        p.add(lp, BorderLayout.WEST);
        p.add(rp, BorderLayout.EAST);
        p.add(tp, BorderLayout.NORTH);
        p.add(bp, BorderLayout.SOUTH);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private JPanel initWildColorPicker() {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());

        JButton rb = new JButton("RED"); rb.setBackground(Color.red);
        JButton gb = new JButton("GREEN"); gb.setBackground(Color.green);
        JButton bb = new JButton("BLUE"); bb.setBackground(Color.blue);
        JButton yb = new JButton("YELLOW"); yb.setBackground(Color.yellow);

//        rb.addActionListener(e -> wildColor = Card.Color.RED);
//        gb.addActionListener(e -> wildColor = Card.Color.GREEN);
//        bb.addActionListener(e -> wildColor = Card.Color.BLUE);
//        yb.addActionListener(e -> wildColor = Card.Color.YELLOW);

        p.add(rb, BorderLayout.NORTH);
        p.add(gb, BorderLayout.SOUTH);
        p.add(bb, BorderLayout.EAST);
        p.add(yb, BorderLayout.WEST);

        p.setMaximumSize(new Dimension(150, 150));
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

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setDisplayedCards(ArrayList<Card> cards) {
//        for (int i=0; i<7; ++i) {
//            Card c = cards.get(i);
//            System.out.println(c);
//            if (c.getEffect().equals(Card.Effect.NONE)) {
//                cardHolders[i].setText(c.getColor() + " " + ((NumberCard) c).getNumber());
//            } else {
//                cardHolders[i].setText(c.getColor() + " " + c.getEffect());
//            }
//        }
//    }

    public void setDisplayedCards(HashMap<String, Object> payload) {
        ArrayList<HashMap<String,Object>> cards = (ArrayList<HashMap<String, Object>>) payload.get("playerCards");
        cardPanel.removeAll();
        cardHolders.clear();

        for (int i=0; i<cards.size(); ++i) {
            HashMap<String, Object> c = cards.get(i);
            JButton button = new JButton();

            if (c.get("effect").equals("NONE")) {
                button.setText(c.get("color") + " " + c.get("number"));
            } else if (c.get("wildType").equals(true)){
                button.setText((String) c.get("effect"));
            } else {
                button.setText(c.get("color") + " " + c.get("effect"));
            }

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

    public void setDisplayedGameState(GameState gs) {
        labelNextSymbol.setText(String.valueOf(gs.getNextEffect()));
        labelNextNumber.setText(String.valueOf(gs.getNextNumber()));
        labelNextColor.setText(String.valueOf(gs.getNextColor()));
    }
}
