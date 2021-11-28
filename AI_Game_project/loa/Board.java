/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/**
 * Represents the state of a game of Lines of Action.
 *
 * @author Hakim McMorris
 */
class Board {

    /**
     * Default number of moves for each side that results in a draw.
     */
    static final int DEFAULT_MOVE_LIMIT = 500;

    /**
     * Pattern describing a valid square designator (cr).
     */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /**
     * A Board whose initial contents are taken from INITIALCONTENTS
     * and in which the player playing TURN is to move. The resulting
     * Board has
     * get(col, row) == INITIALCONTENTS[row][col]
     * Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     * <p>
     * CAUTION: The natural written notation for arrays initializers puts
     * the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /**
     * A new board in the standard initial position.
     */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /**
     * A Board whose initial contents and state are copied from
     * BOARD.
     */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /**
     * Set my state to CONTENTS with SIDE to move.
     */
    void initialize(Piece[][] contents, Piece side) {
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
        int counterR = 0;
        int counterC = 0;
        for (int i = 0; i < _board.length; i++) {
            if (counterC >= 8) {
                counterC = 0;
                counterR++;
            }
            _board[i] = contents[counterR][counterC];
            counterC++;
        }

    }

    /**
     * Set me to the initial configuration.
     */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /**
     * Set my state to a copy of BOARD.
     */
    void copyFrom(Board board) {
        for (int i = 0; i < board._board.length; i++) {
            _board[i] = board._board[i];
        }

        _turn = board.turn();
        _blackRegionSizes.addAll(board._blackRegionSizes);
        _whiteRegionSizes.addAll(board._whiteRegionSizes);
        _subsetsInitialized = board._subsetsInitialized;
        _winner = board._winner;
        _winnerKnown = board._winnerKnown;
        _moveLimit = board._moveLimit;
        _moves.addAll(board._moves);
    }

    /**
     * Return the contents of the square at SQ.
     * @param sq Square on the board.
     * @return Piece.
     */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /**
     * Set the square at SQ to V and set the side that is to move next
     * to NEXT, if NEXT is not null.
     * @param sq Square being set.
     * @param v The piece being placed on that square.
     * @param next The turn of the next move.
     */
    void set(Square sq, Piece v, Piece next) {
        _board[sq.index()] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /**
     * Set the square at SQ to V, without modifying the side that
     * moves next.
     */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /**
     * Set limit on number of moves by each side that results in a tie to
     * LIMIT, where 2 * LIMIT > movesMade().
     */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /**
     * Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     * is false.
     */
    void makeMove(Move move) {
        assert isLegal(move);

        set(move.getTo(), _board[move.getFrom().index()], _turn.opposite());
        set(move.getFrom(), EMP, null);

        _moves.add(move);
        _subsetsInitialized = false;
    }

    /**
     * Retract (unmake) one move, returning to the state immediately before
     * that move.  Requires that movesMade () > 0.
     */
    void retract() {
        assert movesMade() > 0;
        Move temp = _moves.get(_moves.size() - 1);
        set(temp.getFrom(), _board[temp.getTo().index()], _turn.opposite());
        set(temp.getTo(), _board[temp.getFrom().index()].opposite(), null);
        _moves.remove(temp);
        _subsetsInitialized = false;
    }

    /**
     * Return the Piece representing who is next to move.
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Return true iff FROM - TO is a legal move for the player currently on
     * move.
     * @param from From square.
     * @param to To square.
     * @return boolean
     */
    boolean isLegal(Square from, Square to) {
        if (_board[from.index()] != _turn) {
            return false;
        }
        return from.isValidMove(to)
                && !blocked(from, to)
                && !_board[from.index()].equals(EMP);
    }

    /**
     * Return true iff MOVE is legal for the player currently on move.
     * The isCapture() property is ignored.
     * @param move The move being checked.
     * @return boolean
     */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /**
     * Return a sequence of all legal moves from this position.
     */
    List<Move> legalMoves() {
        ArrayList<Move> allMoves = new ArrayList<Move>();
        for (int i = 0; i < _board.length; i++) {
            if (_board[i] == _turn) {
                Square fromSq = ALL_SQUARES[i];
                for (int dir = 0; dir < 8; dir++) {
                    int steps = numberOfPieces(fromSq, dir);
                    Square toSq = fromSq.moveDest(dir, steps);
                    if (toSq != null && isLegal(fromSq, toSq)) {
                        allMoves.add(Move.mv(fromSq, toSq));
                    }
                }
            }
        }
        return allMoves;
    }

    /**
     * Return true iff the game is over (either player has all his
     * pieces continguous or there is a tie).
     */
    boolean gameOver() {
        return winner() != null;
    }

    /**
     * Return true iff SIDE's pieces are continguous.
     */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /**
     * Return the winning side, if any.  If the game is not over, result is
     * null.  If the game has ended in a tie, returns EMP.
     */
    Piece winner() {
        if (!_winnerKnown) {
            if (piecesContiguous(WP)) {
                _winner = WP;
                _winnerKnown = true;
            } else if (piecesContiguous(BP)) {
                _winner = BP;
                _winnerKnown = true;
            } else if (_moveLimit <= _moves.size()) {
                _winnerKnown = true;
                _winner = EMP;
            }
        }
        return _winner;
    }

    /**
     * Return the total number of moves that have been made (and not
     * retracted).  Each valid call to makeMove with a normal move increases
     * this number by 1.
     */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /**
     * Return true if a move from FROM to TO
     * is blocked by an opposing
     * piece or by a friendly piece on the target square.
     * @param from Piece that wants to move.
     * @param to The square the piece wants to move to.
     * @return boolean True iff blocked.
     */
    private boolean blocked(Square from, Square to) {
        if (_board[from.index()] == _board[to.index()]
                || numberOfPieces(from, from.direction(to))
                != from.distance(to)) {
            return true;
        }
        if (!exists(from.col(), from.row()) || !exists(to.col(), to.row())) {
            return true;
        }
        if (from != to && _board[from.index()] != EMP) {
            int dir = from.direction(to);
            int steps = from.distance(to);
            Piece fromP = _board[from.index()];
            for (int i = 1; i < steps; i++) {
                if (_board[from.moveDest(dir, i).index()]
                        == fromP.opposite()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns number of squares the piece is allowed to
     * move in the dir1 given.
     * @param sq1 Start square.
     * @param dir1 The direction it moves in.
     * @return int The amount of squares the piece is allowed to move.
     */
    int numberOfPieces(Square sq1, int dir1) {
        int stepStone = 1;
        int counter = 1;
        while (true) {
            Square to = sq1.moveDest(dir1, stepStone);
            if (to == null) {
                break;
            }
            if (!_board[to.index()].equals(EMP)) {
                counter++;
            }
            stepStone++;
        }
        dir1 = oppositeDir(dir1);
        stepStone = 1;
        while (true) {
            Square to = sq1.moveDest(dir1, stepStone);
            if (to == null) {
                break;
            }
            if (!_board[to.index()].equals(EMP)) {
                counter++;
            }
            stepStone++;
        }
        return counter;

    }

    /**
     * Gives the opposite direction of the input out of 8.
     * @param dir Direction.
     * @return int The opposite direction.
     */
    int oppositeDir(int dir) {
        return (dir + 4) % 8;
    }

    /**
     * Return the size of the as-yet unvisited cluster of squares
     * containing P at and adjacent to SQ.  VISITED indicates squares that
     * have already been processed or are in different clusters.  Update
     * VISITED to reflect squares counted.
     */
    private int numContig(Square sq, boolean[][] visited, Piece p) {
        if (p == EMP
                || p != _board[sq.index()]
                || visited[sq.row()][sq.col()]) {
            return 0;
        }
        visited[sq.row()][sq.col()] = true;
        int counter = 1;
        for (Square adj : sq.adjacent()) {
            counter += numContig(adj, visited, p);
        }
        return counter;
    }

    /**
     * Set the values of _whiteRegionSizes and _blackRegionSizes.
     */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();

        boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < _board.length; i++) {
            if (_board[i] == WP
                    && !visited[ALL_SQUARES[i].row()][ALL_SQUARES[i].col()]) {
                _whiteRegionSizes.add(
                        numContig(ALL_SQUARES[i], visited, _board[i]));
            } else if (_board[i] == BP
                    && !visited[ALL_SQUARES[i].row()][ALL_SQUARES[i].col()]) {
                _blackRegionSizes.add(
                        numContig(ALL_SQUARES[i], visited, _board[i]));
            }
        }
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }

    /**
     * Return the sizes of all the regions in the current union-find
     * structure for side S.
     */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }


    /**
     * The standard initial configuration for Lines of Action (bottom row
     * first).
     */
    static final Piece[][] INITIAL_PIECES = {
            {EMP, BP, BP, BP, BP, BP, BP, EMP},
            {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
            {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
            {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
            {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
            {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
            {WP, EMP, EMP, EMP, EMP, EMP, EMP, WP},
            {EMP, BP, BP, BP, BP, BP, BP, EMP}
    };

    /**
     * Current contents of the board.  Square S is at _board[S.index()].
     */
    private final Piece[] _board = new Piece[BOARD_SIZE * BOARD_SIZE];

    /**
     * List of all unretracted moves on this board, in order.
     */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /**
     * Current side on move.
     */
    private Piece _turn;
    /**
     * Limit on number of moves before tie is declared.
     */
    private int _moveLimit;
    /**
     * True iff the value of _winner is known to be valid.
     */
    private boolean _winnerKnown;
    /**
     * Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     * in progress).  Use only if _winnerKnown.
     */
    private Piece _winner;

    /**
     * True iff subsets computation is up-to-date.
     */
    private boolean _subsetsInitialized;

    /**
     * List of the sizes of continguous clusters of pieces, by color.
     */
    private final ArrayList<Integer>
            _whiteRegionSizes = new ArrayList<>(),
            _blackRegionSizes = new ArrayList<>();

    ArrayList<Move> getAllMoves() {
        return _moves;
    }



}
