import uni_bayreuth.ai6.compiler.exercises.chesschecker.interfaces.ChessPlayer;
import uni_bayreuth.ai6.compiler.exercises.chesschecker.interfaces.ChessState;
import uni_bayreuth.ai6.compiler.exercises.chesschecker.interfaces.IChessChecker;

import java.util.*;

public class ChessChecker implements IChessChecker {
    /**
     * speichert über den Buchstaben für eine Figur alle Position auf der sie steht in der Form [Spalte1, Zeile1,
     * Spalte2, Zeile2,...]
     */
    private HashMap<Character, List<Integer>> allFigures = new HashMap<>();
    private boolean[][] fieldTaken = new boolean[8][8];
    private List<Character> figures = Arrays.asList('K', 'Q', 'R', 'B', 'N', 'P');
    private List<Character> columns = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
    private List<Character> rows = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8');
    private List<Integer> reasons = new ArrayList<>();

    @Override
    public ChessState checkPlacement(String s) throws IllegalArgumentException {
        for(int i = 0; i < s.length()/3; i++) {
            //iteriere über alle PCR-Sequenzen
            String placement = s.substring(3*i, 3*(i+1));
            parsePlacement(placement);
        }

        //für jede existierende Figur in allFigures, überprüfe die Bedingungen an eine gültige Platzierung und Anzahl
        for(Map.Entry<Character, List<Integer>> pair: allFigures.entrySet()) {
            if(!isValidNumber(pair.getKey(), pair.getValue().size()/2)) reasons.add(ChessState.TOO_MAY_FIGURES);
            if(isFieldTaken(pair.getValue())) reasons.add(ChessState.DOUBLE_PLACEMENT);
        }

        //überprüfe Platzierungsregeln für Läufer
        if(!areBishopsValid(allFigures.get('B')) || !areBishopsValid(allFigures.get('b')))
            reasons.add(ChessState.BISHOP_ON_SAME_COLOR);

        //prüfe ob beide Könige exisitieren, falls ja: überprüfe ob beide nicht im Schach stehen
        ChessPlayer playerInChess = null;
        if(allFigures.get('K') == null || allFigures.get('k') == null) reasons.add(ChessState.KING_MISSING);
        else {
            boolean whiteKingChess = isKingInChess(allFigures.get('K'), ChessPlayer.WHITE);
            boolean blackKingChess = isKingInChess(allFigures.get('k'), ChessPlayer.BACK);
            if(whiteKingChess && blackKingChess) reasons.add(ChessState.BOTH_KINGS_ARE_IN_CHESS);
            else if(whiteKingChess) playerInChess = ChessPlayer.WHITE;
            else if(blackKingChess) playerInChess = ChessPlayer.BACK;
            else playerInChess = ChessPlayer.NOBODY;
        }

        if(reasons.isEmpty()) {
            return new ChessState(s, playerInChess);
        }
        return new ChessState(s, reasons.toArray(new Integer[reasons.size()]));
    }

    /**
     * Überprüft, ob der König der Spielerfarbe player im Schach steht
     */
    private boolean isKingInChess(List<Integer> kingPosition, ChessPlayer player) {
        List<Character> foes = new ArrayList<>(figures);
        //wenn white, dann Gegner schwarz und Buchstaben klein
        if(player == ChessPlayer.WHITE) {
            for(int i = 0; i<foes.size(); i++) {
                foes.set(i, Character.toLowerCase(foes.get(i)));
            }
        }
        for(Character c : foes){
            //horizontal oder vertikal zu dame oder turm ohne andere Figur dazwischen
            //diagonal zu dame oder läufer
            //diagonal ein feld zu bauer
            //springer ganz komisch
            List<Integer> pos = allFigures.get(c);
            if(pos == null) continue;
            switch(c){
                case 'K':
                case 'k':
                    if(isHorizontallyOrVerticallyChess(kingPosition, pos, 1) || isDiagonalChess(kingPosition, pos, 1))
                        return true;
                    break;
                case 'Q':
                case 'q':
                    if(isHorizontallyOrVerticallyChess(kingPosition, pos, 8) || isDiagonalChess(kingPosition, pos, 8))
                        return true;
                    break;
                case 'R':
                case 'r':
                    if(isHorizontallyOrVerticallyChess(kingPosition, pos, 8)) return true;
                    break;
                case 'B':
                case 'b':
                    if(isDiagonalChess(kingPosition, pos, 8)) return true;
                    break;
                case 'N':
                case 'n':
                    if(inKnightChess(kingPosition, pos)) return true;
                    break;
                case 'P':
                case 'p':
                    if(isDiagonalChess(kingPosition, pos, 1)) return true;
            }
        }
        return false;
    }

    private boolean inKnightChess(List<Integer> kingPosition, List<Integer> knightPositions) {
        for(int i = 0; i<knightPositions.size()/2; i+=2) {
            //nicht horizontal oder vertikal gleich
            if(Objects.equals(kingPosition.get(0), knightPositions.get(i)) ||
                    Objects.equals(kingPosition.get(1), knightPositions.get(i+1))) break;
            if(Math.abs(kingPosition.get(0) - knightPositions.get(i)) +
                    Math.abs(kingPosition.get(1) - knightPositions.get(i+1)) == 3) return true;
        }
        return false;
    }

    /**
     * Überprüft ob der König diagonal im Schach steht innerhalb der Reichweite range.
     */
    private boolean isDiagonalChess(List<Integer> kingPosition, List<Integer> foePositions, int range) {
        for(int i = 0; i<foePositions.size()/2; i+=2) {
            if(Math.abs(kingPosition.get(0) + kingPosition.get(1) - foePositions.get(0) - foePositions.get(1)) <= range &&
                    isNotBlocked(kingPosition, foePositions)) return true;
        }
        return false;
    }

    /**
     * Überprüft ob der König horizontal oder vertikal im Schach steht innerhalb der Reichweite range.
     */
    private boolean isHorizontallyOrVerticallyChess(List<Integer> kingPosition, List<Integer> foePositions, int range) {
        for(int i = 0; i<foePositions.size()/2; i+=2){
            if(Objects.equals(kingPosition.get(0), foePositions.get(i)) &&
                    Math.abs(kingPosition.get(0) - foePositions.get(i)) <=range &&
                    isNotBlocked(kingPosition, foePositions))
                return true;
            if(Objects.equals(kingPosition.get(1), foePositions.get(i+1)) &&
                    Math.abs(kingPosition.get(1) - foePositions.get(i+1)) <=range &&
                    isNotBlocked(kingPosition, foePositions))
                return true;
        }
        return false;
    }

    /**
     * Gibt true zurück, wenn keine Figur zwischen den zwei Positionen steht. 
     */
    private boolean isNotBlocked(List<Integer> start, List<Integer> target) {
        //wenn nicht diagonal zueinander und nicht direkt nebeneinander, überprüfe nur eine Richtung
        if(Objects.equals(start.get(0), target.get(0)) && Math.abs(start.get(1) - target.get(1)) > 1){
            int i = start.get(1);
            while(i != target.get(1)) {
                if(i < target.get(1)) i++;
                else if(i > target.get(1)) i--;
                if(fieldTaken[start.get(0)][i]) return false;
            }
        }
        else if(Objects.equals(start.get(1), target.get(1)) && Math.abs(start.get(0) - target.get(0)) > 1){
            int i = start.get(0);
            while(i != target.get(0)) {
                if(i < target.get(0)) i++;
                else if(i > target.get(0)) i--;
                if(fieldTaken[i-1][start.get(1)-1]) return false;
            }
        }
        //diagonal
        else {
            int i = start.get(0);
            int j = start.get(1);
            while(i != target.get(0) && j != target.get(1)) {
                if(i < target.get(0)) i++;
                else if(i > target.get(0)) i--;
                if(j < target.get(1)) j++;
                else if(j > target.get(1)) j--;
                if(fieldTaken[i-1][j-1]) return false;
            }
        }
        return true;
    }


    private boolean areBishopsValid(List<Integer> bishopPositions) {
        /* wenn beide Läufer vorhanden sind, muss die Summe einer Position ungerade sein und der anderen gerade
            (gerade <--> weiß, ungerade <--> schwarz) */
        if(bishopPositions != null && bishopPositions.size() == 4) {
            int sumFirstPosition = bishopPositions.get(0) + bishopPositions.get(1);
            int sumSecondPosition = bishopPositions.get(2) + bishopPositions.get(3);
            return sumFirstPosition % 2 == 0 && sumSecondPosition % 2 == 1 ||
                    sumFirstPosition % 2 == 1 && sumSecondPosition % 2 == 0;
        }
        return true;
    }

    private boolean isFieldTaken(List<Integer> coordinates) {
        boolean taken = false;
        for(int i = 0; i < coordinates.size() / 2; i++) {
            if(fieldTaken[coordinates.get(i*2)][coordinates.get(i*2+1)-1]) taken = true;
            else fieldTaken[coordinates.get(i*2)][coordinates.get(i*2+1)-1] = true;
        }
        return taken;
    }

    private boolean isValidNumber(Character figure, int count) {
        switch(figure){
            case 'K':
            case 'k':
                return count<=1;
            case 'Q':
            case 'q':
                return count<=1;
            case 'R':
            case 'r':
                return count<=2;
            case 'B':
            case 'b':
                return count<=2;
            case 'N':
            case 'n':
                return count<=2;
            case 'P':
            case 'p':
                return count<=8;
        }
        return false;
    }

    /***
     * Erhält eine Sequence PCR und bewertet, ob dies eine gültige Platzierung ist. Falls ja, wird diese in "allFigures"
     * hinzugefügt.
     */
    private void parsePlacement(String placement) {
        int state = 0;
        while(true){
            switch(state){
                case 0:
                    if(!isPiece(placement.charAt(0)))
                        throw new IllegalArgumentException(placement.charAt(0) +
                                "ist keine gültige Figurenbezeichnung.");
                    state = 1;
                    break;
                case 1:
                    if(!isColumn(placement.charAt(1)))
                        throw new IllegalArgumentException(placement.charAt(1) + "ist keine gültige Spalte.");
                    state = 2;
                    break;
                case 2:
                    if(!isRow(placement.charAt(2)))
                        throw new IllegalArgumentException(placement.charAt(2) + "ist keine gültige Zeile.");

                    int col = positionInAlphabet(placement.charAt(1));
                    int row = Character.getNumericValue(placement.charAt(2));
                    if(allFigures.containsKey(placement.charAt(0))){
                        List<Integer> oldValue = allFigures.get(placement.charAt(0));
                        oldValue.add(col);
                        oldValue.add(row);
                        return;
                    }
                    ArrayList<Integer> position = new ArrayList<>();
                    position.add(col);
                    position.add(row);
                    allFigures.put(placement.charAt(0), position);
                    return;
            }
        }
    }

    private int positionInAlphabet(char c) {
        return "abcdefgh".indexOf(c)+1;
    }

    private boolean isRow(char c) {
        return rows.contains(c);
    }

    private boolean isColumn(char c) {
        return columns.contains(c);
    }

    private boolean isPiece(char c) {
        return figures.contains(Character.toUpperCase(c));
    }

    public static void main(String[] args) {
        ChessChecker checker = new ChessChecker();
        String placement = "qa8Bf7kf1rg5Kf4Pa7Pb7nf2";
        ChessState state = checker.checkPlacement(placement);
        System.out.print(state.whoIsInChess().name() + "\n");
        System.out.print("Gültige Platzierung: " + state.isValid() + "\n");
    }
}

