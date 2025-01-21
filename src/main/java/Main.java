package main.java;

import main.java.entities.Game;
import main.java.services.GameService;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to the Game Library! Here you can explore an internal library of games " +
                "and Create, Read, Update and Delete games at will.");

        // Game game = GameService.createGame();


        try {
            List<Game> games = GameService.init();
            GameService.updateGameName("Forza Horizon 8", "Forza Horizon 9", games);
            for (Game game : games) {
                System.out.println(game);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}