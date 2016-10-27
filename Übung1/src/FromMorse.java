import uni_bayreuth.ai6.compiler.exercises.interfaces.IFromMorse;
import uni_bayreuth.ai6.compiler.exercises.interfaces.MorseSymbol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FromMorse implements IFromMorse {

    private final MorseDictionary morseDictionary = new MorseDictionary();
    private BufferedReader inputStream;
    private BufferedWriter outputStream;

    @Override
    public void setStreams(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        this.inputStream = bufferedReader;
        this.outputStream = bufferedWriter;
    }

    @Override
    public void terminate() {
        Thread.currentThread().interrupt();
    }

    @Override
    public void run(){
        try {
            List<MorseSymbol> morseSequence = new ArrayList<>();
            MorseSymbol morseSymbol;
            int ordinal;
            while((ordinal = inputStream.read()) != -1){
                ordinal = Integer.parseInt(Character.toString((char) ordinal));
                switch(ordinal){
                    case 0:
                        morseSymbol = MorseSymbol.DIT;
                        morseSequence.add(morseSymbol);
                        break;
                    case 1:
                        morseSymbol = MorseSymbol.DAH;
                        morseSequence.add(morseSymbol);
                        break;
                    case 2:
                        morseSymbol = MorseSymbol.PAUSE;
                        morseSequence.add(morseSymbol);
                        char character = morseDictionary.getCharacter(morseSequence);
                        outputStream.write(String.valueOf(character));
                        outputStream.flush();
                        morseSequence.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
