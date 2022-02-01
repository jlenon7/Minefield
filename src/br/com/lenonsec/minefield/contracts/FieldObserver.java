package br.com.lenonsec.minefield.contracts;

import br.com.lenonsec.minefield.enums.FieldEvent;
import br.com.lenonsec.minefield.models.Field;

public interface FieldObserver {
    public void fire(Field field, FieldEvent event);
}
