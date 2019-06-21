package orm.entity;

import orm.annotations.ColumnField;
import orm.annotations.Table;

/**
 * ORM Entity class for 'agent_metrics_table' table.
 *
 * @ColumnField annotation injects the table schema information on each entity fields like
 * i) the column name,
 * ii) column alias to be used for aggregate function queries,
 * iii) data type to which the table column data is to casted during data mapping to entity class
 * iv) if this field corresponds to PK column and its position in the PK composite column and
 * v)  if this column is to be used as group-by field for an aggregated query
 * vi) the aggregate function/UDF to applied on the column during query
 */
@Table(name = "agent_metrics_table")
public class AgentMetrics {

    @ColumnField(type = ColumnField.FieldType.STRING,
            name = "domain",
            pk = true,
            pkPosition = 0,
            groupBy = true)
    public String domain;

    @ColumnField(type = ColumnField.FieldType.STRING,
            name = "time_bucket",
            alias = "time",
            pk = true,
            pkPosition = 1,
            groupBy = true,
            function = "TO_CHAR(CONVERT_TZ(TO_DATE(time_bucket,'yyyyMMddHHmm'), 'UTC', 'Asia/Kolkata'),'yyyy-MM-dd\\'T\\'HH:00:00')")
    public String time;

    @ColumnField(type = ColumnField.FieldType.STRING,
            name = "agent_id",
            pk = true,
            pkPosition = 2,
            groupBy = true)
    public String agentId;

    @ColumnField(type = ColumnField.FieldType.STRING,
            name = "ttfr_sum",
            alias = "ttfrSum",
            function = "SUM(ttfr_sum)")
    public Long ttfrSum;

    @ColumnField(type = ColumnField.FieldType.STRING,
            name = "ttfr_count",
            alias = "ttfrCount",
            function = "SUM(ttfr_count)")
    public Long ttfrCount;

    @ColumnField(type = ColumnField.FieldType.STRING,
            name = "csat",
            alias = "csatAvg",
            function = "AVG(csat)")
    public Long csatAvg;

}
