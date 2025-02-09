package application;

import gamelibrary.Game;
import gamelibrary.GameService;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {

    private final static Scanner sc = new Scanner(System.in);

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void mainMenu(List<Game> games) {
        System.out.println("Welcome to the Game Library! Here you can explore an internal library of games " +
                "and Create, Read, Update and Delete games at will.");

        System.out.println("1 - Create a new Game for library");
        System.out.println("2 - Display games from library");
        System.out.println("3 - Update game from library");
        System.out.println("4 - Search for game from library");
        System.out.println("5 - Delete game from library");
        System.out.println("6 - Close library");

        // Tenho que resolver essa parada
        int option = catchOption();

        while (option == 0) {
            option = catchOption();
        }

        sc.nextLine();

        switch (option) {
            case 1:
                System.out.println("Enter the information of the new game:");
                GameService.createGame(games);
                break;
            case 2:
                System.out.println("All games from library: ");
                games.forEach(g -> System.out.println(g.getName()));
                break;
            case 3:
                System.out.print("Enter the name of the game you want to update: ");
                String updatedGameName = sc.nextLine();
                while (!GameService.gameIsInTheLibrary(updatedGameName, games)) {
                    System.out.println("The game was not found in the library.");
                    System.out.print("Retype the game name: ");
                    updatedGameName = sc.nextLine();
                }
                gameUpdateMenu(updatedGameName, games);
                break;
            case 4:
                System.out.print("Enter the name of the game you want to search for: ");
                String gameNameSearched = sc.nextLine();
                while (!GameService.gameIsInTheLibrary(gameNameSearched, games)) {
                    System.out.println("The game was not found.");
                    System.out.print("Enter the name of the game you want to search again: ");
                    gameNameSearched = sc.nextLine();
                }
                GameService.searchGame(gameNameSearched);
                break;
            case 5:
                System.out.print("Enter the name of the game you want to delete: ");
                String gameNameDeleted = sc.nextLine();
                GameService.deleteGameList(gameNameDeleted, games);
                break;
            case 6:
                System.exit(0);
            default:
                System.out.println("The option you entered is invalid!");
        }
    }

    public static void gameCreateMenu() {

    }

    public static void gameUpdateMenu(String gameName, List<Game> games) {
        clearScreen();
        System.out.println("1 - Update game name");
        System.out.println("2 - Update game release date");
        System.out.println("3 - Update game studio");
        System.out.println("4 - Update game genre");
        System.out.println("5 - Update game synopsis");
        System.out.print("Choose the number above what you want to change in the game: ");
        int option = sc.nextInt();

        sc.nextLine();

        // Erros de entrada por aqui, resolver mais tarde
        switch (option) {
            case 1:
                System.out.print("Enter the new game name: ");
                String newGameName = sc.nextLine();
                while (GameService.gameIsInTheLibrary(newGameName, games)) {
                    System.out.println("There is already a game with this name in the library.");
                    System.out.print("Enter another name for the game: ");
                    newGameName = sc.nextLine();
                }
                GameService.updateGameName(gameName, newGameName, games);
                break;
            case 2:
                System.out.print("Enter the new game release date: ");
                String newReleaseDate = sc.next();
                while (!GameService.dateIsValid(newReleaseDate)) {
                    System.out.println("Invalid date.");
                    System.out.print("New Release Date: ");
                    newReleaseDate = sc.next();
                }
                GameService.updateReleaseDate(gameName, newReleaseDate, games);
                break;
            case 3:
                System.out.print("Enter the new game studio: ");
                String newStudio = sc.nextLine();
                GameService.updateStudio(gameName, newStudio, games);
            case 4:
                System.out.print("Enter the new game genre: ");
                String newGenre = sc.nextLine();
                sc.nextLine();
                GameService.updateGenre(gameName, newGenre, games);
            case 5:
                System.out.println("Enter the new game synopsis:");
                StringBuilder newSynopsis = new StringBuilder();
                while (true) {
                    String line = sc.nextLine();
                    if (line.equalsIgnoreCase("END")) {
                        break;
                    }
                    newSynopsis.append(line).append("\n");
                }
                GameService.updateSynopsis(gameName, newSynopsis, games);
            default:
        }
    }


    // Resolver essa parada
    public static int catchOption() {
        int option = 0;
        try {
            System.out.print("Choose a number to perform any of the operations above: ");
            option = sc.nextInt();
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid Option");
        }
        return option;
    }
}
