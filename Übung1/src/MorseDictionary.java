import uni_bayreuth.ai6.compiler.exercises.interfaces.MorseSymbol;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MorseDictionary {
    private Hashtable<List<MorseSymbol>, Character> morseToCharacter = new Hashtable<>();
    private Hashtable<Character, List<MorseSymbol>> characterToMorse = new Hashtable<>();

    public MorseDictionary(){
        insert('a', new int[]{0,1});
        insert('b', new int[]{1,0,0});
        insert('c', new int[]{1,0,1,0});
        insert('d', new int[]{1,0,0});
        insert('e', new int[]{0});
        insert('f', new int[]{0,0,1,0});
        insert('g', new int[]{1,1,0});
        insert('h', new int[]{0,0,0,0});
        insert('i', new int[]{0,0});
        insert('j', new int[]{0,1,1,1});
        insert('k', new int[]{1,0,1,0});
        insert('l', new int[]{0,1,0,0});
        insert('m', new int[]{1,1});
        insert('n', new int[]{1,0});
        insert('o', new int[]{1,1,1});
        insert('p', new int[]{0,1,1,0});
        insert('q', new int[]{1,1,0,1});
        insert('r', new int[]{0,1,0});
        insert('s', new int[]{0,0,0});
        insert('t', new int[]{1});
        insert('u', new int[]{0,0,1});
        insert('v', new int[]{0,0,0,1});
        insert('w', new int[]{0,1,1});
        insert('x', new int[]{1,0,0,1});
        insert('y', new int[]{1,0,1,1});
        insert('z', new int[]{1,1,0,0});
        insert('ä', new int[]{0,1,0,1});
        insert('ö', new int[]{1,1,1,0});
        insert('ü', new int[]{0,0,1,1});
        insert('0', new int[]{1,1,1,1,1});
        insert('1', new int[]{0,1,1,1,1});
        insert(' ', new int[]{2});
        insert('.', new int[]{0,1,0,1,0,1});
        insert(',', new int[]{1,1,0,0,1,1});
        insert('?', new int[]{0,0,1,1,0,0});
    }

    private void insert(char c, int[] ordinals) {
        List<MorseSymbol> morseSymbols = new ArrayList<>();
        for (int i : ordinals) {
            morseSymbols.add(MorseSymbol.values()[i]);
        }
        morseToCharacter.put(morseSymbols, c);
        characterToMorse.put(c, morseSymbols);
    }

    public char getCharacter(List<MorseSymbol> morseSymbol){
        if(morseSymbol.get(morseSymbol.size()-1) == MorseSymbol.PAUSE && morseSymbol.size() > 1) morseSymbol.remove(morseSymbol.size()-1);
        Character c = morseToCharacter.get(morseSymbol);
        if(c == null){
            System.err.print("Unknown morse sequence was given!\n");
            throw new IllegalArgumentException();
        }
        return c;
    }

    public List<MorseSymbol> getMorseSequence(Character symbol){
        List<MorseSymbol> morseSequence = characterToMorse.get(Character.toLowerCase(symbol));
        if(morseSequence == null){
            System.err.print("Invalid character was given!");
        }
        return morseSequence;
    }
}
