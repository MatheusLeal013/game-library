package main.java.services;

import main.java.entities.Game;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GameService {

    public static Game createGame() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        Scanner sc = new Scanner(System.in);

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


        Game game = new Game(name, releaseDate, studio, genre, synopsis.toString());

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

    public static void updateGameName(String gameName, String newGameName) {

        String path = "file-library/" + gameName + ".txt";

        File file = new File(path);

        File pathNew = new File ("file-library/" + newGameName + ".txt");

        try (BufferedReader bf = new BufferedReader(new FileReader(path))){
            String line = bf.readLine();

            do {
                
            } while (line != "Game:" + newGameName);

            file.renameTo(pathNew);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
