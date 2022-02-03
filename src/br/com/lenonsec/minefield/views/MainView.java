package br.com.lenonsec.minefield.views;

import br.com.lenonsec.minefield.models.GameBoard;

import javax.swing.JFrame;

public class MainView extends JFrame {
    public MainView() {
        // TODO Ask for game difficulty
        GameBoard gameBoard = new GameBoard(16, 30, 50);

        add(new GameBoardPanel(gameBoard));

        setTitle("Minefield");
        // TODO Set the size of the game according to difficulty
        setSize(690, 428);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainView();
    }
}
