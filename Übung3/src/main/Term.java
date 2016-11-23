package main;

import uni_bayreuth.ai6.compiler.interfaces.Terminal;

/**
 * Created by marcelfraas on 22.11.16.
 *
 * Hilfsklasse welche ein Terminal darstellt.
 * Die toString() Methode wurde überladen, sodass der Bezeichner des Terminals ausgegeben werden kann.
 */
public class Term implements Terminal {

    public Term(String value) {
        this.value = value;
    }

    @Override
    public boolean isEmptyWord() {
        return false;
    }

    @Override
    public Terminal getEmptyWord() {
        return null;
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isNonTerminal() {
        return false;
    }

    /**
     * value stellt den Bezeichner des Nichtterminals dar.
     */
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}