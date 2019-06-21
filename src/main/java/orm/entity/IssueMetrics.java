package orm.entity;

import orm.annotations.ColumnField;
import orm.annotations.Table;

/**
 * ORM Entity class for 'issue_metrics_table' table.
 */
@Table(name = "issue_metrics_table")
public class IssueMetrics {
    @ColumnField(type = ColumnField.FieldType.STRING,
            name = "domain",
            pk = true,
            pkPosition = 0)
    public String domain;

    @ColumnField(type = ColumnField.FieldType.STRING,
            name = "issue_id",
            pk = true,
            pkPosition = 1)
    public String issueId;

    @ColumnField(type = ColumnField.FieldType.LONG,
            name = "created_at",
            pk = true,
            pkPosition = 2)
    public Long createdAt;

    @ColumnField(type = ColumnField.FieldType.LONG,
            name = "inbound_count")
    public Long inboundMessages;

    @ColumnField(type = ColumnField.FieldType.LONG,
            name = "outbound_count")
    public Long outboundMessages;

    @ColumnField(type = ColumnField.FieldType.INTEGER,
            name = "csat")
    public Integer csat;

    @Override
    public String toString() {
        return "IssueMetrics{" +
                "domain='" + domain + '\'' +
                ", issueId='" + issueId + '\'' +
                ", createdAt=" + createdAt +
                ", inboundMessages=" + inboundMessages +
                ", outboundMessages=" + outboundMessages +
                ", csat=" + csat +
                '}';
    }
}
