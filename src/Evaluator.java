import lingolava.Mathx;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Evaluator {

    private static final String FOREIGN_WORDS_FILE_PATH = "./src/Data/ForeignWords.txt";
    private static final String COMPOUND_WORDS_FILE_PATH = "./src/Data/CompoundWords.txt";
    private static final String ACRONYMS_FILE_PATH = "./src/Data/Acronyms.txt";

    private Text text;
    private ArrayList<Sentence> sentencesList;
    private ArrayList<Word> wordsList;

    private ArrayList<Word> foreignWordsListFromText;
    private ArrayList<Word> compoundWordsListFromText;
    private HashMap<String, String> acronymsInTextMap;

    private ArrayList<String> synonymsListFromUrl;

    /**
     * Constructor-method of the Evaluator-class.
     * @param text the normalized text provided by the user in the beginning
     */
    public Evaluator(Text text) {

        this.text = text;
        this.sentencesList = getSentencesListFromText();
        this.wordsList = getWordsListFromText();

        foreignWordsListFromText = new ArrayList<>();
        compoundWordsListFromText = new ArrayList<>();
        acronymsInTextMap = new HashMap<>();
        synonymsListFromUrl = new ArrayList<>();
    }

    /**
     * Outputs the words contained in the text to be improved, previously determined by the program. This includes compound words, acronyms as well as foreign words.
     * @throws IOException
     */
    public void printImprovableWords() throws IOException {
        System.out.println("\nDer Text enthält " + countCompoundWords() + " Komposita. \nSie sollten versuchen, folgende Wörter zu ersetzen:");
        for (Word compound : getCompoundWordsListFromText()) {
            System.out.println("[" + compound + "]");
        }

        System.out.println("\nDer Text enthält " + countAcronyms() + " Akronyme. \nSie sollten versuchen, folgende Wörter zu ersetzen:\n" + getAcronymsMeaning());

        System.out.println("\nDer Text enthält " + countForeignWords() + " Fremdwörter. \nSie sollten versuchen, folgende Wörter zu ersetzen:");
        for (Word foreign : getForeignWordsListFromText()) {
            System.out.println("[" + foreign + "]");
        }
    }

    /**
     * Outputs sentences to be improved, with a length of more than 15 words or with one or more commas.
     */
    public void printImprovableSentences() {
        ArrayList<Sentence> improvableSentences = new ArrayList<>();
        for (Sentence sentence: sentencesList)
            if (sentence.getNumWordsPerSentence() > 15){
                improvableSentences.add(sentence);
            } else if (sentence.getNumCommas() > 1){
                improvableSentences.add(sentence);
            }
        System.out.println("\nDie folgenden Sätze sollten gekürzt oder aufgeteilt werden:\n");
        for (Sentence sentence : improvableSentences) {
            System.out.println("[\"" + sentence + "\"]");
        }
    }

    /**
     * Ranks the determined readability-score of the text into the Flesch-Index scale. Outputs the result as number as well as text.
     */
    public void printReadabilityScore(){
        double readabilityScore = getReadabilityScore();
        String readabilityScoreCategory;

        if (isBetween(readabilityScore, 0, 30)) {
            readabilityScoreCategory = "sehr schwer";
        } else if (isBetween(readabilityScore, 30, 50)) {
            readabilityScoreCategory = "schwer";
        } else if (isBetween(readabilityScore, 50, 60)) {
            readabilityScoreCategory = "mittelschwer";
        } else if (isBetween(readabilityScore, 60, 70)) {
            readabilityScoreCategory = "mittel";
        } else if (isBetween(readabilityScore, 70, 80)) {
            readabilityScoreCategory = "mittelleicht";
        } else if (isBetween(readabilityScore, 80, 90)) {
            readabilityScoreCategory = "leicht";
        } else {
            readabilityScoreCategory = "sehr leicht";
        }

        System.out.println("Der Text erzielt eine " + Math.round(readabilityScore * 100.0) / 100.0 + " auf dem Flesch-Index. Das bedeutet, der Text ist " + readabilityScoreCategory + " zu lesen.");
    }

    /**
     * Used to determine if a specific number is between two other values or not.
     * @param x the number to be tested
     * @param lower the lower number limit
     * @param upper the upper number limit
     * @return boolean-value containing the information ob the provided number lies within the upper and lower limit
     */
    private boolean isBetween(double x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    /**
     * Outputs the average length of the words used in the provided text.
     */
    public void printAvgLenWord(){
        System.out.println("Die durchschnittliche Wortlänge beträgt " + Math.round(calAverageWordLength() * 100.0) / 100.0 + " Zeichen pro Wort.");
    }

    /**
     * Counts the number of acronyms in the provided text.
     * @return the number of acronyms in the text or 0 if there are none
     * @throws IOException
     */
    private int countAcronyms() throws IOException {
        for (Word word: wordsList){
            if (checkAcronyms(word))
                return acronymsInTextMap.size();
        }
        return 0;
    }

    /**
     * Counts the number of compounds in the provided text.
     * @return the number of compounds in the text
     * @throws IOException
     */
    private int countCompoundWords() throws IOException {
        for (Word word :  wordsList){
            if (checkCompound(word)){
                compoundWordsListFromText.add(word);
            }
        }
        return compoundWordsListFromText.size();
    }

    /**
     * Counts the number of foreign words in the provided text.
     * @return the number of foreign words in the text
     * @throws IOException
     */
    private int countForeignWords() throws IOException {
        for (Word word :  wordsList){
            if (checkForeign(word)){
                foreignWordsListFromText.add(word);
            }
        }
        return foreignWordsListFromText.size();
    }

    /**
     * Checks if a word is an acronym by comparing it to the acronyms-corpus.
     * @return boolean-value of the word being an acronym or not
     * @throws IOException
     */
    private boolean checkAcronyms(Word word) throws IOException {
        File abbreviationsFile = new File(ACRONYMS_FILE_PATH);
        FileReader fr = new FileReader(abbreviationsFile);

        HashMap<String, String> acronymsFromFileMap = new HashMap<>();

        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine())!= null) {
            String[] parts = line.split(";");
            String key = parts[0];
            String value = parts[1];
            acronymsFromFileMap.put(key, value);

            for (String acronym : acronymsFromFileMap.keySet()){
                if (word.toString().equals(acronym.toLowerCase())){
                    acronymsInTextMap.put(acronym, acronymsFromFileMap.get(acronym));
                    word.setAcronym(true);
                    return word.getAcronym();
                }
            }
        }
        return false;
    }

    /**
     * Checks if a word is a compound by comparing it to the compounds-corpus.
     * @return boolean-value of the word being a compound or not
     * @throws IOException
     */
    private boolean checkCompound (Word word) throws IOException {
        ArrayList<String> compoundWordsList = readWordsFromFile(COMPOUND_WORDS_FILE_PATH);
        for (String compoundWord : compoundWordsList){
            if (word.toString().equals(compoundWord.toLowerCase())){
                word.setCompound(true);
                return word.getCompound();
            }
        }
        return false;
    }

    /**
     * Checks if a word is a foreign word by comparing it to the foreign-words-corpus.
     * @return boolean-value of the word being a foreign word or not
     * @throws IOException
     */
    private boolean checkForeign(Word word) throws IOException {
        ArrayList<String> foreignWordsList = readWordsFromFile(FOREIGN_WORDS_FILE_PATH);
        for (String foreignWord : foreignWordsList){
            if (word.toString().equals(foreignWord.toLowerCase())){
                word.setForeign(true);
                return word.getForeign();
            }
        }
        return false;
    }

    /**
     * Reads provided corpus-files line by line for later comparing the words contained in the text with those in the here generated corpus-word-list.
     * @param path the path to the corpus-file
     * @return ArrayList of Strings containing all the entries of the provided corpus-file
     * @throws IOException
     */
    private ArrayList<String> readWordsFromFile(String path) throws IOException {

        File wordsFromFile = new File(path);
        FileReader fr = new FileReader(wordsFromFile);

        ArrayList<String> wordsListFromFile = new ArrayList<>();

        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            wordsListFromFile.add(line);
        }
            return wordsListFromFile;
    }

    /**
     * Getter-method for the Hashmap containing the acronyms of the text together with their meaning.
     * @return the Hashmap containing acronyms paired with their meaning
     */
    //get compoundWordsList, foreignWordsList and acronymsHashMap
    private HashMap<String, String> getAcronymsMeaning() {
        return acronymsInTextMap;
    }

    /**
     * Getter-method for the ArrayList containing the compound words of the text.
     * @return ArrayList of Words, containing the compound words of the text
     */
    private ArrayList<Word> getCompoundWordsListFromText() {
        return compoundWordsListFromText;
    }

    /**
     * Getter-method for the ArrayList containing the foreign words of the text.
     * @return ArrayList of Words, containing the foreign words of the text
     */
    private ArrayList<Word> getForeignWordsListFromText() {
        return foreignWordsListFromText;
    }

    /**
     * Calculates the readability-score of the text using the Flesch formula (Flesch-Index = 180 – ASL – 58,5 x ASW).
     * @return calculated readability-score
     */
    public double getReadabilityScore(){
        double ASL = calAverageSentenceLength();
        double ASW = calAverageNumberOfSyllablesPerWord();

        double readabilityScore = 180 - ASL - 58.5 * ASW;
        return readabilityScore;
    }

    /**
     * Calculates the average Sentence Lenght throughout the provided Text
     * @return average sentence length
     */
    private double calAverageSentenceLength(){
        List<Integer> lengthSentenceList = new ArrayList<>();
        for (Sentence sentence : sentencesList){
            lengthSentenceList.add(sentence.getNumWordsPerSentence());
        }

        double ASL = Mathx.mean(lengthSentenceList);
        return ASL;
    }

    /**
     * Calculates the average number of syllables per word throughout the provided text.
     * @return average number of syllables per word
     */
    private double calAverageNumberOfSyllablesPerWord(){
        List<Integer> numSyllablesWordList = new ArrayList<>();

        for (Word word: wordsList){
            numSyllablesWordList.add(word.countSyllable());
        }

        double ASW = Mathx.mean(numSyllablesWordList);
        return ASW;
    }

    /**
     * Calculates the average word-length throughout the provided text.
     * @return average word length
     */
    private double calAverageWordLength(){
        List<Integer> lengthWordList = new ArrayList<>();

        for (Word word: wordsList){
            lengthWordList.add(word.getLength());
        }

        double avgLen = Mathx.mean(lengthWordList);
        return avgLen;
    }

    /**
     * Splits the text into its individual words.
     * @return ArrayList of Words contained in the text
     */
    private ArrayList<Word> getWordsListFromText() {
        ArrayList<Word> wordsListFromText = new ArrayList<>();

        for (Sentence sentence: sentencesList){
            ArrayList<Word> wordsListFromSentence = sentence.splitSentenceIntoWords();
            for (Word w : wordsListFromSentence){
                wordsListFromText.add(w);
            }
        }
        return wordsListFromText;
    }

    /**
     * Splits the text into its individual sentences.
     * @return ArrayList of Sentences contained in the text.
     */
    private ArrayList<Sentence> getSentencesListFromText(){
        return text.splitTextToSentences();
    }

}
