package ch.joris.morse;

import java.util.ArrayList;

/**
 * Created by Joris on 02.04.19.
 */

public class MorsePattern {
    private int id;
    private ArrayList<Long> morsePattern;

    public MorsePattern(int id) {
        this.id = id;

    }

    public MorsePattern(int id, ArrayList<Long> morsePattern) {
        this.id = id;
        this.morsePattern = morsePattern;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Long> getMorsePattern() {
        return morsePattern;
    }

    public void setmorsePattern(ArrayList<Long> morsePattern) {
        this.morsePattern = morsePattern;
    }
}
