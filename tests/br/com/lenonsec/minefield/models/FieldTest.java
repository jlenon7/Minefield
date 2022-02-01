package br.com.lenonsec.minefield.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    private Field field;

    @BeforeEach
    void initField() {
        field = new Field(3, 3);
    }

    @Test
    void testNeighborDistanceLeft() {
        Field neighbor = new Field(3, 2);

        Boolean result = this.field.addNeighbor(neighbor);

        assertTrue(result);
    }

    @Test
    void testNeighborDistanceRight() {
        Field neighbor = new Field(3, 4);

        Boolean result = this.field.addNeighbor(neighbor);

        assertTrue(result);
    }

    @Test
    void testNeighborDistanceTop() {
        Field neighbor = new Field(2, 3);

        Boolean result = this.field.addNeighbor(neighbor);

        assertTrue(result);
    }

    @Test
    void testNeighborDistanceBottom() {
        Field neighbor = new Field(4, 3);

        Boolean result = this.field.addNeighbor(neighbor);

        assertTrue(result);
    }

    @Test
    void testNeighborDistanceDiagonal() {
        Field neighbor = new Field(2, 2);

        Boolean result = this.field.addNeighbor(neighbor);

        assertTrue(result);
    }

    @Test
    void testNeighborFalseDistance() {
        Field neighbor = new Field(1, 1);

        Boolean result = this.field.addNeighbor(neighbor);

        assertFalse(result);
    }

    @Test
    void testToggleMarker() {
        assertFalse(field.getMarked());

        field.toggleMarker();

        assertTrue(field.getMarked());

        field.toggleMarker();

        assertFalse(field.getMarked());
    }

    @Test
    void testOpenNotMinedAndNotMarkedField() {
        assertTrue(field.open());
    }

    @Test
    void testOpenNotMinedAndMarkedField() {
        field.toggleMarker();

        assertFalse(field.open());
    }

    @Test
    void testOpenMinedAndMarkedField() {
        field.addMine();
        field.toggleMarker();

        assertFalse(field.open());
    }

    @Test
    void testOpenWithNeighbor() {
        Field field22 = new Field(2, 2);
        Field field23 = new Field(2, 3);

        field22.addNeighbor(field23);

        field.addNeighbor(field22);

        assertTrue(field.open());

        assertTrue(field.getOpened());
        assertTrue(field22.getOpened());
        assertTrue(field23.getOpened());
    }

    @Test
    void testOpenWithMinedNeighbor() {
        Field field22 = new Field(2, 2);
        Field field23 = new Field(2, 3);

        field23.addMine();

        field22.addNeighbor(field23);

        field.addNeighbor(field22);

        assertTrue(field.open());

        assertTrue(field.getOpened());
        assertTrue(field22.getOpened());
        assertFalse(field23.getOpened());
    }
}
