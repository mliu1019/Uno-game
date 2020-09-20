package MVC.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import MVC.Models.*;

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

    @PutMapping(value = "/makeplay")
    public PlayFeedback MakePlay(@RequestBody PlayCommand cmd) {
        return game.makePlay(cmd);
    }

    @PutMapping(value = "/endplay")
    public PlayFeedback EndPlay(@RequestParam String playerID) {
        return game.endPlay(playerID);
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
