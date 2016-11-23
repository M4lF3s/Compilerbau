package main;

import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeGrammar;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeProduction;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeRule;

import java.util.*;

/**
 * Created by marcelfraas on 22.11.16.
 */
public class Main {

    public static void main(String[] args) {

        NTerm S = new NTerm("S");
        NTerm A = new NTerm("A");
        NTerm B = new NTerm("B");
        NTerm C = new NTerm("C");

        Term a = new Term("a");
        Term b = new Term("b");
        Term c = new Term("c");

        ContextFreeRule r1 = new ContextFreeRule(S);
        r1.addProduction(new ContextFreeProduction(1, r1, Arrays.asList(A,B,a)));
        r1.addProduction(new ContextFreeProduction(2, r1, Arrays.asList(c)));

        ContextFreeRule r2 = new ContextFreeRule(A);
        r2.addProduction(new ContextFreeProduction(3, r2, Arrays.asList(B,a)));
        r2.addProduction(new ContextFreeProduction(4, r2, Arrays.asList(a)));

        ContextFreeRule r3 = new ContextFreeRule(B);
        r3.addProduction(new ContextFreeProduction(5, r3, Arrays.asList(C,c)));
        r3.addProduction(new ContextFreeProduction(6, r3, Arrays.asList(b)));

        ContextFreeRule r4 = new ContextFreeRule(C);
        r4.addProduction(new ContextFreeProduction(7, r4, Arrays.asList(A,a)));
        r4.addProduction(new ContextFreeProduction(8, r4, Arrays.asList(c)));

        List<ContextFreeRule> ruleList = new ArrayList<>();
        ruleList.add(r1);
        ruleList.add(r2);
        ruleList.add(r3);
        ruleList.add(r4);

        ContextFreeGrammar grammar = new ContextFreeGrammar(S);
        grammar.addRule(r1);
        grammar.addRule(r2);
        grammar.addRule(r3);
        grammar.addRule(r4);


        LeftRecursionTester tester = new LeftRecursionTester();
        List<ContextFreeProduction> productions = tester.testForRecursion(grammar);
        productions.forEach((p)->System.out.println(p));
    }


}
