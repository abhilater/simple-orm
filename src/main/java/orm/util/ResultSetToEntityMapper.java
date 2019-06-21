package orm.util;

import org.apache.commons.lang3.reflect.ConstructorUtils;
import orm.annotations.ColumnField;
import orm.model.EntityField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Maps the {@link ResultSet} object from SQL query result to ORM Entity class.
 * <p>
 * For reflection cost optimization it uses the entity class fields list from
 * {@link orm.cache.EntityFieldsCache} cache object.
 * <p>
 * It maps SQL data types to the Java data types as defined in the {@link ColumnField} annotations.
 */
public final class ResultSetToEntityMapper {

    public static Object map(ResultSet rs, Class clazz, List<EntityField> fieldList) throws Exception {
        Object entity = ConstructorUtils.invokeConstructor(clazz, null);

        for (EntityField field : fieldList) {
            field.getField().set(entity, getObjectValueFromResultSet(field.getFieldAnnotation(), rs));
        }
        return entity;
    }

    private static Object getObjectValueFromResultSet(ColumnField field, ResultSet rs) throws Exception {
        ColumnField.FieldType fieldType = field.type();
        try {
            switch (fieldType) {
                case BOOLEAN:
                    return rs.getBoolean(QueryUtil.getFieldName(field));
                case INTEGER:
                    return rs.getInt(QueryUtil.getFieldName(field));
                case LONG:
                    return rs.getLong(QueryUtil.getFieldName(field));
                case STRING:
                    return rs.getString(QueryUtil.getFieldName(field));
                case OBJECT:
                    return rs.getObject(QueryUtil.getFieldName(field)); // default value annotation field not required, can be replaced with fieldType in Entity class
                default:
                    throw new RuntimeException("Field type not supported: " + fieldType);
            }
        } catch (SQLException se) {
            return null;
        }
    }
}
