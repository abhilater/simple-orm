package orm.cache;

import org.apache.commons.lang3.reflect.FieldUtils;
import orm.annotations.ColumnField;
import orm.model.EntityField;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caches all field objects for each Entity class in the JVM to save reflection cost
 */
public final class EntityFieldsCache {

    private static ConcurrentHashMap<Class, List<EntityField>> fieldsCache = new ConcurrentHashMap<>();

    public static List<EntityField> lookup(Class clazz) {
        if (!fieldsCache.containsKey(clazz)) {
            List<EntityField> entityFields = new ArrayList<>();
            for (Field field : FieldUtils.getFieldsListWithAnnotation(clazz, ColumnField.class)) {
                entityFields.add(new EntityField(field, field.getAnnotation(ColumnField.class)));
            }
            fieldsCache.putIfAbsent(clazz, entityFields);
        }
        return fieldsCache.get(clazz);
    }
}
