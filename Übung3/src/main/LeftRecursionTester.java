package main;

import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeGrammar;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeProduction;
import uni_bayreuth.ai6.compiler.contextfreegrammar.ContextFreeRule;
import uni_bayreuth.ai6.compiler.exercises.recursionDetection.ILeftRecursionTester;
import uni_bayreuth.ai6.compiler.interfaces.Symbol;

import java.util.*;

/**
 * Created by marcelfraas on 22.11.16.
 */
public class LeftRecursionTester implements ILeftRecursionTester {


    /**
     * Hashmap um Kandidaten für indirekte Linksrekursion zu speichern
     */
    private Map<Symbol, List<Integer>> memoryMap = new HashMap<>();

    /**
     * Hashmap die alle Produktionen der Grammatik enthält. Schlüssel ist der jeweilige Index der Produktion.
     */
    private Map<Integer, ContextFreeProduction> productionMap = new HashMap<>();

    @Override
    public List<ContextFreeProduction> testForRecursion(ContextFreeGrammar contextFreeGrammar) {
        initializeProductionList(contextFreeGrammar);
        if(!testDirectRecursion(contextFreeGrammar).isEmpty()){
            return testDirectRecursion(contextFreeGrammar);
        } else {
            System.out.println("Keine direkte Linksrekursion erkannt");
            System.out.println("Kandidaten für indirekte Linksrekursion:");
            this.memoryMap.forEach((k,v)->{
                System.out.print("Symbol: " + k + " Produktionen: ");
                v.forEach((i)->System.out.print(i + " "));
                System.out.print("\n");
            });

            return testIndirectRecursion(contextFreeGrammar);
        }
    }


    /**
     * Testet die übergebene Grammatik auf direkte Linksrekursion.
     * @param contextFreeGrammar
     * @return Gibt eine Liste an Produktionen zurück, die Zeuge für direkte Linksrekursion sind
     */
    public List<ContextFreeProduction> testDirectRecursion(ContextFreeGrammar contextFreeGrammar) {

        /**
         * Leere Liste anlegen für die Rückgabe
         */
        List<ContextFreeProduction> returnList = new ArrayList<>();

        /**
         * Iteriere über alle Regeln in der Grammatik
         */
        for(ContextFreeRule r : contextFreeGrammar.getRules()) {

            /**
             * Für Jede Regel: Iteriere über alle Produktionen dieser Regel
             */
            for(ContextFreeProduction p : r.getProductions()) {

                /**
                 * Handelt es sich bei dem ersten Symbol auf der rechten Seite um ein Terminalsymbol?
                 */
                if(p.getRightHandSide().get(0).isTerminal()){
                    //System.out.println("Terminalsymbol erkannt. Kein Rekursionskandidat");
                } else {

                    /**
                     * Falls es sich nicht um ein Terminalsymbol handelt: ist das erste (linkeste) Nichtterminalsymbol
                     * auf der rechten Seite gleich dem Nichtterminalsymbol auf der linken Seite?
                     */
                    if(p.getLeftHandSide().equals(p.getRightHandSide().get(0))){
                        System.out.println("Heureka!! Direkte Linksrekursion erkannt");
                        returnList.add(p);
                    }
                    else {
                        //System.out.println("Kandidat für indirekte Linksrekursion");
                        if(memoryMap.containsKey(p.getRightHandSide().get(0))){
                            List<Integer> t = memoryMap.get(p.getRightHandSide().get(0));
                            t.add(p.getIndex());
                            memoryMap.put(p.getRightHandSide().get(0), t);
                        } else {
                            memoryMap.put(p.getRightHandSide().get(0), new ArrayList() {{add(p.getIndex());}} );
                        }
                    }
                }
            }
        }
        return returnList;
    }


    /**
     * Testet die übergebene Grammatik auf indirekte Linksrekursion.
     * @param contextFreeGrammar
     * @return Gibt eine Liste an Produktionen zurück, die Zeuge für direkte Linksrekursion sind
     */
    public List<ContextFreeProduction> testIndirectRecursion(ContextFreeGrammar contextFreeGrammar) {

        /**
         * Leere Liste anlegen für die Rückgabe
         */
        List<ContextFreeProduction> returnList = new ArrayList<>();

        /**
         * Iteriere über alle Regeln in der Grammatik
         */
        for(ContextFreeRule r : contextFreeGrammar.getRules()) {

            /**
             * Für Jede Regel: Iteriere über alle Produktionen dieser Regel
             */
            for (ContextFreeProduction p : r.getProductions()) {

                if(!memoryMap.containsKey(p.getLeftHandSide())){
                    //System.out.println("Keine indirekte Linksrekursion");
                } else {
                    processProductionPath(p.getLeftHandSide(), p.getLeftHandSide());

                    /**
                     * Ghetto Fix: die Produktion 'S -> ...' muss vom Ende der Liste an den Anfang gesetzt werden.
                     * Falls nur 1 Produktion in der Liste ist, handelt es sich um die erste Produktion mit 'S -> ...'
                     * da hier bereits auf direkte Linksrekursion geprüft wurde muss diese aus der Liste entfernt werden
                     * damit wie verlangt eine leere Liste zurückgegeben wird. (Da es sich bei einer einzelnen
                     * Produktion nicht um eine indirekte Linksrekursion handeln kann.)
                     */
                    if(path.size()>1){
                        ContextFreeProduction first = path.get(path.size()-1);
                        path.remove(path.size()-1);
                        path.add(0, first);
                    } else {
                        path.remove(0);
                    }
                    return path;
                }
            }
        }
        return returnList;
    }


    /**
     * Temporäre Liste für die Funktion 'processProdutionPath'
     */
    private List<ContextFreeProduction> path = new ArrayList<>();

    /**
     * Rekursive Funktion zum Verarbeiten einer Kette an Produktionen
     * @param x Nichtterminal gegen welches auf Linksrekursion geprüft wird.
     * @param y Nichtterminal der aktuell zu prüfenden Produktion
     */
    private void processProductionPath(Symbol x, Symbol y) {

        /**
         * Zuerst wird geprüft ob es für das aktuelle Symbol schon einen Eintrag in der Map, welche die Kandidaten für
         * indirekte Linksrekursion speichert, gibt. Falls nicht kann die aktuelle Produktion nicht an einer
         * Linksrekursion beteiligt sein.
         */
        try{

            /**
             * Holt aus der Kandidaten-Map die Indizes der Produktionen für das zu prüfende Symbol y
             */
            List<Integer> indices = memoryMap.get(y);

            /**
             * Iteriert über alle Indizes und speichert die aktuelle Produktion als Zeuge in der path-Liste
             */
            for(int i : indices) {
                path.add(0, productionMap.get(i));

                /**
                 * Prüft ob die linke Seite der Produktion mit dem aktuell zu prüfenden Index gleich dem übergebenen
                 * Symbol x ist.
                 * Ist dies der Fall wurde eine indirekte Linksrekursion gefunden. Andernfalls wird die aktuelle
                 * Methode rekursiv wieder aufgerufen. Anschaulich bedeutet das: Die Kette der Grammatiken, welche evtl.
                 * zu einer indirekten Linksrekursion führen könnten, wird einen weiter gelaufen. (So lange bis man in
                 * eine Sackgasse gerät oder das ursprüngliche Symbol x wieder findet)
                 */
                if(x.toString() == productionMap.get(i).getLeftHandSide().toString()){
                    System.out.println("Indirekte Linksrekursion für Symbol: " + x + " gefunden");
                    //return;
                } else {
                    processProductionPath(x, productionMap.get(i).getLeftHandSide());
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Für das Symbol: " + y + " wurde keine indirekte Linksrekursion gefunden.");
        }
    }


    /**
     * Hilfsmethode zum speichern aller Produktionen der Grammatik
     * @param contextFreeGrammar
     */
    public void initializeProductionList(ContextFreeGrammar contextFreeGrammar){
        for(ContextFreeRule r : contextFreeGrammar.getRules()) {
            for(ContextFreeProduction p : r.getProductions()) {
                this.productionMap.put(p.getIndex(), p);
            }
        }
    }
}
