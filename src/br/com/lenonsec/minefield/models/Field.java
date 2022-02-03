package br.com.lenonsec.minefield.models;

import br.com.lenonsec.minefield.contracts.FieldObserver;
import br.com.lenonsec.minefield.enums.FieldEvent;
import lombok.Getter;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class Field {
    private Boolean mined = false;
    private Boolean opened = false;
    private Boolean marked = false;

    private List<Field> neighbors = new ArrayList<Field>();
    private Set<FieldObserver> observers = new HashSet<>();

    private final Integer x;
    private final Integer y;

    Field(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void registerObserver(FieldObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(FieldEvent event) {
        observers.forEach(o -> o.fire(this, event));
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

        if (opened) notifyObservers(FieldEvent.OPEN);
    }

    public void toggleMarker() {
        if (!opened) {
            marked = !marked;

            if (marked) {
                notifyObservers(FieldEvent.MARK);
            } else {
                notifyObservers(FieldEvent.UNMARK);
            }
        }
    }

    public Boolean open() {
        if (!opened && !marked) {
            if (mined) {
                notifyObservers(FieldEvent.EXPLODE);

                return true;
            }

            setOpened(true);

            if (this.safeNeighborhood()) {
                neighbors.forEach(Field::open);
            }

            return true;
        } else {
            return false;
        }
    }

    public Boolean safeNeighborhood() {
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

    public Integer minesInNeighborhood() {
        return Math.toIntExact(neighbors.stream().filter(Field::getMined).count());
    }

    void restart() {
        opened = false;
        mined = false;
        marked = false;
        this.notifyObservers(FieldEvent.RESTART);
    }

    public String toString() {
        if (marked) return "x";
        else if (opened && mined) return "*";
        else if (opened && minesInNeighborhood() > 0) return Long.toString(minesInNeighborhood());
        else if (opened) return " ";

        return "?";
    }
}
