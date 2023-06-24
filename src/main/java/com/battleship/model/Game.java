package com.battleship.model;

import lombok.Data;

@Data
public class Game {

    private String gameId;
    private GameStatus status;
    private Player player1;
    private Player player2;
    private int [][] board1;
    private int [][] board2;
    private GameWinner winner;
    private int currentPlayer;
    private int playerOneShipsNumber;
    private int playerTwoShipsNumber;

    public void setBoard1(int[][] board1) {
        this.board1 = board1;
    }

    public void setBoard2(int[][] board2) {
        this.board2 = board2;
    }

    public void setWinner(GameWinner winner) {
        this.winner = winner;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPlayerOneShipsNumber(int playerOneShipsNumber) {
        this.playerOneShipsNumber = playerOneShipsNumber;
    }

    public void setPlayerTwoShipsNumber(int playerTwoShipsNumber) {
        this.playerTwoShipsNumber = playerTwoShipsNumber;
    }
}
