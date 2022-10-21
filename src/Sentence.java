import lingologs.Script;

import java.util.ArrayList;
import java.util.List;

public class Sentence {
    private Script content;
    private ArrayList<Word> wordsListFromSentence;
    private int numCommas;

    /**
     * Constructor for the Sentence-class.
     * @param content the sentence to be saved in this instance of Sentence
     */
    public Sentence(Script content) {
        this.content = content;
        numCommas = 0;
        wordsListFromSentence = splitSentenceIntoWords();
    }

    /**
     * Splits the sentence into its individual words.
     * @return the individual words of the sentence
     */
    public ArrayList<Word> splitSentenceIntoWords() {
        ArrayList<Word> words = new ArrayList<>();
        List<Script> wordsList = content.split("(\s|[.;,])+");
        for (Script word : wordsList){
            words.add(new Word(word));
        }
        return words;
    }

    /**
     * Counts the number of commas in the sentence.
     * @return the number of commas in the sentence
     */
    public int getNumCommas(){
        numCommas = content.count(",");
        return numCommas;
    }

    /**
     * Determines the number of words in the sentence.
     * @return the number of words in the sentence
     */
    public int getNumWordsPerSentence(){
        return wordsListFromSentence.size();
    }

    /**
     * Getter-method for wordListFromSentence, containing the individual words of the sentence.
     * @return wordListFromSentence
     */
    public ArrayList<Word> getWordsListFromSentence() {
        return wordsListFromSentence;
    }

    /**
     * Getter-method for content, containing the sentence saved in this instance of Sentence.
     * @return content of sentence
     */
    public Script getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
