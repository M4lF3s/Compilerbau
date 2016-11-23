package main;

import uni_bayreuth.ai6.compiler.interfaces.NonTerminal;

/**
 * Created by marcelfraas on 22.11.16.
 *
 * Hilfsklasse welche ein Nichtterminal darstellt.
 * Die toString() Methode wurde überladen, sodass der Bezeichner des Nichtterminals ausgegeben werden kann.
 */
public class NTerm implements NonTerminal {

    public NTerm(String value) {
        this.value = value;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public boolean isNonTerminal() {
        return true;
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