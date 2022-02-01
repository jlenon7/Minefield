package br.com.lenonsec.minefield.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    private GameBoard gameBoard;

    @BeforeEach
    void initField() {
        gameBoard = new GameBoard(3, 3, 9);
    }

    @Test
    void testOpenFieldEvent() {
        gameBoard.registerObserver(e -> {
            if (e.hasWon()){
                System.out.println("Won");
            } else {
                System.out.println("Lost");
            }
        });

        gameBoard.mark(0, 0);
        gameBoard.mark(0, 1);
        gameBoard.mark(0, 2);
        gameBoard.mark(1, 0);
        gameBoard.mark(1, 1);
        gameBoard.mark(1, 2);
        gameBoard.mark(2, 0);
        gameBoard.mark(2, 1);
        gameBoard.open(2, 2);
    }
}
