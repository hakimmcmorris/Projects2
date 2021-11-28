import java.util.ArrayList;
import java.util.Scanner;

public class Move {

    private final ArrayList<int[]> moveSets;

    public Move(Board b, Piece p) {
        moveSets = new ArrayList<>();
        availableMoves(b, p);
    }

    private static boolean legality(Board b, int x, int y) {
        return  x >= 0 && y >= 0 && x <= 7 && y <= 7;
    }

    public ArrayList<int[]> getMoveSets() {
        return moveSets;
    }

    private void availableMoves(Board b, Piece p) {
        int x = p.getxCor();
        int y = p.getyCor();
        if (p.isQUEEN() || p.getPlayer().equals("1")) {
            if (legality(b, x + 1, y + 1) && b.getBoard()[y + 1][x + 1].getPiece() == null) {
                moveSets.add(new int[]{x + 1, y + 1});
            } else if (legality(b, x + 2, y + 2) && b.getBoard()[y + 2][x + 2].getPiece() == null
                    && !b.getBoard()[y + 1][x + 1].getPiece().getPlayer().equals(p.getPlayer())) {
                moveSets.add(new int[]{x + 2, y + 2});
            }
            if (legality(b, x - 1, y + 1) && b.getBoard()[y + 1][x - 1].getPiece() == null) {
                moveSets.add(new int[]{x - 1, y + 1});
            } else if (legality(b, x - 2, y + 2) && b.getBoard()[y + 2][x - 2].getPiece() == null
                    && !b.getBoard()[y + 1][x - 1].getPiece().getPlayer().equals(p.getPlayer())) {
                moveSets.add(new int[]{x - 2, y + 2});
            }
        }

        if (p.isQUEEN() || p.getPlayer().equals("2")) {
            if (legality(b, x - 1, y - 1) && b.getBoard()[y - 1][x - 1].getPiece() == null) {
                moveSets.add(new int[]{x - 1, y - 1});
            } else if (legality(b, x - 2, y - 2) && b.getBoard()[y - 2][x - 2].getPiece() == null
                    && !b.getBoard()[y - 1][x - 1].getPiece().getPlayer().equals(p.getPlayer())) {
                moveSets.add(new int[]{x - 2, y - 2});
            }
            if (legality(b, x + 1, y - 1) && b.getBoard()[y - 1][x + 1].getPiece() == null) {
                moveSets.add(new int[]{x + 1, y - 1});
            } else if (legality(b, x + 2, y - 2) && b.getBoard()[y - 2][x + 2].getPiece() == null
                    && !b.getBoard()[y - 1][x + 1].getPiece().getPlayer().equals(p.getPlayer())) {
                moveSets.add(new int[]{x + 2, y - 2});
            }
        }
    }

    public static void move(int x, int y, Square s, Board b) {
        Piece newP = s.getPiece();
        int currSize = b.getTotalSize();
        jump(b, s.getxCor(), s.getyCor(), x, y);
        b.getBoard()[s.getyCor()][s.getxCor()].setPlayer(null);
        b.getBoard()[y][x].setPlayer(newP);
        newP.setXandYCor(x, y);
        newP.checkQueen();
        newP.setMoves(b);
        if (isJump(b, newP) && b.getTotalSize() != currSize) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Would you like to perform a double jump? (Enter yes or no)");
                String dec = scanner.nextLine();
                dec = dec.toLowerCase();
                if (dec.equals("yes")) {
                    System.out.print("Here are your options: ");
                    ArrayList<int[]> moves = newP.getAllMoves();
                    for (int[] i : moves) {
                        int xcor = i[0];
                        int ycor = i[1];
                        Square s2 = b.getBoard()[ycor][xcor];
                        if (s2.getJUMP()) {
                            System.out.print(xcor);
                            System.out.print(ycor);
                            System.out.print(" ");
                            s2.resetJUMP();
                        }
                    }
                    System.out.println();
                    System.out.println("Choose one...");
                    while (true) {
                        dec = scanner.nextLine();
                        if (dec.length() == 2) {
                            int xcor = Character.getNumericValue(dec.charAt(0));
                            int ycor = Character.getNumericValue(dec.charAt(1));
                            move(xcor, ycor, b.getBoard()[newP.getyCor()][newP.getxCor()], b);
                            break;
                        } else {
                            System.out.println("Incorrect move input. Must be 2 numbers.");
                            System.out.println("Choose one of the given options");
                        }
                    }
                    break;
                } else if (dec.equals("no")) {
                    ArrayList<int[]> moves = newP.getAllMoves();
                    for (int[] i : moves) {
                        int xcor = i[0];
                        int ycor = i[1];
                        Square s2 = b.getBoard()[ycor][xcor];
                        if (s2.getJUMP()) {
                            s2.resetJUMP();
                        }
                    }
                    break;
                } else {
                    System.out.println("Incorrect input");
                }
            }
        }
    }

    private static final int[] DIR = new int[]{-2, 2};
    public static void jump(Board b, int x1, int y1, int x2, int y2) {
        boolean res = false;
        for (int i : DIR) {
            for (int j : DIR) {
                if ((x1 + i == x2 || x1 + j == x2) && (y1 + i == y2 || y1 + j == y2)) {
                    res = true;
                    if (x2 - x1 > 0 && y2 - y1 > 0) {
                        b.decreaseSize(b.getBoard()[y1 + 1][x1 + 1]);
                        b.getBoard()[y1 + 1][x1 + 1].nullPiece();
                    } else if (x2 - x1 < 0 && y2 - y1 > 0) {
                        b.decreaseSize(b.getBoard()[y1 + 1][x1 - 1]);
                        b.getBoard()[y1 + 1][x1 - 1].nullPiece();
                    } else if (x2 - x1 < 0 && y2 - y1 < 0) {
                        b.decreaseSize(b.getBoard()[y1 - 1][x1 - 1]);
                        b.getBoard()[y1 - 1][x1 - 1].nullPiece();
                    } else if (x2 - x1 > 0 && y2 - y1 < 0) {
                        b.decreaseSize(b.getBoard()[y1 - 1][x1 + 1]);
                        b.getBoard()[y1 - 1][x1 + 1].nullPiece();
                    }
                    break;
                }
            }
            if (res) {
                break;
            }
        }
    }

    private static boolean isJump(Board b,Piece p) {
        int x1 = p.getxCor();
        int y1 = p.getyCor();
        boolean res = false;
        for (int[] i : p.getAllMoves()) {
            int x2 = i[0];
            int y2 = i[1];
            for (int x : DIR) {
                for (int y : DIR) {
                    if ((x1 + x == x2 || x1 + y == x2) && (y1 + x == y2 || y1 + y == y2)) {
                        res = true;
                        b.getBoard()[y2][x2].makeJUMP();
                        break;
                    }
                }
                if (res) {
                    break;
                }
            }
        }
        return res;
    }

}
