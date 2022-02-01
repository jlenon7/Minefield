package br.com.lenonsec.minefield.views;

import br.com.lenonsec.minefield.models.GameBoard;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Console {
    private GameBoard gameBoard;
    private Scanner scanner = new Scanner(System.in);

    public Console(GameBoard gameBoard) {
        this.gameBoard = gameBoard;

        executeGame();
    }

    private void executeGame() {
        Boolean continueGame = true;

        while (continueGame) {
            gameCycle();

            System.out.println("Another match ? (Y/n) ");

            if ("n".equalsIgnoreCase(scanner.nextLine())) {
                continueGame = false;
            } else {
                gameBoard.restart();
            }
        }
    }

    private void gameCycle() {
        while (!gameBoard.goalAchieved()) {
            System.out.println(gameBoard);

            String typed = captureTypedValue("Type (x, y): ");

            Iterator<Integer> XY = Arrays
                    .stream(typed.split(","))
                    .map(element -> Integer.parseInt(element.trim()))
                    .iterator();

            typed = captureTypedValue("1 - Open ou 2 - (Un)mark: ");

            if ("1".equals(typed)) gameBoard.open(XY.next(), XY.next());
            else if ("2".equals(typed)) gameBoard.mark(XY.next(), XY.next());
        }

        System.out.println(gameBoard);
        System.out.println("You win!");
    }

    private String captureTypedValue(String text) {
        System.out.print(text);

        return scanner.nextLine();
    }
}
