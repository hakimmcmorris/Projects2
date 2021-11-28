
import java.util.*;


public class Main {

    private static Scanner x = new Scanner(System.in);


    public static void printBoard(Board b) {
        Square[][] board = reverse(b.getBoard());
         for (Square[] x : board) {
            for (Square y : x) {
                System.out.print(y.getName());
                System.out.print(" ");
            }
            System.out.println();
         }
        System.out.println();
        System.out.println(b.getPlayer1() + ": " + b.getP1Size() + "     " + b.getPlayer2() + ": " + b.getP2Size());
    }

    private static Square[][] reverse(Square[][] b) {
        Square[][] b2 = new Square[8][1];
        int i = 0;
        int j = b.length - 1;
        while (i <= j) {
            Square[] temp = b[j].clone();
            b2[j] = b[i].clone();
            b2[i] = temp;
            i++;
            j--;
        }

        return b2;
    }

    private static boolean legalMove(Board b, Piece p) {
        return true;
    }

    private static boolean checkPiece(String s, Board b) {
        int xcor = Character.getNumericValue(s.toCharArray()[0]);
        int ycor = Character.getNumericValue(s.toCharArray()[1]);
        Square sq = b.getBoard()[ycor][xcor];
        return sq.getPiece() == null;
    }

    private static void process(Board b1) {
        int check = b1.getTurnCount();
        String move;
        while (true) {
            b1.setAllMoves();
            System.out.println(b1.getTurn() + ", pick your checker to move");
            System.out.print("Here are your movable pieces: ");
            for (Piece p : b1.getPlayerPieces()) {
                if (!p.getAllMoves().isEmpty()) {
                    System.out.print(String.format("%d%d ", p.getxCor(), p.getyCor()));
                }
            }
            System.out.println();
            move = x.nextLine();
            if (move.length() == 2) {
                break;
            } else {
                System.out.println("Incorrect move input. Must be 2 numbers.");
            }
        }
        int xcor = Character.getNumericValue(move.toCharArray()[0]);
        int ycor = Character.getNumericValue(move.toCharArray()[1]);
        while (true) {
            boolean incorrect = false;
            ArrayList<int[]> arr = new ArrayList<>();
            Square s = null;
            while (true) {
                if (xcor > 7 || ycor > 7) {
                    System.out.println("Not on board");
                    break;
                } else {
                    s = b1.getBoard()[ycor][xcor];
                }
                if (s.getPiece() == null) {
                    System.out.println("Empty");
                    break;
                }
                if (Integer.parseInt(s.getPiece().getPlayer()) != b1.getTurnCount()) {
                    incorrect = true;
                    System.out.println("Not your checker to move");
                    break;
                } else if (s.getPiece() != null) {
                    s.getPiece().setMoves(b1);
                    arr = s.getPiece().getAllMoves();
                    if (arr.size() == 0) {
                        System.out.println("No available moves");
                    } else {
                        System.out.print("Here are you available moves: ");
                        for (int[] i : arr) {
                            for (int j : i) {
                                System.out.print(j);
                            }
                            System.out.print(" ");
                        }
                        System.out.println();
                        while (true) {
                            System.out.println("Pick a move or a new checker to move");
                            move = x.nextLine();
                            xcor = Character.getNumericValue(move.toCharArray()[0]);
                            ycor = Character.getNumericValue(move.toCharArray()[1]);
                            if (xcor > 7 || ycor > 7) {
                                System.out.println("Not on board");
                            } else if (move.length() == 2) {
                                break;
                            } else {
                                System.out.println("Incorrect move input. Must be 2 numbers.");
                            }
                        }

                        if (checkPiece(move, b1)) {
                            Move.move(xcor, ycor, s, b1);
                            printBoard(b1);
                            b1.switchTurn();
                        }
                    }
                    break;
                }
            }
            if (check != b1.getTurnCount() || incorrect || arr.size() == 0 || s.getPiece() == null) {
                break;
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Board b1 = new Board();
        b1.placePieces();
        b1.setSizes();
        b1.setPlayers(x);

        java.lang.Thread.sleep(250);
        System.out.println(b1.getPlayer1() + "  vs  " + b1.getPlayer2());
        String msg = "GET READY!!!";
        for (char c : msg.toCharArray()) {
            System.out.print(c);
            java.lang.Thread.sleep(250);
        }
        System.out.println();

        printBoard(b1);
        while(!b1.gameOver()) {
            process(b1);
        }
        b1.nameWinner();
        if (b1.rematch(x)) {
            Board.setupBoard(b1);
            b1.placePieces();
            b1.setSizes();
            process(b1);
        } else {
            System.exit(0);
        }
    }
}

