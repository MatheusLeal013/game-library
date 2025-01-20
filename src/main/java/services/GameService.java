package main.java.services;

import main.java.entities.Game;

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

    public static List<Game> init() throws IOException {

        Stream<Path> stream = Files.list(Path.of("file-library/"));

        List<Path> files = stream.toList();

        List<Game> games = new ArrayList<>();

        for (Path p : files) {
            try (BufferedReader bf = new BufferedReader(new FileReader(p.toString()))) {
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
                            synopsis.append(line);
                        } while (line != null);
                    }
                    games.add(new Game(gameName, releaseDate, studio, genre, synopsis.toString()));
                }
            }
        }

        return games;
    }

    public static Game createGame(List<Game> games) {
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Release Date: ");
        LocalDate releaseDate = LocalDate.parse(sc.next(), fmt);
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
        Game game = new Game(name, releaseDate, studio, genre, synopsis.toString());;

        games.add(game);

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
        return game;
    }

    public static void readGame(String gameName) {
        String path = "file-library/" + gameName + ".txt";

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

    public static void deleteGame(String gameName) {

        String path = "file-library/" + gameName + ".txt";

        File file = new File(path);

        try {
            file.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Refazer
    public static void updateGameName(String gameName, String newGameName) throws IOException {
        String path = "file-library/" + gameName + ".txt";

        File pathNew = new File("file-library/" + newGameName + ".txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(pathNew))) {
            bw.write("Game Name: " + newGameName);
            bw.newLine();
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                br.readLine();
                String line = br.readLine();
                do {
                    bw.write(line);
                    bw.newLine();
                    line = br.readLine();
                } while (line != null);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        GameService.deleteGame(gameName);
    }
}
