package gamelibrary;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class GameService {

    private final static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static List<Game> init() {
        List<Game> games = new ArrayList<>();
        processInitGameFile(games);
        return games;
    }

    public static void createGame(Game newGame, List<Game> gamesList) {
        String path = "file-library/" + newGame.getName() + ".txt";

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(path))) {
            bf.write("Game Name: " + newGame.getName());
            bf.newLine();
            bf.write("Release Date: " + newGame.getReleaseDate().format(fmt));
            bf.newLine();
            bf.write("Studio: " + newGame.getStudio());
            bf.newLine();
            bf.write("Genre: " + newGame.getGenre());
            bf.newLine();
            bf.write("Synopsis: ");
            bf.newLine();
            bf.write(   newGame.getSynopsis());
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        catch (DateTimeException e) {
            throw new DateTimeException(e.getMessage());
        }
        gamesList.add(newGame);
    }

    // Verificar se jogo existe na biblioteca e uppercase
    public static void searchGame(String gameNameSearched) {
        String path = "file-library/" + gameNameSearched + ".txt";
        processReadGame(path);
    }

    public static void deleteGameList(String gameName, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);
                games.remove(game);
                break;
            }
        }
    }

    public static void updateGameName(String gameName, String newGameName, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(game.getName());
                game.setName(newGameName);
                createGame(game, games);
            }
        }
    }

    public static void updateReleaseDate(String gameName, String newReleaseDate, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);
                game.setReleaseDate(LocalDate.parse(newReleaseDate, fmt));
                createGame(game, games);
            }
        }
    }

    public static void updateStudio(String gameName, String newStudio, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);
                game.setStudio(newStudio);
                createGame(game, games);
            }
        }
    }

    public static void updateGenre(String gameName, String newGenre, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);
                game.setGenre(newGenre);
                createGame(game, games);
            }
        }
    }

    public static void updateSynopsis(String gameName, StringBuilder newSynopsis, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);
                game.setSynopsis(newSynopsis.toString());
                createGame(game, games);
            }
        }
    }

    private static void deleteGameFile(String gameName) {
        try {
            Stream<Path> stream = Files.list(Path.of("file-library/"));
            stream.forEach(p -> processDeletFile(p, gameName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void processInitGameFile(List<Game> games) {
        try {
            Stream<Path> stream = Files.list(Path.of("file-library/"));
            stream.forEach(p -> processFile(p, games));
        }
        catch (IOException | SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void processReadGame(String path) {
        try (BufferedReader bf = new BufferedReader(new FileReader(path))){
            String line = bf.readLine();
            while (line != null) {
                System.out.println(line);
                line = bf.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void processDeletFile(Path p, String gameName) {
        if (p.getFileName().toString().contains(gameName)) {
            p.toFile().delete();
        }
    }

    private static void processFile(Path p, List<Game> games) {
        try (BufferedReader bf = new BufferedReader(new FileReader(p.toFile()))) {
            String line = bf.readLine();
            String gameName = "";
            String studio = "";
            String genre = "";
            StringBuilder synopsis = new StringBuilder();
            LocalDate releaseDate = null;
            while (line != null) {
                if (line.contains("Game Name: ")) {
                    String[] a = line.split(": ");
                    gameName = a[1];
                    line = bf.readLine();
                }
                if (line.contains("Release Date: ")) {
                    String[] a = line.split(": ");
                    releaseDate = LocalDate.parse(a[1], fmt);
                    line = bf.readLine();
                }
                if (line.contains("Studio: ")) {
                    String[] a = line.split(": ");
                    studio = a[1];
                    line = bf.readLine();
                }
                if (line.contains("Genre: ")) {
                    String[] a = line.split(": ");
                    genre = a[1];
                    line = bf.readLine();
                }
                if (line.contains("Synopsis:")) {
                    do {
                        line = bf.readLine();
                        synopsis.append(line).append("\n");
                    } while (line != null);
                    synopsis.delete(synopsis.length() - 5, synopsis.length());
                }
                games.add(new Game(gameName, releaseDate, studio, genre, synopsis.toString()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean dateIsValid(String date) {
        try {
            LocalDate.parse(date, fmt);
        } catch (DateTimeException e) {
            return false;
        }
        return true;
    }

    public static boolean gameIsInTheLibrary(String gameName, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                return true;
            }
        }
        return false;
    }

}
