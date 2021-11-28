import java.util.ArrayList;
import java.util.Scanner;

public class Board {

    private final Square[][] BOARD;
    private final ArrayList<Piece> p1Size = new ArrayList<Piece>();
    private final ArrayList<Piece> p2Size = new ArrayList<Piece>();
    private int turnCount = 1;
    private String player1;
    private String player2;

    public Board() {
        BOARD = new Square[8][8];
        setupBoard(this);
    }

    public int getTotalSize() {
        return p1Size.size() + p2Size.size();
    }

    public int getP1Size() { return p1Size.size(); }

    public int getP2Size() { return p2Size.size(); }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getLENGTH() {
        return BOARD.length;
    }

    public Square[][] getBoard() {
        return BOARD;
    }

    public static void setupBoard(Board b) {
        for (int i = 0; i < b.getLENGTH(); i++) {
            for (int j = 0; j < b.getBoard()[i].length; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        b.getBoard()[i][j] = new Square("R", j, i);
                    } else {
                        b.getBoard()[i][j] = new Square("B", j, i);
                    }
                } else {
                    if (j % 2 == 1) {
                        b.getBoard()[i][j] = new Square("R", j, i);
                    } else {
                        b.getBoard()[i][j] = new Square("B", j, i);
                    }
                }
            }
        }
    }

    public void setPlayers(Scanner x) {
        while (true) {
            System.out.println("Player1, What is your name?");
            player1 = x.nextLine();
            if (player1.isEmpty()) {
                System.out.println("You must have a name");
            } else {
                System.out.println();
                break;
            }
        }
        while (true) {
            System.out.println("Player2, What is your name?");
            player2 = x.nextLine();
            if (player2.isEmpty()) {
                System.out.println("You must have a name");
            } else {
                System.out.println();
                break;
            }
        }
    }

    public void setSizes() {
        for (Square[] pA : this.getBoard()) {
            for (Square s : pA) {
                if (s.getPiece() != null) {
                    if (s.getPiece().getPlayer().equals("1")) {
                        p1Size.add(s.getPiece());
                    } else if (s.getPiece().getPlayer().equals("2")) {
                        p2Size.add(s.getPiece());
                    }
                }
            }
        }
    }

    public void setAllMoves() {
        if (turnCount == 1) {
            for (Piece p : p1Size) {
                p.setMoves(this);
            }
        } else {
            for (Piece p : p2Size) {
                p.setMoves(this);
            }
        }
    }

    public void placePieces() {
        for (int i = 0; i < this.getLENGTH(); i++) {
            for (int j = 0; j < this.getBoard()[i].length; j++) {
                if ((i == 0 || i == 2) && j % 2 == 1) {
                    this.getBoard()[i][j].setPlayer(new Piece("1", j, i));
                } else if (i == 1 &&  (j == 0 || j % 2 == 0)) {
                    this.getBoard()[i][j].setPlayer(new Piece("1", j, i));
                } else if ((i == 5 || i == 7) && (j == 0 || j % 2 == 0)) {
                    this.getBoard()[i][j].setPlayer(new Piece("2", j, i));
                } else if (i == 6 && j % 2 == 1) {
                    this.getBoard()[i][j].setPlayer(new Piece("2", j, i));
                }
            }
        }
    }

    public void switchTurn() {
        if (turnCount == 1) {
            turnCount = 2;
        } else {
            turnCount = 1;
        }
    }

    public String getTurn() {
        if (turnCount == 1) {
            return player1;
        }
        return player2;
    }

    public boolean gameOver() {
        return (p1Size.size() == 0 || p2Size.size() == 0);
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void decreaseSize(Square s) {
        int index;
        if (s.getPiece() != null) {
            if (s.getPiece().getPlayer().equals("1")) {
                index = p1Size.indexOf(s.getPiece());
                p1Size.remove(index);
            } else if (s.getPiece().getPlayer().equals("2")) {
                index = p2Size.indexOf(s.getPiece());
                p2Size.remove(index);
            }
        }
    }

    public void nameWinner() {
        if (p1Size.size() == 0) {
            System.out.println(player1 + " has won the match!");
        } else if (p2Size.size() == 0) {
            System.out.println(player2 + " has won the match!");
        }
        System.out.println("Game Over");
    }

    public boolean rematch(Scanner x) {
        while (true) {
            System.out.println("Would you like a rematch? (Enter yes or no)");
            String ans = x.nextLine();
            ans = ans.toLowerCase();
            if (ans.equals("yes")) {
                return true;
            } else if (ans.equals("no")) {
                return false;
            } else {
                System.out.println("Incorrect Input");
            }
        }
    }

    public ArrayList<Piece> getPlayerPieces() {
        if (turnCount == 1) {
            return p1Size;
        } else {
            return p2Size;
        }
    }



}
