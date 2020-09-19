package Views;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import Models.GameState;
import Models.JoinMessage;

public class Client {
    private static MainWindow window;

    private static StompSession session;

    private static class gameStateHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return GameState.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            GameState gs = (GameState) payload;
            System.out.println("Receive Game State." + gs);
            if (!gs.getShouldStart()) {
                window.setDisplayedText("Waiting for more players to join...");
                return;
            }

            window.setDisplayedText("");
            window.setDisplayedGameState(gs);

        }
    }

    private static class playerDeckHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return Object.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            System.out.println("Receive deck: " + payload);
            window.setDisplayedCards((HashMap<String, Object>) payload);
        }
    }

    private static class playerStateHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return HashMap.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            HashMap<String, Object> map = (HashMap<String, Object>) payload;

            String sessionID = (String) map.get("playerID");

            window.setDisplayedName(sessionID);
            window.setDisplayedCards(map);
//            Player p = (Player) payload;
//            window.setDisplayedName(p.getPlayerID());
//            window.setDisplayedCards(p.getPlayerCards());
            session.subscribe("/deck/" + sessionID, new playerDeckHandler());
        }
    }

    private static class playerListHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return List.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            List<String> players = (List<String>)payload;
        }
    }

    private static class sessionHandler extends StompSessionHandlerAdapter  {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            session.subscribe("/game/state", new gameStateHandler());
            session.subscribe("/user/state", new playerStateHandler());
            session.subscribe("/game/players", new playerListHandler());
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
