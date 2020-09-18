package Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;

import GamePackage.*;
import Models.*;

@RestController
@RequestMapping(path="/game")
public class GameController extends MainController {
    @Autowired
    private SimpMessagingTemplate messenger;

    public GameController() {

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

    @RequestMapping(value = "/play/{index}", method = RequestMethod.POST)
    public PlayFeedback playCard(
            @PathVariable("index") int index,
            @RequestParam(name="playerID") String playerID) {
        System.out.println("Player " + playerID + " played a card at " + index);
        return game.makePlay(playerID, index);
    }


//    @RequestMapping("/")
}
