package orm.util;

import org.apache.commons.lang3.StringUtils;
import orm.annotations.ColumnField;

public class QueryUtil {

    public static final String getFieldName(ColumnField field) {
        return StringUtils.isNotEmpty(field.alias()) ? field.alias() : field.name();
    }

    public static final String createNextKey(Object... args) {
        return StringUtils.join(args, ',');
    }
}
