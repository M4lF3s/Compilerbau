import uni_bayreuth.ai6.compiler.interfaces.Symbol;
import uni_bayreuth.ai6.compiler.interfaces.Terminal;

public class TerminalImpl implements Terminal {

    private String value;

    public TerminalImpl(String value) {
        if(value == null || value.length() == 0) this.value = Symbol.EMPTY_WORD_STRING;
        else if(value.equals("$")) this.value = Symbol.END_OF_FILE_STRING;
        else this.value = value;
    }

    @Override
    public boolean isEmptyWord() {
        return value.equals(Symbol.EMPTY_WORD_STRING);
    }

    @Override
    public Terminal getEmptyWord() {
        return new TerminalImpl("");
    }

    @Override
    public boolean isTerminal() {
        return true;
    }

    @Override
    public boolean isNonTerminal() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        TerminalImpl terminal = (TerminalImpl) o;
        return value != null ? value.equals(terminal.value) : terminal.value == null;
    }
}
