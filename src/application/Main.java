package application;

import gamelibrary.Game;
import gamelibrary.GameService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Game> games = GameService.init();
        UI.mainMenu(games);
    }
}
