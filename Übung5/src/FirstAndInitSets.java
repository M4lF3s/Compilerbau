import javafx.util.Pair;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeGrammar;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeProduction;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeRule;
import uni_bayreuth.ai6.compiler.interfaces.NonTerminal;
import uni_bayreuth.ai6.compiler.interfaces.Symbol;
import uni_bayreuth.ai6.compiler.interfaces.Terminal;

import java.util.*;

public class FirstAndInitSets {

    private static final Terminal epsilon = new TerminalImpl("");

    /**
     * Returns the Follow_1 ({ @code A}) set of the non - terminal { @code A} of
     * the grammar { @code G}.
     *
     * @param G the grammar that contains the non - terminal { @code A} whose
     *          First_1 set is to be computed .
     * @param A the non - terminal whose First_1 set is to be computed .
     * @return the set Follow_1 ({ @code A}).
     */
    public static Collection<Terminal> firstSet(ContextFreeGrammar G, NonTerminal A) {
        Collection<Terminal> set = new HashSet<>();
        ContextFreeRule rule = G.getRuleWithThisLeftHandSide(A);
        for (ContextFreeProduction production : rule.getProductions()) {
            List<Symbol> symbols = production.getRightHandSide();
            // erstes Symbol ist Terminal
            if (symbols.get(0).isTerminal()) {
                set.add((Terminal) symbols.get(0));
                // Regel hat die Form A --> epsilon
            } else if (symbols.size() == 1 && symbols.contains(epsilon)) {
                set.add(epsilon);
            } else {
                Collection<Terminal> subset = new HashSet<>();
                subset.addAll(firstSet(G, (NonTerminal) symbols.get(0)));
                // Nichtterminal ohne epsilon in der Firstmenge
                if (!subset.contains(epsilon)) {
                    set.addAll(subset);
                    continue;
                }
                // Firstmenge des Nichtterminals enthält epsilon
                subset.remove(epsilon);
                for (int j = 1; j < symbols.size(); j++) {
                    if (symbols.get(j).isTerminal()) {
                        if (symbols.get(j).equals(epsilon)) continue;
                        subset.add((Terminal) symbols.get(j));
                        break;
                    }
                    subset.addAll(firstSet(G, (NonTerminal) symbols.get(j)));
                    // wenn Nichtterminal nicht epsilon produzieren kann, dann breche ab
                    if (!subset.contains(epsilon)) break;
                    // entferne epsilon, außer wenn das letzte Symbol ein Nichtterminal ist und epsilon produziert
                    if (j != symbols.size() - 1) subset.remove(epsilon);
                }
                set.addAll(subset);
            }
        }
        return set;
    }

    /**
     * Computes the initial LL (1) control sets of the productions of the grammar
     * { @code G} as a set of tuples whose first element is the production an the
     * second element is the initial control set of the first element .
     *
     * @param G the grammar .
     * @return a set of tuples of productions and initial control sets .
     */
    public static Collection<Pair<ContextFreeProduction, Collection<Terminal>>> initialControlSets(ContextFreeGrammar G) {
        Collection<Pair<ContextFreeProduction, Collection<Terminal>>> initSets = new ArrayList<>();
        for (ContextFreeRule rule : G.getRules()) {
            for (ContextFreeProduction production : rule.getProductions()) {
                Collection<Terminal> set = new HashSet<>();
                List<Symbol> symbols = production.getRightHandSide();
                // erstes Symbol ist Terminal
                if (symbols.get(0).isTerminal()) {
                    set.add((Terminal) symbols.get(0));
                    initSets.add(new Pair<>(production, set));
                    // Regel hat die Form A --> epsilon
                } else if (symbols.size() == 1 && symbols.contains(epsilon)) {
                    set.add(epsilon);
                    initSets.add(new Pair<>(production, set));
                } else {
                    set.addAll(firstSet(G, (NonTerminal) symbols.get(0)));
                    // Nichtterminal ohne epsilon in der Firstmenge
                    if (!set.contains(epsilon)) {
                        initSets.add(new Pair<>(production, set));
                        continue;
                    }
                    // Firstmenge des Nichtterminals enthält epsilon
                    set.remove(epsilon);
                    for (int j = 1; j < symbols.size(); j++) {
                        if (symbols.get(j).isTerminal()) {
                            if (symbols.get(j).equals(epsilon)) continue;
                            set.add((Terminal) symbols.get(j));
                            break;
                        }
                        set.addAll(firstSet(G, (NonTerminal) symbols.get(j)));
                        // wenn Nichtterminal nicht epsilon produzieren kann, dann breche ab
                        if (!set.contains(epsilon)) break;
                        // entferne epsilon, außer wenn das letzte Symbol ein Nichtterminal ist und epsilon produziert
                        if (j != symbols.size() - 1) set.remove(epsilon);
                    }
                    initSets.add(new Pair<>(production, set));
                }
            }
        }
        return initSets;
    }
}
