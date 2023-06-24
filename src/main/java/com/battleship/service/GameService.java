package com.battleship.service;

import com.battleship.exception.InvalidGameException;
import com.battleship.exception.NotFoundException;
import com.battleship.model.Game;
import com.battleship.model.GamePlay;
import com.battleship.model.GameWinner;
import com.battleship.model.Player;
import com.battleship.storage.GameStorage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.UUID;

import static com.battleship.model.GameStatus.*;

@Service
@AllArgsConstructor
@Slf4j
public class GameService {

    public Game createGame(Player player){
        Game game = new Game();
        game.setBoard1(new int[10][10]);
        game.setBoard2(new int[10][10]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setStatus(NEW);
        GameStorage.getInstance().setGame(game);
        game.setPlayerOneShipsNumber(2);
        game.setPlayerTwoShipsNumber(2);
        return game;
    }

    public Game connectToGame(Player player2, String gameId) throws InvalidGameException {
        if(!GameStorage.getInstance().getGames().containsKey(gameId)){
            throw new InvalidParameterException("There is no game with matching game id");

        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        if(game.getPlayer2() != null){
            throw new InvalidGameException("Game is not valid");
        }

        game.setPlayer2(player2);
        game.setStatus(IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        game.setCurrentPlayer(1);
        return game;
    }


//Player 1 ship setup turn
    public Game gamePlay1(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }


        int[][] board1 = game.getBoard1();
        int[][] board2 = game.getBoard2();


        // Setting up ships on the board
            if (game.getCurrentPlayer() == 1 && game.getPlayerOneShipsNumber() > 0) {
                board1[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = 1;

                game.setPlayerOneShipsNumber(game.getPlayerOneShipsNumber() - 1);;
                // Switch to the next player's turn
                game.setCurrentPlayer(game.getCurrentPlayer() == 1 ? 2 : 1);
            } else {
                log.info("You must wait for your turn in order to place your ship");
            }


        return game;
    }


    //Player 2 ship setup turn
    public Game gamePlay2(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }


        int[][] board1 = game.getBoard1();
        int[][] board2 = game.getBoard2();


        // Setting up ships on the board

            if (game.getCurrentPlayer() == 2 && game.getPlayerTwoShipsNumber() > 0) {
                //Place ship on the board
                board2[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = 1;

                //Reduce number of ships avaliable in the pool for playerTwo
                game.setPlayerTwoShipsNumber(game.getPlayerTwoShipsNumber() - 1);

                // Switch to the next player's turn
                game.setCurrentPlayer(game.getCurrentPlayer() == 1 ? 2 : 1);
            } else {
                log.info("You must wait for your turn in order to place your ship");
            }


        return game;
    }


    //Player1 firing at enemy ships
    public Game gamePlay1fire(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }


        int[][] board1 = game.getBoard1();
        int[][] board2 = game.getBoard2();


            if (game.getCurrentPlayer() == 1 && game.getStatus() == IN_PROGRESS) {
                board2[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = 2;
                // Switch to the next player's turn
                game.setCurrentPlayer(game.getCurrentPlayer() == 1 ? 2 : 1);
                if (checkWinner(board2)) {
                    game.setWinner(GameWinner.PLAYER1_WON);
                    game.setStatus(FINISHED);
                }
            } else {
                log.info("Its player's 2 turn to fire");
            }


        return game;
    }


    //Player 2 firing at enemy ships
    public Game gamePlay2fire(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }


        int[][] board1 = game.getBoard1();
        int[][] board2 = game.getBoard2();



        // Firing at enemy ships

            if (game.getCurrentPlayer() == 2 && game.getStatus() == IN_PROGRESS) {
                board1[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = 2;

                // Switch to the next player's turn
                game.setCurrentPlayer(game.getCurrentPlayer() == 1 ? 2 : 1);
                if (checkWinner(board1)) {
                    game.setWinner(GameWinner.PLAYER2_WON);
                    game.setStatus(FINISHED);
                }
            } else {
                log.info("Its player's 1 turn to fire");
            }


        return game;
    }

















    public static boolean checkWinner(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
