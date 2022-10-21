import lingologs.Script;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor {

    private static final String ABBREVIATIONS_FILE_PATH = "./src/Data/Abbreviations.txt";
    private Text text;
    private ArrayList<String> abbrListFromText;
    private HashMap<String, String> abbrInTextMap ;

    /**
     * Constructor-method for Processor.
     * @param text contains the text to be processed
     */
    public Processor(Text text) throws IOException {
        this.text = text;
        abbrListFromText = getAbbrListFromText();
        abbrInTextMap = getAbbrMeaning();
    }

    /**
     * Prints the number as well as the abbreviations themselves contained in the text.
     * @throws IOException
     */
    public void printAbbreviations() throws IOException {
        System.out.println("\nDer Text enthält " + countAbbrInText() + " Abkürzungen. \nSie sollten versuchen, folgende Abkürzungen auszuschreiben: \n" + getAbbrMeaning());
    }

    /**
     * Normalizes the text by removing abbreviations and redundant spaces, as well as lowercasing the text.
     * @return lowercase text with removed abbreviations and redundant spaces as instance of the class Text
     */
    public Text normalize(){
        Script textWithoutAbbrAndRedundantSpaces = new Script();

        for (String str : abbrInTextMap.keySet()) {
            Script textWithoutAbbr = text.getContent().replace(str, "");
            textWithoutAbbrAndRedundantSpaces = textWithoutAbbr.replace(" {2,}", " ");
        }
        
        return new Text(textWithoutAbbrAndRedundantSpaces.toLower());
    }

    /**
     * Counts the number of abbreviations contained in the text.
     * @return number of abbreviations
     * @throws IOException
     */
    private int countAbbrInText() throws IOException {
        return abbrInTextMap.size();
    }

    /**
     * Determines the meaning of abbreviations contained in the text by comparing them to a Corpus.
     * @return a HashMap containing the abbreviations paired with their meaning
     * @throws IOException
     */
    private HashMap<String, String> getAbbrMeaning() throws IOException {
        HashMap<String, String> abbrMap = new HashMap<>();
        File abbreviationsFile = new File(ABBREVIATIONS_FILE_PATH);
        FileReader fr = new FileReader(abbreviationsFile);

        HashMap<String, String> abbrFromFileMap = new HashMap<>();

        BufferedReader br = new BufferedReader(fr);
        String line;
        String[] parts;

        while ((line = br.readLine())!= null) {
            parts = line.split(";");

            String key = parts[0];
            String value = parts[1];
            abbrFromFileMap.put(key, value);

            for (String abbr : abbrListFromText){
                for (String abbrKey : abbrFromFileMap.keySet()){
                    if (abbr.toLowerCase().equals(abbrKey.toLowerCase())){
                        abbrMap.put(abbr, abbrFromFileMap.get(abbrKey));
                    }
                }
            }
        }
        return abbrMap;
    }

    /**
     * Determines abbreviations contained in the text using a regex-statement.
     * @return ArrayList containing found abbreviations as String
     */
    private ArrayList<String> getAbbrListFromText(){
        ArrayList<String> arr = new ArrayList<>();
        Pattern tokenSplitter = Pattern.compile("(([A-ZÜÖÄßa-zäöü]*\\.(\\s|-)?){1,}|([A-ZÜÖÄßa-zäöü]*))([A-ZÜÖÄßa-zäöü]*\\.)");
        Matcher matcher = tokenSplitter.matcher(text.getContent().toString());

        while (matcher.find()){
            arr.add(matcher.group());
        }
        return arr;
    }

}
