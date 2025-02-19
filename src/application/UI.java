package application;

import gamelibrary.Game;
import gamelibrary.GameService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {

    private final static Scanner sc = new Scanner(System.in);
    private final static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void mainMenu(List<Game> games) {
        displayMainMenu();

        int mainMenuOption = optionMenu(0, 6);

        sc.nextLine();

        chooseTheMainMenuOption(mainMenuOption, games);
    }

    public static void newGameMenu(List<Game> games) {
        clearScreen();

        Game newGame = processGameCreation(games);
        GameService.createGame(newGame, games);

        System.out.println(newGame.getName() + " was created.");
    }

    public static void gameUpdateMenu(List<Game> games) {
        System.out.println("Enter the name of the game you want to update below: ");
        String updatedGameName = getGameName(games);

        clearScreen();

        displayUpdateMenu();

        int updateMenuOption = optionMenu(0, 6);

        sc.nextLine();

        chooseUpdateMenuOption(updateMenuOption, updatedGameName, games);

        System.out.println(updatedGameName + " has been updated.");
    }

    public static void gameSearchMenu(List<Game> games) {
        clearScreen();

        displaySearchMenu();

        int searchMenuOption = optionMenu(1, 6);

        sc.nextLine();

        chooseOptionFromSearchMenu(searchMenuOption, games);
    }

    public static void gameDeleteMenu(List<Game> games) {
        System.out.print("Enter the name of the game you want to delete: ");
        String gameNameDeleted = getGameName(games);

        GameService.deleteGameList(gameNameDeleted, games);

        System.out.println(gameNameDeleted + " was deleted.");
    }

    public static void displayMainMenu() {
        System.out.println("Welcome to the Game Library! Here you can explore an internal library of games " +
                "and Create, Read, Update and Delete games at will.");
        System.out.println("1 - Create a new Game for library");
        System.out.println("2 - Display games from library");
        System.out.println("3 - Update game from library");
        System.out.println("4 - Search for game from library");
        System.out.println("5 - Delete game from library");
        System.out.println("6 - Close library");
    }

    public static void displayUpdateMenu() {
        System.out.println("1 - Update game name");
        System.out.println("2 - Update game release date");
        System.out.println("3 - Update game studio");
        System.out.println("4 - Update game genre");
        System.out.println("5 - Update game synopsis");
        System.out.println("6 - Return to main menu");
    }

    public static void displaySearchMenu() {
        System.out.println("1 - Search games by name");
        System.out.println("2 - Search games by release date");
        System.out.println("3 - Search games by genre");
        System.out.println("4 - Search games by studio");
    }

    public static Game processGameCreation(List<Game> games) {
        System.out.println("Enter the information of the new game:");

        String name = processNewGameName(games);

        String releaseDate = processReleaseDate();
        sc.nextLine();

        System.out.print("Studio: ");
        String studio = sc.nextLine();

        System.out.print("Genre: ");
        String genre = sc.nextLine();

        String synopsis = processSynopsis();

        return new Game(name, LocalDate.parse(releaseDate, fmt), studio, genre, synopsis);
    }

    public static String processNewGameName(List<Game> games) {
        System.out.print("Name: ");
        String name = sc.nextLine();
        while (GameService.gameIsInTheLibrary(name, games)) {
            System.out.println("There is already a game with this name in the library.");
            System.out.print("Enter another name for the game: ");
            name = sc.nextLine();
        }
        return name;
    }

    public static String processReleaseDate() {
        System.out.print("Release Date: ");
        String releaseDate = sc.next();
        while (!GameService.dateIsValid(releaseDate)) {
            System.out.println("Invalid date.");
            System.out.print("Release Date: ");
            releaseDate = sc.next();
        }
        return releaseDate;
    }

    public static String processSynopsis() {
        System.out.println("Synopsis (Skip a line and type 'END' to finish): ");
        StringBuilder synopsis = new StringBuilder();
        while (true) {
            String line = sc.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            synopsis.append(line).append("\n");
        }
        return synopsis.toString();
    }

    public static void processGameSearchByName(List<Game> games) {
        System.out.print("Enter the name of the game you want to search for by name: ");
        String gameNameSearched = sc.nextLine();
        while (GameService.searchGameByName(gameNameSearched, games) == null) {
            System.out.print("The game you were looking for was not found, enter the name of another game: ");
            gameNameSearched = sc.nextLine();
        }
        System.out.println("Game found, game information below: ");
        GameService.showGame(gameNameSearched);
    }

    public static void processGameSearchByReleaseDate(List<Game>games) {
        System.out.print("Enter the release date to search for games with this date: ");
        String searchedGamesreleaseDate = processReleaseDate();
        if (GameService.searchGameByReleaseDate(searchedGamesreleaseDate, games) == null) {
            System.out.println("The games were not found with the release date stated!");
        }
        System.out.println("Games with this release date");
        GameService.searchGameByReleaseDate(searchedGamesreleaseDate, games).forEach(game -> System.out.println(game.getName()));
    }

    public static void processGameSearchByGenre(List<Game> games) {
        System.out.print("Enter the name of the genre to search for games that match it: ");
        String gameGenreSearched = sc.nextLine();
        if (GameService.serchGameByGenre(gameGenreSearched, games) == null) {
            System.out.println("Games with the specified genre were not found!");
        }
        System.out.println("Games of the genre " + gameGenreSearched + " below");
        GameService.serchGameByGenre(gameGenreSearched, games).forEach(game -> System.out.println(game.getName()));
    }

    public static void processGameSearchByStudio(List<Game> games) {
        System.out.print("Enter the studio name to search for games that match it: ");
        String gameStudioSearched = sc.nextLine();
        if (GameService.searchGameByStudio(gameStudioSearched, games) == null) {
            System.out.println("Games with the specified studio were not found!");
        }
        System.out.println("Games from the studio " + gameStudioSearched + " below");
        GameService.searchGameByStudio(gameStudioSearched, games).forEach(game -> System.out.println(game.getName()));
    }
    public static int optionMenu(int firstOption, int lastOption) {
        System.out.print("Choose a number to perform any of the operations above: ");
        int menuOption = catchOption();
        while (menuOption <= firstOption || menuOption > lastOption) {
            System.out.print("Invalid option! Choose another number to perform any of the operations above: ");
            menuOption = catchOption();
        }
        return menuOption;
    }

    public static void chooseTheMainMenuOption(int mainMenuOption, List<Game> games) {
        switch (mainMenuOption) {
            case 1:
                newGameMenu(games);
                break;
            case 2:
                System.out.println("All games from library: ");
                games.forEach(g -> System.out.println(g.getName()));
                break;
            case 3:
                gameUpdateMenu(games);
                break;
            case 4:
                gameSearchMenu(games);
                break;
            case 5:
                gameDeleteMenu(games);
                break;
            case 6:
                System.exit(0);
        }
    }

    public static void chooseUpdateMenuOption(int updatedMenuOption, String updatedGameName, List<Game> games) {
        switch (updatedMenuOption) {
            case 1:
                System.out.println("Enter the new game name below: ");
                String newGameName = processNewGameName(games);
                GameService.updateGameName(updatedGameName, newGameName, games);
                break;
            case 2:
                System.out.println("Enter the new game release date below: ");
                String newReleaseDate = processReleaseDate();
                GameService.updateReleaseDate(updatedGameName, newReleaseDate, games);
                break;
            case 3:
                System.out.println("Enter the new game studio below: ");
                String newStudio = sc.nextLine();
                GameService.updateStudio(updatedGameName, newStudio, games);
                break;
            case 4:
                System.out.print("Enter the new game genre below: \n" + "Genre");
                String newGenre = sc.nextLine();
                GameService.updateGenre(updatedGameName, newGenre, games);
                break;
            case 5:
                System.out.println("Enter the new game synopsis below:");
                String newSynopsis = processSynopsis();
                GameService.updateSynopsis(updatedGameName, newSynopsis, games);
                break;
            case 6:
                mainMenu(games);
        }
    }

    public static void chooseOptionFromSearchMenu(int searchMenuOption, List<Game> games) {
        switch (searchMenuOption) {
            case 1:
                processGameSearchByName(games);
                break;
            case 2:
                processGameSearchByReleaseDate(games);
                break;
            case 3:
                processGameSearchByGenre(games);
                break;
            case 4:
                processGameSearchByStudio(games);
                break;
        }
    }

    public static String getGameName(List<Game> games) {
        System.out.print("Game name: ");
        String gameName = sc.nextLine();

        while (!GameService.gameIsInTheLibrary(gameName, games)) {
            System.out.println("The game was not found in the library!");
            System.out.print("Enter the name of the game you want to update again: ");
            gameName = sc.nextLine();
        }
        return gameName;
    }

    public static int catchOption() {
        int option = 0;
        try {
            option = sc.nextInt();
        }
        catch (InputMismatchException e) {
            sc.nextLine();
        }
        return option;
    }
}
