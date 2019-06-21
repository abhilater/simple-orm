package orm.model;

import orm.annotations.ColumnField;

import java.lang.reflect.Field;

public class EntityField {

    private Field field;
    private ColumnField fieldAnnotation;

    public EntityField(Field field, ColumnField fieldAnnotation) {
        this.field = field;
        this.fieldAnnotation = fieldAnnotation;
    }

    public Field getField() {
        return field;
    }

    public ColumnField getFieldAnnotation() {
        return fieldAnnotation;
    }
}