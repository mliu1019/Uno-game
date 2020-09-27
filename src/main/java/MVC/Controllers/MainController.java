package MVC.Controllers;

import GamePackage.Game;

public class MainController {
    protected Game game;

    public MainController() {
        game = new Game();
        game.setNumPlayers(2);
        game.initDeck();
    }
}
