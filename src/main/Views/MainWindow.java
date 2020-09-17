package Views;

import CardPackage.Card;
import CardPackage.NumberCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainWindow {
    private JFrame window;
    private JLabel notification;
    private JLabel playerName;

    JButton[] cardHolders = new JButton[7];

    public MainWindow() {
        window = new JFrame("Basic Application Example");
        window.setSize(960, 540);

        JPanel mainCont = new JPanel();
        mainCont.setLayout(new OverlayLayout(mainCont));

        JPanel myPanel = initContainer();

        window.setContentPane(mainCont);
        window.setVisible(true);

//        window.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                client.closeSession();
//            }
//
//            public void windowOpened(WindowEvent e) {
//                client = new Views.Client(e.getWindow());
//            }
//        });

        notification = new JLabel("");

        mainCont.add(notification);
        mainCont.add(myPanel);
    }

    public void addCallback(WindowAdapter w) {
        window.addWindowListener(w);
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

        JPanel cp = new JPanel();
        cp.setLayout(new FlowLayout());
        for (int i=0; i<7; ++i) {
            cardHolders[i] = new JButton();
            cp.add(cardHolders[i]);
        }

        JPanel rp = new JPanel();
        rp.setLayout(new BoxLayout(rp, BoxLayout.Y_AXIS));
        rp.add(new JButton("DRAW"));
        rp.add(new JButton("PLAY"));

        playerName = new JLabel("display player name here");
        lp.add(playerName);

        p.add(lp);
        p.add(cp);
        p.add(rp);

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

        p.add(lp, BorderLayout.WEST);
        p.add(rp, BorderLayout.EAST);
        p.add(tp, BorderLayout.NORTH);
        p.add(bp, BorderLayout.SOUTH);
        p.add(center, BorderLayout.CENTER);

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

    public void setDisplayedCards(ArrayList<HashMap<String,Object>> cards) {
        for (int i=0; i<7; ++i) {
            HashMap<String, Object> c = cards.get(i);
//            System.out.println(c);

            if (c.get("effect").equals("NONE")) {
                cardHolders[i].setText(c.get("color") + " " + c.get("number"));
            } else if (c.get("wildType").equals(true)){
                cardHolders[i].setText((String) c.get("effect"));
            } else {
                cardHolders[i].setText(c.get("color") + " " + c.get("effect"));
            }
        }
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
