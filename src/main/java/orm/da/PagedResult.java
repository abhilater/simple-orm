package orm.da;

import java.util.List;

/**
 * Holds a paged response of entity class {@link T} objects for the query result
 *
 * Created by abhishek on 21/06/19.
 */
public class PagedResult<T> {
    List<T> records;
    String nextKey;

    public PagedResult(List<T> records, String nextKey) {
        this.records = records;
        this.nextKey = nextKey;
    }

    @Override
    public String toString() {
        return "PagedResult{" +
                "records=" + records +
                ", nextKey='" + nextKey + '\'' +
                '}';
    }
}
