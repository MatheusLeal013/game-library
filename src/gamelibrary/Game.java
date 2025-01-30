package gamelibrary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Game {

    private final static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private String name;
    private LocalDate releaseDate;
    private String studio;
    private String genre;
    private String synopsis;

    public Game(String name, LocalDate releaseDate, String studio, String genre, String synopsis) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.studio = studio;
        this.genre = genre;
        this.synopsis = synopsis;
    }

    public Game() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(name, game.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "Game: " + name + "\n" +
                "Release Date: " + releaseDate.format(fmt) + "\n" +
                "Studio: " + studio + "\n" +
                "Genre: " + genre + "\n" +
                "Synopsis:" + "\n" + synopsis;
    }
}
