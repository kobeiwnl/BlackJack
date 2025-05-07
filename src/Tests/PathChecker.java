package Tests;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
// PathChecker è una classe di utilità che fornisce un metodo statico per verificare se un percorso esiste.
public class PathChecker {
    public static boolean doesPathExist(String pathString) {
        Path path = Paths.get(pathString);
        return Files.exists(path);
    }

    public static void main(String[] args) {
        String pathToCheck = "src/Music/BackGroundMusic.wav";
        if (doesPathExist(pathToCheck)) {
            System.out.println("The path exists.");
        } else {
            System.out.println("The path does not exist.");
        }
    }
}