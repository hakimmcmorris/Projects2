public class Square {

    private final String color;
    private Piece player;
    private final int xCor;
    private final int yCor;
    private boolean JUMP = false;

    public Square(String color, int x, int y) {
        this.color = color;
        this.xCor = x;
        this.yCor = y;
    }

    public String getName() {
        if (player != null) {
            return player.getPlayer();
        }
        return color;
    }

    public Piece getPiece() {
        return player;
    }

    public String getColor() {
        return color;
    }

    public void setPlayer(Piece p) {
        player = p;
    }

    public int getyCor() {
        return yCor;
    }

    public int getxCor() {
        return xCor;
    }

    public void nullPiece() {
        player = null;
    }

    public void makeJUMP() {
        JUMP = true;
    }

    public void resetJUMP() {
        JUMP = false;
    }

    public boolean getJUMP() {
        return JUMP;
    }

}
