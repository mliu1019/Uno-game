package Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.handler.annotation.MessageMapping;

import GamePackage.*;
import Models.*;

import java.security.Principal;

@RestController("/game")
public class GameController {
    @Autowired
    private SimpMessagingTemplate messenger;

    private final Game game;

    public GameController() {
        game = new Game();
        game.setNumPlayers(1);
        game.initDeck();
    }

    @MessageMapping("/hello")
    @SendTo("/player/state")
    public Player join(SimpMessageHeaderAccessor headerAccessor, JoinMessage msg) {
//        System.out.println("SEESION ID: " + headerAccessor.getSessionId());
        Player p = new Player(game, headerAccessor.getSessionId());
        p.setName(msg.getName());
//
        gameSetUp();

        return p;
    }

    void gameSetUp() {
        if (game.ready()) {
            game.setState(Game.GameState.shouldStart, true);
            game.dealFirstHand();
        }
        messenger.convertAndSend("/game/state", game.getGameSate());
    }

//    @RequestMapping("/")
}
