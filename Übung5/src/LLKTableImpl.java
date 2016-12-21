import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeProduction;
import uni_bayreuth.ai6.compiler.exercises.FirstAndControlSets.interfaces.InvalidCombinationException;
import uni_bayreuth.ai6.compiler.exercises.FirstAndControlSets.interfaces.LLKTable;
import uni_bayreuth.ai6.compiler.interfaces.NonTerminal;
import uni_bayreuth.ai6.compiler.interfaces.Terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LLKTableImpl implements LLKTable{
    private final int k;
    private final List<NonTerminal> rows;
    private final List<List<Terminal>> columns;
    private ContextFreeProduction[][] table;

    /**
     * Erstellt eine Parsertabelle für einen predictive parser.
     *
     * @param columns Liste von k-langen Listen aller Terminale, sodass die passenden Spalten angelegt werden können.
     *                Reihenfolge ist relevant für addRow.
     */
    public LLKTableImpl(List<List<Terminal>> columns, List<NonTerminal> rows, int k) {
        this.k = k;
        this.columns = columns;
        List<Terminal> endOfFileList = new ArrayList<>(Arrays.asList(new TerminalImpl("$")));
        if (!this.columns.get(this.columns.size() - 1).equals(endOfFileList))
            this.columns.add(endOfFileList);

        this.rows = rows;
        table = new ContextFreeProduction[rows.size()][];
        for (int j = 0; j < rows.size(); j++) {
            table[j] = new ContextFreeProduction[columns.size()];
        }
    }

    /**
     * Fügt eine Zeile zur Parsertabelle hinzu.
     *
     * @param productions Produktionen, die genutzt werden soll, wenn das Terminal der betreffenden Spalte
     *                    (siehe Konstruktor) gelesen wird
     */
    public void setRow(NonTerminal nonTerminal, List<ContextFreeProduction> productions) {
        table[rows.indexOf(nonTerminal)] = productions.toArray(new ContextFreeProduction[productions.size()]);
    }

    @Override
    public ContextFreeProduction getTableEntry(NonTerminal nonTerminal, List<Terminal> list) throws InvalidCombinationException {
        if(rows.indexOf(nonTerminal) == -1 || columns.indexOf(list) == -1) throw new InvalidCombinationException();
        ContextFreeProduction p = table[rows.indexOf(nonTerminal)][columns.indexOf(list)];
        if(p == null) throw new InvalidCombinationException();
        return p;
    }

    @Override
    public int getK() {
        return k;
    }
}
