package orm.util;

import ca.krasnay.sqlbuilder.SelectBuilder;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import orm.annotations.ColumnField;
import orm.annotations.Table;
import orm.cache.EntityFieldsCache;
import orm.da.DataAccessor;
import orm.model.EntityField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryGenerator {

    public static final Map FILTER_OPS =
            ImmutableMap.of("eq", " = ", "gt", " > ", "gte", " >= ", "lt", " < ", "lte", " <= ");
    public static final String QUERY_COMPONENT_LIMIT = " LIMIT ? ";
    public static final String QUERY_COMPONENT_OFFSET = "OFFSET ?";
    public static final String QUERY_COMPONENT_AS = " AS ";

    public static String generate(DataAccessor bda) {
        List<EntityField> fieldList = EntityFieldsCache.lookup(bda.getEntityClass());
        List<EntityField> defaultGroupByFields = getFilteredTypes(fieldList, true);
        List<EntityField> selectFields = getFilteredTypes(fieldList, false);

        // sort group by field based on primary key position and filter applicable group by fields
        defaultGroupByFields.sort(QueryGenerator::compareByPrimaryKeyPosition);
        List<EntityField> applicableGroupFields = defaultGroupByFields.stream()
                .filter(field -> bda.getGroupByFields().contains(field.getFieldAnnotation().name()))
                .collect(Collectors.toList());
        // group by fields must also be in SELECT clause always, so add to select list
        selectFields.addAll(applicableGroupFields);
        return buildSQL(bda, selectFields, applicableGroupFields);
    }

    private static String buildSQL(DataAccessor bda, List<EntityField> selectFields, List<EntityField> groupByFields) {
        SelectBuilder selectBuilder = new SelectBuilder();

        buildSelect(selectBuilder, selectFields);
        buildFrom(selectBuilder, ((Table) bda.getEntityClass().getAnnotation(Table.class)).name());
        buildWhere(selectBuilder, buildWhereClause(bda.getFilters()));
        buildGroupBy(selectBuilder, groupByFields);
        return buildLimit(selectBuilder.toString());
    }

    private static List<EntityField> getFilteredTypes(List<EntityField> fieldList, Boolean filterValue) {
        return fieldList.stream()
                .filter(field -> filterValue.equals(field.getFieldAnnotation().groupBy()))
                .collect(Collectors.toList());
    }

    private static void buildSelect(SelectBuilder selectBuilder, List<EntityField> selectFields) {
        if (null == selectFields || selectFields.size() <= 0)
            throw new RuntimeException("Invalid input for select clause builder");
        for (EntityField field : selectFields) {
            ColumnField phoenixField = field.getFieldAnnotation();
            //if (phoenixField.name().equals("domain")) continue;
            String selectField = applyColumnFunctionFromAnnotation(phoenixField);
            if (StringUtils.isNotEmpty(phoenixField.alias()))
                selectField = selectField + QUERY_COMPONENT_AS + phoenixField.alias();
            selectBuilder.column(selectField);
        }
    }

    private static void buildFrom(SelectBuilder selectBuilder, String tableName) {
        if (StringUtils.isEmpty(tableName))
            throw new RuntimeException("Table name for the ORM entity must not be empty");
        selectBuilder.from(tableName);
    }

    private static void buildWhere(SelectBuilder selectBuilder, String whereClause) {
        if (StringUtils.isEmpty(whereClause)) return;
        selectBuilder.where(whereClause);
    }

    private static void buildGroupBy(SelectBuilder select, List<EntityField> grpbyFields) {
        for (EntityField field : grpbyFields) {
            select.groupBy(QueryUtil.getFieldName(field.getFieldAnnotation()));
        }
    }

    private static String buildLimit(String query) {
        return query + QUERY_COMPONENT_LIMIT + QUERY_COMPONENT_OFFSET;
    }

    private static String applyColumnFunctionFromAnnotation(ColumnField phoenixField) {
        if (StringUtils.isNotBlank(phoenixField.function())) {
            return phoenixField.function();
        } else {
            return phoenixField.name();
        }
    }

    private static String buildWhereClause(Map<String[], Object> filterMap) {
        List<String> filterTokens = new ArrayList<>(filterMap.size());
        for (String[] keyOp : filterMap.keySet()) {
            filterTokens.add(keyOp[0] + FILTER_OPS.get(keyOp[1]) + "?");
        }
        return String.join(" AND ", filterTokens);
    }

    private static int compareByPrimaryKeyPosition(EntityField o1, EntityField o2) {
        ColumnField o1Annotation = o1.getFieldAnnotation();
        ColumnField o2Annotation = o2.getFieldAnnotation();
        /* If both non-primary key -> sort by name
         * if both primary key -> sort by position
         * else primary key field before non-primary
         */
        if (!o1Annotation.pk() && !o2Annotation.pk())
            return o1Annotation.name().compareTo(o2Annotation.name());
        else if (o1Annotation.pk() && o2Annotation.pk())
            return Integer.valueOf(o1Annotation.pkPosition()).compareTo(o2Annotation.pkPosition());
        else if (o1Annotation.pk()) return -1;
        else return 1;
    }
}
