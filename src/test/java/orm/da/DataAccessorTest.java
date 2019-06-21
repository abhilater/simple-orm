package orm.da;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import orm.db.util.DatabaseTestFixture;
import orm.entity.AgentMetrics;
import orm.entity.IssueMetrics;

/**
 * Created by abhishek on 21/06/19.
 */
public class DataAccessorTest {

    @BeforeClass
    public static void init(){
        DatabaseTestFixture.createFixture("populate_tables.sql");
    }

    @Test
    public void issueMetricsPaginateTest() throws Exception {
        DataAccessor<IssueMetrics> da = new DataAccessor<IssueMetrics>(IssueMetrics.class)
                .withFilter(new String[]{"domain", "eq"}, "hscustomer")
                .withFilter(new String[]{"created_at", "gte"}, 1001)
                .withFilter(new String[]{"created_at", "lt"}, 1003)
                .build();
        Assert.assertNotNull(da.getGeneratedQuery());
        Assert.assertEquals("select domain, " +
                        "issue_id, " +
                        "created_at, " +
                        "inbound_count, " +
                        "outbound_count, " +
                        "csat " +
                        "from issue_metrics_table " +
                        "where domain = ? AND " +
                        "created_at >= ? AND created_at < ? " +
                        "LIMIT ? OFFSET ?",
                da.getGeneratedQuery());

        PagedResult result = da.paginate();
        Assert.assertNull(result.nextKey);
        Assert.assertEquals(2, result.records.size());
        System.out.println(result);

        da = new DataAccessor<IssueMetrics>(IssueMetrics.class)
                .withFilter(new String[]{"domain", "eq"}, "hscustomer")
                .withFilter(new String[]{"issue_id", "eq"}, "3")
                .withFilter(new String[]{"created_at", "gte"}, 1001)
                .withFilter(new String[]{"created_at", "lt"}, 1003)
                .withLimit(10)
                .withOffset(0)
                .build();
        Assert.assertNotNull(da.getGeneratedQuery());
        Assert.assertEquals("select domain, " +
                        "issue_id, " +
                        "created_at, " +
                        "inbound_count, " +
                        "outbound_count, " +
                        "csat " +
                        "from issue_metrics_table " +
                        "where domain = ? AND " +
                        "issue_id = ? AND " +
                        "created_at >= ? AND created_at < ? " +
                        "LIMIT ? OFFSET ?",
                da.getGeneratedQuery());

        result = da.paginate();
        Assert.assertNull(result.nextKey);
        Assert.assertEquals(1, result.records.size());
        System.out.println(result);
        Assert.assertEquals("3", ((IssueMetrics)result.records.get(0)).issueId);
    }

    @Test
    public void agentMetricsPaginateTest() throws Exception {
        DataAccessor<AgentMetrics> da = new DataAccessor<AgentMetrics>(AgentMetrics.class)
                .withGroupBy("domain")
                .withGroupBy("time_bucket")
                .withGroupBy("agent_id")
                .withFilter(new String[]{"domain", "eq"}, "hscustomer")
                .withFilter(new String[]{"time_bucket", "gte"}, "201901010130")
                .withFilter(new String[]{"time_bucket", "lt"}, "201901010230")
                .build();
        Assert.assertNotNull(da.getGeneratedQuery());
        Assert.assertEquals("select SUM(ttfr_sum) AS ttfrSum, " +
                        "SUM(ttfr_count) AS ttfrCount, " +
                        "AVG(csat) AS csatAvg, " +
                        "domain, " +
                        "TO_CHAR(CONVERT_TZ(TO_DATE(time_bucket,'yyyyMMddHHmm'), 'UTC', 'Asia/Kolkata'),'yyyy-MM-dd\\'T\\'HH:00:00') AS time, " +
                        "agent_id " +
                        "from agent_metrics_table " +
                        "where domain = ? AND " +
                        "time_bucket >= ? AND time_bucket < ? " +
                        "group by domain, time, agent_id " +
                        "LIMIT ? OFFSET ?",
                da.getGeneratedQuery());

//        PagedResult result = da.paginate();
//        Assert.assertNull(result.nextKey);
//        Assert.assertEquals(2, result.records.size());
//        System.out.println(result);

        da = new DataAccessor<AgentMetrics>(AgentMetrics.class)
                .withGroupBy("domain")
                .withGroupBy("time_bucket")
                .withGroupBy("agent_id")
                .withFilter(new String[]{"domain", "eq"}, "hscustomer")
                .withFilter(new String[]{"agent_id", "eq"}, "a1")
                .withFilter(new String[]{"time_bucket", "gte"}, "201901010130")
                .withFilter(new String[]{"time_bucket", "lt"}, "201901010230")
                .withLimit(10)
                .withOffset(0)
                .build();
        Assert.assertNotNull(da.getGeneratedQuery());
        Assert.assertEquals("select SUM(ttfr_sum) AS ttfrSum, " +
                        "SUM(ttfr_count) AS ttfrCount, " +
                        "AVG(csat) AS csatAvg, " +
                        "domain, " +
                        "TO_CHAR(CONVERT_TZ(TO_DATE(time_bucket,'yyyyMMddHHmm'), 'UTC', 'Asia/Kolkata'),'yyyy-MM-dd\\'T\\'HH:00:00') AS time, " +
                        "agent_id " +
                        "from agent_metrics_table " +
                        "where domain = ? AND " +
                        "agent_id = ? AND " +
                        "time_bucket >= ? AND time_bucket < ? " +
                        "group by domain, time, agent_id " +
                        "LIMIT ? OFFSET ?",
                da.getGeneratedQuery());
//
//        result = da.paginate();
//        Assert.assertNull(result.nextKey);
//        Assert.assertEquals(1, result.records.size());
//        System.out.println(result);
//        Assert.assertEquals("3", ((IssueMetrics)result.records.get(0)).issueId);
    }

}
