package br.com.lenonsec.minefield;

import br.com.lenonsec.minefield.models.GameBoard;
import br.com.lenonsec.minefield.views.Console;

public class Application {
    public static void main(String[] args) {
        GameBoard gameBoard = new GameBoard(6, 6, 6);

        new Console(gameBoard);
    }
}
