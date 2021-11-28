/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static loa.Piece.*;

/**
 * An automated Player.
 *
 * @author Hakim McMorris
 */
class MachinePlayer extends Player {

    /**
     * A position-score magnitude indicating a win (for white if positive,
     * black if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /**
     * A magnitude greater than a normal value.
     */
    private static final int INFTY = Integer.MAX_VALUE;

    /**
     * A new MachinePlayer with no piece or controller (intended to produce
     * a template).
     */
    MachinePlayer() {
        this(null, null);
    }

    /**
     * A MachinePlayer that plays the SIDE pieces in GAME.
     */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /**
     * Return a move after searching the game tree to DEPTH>0 moves
     * from the current position. Assumes the game is not over.
     */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _foundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _foundMove. If the game is over
     * on BOARD, does not set _foundMove.
     */
    private static int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {

        if (depth == 0 || board.gameOver()) {
            return heuristicsVal(board, _move);
        }

        if (saveMove) {
            _currentBoard = board;
        }

        int bestScore;
        if (sense == 1) {
            bestScore = INFTY;
        } else {
            bestScore = -INFTY;
        }

        for (Move move : board.legalMoves()) {
            Board next = new Board(board);
            _move = move;
            next.makeMove(move);
            int score = findMove(next, depth - 1, false, sense * -1, alpha, beta);
            if (score == 1000 || score >= bestScore && board.turn() == BP
                    || score <= bestScore && board.turn() == WP) {
                bestScore = score;
                if (saveMove) {
                    _foundMove = move;
                }
                if (bestScore == 1000) {
                    break;
                }
            }
            if (sense == 1) {
                alpha = Math.max(score, alpha);
            } else {
                beta = Math.min(score, beta);
            }

            if (alpha >= beta) {
                break;
            }
        }

        return bestScore;
    }

    /**
     * Return a search depth for the current position.
     */
    private int chooseDepth() {
        return 2;
    }


    /**
     * @param board The current position of all pieces.
     * @return int
     */
    private static int heuristicsVal(Board board, Move currentMove) {
        Piece turn = board.turn();
        int val = 0;
        boolean recentMove = false;

        for (int i = 0; i < 5 && _currentBoard.getAllMoves().size() >= 5; i++) {
            Move latestMove = _currentBoard.getAllMoves().get(_currentBoard.getAllMoves().size() - (i + 1));
            if (latestMove.getFrom() == currentMove.getFrom()
                    && latestMove.getTo() == currentMove.getTo()) {
                recentMove = true;
                break;
            }

        }

        if (!recentMove) {
            if (turn.opposite() == WP) {
                val = -board.getRegionSizes(WP).size();
            } else if (turn.opposite() == BP) {
                val = -board.getRegionSizes(BP).size();
            }

            if (turn.opposite() == BP && board.getRegionSizes(WP).size() > 3) {
                val += 3;
            } else if (turn.opposite() == WP && board.getRegionSizes(BP).size() > 3){
                val += 4;
            }

        }

        if (currentMove.isCapture()) {
            val += 2;
        }

        if (board.gameOver() &&
                (board.winner() == BP || board.winner() == WP)) {
            val = 1000;
        }

        return val;
    }


    /**
     * Used to convey moves discovered by findMove.
     */
    private static Move _foundMove;

    private static Move _move;

    private static Board _currentBoard;
}
