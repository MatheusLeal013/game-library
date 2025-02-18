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

    // Criar metodos para cada caso
    public static void mainMenu(List<Game> games) {
        displayMainMenu();

        int mainMenuOption = optionMenu(0, 6);

        sc.nextLine();

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
                gameDeletMenu(games);
                break;
            case 6:
                System.exit(0);
        }
    }

    public static void newGameMenu(List<Game> games) {
        clearScreen();
        System.out.println("Enter the information of the new game:");

        System.out.print("Name: ");
        String name = sc.nextLine();
        while (GameService.gameIsInTheLibrary(name, games)) {
            System.out.println("There is already a game with this name in the library.");
            System.out.print("Enter another name for the game: ");
            name = sc.nextLine();
        }

        System.out.print("Release Date: ");
        String releaseDate = sc.next();
        while (!GameService.dateIsValid(releaseDate)) {
            System.out.println("Invalid date.");
            System.out.print("Release Date: ");
            releaseDate = sc.next();
        }

        sc.nextLine();

        System.out.print("Studio: ");
        String studio = sc.nextLine();

        System.out.print("Genre: ");
        String genre = sc.nextLine();

        StringBuilder synopsis = new StringBuilder();
        System.out.println("Synopsis (Skip a line and type 'END' to finish): ");
        while (true) {
            String line = sc.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            synopsis.append(line).append("\n");
        }

        Game newGame = new Game(name, LocalDate.parse(releaseDate, fmt), studio, genre, synopsis.toString());
        GameService.createGame(newGame, games);

        System.out.println(name + " was created.");
    }

    public static void gameUpdateMenu(List<Game> games) {
        System.out.print("Enter the name of the game you want to update: ");
        String updatedGameName = sc.nextLine();

        // Talvez criar um metodo para fazer a verificação
        while (!GameService.gameIsInTheLibrary(updatedGameName, games)) {
            System.out.println("The game was not found in the library!");
            System.out.print("Enter the name of the game you want to update again: ");
            updatedGameName = sc.nextLine();
        }

        clearScreen();

        System.out.println("1 - Update game name");
        System.out.println("2 - Update game release date");
        System.out.println("3 - Update game studio");
        System.out.println("4 - Update game genre");
        System.out.println("5 - Update game synopsis");
        System.out.println("6 - Return to main menu");

        System.out.print("Choose a number above what you want to change in the game: ");
        int updateMenuOption = catchOption();
        while (updateMenuOption <= 0 || updateMenuOption > 6) {
            System.out.print("Invalid option! Choose another number above the one you want to change in the game: ");
            updateMenuOption = catchOption();
        }

        sc.nextLine();

        switch (updateMenuOption) {
            case 1:
                System.out.print("Enter the new game name: ");
                String newGameName = sc.nextLine();
                while (GameService.gameIsInTheLibrary(newGameName, games)) {
                    System.out.println("There is already a game with this name in the library.");
                    System.out.print("Enter another name for the game: ");
                    newGameName = sc.nextLine();
                }
                GameService.updateGameName(updatedGameName, newGameName, games);
                break;
            case 2:
                System.out.print("Enter the new game release date: ");
                String newReleaseDate = sc.next();
                while (!GameService.dateIsValid(newReleaseDate)) {
                    System.out.println("Invalid date.");
                    System.out.print("New Release Date: ");
                    newReleaseDate = sc.next();
                }
                GameService.updateReleaseDate(updatedGameName, newReleaseDate, games);
                break;
            case 3:
                System.out.print("Enter the new game studio: ");
                String newStudio = sc.nextLine();
                GameService.updateStudio(updatedGameName, newStudio, games);
                break;
            case 4:
                System.out.print("Enter the new game genre: ");
                String newGenre = sc.nextLine();
                GameService.updateGenre(updatedGameName, newGenre, games);
                break;
            case 5:
                System.out.println("Enter the new game synopsis (Skip a line and type 'END' to finish)  :");
                StringBuilder newSynopsis = new StringBuilder();
                while (true) {
                    String line = sc.nextLine();
                    if (line.equalsIgnoreCase("END")) {
                        break;
                    }
                    newSynopsis.append(line).append("\n");
                }
                GameService.updateSynopsis(updatedGameName, newSynopsis, games);
                break;
            case 6:
                return;
            default:
        }

        System.out.println(updatedGameName + " has been updated.");
    }

    public static void gameSearchMenu(List<Game> games) {
        clearScreen();
        System.out.println("1 - Search games by name");
        System.out.println("2 - Search games by release date");
        System.out.println("3 - Search games by genre");
        System.out.println("4 - Search games by studio");

        System.out.print("Choose a number above to perform the type of search you want: ");
        int searchMenuOption = catchOption();
        while (searchMenuOption <= 0 || searchMenuOption > 4) {
            System.out.print("Invalid option! Choose another number above to perform the type of search you want: ");
            searchMenuOption = catchOption();
        }

        sc.nextLine();

        // Criar metodos para cada caso
        switch (searchMenuOption) {
            case 1:
                System.out.print("Enter the name of the game you want to search for by name: ");
                String gameNameSearched = sc.nextLine();
                while (GameService.searchGameByName(gameNameSearched, games) == null) {
                    System.out.print("The game you were looking for was not found, enter the name of another game: ");
                    gameNameSearched = sc.nextLine();
                }
                System.out.println("Game found, game information below: ");
                GameService.showGame(gameNameSearched);
                break;
            case 2:
                System.out.print("Enter the release date to search for games with this date: ");
                String searchedGamesreleaseDate = sc.next();
                // Criar um metodo com essa funcionalidade de verificação
                while (!GameService.dateIsValid(searchedGamesreleaseDate)) {
                    System.out.println("Invalid date.");
                    System.out.print("Release Date: ");
                    searchedGamesreleaseDate = sc.next();
                }
                if (GameService.searchGameByReleaseDate(searchedGamesreleaseDate, games) == null) {
                    System.out.println("The games were not found with the release date stated!");
                    break;
                }
                System.out.println("Games with this release date");
                GameService.searchGameByReleaseDate(searchedGamesreleaseDate, games).forEach(game -> System.out.println(game.getName()));
                break;
            case 3:
                System.out.print("Enter the name of the genre to search for games that match it: ");
                String gameGenreSearched = sc.nextLine();
                if (GameService.serchGameByGenre(gameGenreSearched, games) == null) {
                    System.out.println("Games with the specified genre were not found!");
                    break;
                }
                System.out.println("Games of the genre " + gameGenreSearched + " below");
                GameService.serchGameByGenre(gameGenreSearched, games).forEach(game -> System.out.println(game.getName()));
                break;
            case 4:
                System.out.print("Enter the studio name to search for games that match it: ");
                String gameStudioSearched = sc.nextLine();
                if (GameService.searchGameByStudio(gameStudioSearched, games) == null) {
                    System.out.println("Games with the specified studio were not found!");
                }
                System.out.println("Games from the studio " + gameStudioSearched + " below");
                GameService.searchGameByStudio(gameStudioSearched, games).forEach(game -> System.out.println(game.getName()));
                break;
        }
    }

    public static void gameDeletMenu(List<Game> games) {
        System.out.print("Enter the name of the game you want to delete: ");
        String gameNameDeleted = sc.nextLine();

        if (!GameService.gameIsInTheLibrary(gameNameDeleted, games)) {
            System.out.println("The game was not found in the library!");
            System.out.print("Retype the name of the game you want to delete: ");
            gameNameDeleted = sc.nextLine();
        }

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

    public static int optionMenu(int firstOption, int lastOption) {
        System.out.print("Choose a number to perform any of the operations above: ");
        int menuOption = catchOption();
        // Criar metodo para fazer verificação da opção
        while (menuOption <= firstOption || menuOption > lastOption) {
            System.out.print("Invalid option! Choose another number to perform any of the operations above: ");
            menuOption = catchOption();
        }
        return menuOption;
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
