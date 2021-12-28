package br.com.lenonsec.minefield.views;

import br.com.lenonsec.minefield.exceptions.CloseGameException;
import br.com.lenonsec.minefield.exceptions.ExplosionException;
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
        try {
            Boolean continuar = true;

            while (continuar) {
                gameCycle();

                System.out.println("Outra partida ? (S/n) ");

                if ("n".equalsIgnoreCase(scanner.nextLine())) {
                    continuar = false;
                } else {
                    gameBoard.restart();
                }
            }
        } catch (CloseGameException e) {
            System.out.println("Bye bye!!!");
        } finally {
            scanner.close();
        }
    }

    private void gameCycle() {
        try {
            while (!gameBoard.goalAchieved()) {
                System.out.println(gameBoard);

                String typed = captureTypedValue("Digite (x, y): ");

                Iterator<Integer> XY = Arrays
                        .stream(typed.split(","))
                        .map(element -> Integer.parseInt(element.trim()))
                        .iterator();

                typed = captureTypedValue("1 - Abrir ou 2 - (Des)Marcar: ");

                if ("1".equals(typed)) gameBoard.open(XY.next(), XY.next());
                else if ("2".equals(typed)) gameBoard.mark(XY.next(), XY.next());
            }

            System.out.println(gameBoard);
            System.out.println("Você ganhou!");
        } catch (ExplosionException e) {
            System.out.println(gameBoard);
            System.out.println("Você perdeu!");
        }
    }

    private String captureTypedValue(String text) {
        System.out.print(text);
        String typed = scanner.nextLine();

        if ("sair".equalsIgnoreCase(typed)) {
            throw new CloseGameException();
        }

        return typed;
    }
}
