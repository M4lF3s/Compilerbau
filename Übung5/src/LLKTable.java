import uni_bayreuth.ai6.compiler.interfaces.NonTerminal;
import uni_bayreuth.ai6.compiler.interfaces.Terminal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LLKTable {
    private final int k;
    private final List<NonTerminal> rows;
    private final List<List<Terminal>> columns;
    private Integer[][] table;

    /**
     * Erstellt eine Parsertabelle für einen predictive parser.
     *
     * @param columns Liste von k-langen Listen aller Terminale, sodass die passenden Spalten angelegt werden können.
     *                Reihenfolge ist relevant für addRow.
     */
    public LLKTable(List<List<Terminal>> columns, List<NonTerminal> rows, int k) {
        this.k = k;
        this.columns = columns;
        List<Terminal> endOfFileList = new ArrayList<>(Arrays.asList(new TerminalImpl("$")));
        if (!this.columns.get(this.columns.size() - 1).equals(endOfFileList))
            this.columns.add(endOfFileList);

        this.rows = rows;
        table = new Integer[rows.size()][];
        for (int j = 0; j < rows.size(); j++) {
            table[j] = new Integer[columns.size()];
        }
    }

    /**
     * Fügt eine Zeile zur Parsertabelle hinzu.
     *
     * @param indexOfRule Indize der Produktion, die genutzt werden soll, wenn das Terminal der betreffenden Spalte
     *                    (siehe Konstruktor) gelesen wird
     */
    public void setRow(NonTerminal nonTerminal, List<Integer> indexOfRule) {
        table[rows.indexOf(nonTerminal)] = indexOfRule.toArray(new Integer[indexOfRule.size()]);
    }

    /**
     * Erhalte Indize der Produktionen von nonTerminal, wenn terminal gelesen wird.
     *
     * @return Indize der Produktion oder null, wenn keine passende Produktion gespeichert
     */
    public Integer getRuleIndice(NonTerminal nonTerminal, List<Terminal> terminal) {
        return table[rows.indexOf(nonTerminal)][columns.indexOf(terminal)];
    }

    public int getK() {
        return k;
    }
}
