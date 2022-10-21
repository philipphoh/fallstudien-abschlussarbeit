import lingologs.Script;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main-method, entry-point of the program. Controls the program execution.
 */
public class Main {
    private static String textAsString;

    public static void main(String[] args) throws IOException {

        //STEP 1: read Text from file
        textAsString = readFile();

        System.out.println("\nDie bereitgestellte Datei enthält den folgenden Text: \n" + textAsString);

        Script textAsScript = new Script(textAsString);
        Text text = new Text(textAsScript);

        Processor processedText = new Processor(text);

        Text normalizedText = processedText.normalize();

        Evaluator evaluatedText = new Evaluator(normalizedText);

        //print improvable Words & Sentences
        System.out.println("\n\n\nDie folgenden Teile des vorliegenden Textes sollten in ihrer Verständlichkeit verbessert werden:");
        processedText.printAbbreviations();
        evaluatedText.printImprovableWords();
        evaluatedText.printImprovableSentences();

        //print accessibility score
        System.out.println("\n\n\nDer bereitgestellte Text wird wie folgt bewertet:\n");
        evaluatedText.printAvgLenWord();
        evaluatedText.printReadabilityScore();
    }

    /**
     * Reads the file provided by the user.
     * @return text contained in the provided file as String
     * @throws IOException
     */
    private static String readFile() throws IOException {
        BufferedReader brInput;
        brInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Geben Sie den Pfad zu dem auszuwertenden Text ein:");
        String filePath = brInput.readLine();
        //TODO: check if provided path is actually a path
        String textAsString = Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        return textAsString;
    }
}