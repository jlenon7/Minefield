package br.com.lenonsec.minefield.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import br.com.lenonsec.minefield.contracts.FieldObserver;
import br.com.lenonsec.minefield.enums.FieldEvent;

public class GameBoard implements FieldObserver {
    private final Integer lines;
    private final Integer columns;
    private final Integer mines;

    private final List<Field> fields = new ArrayList<>();
    private final List<Consumer<EventResult>> observers = new ArrayList<>();

    public GameBoard(Integer lines, Integer columns, Integer mines) {
        this.lines = lines;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        associateNeighbors();
        raffleMines();
    }

    public void forEachField(Consumer<Field> function) {
        fields.forEach(function);
    }

    public void registerObserver(Consumer<EventResult> observer) {
        observers.add(observer);
    }

    private void notifyObservers(Boolean result) {
        observers.forEach(o -> o.accept(new EventResult(result)));
    }

    public void open(Integer line, Integer colum) {
        fields
                .parallelStream()
                .filter(field -> Objects.equals(field.getX(), line) && Objects.equals(field.getY(), colum))
                .findFirst()
                .ifPresent(Field::open);
    }

    private void showMines() {
        fields
                .stream()
                .filter(Field::getMined)
                .filter(field -> !field.getMarked())
                .forEach(field -> field.setOpened(true));

        fields.forEach(field -> field.setOpened(true));
    }

    public void mark(Integer line, Integer colum) {
        fields
                .parallelStream()
                .filter(field -> Objects.equals(field.getX(), line) && Objects.equals(field.getY(), colum))
                .findFirst()
                .ifPresent(Field::toggleMarker);
    }

    private void generateFields() {
        for (int x = 0; x < lines; x++) {
            for (int y = 0; y < columns; y++) {
                Field field = new Field(x, y);

                field.registerObserver(this);

                fields.add(field);
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

    public void fire(Field field, FieldEvent event) {
        if (event == FieldEvent.EXPLODE) {
            this.showMines();
            this.notifyObservers(false);
        } else if (this.goalAchieved()) {
            this.notifyObservers(true);
        }
    }

    public Integer getLines() {
        return lines;
    }

    public Integer getColumns() {
        return columns;
    }

    public Integer getMines() {
        return mines;
    }
}
