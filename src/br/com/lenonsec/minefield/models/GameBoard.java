package br.com.lenonsec.minefield.models;

import br.com.lenonsec.minefield.exceptions.ExplosionException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GameBoard {
    private Integer lines;
    private Integer columns;
    private Integer mines;

    private final List<Field> fields = new ArrayList<>();

    public GameBoard(Integer lines, Integer columns, Integer mines) {
        this.lines = lines;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        associateNeighbors();
        raffleMines();
    }

    public void open(Integer line, Integer colum) {
        try {
            fields
                    .parallelStream()
                    .filter(field -> field.getX() == line && field.getY() == colum)
                    .findFirst()
                    .ifPresent(Field::open);
        } catch (ExplosionException e) {
            fields.forEach(field -> field.setOpened(true));

            throw e;
        }
    }

    public void mark(Integer line, Integer colum) {
        fields
                .parallelStream()
                .filter(field -> field.getX() == line && field.getY() == colum)
                .findFirst()
                .ifPresent(Field::toggleMarker);
    }

    private void generateFields() {
        for (int x = 0; x < lines; x++) {
            for (int y = 0; y < columns; y++) {
                fields.add(new Field(x, y));
            }
        }
    }

    private void associateNeighbors() {
        for (Field field1 : fields) {
            for (Field field2 : fields) {
                field1.addNeighbor(field2);
            }
        }
    }

    private void raffleMines() {
        long armedMines = 0L;
        Predicate<Field> mined = Field::getMined;

        do {
            int random = (int) (Math.random() * fields.size());

            fields.get(random).addMine();

            armedMines = fields.stream().filter(mined).count();
        } while (armedMines < mines);
    }

    public Boolean goalAchieved() {
        return fields.stream().allMatch(Field::goalAchieved);
    }

    public void restart() {
        fields.forEach(Field::restart);
        raffleMines();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("  ");
        for (int y = 0; y < columns; y++) {
            stringBuilder.append(" ");
            stringBuilder.append(y);
            stringBuilder.append(" ");
        }

        stringBuilder.append("\n");

        int i = 0;
        for (int x = 0; x < lines; x++) {
            stringBuilder.append(x);
            stringBuilder.append(" ");
            for (int y = 0; y < columns; y++) {
                stringBuilder.append(" ");
                stringBuilder.append(fields.get(i));
                stringBuilder.append(" ");
                i++;
            }

            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
