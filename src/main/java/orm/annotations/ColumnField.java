package orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ColumnField {

    /**
     * field data types enum
     */
    enum FieldType {
        STRING, BOOLEAN, LONG, INTEGER, OBJECT
    }

    /**
     * name of the table column to which this field corresponds
     */
    String name();

    /**
     * alias to be used for column in query
     */
    String alias() default "";

    /**
     * type to be used to cast table column data to entity field
     */
    FieldType type();

    /**
     * true if this column is a part of the primary key
     */
    boolean pk() default false;

    /**
     * position of the column in the composite primary key (index starts from 0)
     */
    int pkPosition() default 0;

    /**
     * true if this column is to used in group by for aggregated queries
     * not applicable for non-aggregate queries
     */
    boolean groupBy() default false;

    /**
     * table function/User Defined function to be applied on the column for an aggregated query
     * eg SUM,COUNT etc default will be None, to be used for aggregated/group-by queries on table
     * not applicable for non-aggregate queries
     */
    String function() default "";
}