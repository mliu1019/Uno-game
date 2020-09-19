package Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import GamePackage.*;
import Models.*;

@RestController
@RequestMapping(path="/game")
public class GameController extends MainController {
    @Autowired
    private SimpMessagingTemplate messenger;

    public GameController() {}

    @MessageMapping("/hello")
    @SendToUser("/state")
    public Player join(SimpMessageHeaderAccessor header, JoinMessage msg) {
        String sessionID = header.getSessionId();
        Player p = new Player(game, sessionID);
        p.setName(msg.getName());

        gameSetUp();
        updateGameStatus();

        return p;
    }

    void gameSetUp() {
        if (game.ready()) {
            game.setState(Game.GameState.shouldStart, true);
            game.dealFirstHand();
        }
        messenger.convertAndSend("/game/players", game.getPlayerNames());
    }

    @RequestMapping(value = "/play/{index}", method = RequestMethod.POST)
    public PlayFeedback playCard(
            @PathVariable("index") int index,
            @RequestParam(name="playerID") String playerID) {
        return game.makePlay(playerID, index);
    }

    @PutMapping(value = "/color")
    public PlayFeedback WildColor(@RequestBody WildCardPlay cp) {
//        System.out.println("player wild at " + cp.getIndex());
        game.makeWildColor(cp.getPlayerID(), cp.getColor());
        return game.makePlay(cp.getPlayerID(), cp.getIndex());
    }

    @Bean
    public MappedInterceptor interceptor() {
        return new MappedInterceptor(new String[]{"/game/*", "/game/play/*", "/game/color/*"}, new HandlerInterceptor() {
            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                updateGameStatus();
            }
        });
    }

    private void updateGameStatus() {
        messenger.convertAndSend("/game/state", game.getStates());
        for (var p: game.getPlayers()) {
            messenger.convertAndSend("/deck/" + p.getPlayerID(), p);
        }
    }

    @ExceptionHandler
    public void handleException(Exception e) {
        e.printStackTrace();
    }
}
