import java.io.*;
import java.nio.file.*;

public class TextProcessor {
    public static void main(String[] args) {
        String filePath = "input.txt"; // Fișierul de intrare

        try {
            // Citim conținutul fișierului
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Eliminăm semnele de punctuație
            content = content.replaceAll("[.,;:!?()\"']", "");

            // Eliminăm spațiile multiple
            content = content.replaceAll("\\s+", " ");

            // Convertim textul în litere mici
            content = content.toLowerCase();

            // Afișăm rezultatul
            System.out.println("Rezultat procesat:\n" + content);

        } catch (IOException e) {
            System.out.println("Eroare la citirea fișierului: " + e.getMessage());
        }
    }
}
