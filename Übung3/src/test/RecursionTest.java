import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeGrammar;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeProduction;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeRule;

import main.LeftRecursionTester;
import main.NTerm;
import main.Term;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by marcelfraas on 23.11.16.
 */
public class RecursionTest {

    protected static NTerm S = new NTerm("S");
    protected static NTerm A = new NTerm("A");
    protected static NTerm B = new NTerm("B");
    protected static NTerm C = new NTerm("C");

    protected static Term a = new Term("a");
    protected static Term b = new Term("b");
    protected static Term c = new Term("c");

    protected static ContextFreeRule r1 = new ContextFreeRule(S);

    protected static ContextFreeRule r2 = new ContextFreeRule(A);

    protected static ContextFreeRule r3 = new ContextFreeRule(B);

    protected static ContextFreeRule r4 = new ContextFreeRule(C);

    protected static List<ContextFreeRule> ruleList = new ArrayList<>();

    protected static ContextFreeGrammar grammar = new ContextFreeGrammar(S);

    protected static ContextFreeProduction p1 = new ContextFreeProduction(1, r1, Arrays.asList(A, B, a));
    protected static ContextFreeProduction p2 = new ContextFreeProduction(2, r1, Arrays.asList(c));
    protected static ContextFreeProduction p3 = new ContextFreeProduction(3, r2, Arrays.asList(B,a));
    protected static ContextFreeProduction p4 = new ContextFreeProduction(4, r2, Arrays.asList(a));
    protected static ContextFreeProduction p5 = new ContextFreeProduction(5, r3, Arrays.asList(C,c));
    protected static ContextFreeProduction p6 = new ContextFreeProduction(6, r3, Arrays.asList(b));
    protected static ContextFreeProduction p7 = new ContextFreeProduction(7, r4, Arrays.asList(A,a));
    protected static ContextFreeProduction p8 = new ContextFreeProduction(8, r4, Arrays.asList(c));

    @BeforeClass
    public static void setUp(){
        r1.addProduction(p1);
        r1.addProduction(p2);

        r2.addProduction(p3);
        r2.addProduction(p4);

        r3.addProduction(p5);
        r3.addProduction(p6);

        r4.addProduction(p7);
        r4.addProduction(p8);

        ruleList.add(r1);
        ruleList.add(r2);
        ruleList.add(r3);
        ruleList.add(r4);

        grammar.addRule(r1);
        grammar.addRule(r2);
        grammar.addRule(r3);
        grammar.addRule(r4);
    }

    @Test
    public void testRecursion() {
        LeftRecursionTester tester = new LeftRecursionTester();
        List<ContextFreeProduction> productions = tester.testForRecursion(grammar);
        productions.forEach((p)->System.out.println(p));
        Assert.assertEquals(new ArrayList<ContextFreeProduction>(), productions);
    }

}
