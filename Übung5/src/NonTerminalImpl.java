import uni_bayreuth.ai6.compiler.interfaces.NonTerminal;

public class NonTerminalImpl implements NonTerminal {

    private final String value;

    public NonTerminalImpl(String value) {
        if (value == null || value.length() == 0)
            throw new IllegalArgumentException("Only terminals can be null or empty");
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

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        NonTerminalImpl that = (NonTerminalImpl) o;
        return value != null ? value.equals(that.value) : that.value == null;
    }
}
