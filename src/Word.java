import lingologs.Script;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Word {

    private Script content;
    private boolean isCompound;
    private boolean isAcronym;
    private boolean isForeign;

    /**
     * Constructor for the Word-class.
     * @param content the word to be saved in this instance of Word
     */
    public Word(Script content) {
        this.content = content;
        this.isCompound = false;
        this.isAcronym = false;
        this.isForeign = false;
    }

    /**
     * Counts the number of syllables contained in the word.
     * @return number of syllables
     */
    public int countSyllable(){
        int counter = 0;

        ArrayList<String> arr = new ArrayList<>();
        Pattern tokenSplitter = Pattern.compile("[aeiouyäöü]+[^$e]");
        Matcher matcher = tokenSplitter.matcher(content);

        while (matcher.find()){
            arr.add(matcher.group());
        }

        counter += arr.size();
        return counter;
    }

    /**
     * Getter-method for the length of the word saved in this instance of Word.
     * @return length of the word in this instance of Word
     */
    public int getLength(){
        return content.length();
    }

    /**
     * Getter-method for the content of the word saved in this instance of Word.
     * @return the word saved in this instance of Word
     */
    public Script getContent() {
        return content;
    }

    /**
     * Getter-method for boolean-value of isCompound, returning information about the word of this Instance of Word being a compound word.
     * @return boolean-value of isForeign
     */
    public boolean getCompound() {
        return isCompound;
    }

    /**
     * Setter-method for boolean isCompound, marking this instance of Word as compound word.
     * @param compound
     */
    public void setCompound(boolean compound) {
        isCompound = compound;
    }

    /**
     * Getter-method for boolean-value of isForeign, returning information about the word of this Instance of Word being a foreign word.
     * @return boolean-value of isForeign
     */
    public boolean getForeign() {
        return isForeign;
    }

    /**
     * Setter-method for boolean isForeign, marking this instance of Word as foreign word.
     * @param foreign
     */
    public void setForeign(boolean foreign) {
        isForeign = foreign;
    }

    /**
     * Getter-method for boolean-value of isAcronym, returning information about the word of this Instance of Word being an acronym.
     * @return boolean-value of isAcronym
     */
    public boolean getAcronym() {
        return isAcronym;
    }

    /**
     * Setter-method for boolean isAcronym, marking this instance of Word as acronym.
     * @param acronym
     */
    public void setAcronym(boolean acronym) {
        isAcronym = acronym;
    }

    @Override
    public String toString() {
        return content.toString();
    }

}
