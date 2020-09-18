package Controllers;

import GamePackage.Game;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

public class MainController {
    protected Game game;

    public MainController() {
        game = new Game();
        game.setNumPlayers(1);
        game.initDeck();
    }

    @ModelAttribute("BeforeRequest")
    public void BeforeRequest(HttpServletRequest request) {
        System.out.println(request);
    }
}
