package br.com.lenonsec.minefield.views;

import br.com.lenonsec.minefield.contracts.FieldObserver;
import br.com.lenonsec.minefield.enums.FieldEvent;
import br.com.lenonsec.minefield.models.Field;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;

public class FieldButton extends JButton implements FieldObserver, MouseListener {
    private Field field;

    private final Color BG_DEFAULT = new Color(184, 184, 184);
    private final Color BG_MARK = new Color(8, 179, 247);
    private final Color BG_EXPLODE = new Color(189, 66, 68);
    private final Color GREEN_TEXT = new Color(0, 100, 0);

    public FieldButton(Field field) {
        this.field = field;
        setBackground(BG_DEFAULT);
        setOpaque(true);
        setBorder(BorderFactory.createBevelBorder(0));

        addMouseListener(this);
        field.registerObserver(this);
    }

    public void fire(Field field, FieldEvent event) {
        switch (event) {
            case OPEN -> applyOpenStyle();
            case MARK -> applyMarkStyle();
            case EXPLODE -> applyExplodeStyle();
            default -> applyDefaultStyle();
        }
    }

    private void applyOpenStyle() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (field.getMined()) {
            setBackground(BG_EXPLODE);
            return;
        }

        setBackground(BG_DEFAULT);

        switch (field.minesInNeighborhood()) {
            case 1 -> {
                setForeground(GREEN_TEXT);
            }
            case 2 -> {
                setForeground(Color.BLUE);
            }
            case 3 -> {
                setForeground(Color.YELLOW);
            }
            case 4, 5, 6 -> {
                setForeground(Color.RED);
            }
            default -> setForeground(Color.PINK);
        }

        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });

        String value = !field.safeNeighborhood() ? field.minesInNeighborhood() + "" : "";
        setText(value);
    }

    private void applyMarkStyle() {
        setBackground(BG_MARK);
        setForeground(Color.BLACK);

        setIcon(new ImageIcon("/Users/jlenon7/Development/Github/Minefield/src/br/com/lenonsec/minefield/views/assets/flag.png"));
    }

    private void applyExplodeStyle() {
        setBackground(BG_EXPLODE);
        setForeground(Color.WHITE);

        setIcon(new ImageIcon("/Users/jlenon7/Development/Github/Minefield/src/br/com/lenonsec/minefield/views/assets/bomb.png"));
    }

    private void applyDefaultStyle() {
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createBevelBorder(0));
        setIcon(null);
        setText("");
    }

    // Mouse events interface
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            field.open();
        } else {
            field.toggleMarker();
        }
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}
