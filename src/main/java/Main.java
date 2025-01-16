package main.java;

import main.java.entities.Game;
import main.java.services.GameService;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to the Game Library! Here you can explore an internal library of games " +
                "and Create, Read, Update and Delete games at will.");

        Game game = GameService.createGame();

        GameService.updateGameName(game.getName(), "Forza Horzion 4");
    }
}