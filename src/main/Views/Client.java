package Views;

import CardPackage.Card;
import GamePackage.Player;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import Models.GameState;
import Models.JoinMessage;

public class Client {
    private static MainWindow window;

    private StompSession session;

    private static class gameStateHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameState.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameState gs = (GameState) payload;
            if (!gs.getShouldStart()) {
                window.setDisplayedText("Waiting for more players to join...");
            } else {
                System.out.println("Game is ready to start");
            }
        }
    }

    private static class playerStateHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return HashMap.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            // Unable to process Childclass, hashmap is the best option.

            HashMap<String, Object> map = (HashMap<String, Object>) payload;
            window.setDisplayedName((String) map.get("playerId"));
            window.setDisplayedCards((ArrayList<HashMap<String,Object>>) map.get("playerCards"));
//            Player p = (Player) payload;
//            window.setDisplayedName(p.getPlayerID());
//            window.setDisplayedCards(p.getPlayerCards());
        }
    }

    private static class sessionHandler extends StompSessionHandlerAdapter  {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("Subscribe to /game/state");
            session.subscribe("/game/state", new gameStateHandler());
            session.subscribe("/player/state", new playerStateHandler());
            session.send("/app/hello", new JoinMessage("player"));
        }
    }

    public Client() {
        WebSocketClient cli = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(cli);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        window = new MainWindow();
        window.addCallback(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (session != null) session.disconnect();

                System.exit(0);
            }

            public void windowOpened(WindowEvent e) {
                try {
                    session = stompClient.connect("ws://localhost:8080/uno-ws", new sessionHandler()).get();
                } catch (InterruptedException interruptedException) {
                    System.out.println("interrupted");
                    interruptedException.printStackTrace();
                } catch (ExecutionException ee) {
                    // Schedule reconnect
                    ee.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new Client();
    }
}
