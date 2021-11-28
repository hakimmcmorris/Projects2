import java.util.ArrayList;
import java.util.Scanner;

public class Piece {

    private boolean QUEEN;
    private String player;
    private int xCor;
    private int yCor;
    private Move move;


    public Piece(String player, int x, int y) {
        this.QUEEN = false;
        this.player = player;
        this.xCor = x;
        this.yCor = y;
    }

    public void makeQueen() {
        this.QUEEN = true;
        if (player.equals("1")) {
            player = "K";
        } else {
            player = "Q";
        }
    }

    public boolean isQUEEN() {
        return QUEEN;
    }

    public String getPlayer() {
        return player;
    }

    public int getxCor() {
        return xCor;
    }

    public int getyCor() {
        return yCor;
    }

    public void setMoves(Board b) {
        move = new Move(b, this);
    }

    public ArrayList<int[]> getAllMoves() {
        return move.getMoveSets();
    }

    public Move getMove() {
        return move;
    }

    public void setXandYCor(int x, int y) {
        xCor = x;
        yCor = y;
    }

    public void checkQueen() {
        if (player.equals("1") && !isQUEEN() && yCor == 7) {
            makeQueen();
        } else if (player.equals("2") && !isQUEEN() && yCor == 0) {
            makeQueen();
        }
    }

}
