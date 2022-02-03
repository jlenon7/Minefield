package br.com.lenonsec.minefield.views;

import br.com.lenonsec.minefield.models.GameBoard;

import javax.swing.*;
import java.awt.*;

public class GameBoardPanel extends JPanel {
    public GameBoardPanel(GameBoard gameBoard) {
        setLayout(new GridLayout(gameBoard.getLines(), gameBoard.getColumns()));

        gameBoard.forEachField(field -> add(new FieldButton(field)));
        gameBoard.registerObserver(e -> {
            SwingUtilities.invokeLater(() -> {
                if (e.hasWon()) {
                    JOptionPane.showMessageDialog(this, "You Win :)");
                } else {
                    JOptionPane.showMessageDialog(this, "You Lost :(");
                }

                gameBoard.restart();
            });
        });
    }
}
