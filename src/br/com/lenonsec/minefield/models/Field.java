package br.com.lenonsec.minefield.models;

import lombok.Getter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.ArrayList;

import br.com.lenonsec.minefield.exceptions.ExplosionException;

@Getter
@EqualsAndHashCode
public class Field {
    private Boolean mined = false;
    private Boolean opened = false;
    private Boolean marked = false;
    private List<Field> neighbors = new ArrayList<Field>();

    private final Integer x;
    private final Integer y;

    Field(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    Boolean addNeighbor(Field neighbor) {
        Boolean differentX = x != neighbor.getX();
        Boolean differentY = y != neighbor.getY();
        Boolean diagonal = differentX && differentY;

        Integer deltaX = Math.abs(x - neighbor.getX());
        Integer deltaY = Math.abs(y - neighbor.getY());
        Integer deltaGeneral = deltaX + deltaY;

        if (deltaGeneral == 1 && !diagonal) {
            neighbors.add(neighbor);

            return true;
        } else if (deltaGeneral == 2 && diagonal) {
            neighbors.add(neighbor);

            return true;
        }

        return false;
    }

    void setOpened(Boolean opened) {
        this.opened = opened;
    }

    void toggleMarker() {
        marked = !marked;
    }

    Boolean open() {
        if (!opened && !marked) {
            opened = true;

            if (mined) {
                throw new ExplosionException();
            }

            if (this.safeNeighborhood()) {
                neighbors.forEach(Field::open);
            }

            return true;
        }

        return false;
    }

    Boolean safeNeighborhood() {
        return neighbors.stream().noneMatch(Field::getMined);
    }

    void addMine() {
        mined = true;
    }

    Boolean goalAchieved() {
        Boolean unraveledField = !mined && opened;
        Boolean protectedField = mined && marked;

        return unraveledField || protectedField;
    }

    Long minesInNeighborhood() {
        return neighbors.stream().filter(Field::getMined).count();
    }

    void restart() {
        mined = false;
        opened = false;
        marked = false;
    }

    public String toString() {
        if (marked) return "x";
        else if (opened && mined) return "*";
        else if (opened && minesInNeighborhood() > 0) return Long.toString(minesInNeighborhood());
        else if (opened) return " ";

        return "?";
    }
}
