import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeGrammar;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeProduction;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeRule;
import uni_bayreuth.ai6.compiler.interfaces.NonTerminal;
import uni_bayreuth.ai6.compiler.interfaces.Terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PredictiveParserTest {
    public static Terminal a = new TerminalImpl("a");
    public static Terminal b = new TerminalImpl("b");
    public static Terminal epsilon = new TerminalImpl("");
    public static Terminal stringEnd = new TerminalImpl("$");
    public static Terminal non = new TerminalImpl(null);

    public static NonTerminal S = new NonTerminalImpl("S");
    public static NonTerminal A = new NonTerminalImpl("A");

    public static ContextFreeRule r1 = new ContextFreeRule(S);
    public static ContextFreeRule r2 = new ContextFreeRule(A);

    public static ContextFreeProduction p1 = new ContextFreeProduction(1, r1, Arrays.asList(b, A));
    public static ContextFreeProduction p3 = new ContextFreeProduction(2, r1, Arrays.asList(epsilon));
    public static ContextFreeProduction p4 = new ContextFreeProduction(1, r2, Arrays.asList(b, a, S));
    public static ContextFreeGrammar G = new ContextFreeGrammar(S);

    public static List<Terminal> w1 = new ArrayList<>(Arrays.asList(b, b, a, b, b, a));
    public static List<Terminal> w2 = new ArrayList<>(Arrays.asList(b, b, a, b, b, a, epsilon));
    public static List<Terminal> w3 = new ArrayList<>(Arrays.asList(b, b, a, b, b, a, stringEnd));
    public static List<Terminal> w4 = new ArrayList<>(Arrays.asList(b, a, a));
    public static List<Terminal> w5 = new ArrayList<>(Arrays.asList(b, b, a, b, b, a, b, b, a, b, b, a));

    public static LLKTable table = new LLKTable(Arrays.asList(Arrays.asList(b, b), Arrays.asList(b, a),
            Arrays.asList(stringEnd)), Arrays.asList(S, A), 2);

    public static List<ContextFreeProduction> w1Result = new ArrayList<>(Arrays.asList(p1, p4, p1, p4, p3));
    public static List<ContextFreeProduction> w5Result = new ArrayList<>(Arrays.asList(p1, p4, p1, p4, p1, p4, p1, p4, p3));

    public static List<Terminal> firstS = Arrays.asList(new TerminalImpl("b"), new TerminalImpl(""));
    public static List<Terminal> firstA = Arrays.asList(new TerminalImpl("b"));

    @Before
    public void setUp() throws Exception {
        r1.addProduction(p1);
        r1.addProduction(p3);
        r2.addProduction(p4);
        G.addRule(r1);
        G.addRule(r2);

        table.setRow(S, Arrays.asList(1, null, 2));
        table.setRow(A, Arrays.asList(null, 1, null));
    }

    @Test
    public void testTable() {
        Assert.assertEquals(Integer.valueOf(1), table.getRuleIndice(S, Arrays.asList(b, b)));
        Assert.assertEquals(Integer.valueOf(1), table.getRuleIndice(A, Arrays.asList(b, a)));
    }

    @Test
    public void testTableException() {
        Assert.assertNull(table.getRuleIndice(S, Arrays.asList(b, a)));
        Assert.assertNull(table.getRuleIndice(A, Arrays.asList(b, b)));
    }

    @Test
    public void testParser() throws WordNotInLanguageOfGrammarException {
        Assert.assertEquals(w1Result, PredictiveParser.parse(G, w1, table));
        Assert.assertEquals(w1Result, PredictiveParser.parse(G, w2, table));
        Assert.assertEquals(w1Result, PredictiveParser.parse(G, w3, table));
        Assert.assertEquals(w5Result, PredictiveParser.parse(G, w5, table));
    }

    @Test(expected = WordNotInLanguageOfGrammarException.class)
    public void testException() throws WordNotInLanguageOfGrammarException {
        PredictiveParser.parse(G, w4, table);
    }

}