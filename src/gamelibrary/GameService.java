package gamelibrary;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class GameService {

    private final static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final static Scanner sc = new Scanner(System.in);

    public static List<Game> init() {
        List<Game> games = new ArrayList<>();
        processInitGameFile(games);
        return games;
    }

    public static void createGame(List<Game> games) {
        Game game = instaceOfGame();
        games.add(game);
        instanceOfGameFile(game);
    }

    public static void readGame(String gameName) {
        String path = "file-library/" + gameName + ".txt";

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
                instanceOfGameFile(game);
            }
        }
    }

    public static void updateReleaseDate(String gameName, String newReleaseDate, List<Game> games) throws IOException {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);
                game.setReleaseDate(LocalDate.parse(newReleaseDate, fmt));
                instanceOfGameFile(game);
            }
        }
    }

    public static void updateStudio(String gameName, String newStudio, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);
                game.setStudio(newStudio);
                instanceOfGameFile(game);
            }
        }
    }

    public static void updateGenre(String gameName, String newGenre, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);
                game.setGenre(newGenre);
                instanceOfGameFile(game);
            }
        }
    }

    public static void updateSynopsis(String gameName, List<Game> games) {
        for (Game game : games) {
            if (game.getName().equals(gameName)) {
                deleteGameFile(gameName);

                StringBuilder synopsis = new StringBuilder();

                System.out.println("New Synopsis (Skip a line and type 'END' to finish): ");
                while (true) {
                    String line = sc.nextLine();
                    if (line.equalsIgnoreCase("END")) {
                        break;
                    }
                    synopsis.append(line).append("\n");
                }

                game.setSynopsis(synopsis.toString());
                instanceOfGameFile(game);
            }
        }
    }

    private static void instanceOfGameFile(Game game) {
        String path = "file-library/" + game.getName() + ".txt";

        try (BufferedWriter bf = new BufferedWriter(new FileWriter(path))) {
            bf.write("Game Name: " + game.getName());
            bf.newLine();
            bf.write("Release Date: " + game.getReleaseDate().format(fmt));
            bf.newLine();
            bf.write("Studio: " + game.getStudio());
            bf.newLine();
            bf.write("Genre: " + game.getGenre());
            bf.newLine();
            bf.write("Synopsis: ");
            bf.newLine();
            bf.write(   game.getSynopsis());
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        catch (DateTimeException e) {
            throw new DateTimeException(e.getMessage());
        }
    }

    private static Game instaceOfGame() {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Release Date: ");
        String releaseDate = sc.next();
        while (!dateIsValid(releaseDate)) {
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

        return new Game(name, LocalDate.parse(releaseDate, fmt), studio, genre, synopsis.toString());

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

    private static boolean dateIsValid(String date) {
        try {
            LocalDate.parse(date, fmt);
        }
        catch (DateTimeException e) {
            return false;
        }
        return true;
    }
}
