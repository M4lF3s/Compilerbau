import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.StringReader;

public class MyDemo {
    public static void main(String[] args) {
        ToMorse toMorse = new ToMorse();

        BufferedReader inputStream = new BufferedReader(new StringReader("HaLlo1 "));
        BufferedWriter outputStream = new BufferedWriter(new OutputStreamWriter(System.out));
        toMorse.setStreams(inputStream, outputStream);
        toMorse.run();
        toMorse.terminate();

        System.out.println();

        FromMorse fromMorse = new FromMorse();
        inputStream = new BufferedReader(new StringReader("00002012010020100211120111122"));
        fromMorse.setStreams(inputStream, outputStream);
        fromMorse.run();
        fromMorse.terminate();
    }
}