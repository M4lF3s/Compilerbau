import uni_bayreuth.ai6.compiler.exercises.interfaces.IToMorse;
import uni_bayreuth.ai6.compiler.exercises.interfaces.MorseSymbol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class ToMorse implements IToMorse {

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
    public void run() {
        try {
            int i;
            while((i = inputStream.read()) != -1) {
                char symbol = (char) i;
                List<MorseSymbol> morseSequence = morseDictionary.getMorseSequence(symbol);
                if (morseSequence != null) {
                    for (MorseSymbol m : morseSequence) {
                        outputStream.write(String.valueOf(m.ordinal()));
                    }
                    outputStream.write(String.valueOf(MorseSymbol.PAUSE.ordinal()));
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
