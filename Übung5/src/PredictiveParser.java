import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeGrammar;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeProduction;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeRule;
import uni_bayreuth.ai6.compiler.interfaces.NonTerminal;
import uni_bayreuth.ai6.compiler.interfaces.Symbol;
import uni_bayreuth.ai6.compiler.interfaces.Terminal;

import java.util.*;

public class PredictiveParser {
    /**
     * Parses the given token sequence { @code word } and derives the sequence of
     * left - productions that generates the { @code word } from the start symbol of
     * the grammar { @code G} using the LL(k) table { @code t}.
     * A { @link WordNotInLanguageOfGrammarException } is thrown , if the word is
     * not in the language specified by the grammar .
     *
     * @param G    the LL(k) - grammar that specifies the language that is considered .
     * @param word the token sequence that is to be parsed .
     * @param t    the LL(k) -parser - table that is used to parse the given { @code word }.
     * @return the sequence of left - reductions that derives the { @code word }
     * token sequence from the start symbol of { @code G}.
     * @throws WordNotInLanguageOfGrammarException if { @code word } is not in the
     *                                             language specified by { @code G}.
     */
    public static List<ContextFreeProduction> parse(ContextFreeGrammar G, List<Terminal> word, LLKTable t)
            throws WordNotInLanguageOfGrammarException {
        //Initialisiere Ausgabeband, Stack und füge, wenn nicht vorhanden, an das Eingabewort ein "$" an
        List<ContextFreeProduction> usedProductions = new ArrayList<>();
        Stack<Symbol> stack = new Stack<>();
        Symbol endOfFile = new TerminalImpl("$");
        Symbol epsilon = new TerminalImpl("");
        stack.push(endOfFile);
        stack.push(G.getStartSymbol());

        if (!word.get(word.size() - 1).equals(endOfFile)) word.add(new TerminalImpl("$"));
        //Entferne jedes aufkommen von epsilon, da sie für den Parser irrelevant sind
        word.removeAll(Collections.singleton(new TerminalImpl("")));

        while (stack.peek().isNonTerminal() || (!stack.peek().equals(endOfFile) && !word.get(0).equals(endOfFile))) {
            Symbol current = stack.pop();

            /*
            liegt auf den Stack ein Terminal, dann entferne das erste Zeichen des Wortes, wenn sie gleich sind,
            ansonsten werfe einen Fehler
            */
            if (current.isTerminal()) {
                if (current.equals(word.get(0))) {
                    word.remove(0);
                    continue;
                } else {
                    throw new WordNotInLanguageOfGrammarException();
                }
            }
            /*
            liegt auf den Stack ein Nichtterminal, dann aktualisiere den Stack mit einer passende Produktion laut
            Parsertabelle oder werfe einen Fehler, wenn die Parsertabelle null liefert.
             */
            if (current.isNonTerminal()) {
                List<Terminal> subword = word.size()>1 ? word.subList(0, t.getK()) : word;
                Integer indice = t.getRuleIndice((NonTerminal) current, subword);
                if (indice == null) throw new WordNotInLanguageOfGrammarException();

                ContextFreeRule rule = G.getRuleWithThisLeftHandSide((NonTerminal) current);
                ContextFreeProduction production = null;
                for (ContextFreeProduction p : rule.getProductions()) {
                    if (p.getIndex() == indice) {
                        production = p;
                        break;
                    }
                }
                if (production == null) throw new RuntimeException("LLKTable enthält ein Indize, der zu keiner " +
                        "Produktion passt.");
                usedProductions.add(production);
                for (int i = production.getRightHandSide().size(); i != 0; i--) {
                    Symbol s = production.getRightHandSide().get(i - 1);
                    if (s.isNonTerminal() || !s.equals(epsilon))
                        stack.push(production.getRightHandSide().get(i - 1));
                }
            }
        }
        return usedProductions;
    }

    public static void main(String[] args) throws WordNotInLanguageOfGrammarException {
        Terminal a = new TerminalImpl("a");
        Terminal b = new TerminalImpl("b");
        Terminal epsilon = new TerminalImpl("");
        Terminal stringEnd = new TerminalImpl("$");

        NonTerminal S = new NonTerminalImpl("S");
        NonTerminal A = new NonTerminalImpl("A");

        ContextFreeRule r1 = new ContextFreeRule(S);
        ContextFreeRule r2 = new ContextFreeRule(A);

        ContextFreeProduction p1 = new ContextFreeProduction(1, r1, Arrays.asList(b, A));
        ContextFreeProduction p3 = new ContextFreeProduction(2, r1, Collections.singletonList(epsilon));
        ContextFreeProduction p4 = new ContextFreeProduction(1, r2, Arrays.asList(a, S));
        ContextFreeGrammar G = new ContextFreeGrammar(S);
        LLKTable table = new LLKTable(Arrays.asList(Arrays.asList(b, b), Arrays.asList(b, a),
                Arrays.asList(stringEnd)), Arrays.asList(S, A), 2);
        List<Terminal> w1 = new ArrayList<>(Arrays.asList(b, a, b, a));

        r1.addProduction(p1);
        r1.addProduction(p3);
        r2.addProduction(p4);
        G.addRule(r1);
        G.addRule(r2);

        table.setRow(S, Arrays.asList(null, 1, 2));
        table.setRow(A, Arrays.asList(1, null, null));

        System.out.println(PredictiveParser.parse(G, w1, table));
    }
}

