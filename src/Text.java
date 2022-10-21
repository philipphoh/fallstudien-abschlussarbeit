import lingologs.Script;

import java.util.ArrayList;
import java.util.List;

public class Text {

    private Script content;
    private ArrayList<Sentence> sentences;

    /**
     * Constructor for the Text-class.
     * @param content the text to be saved in this instance of Text
     */
    public Text(Script content) {
        this.content = content;
        sentences = new ArrayList<>();
    }

    /**
     * Splits the text into its individual sentences.
     * @return list of sentences contained in the text
     */
    public ArrayList<Sentence> splitTextToSentences() {
        List<Script> sentencesList = content.split("([!?\\.:]\\s?)+");
        for (Script sentence : sentencesList){
            sentences.add(new Sentence(sentence));
        }
        return sentences;
    }

    /**
     * Getter-method for content, containing the text saved in this instance of Text.
     * @return text-content
     */
    public Script getContent() {
        return content;
    }
}
