package orm.da;

import orm.cache.EntityFieldsCache;
import orm.db.ConnectionFactory;
import orm.model.EntityField;
import orm.util.QueryGenerator;
import orm.util.QueryUtil;
import orm.util.ResultSetToEntityMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 * Data accessor class to access paginated entity objects {@link T}.
 * These entity objects are mapped from {@link ResultSet} objects obtained from querying the
 * table for the specified objects {@link T} for which {@link DataAccessor} class is defined
 * <p>
 * Created by abhishek on 21/06/19.
 */
public class DataAccessor<T> {

    private final Class<T> entityClass;
    private Set<String> groupByFields = new HashSet<>();
    private Map<String[], Object> filters = new LinkedHashMap<>();
    private int limit = 1000;
    private int offset = 0;
    private String nextKey = null;

    private String generatedQuery;

    public DataAccessor(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public DataAccessor build() {
        this.generatedQuery = QueryGenerator.generate(this);
        return this;
    }

    public DataAccessor withGroupBy(String groupByField) {
        this.groupByFields.add(groupByField);
        return this;
    }

    public DataAccessor withFilter(String[] keyOp, Object val) {
        this.filters.put(keyOp, val);
        return this;
    }

    public DataAccessor withLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public DataAccessor withOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public Set<String> getGroupByFields() {
        return groupByFields;
    }

    public Map<String[], Object> getFilters() {
        return filters;
    }

    public String getGeneratedQuery() {
        return generatedQuery;
    }

    public PagedResult<T> paginate() throws Exception {
        List<T> results = new ArrayList<>(this.limit);
        List<EntityField> fieldList = EntityFieldsCache.lookup(getEntityClass());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(this.generatedQuery)) {
            int psIdx = setStatementParameters(ps);
            ps.setInt(psIdx++, this.limit + 1);
            ps.setInt(psIdx, this.offset);
            try (ResultSet rs = ps.executeQuery()) {
                for (int index = 0; index < this.limit; index++) {
                    if (rs.next()) {
                        results.add((T) ResultSetToEntityMapper.map(rs, getEntityClass(), fieldList));
                    } else break;
                }
                if (rs.next()) {
                    this.nextKey = QueryUtil.createNextKey("requestId", (this.offset + this.limit));
                } else {
                    this.nextKey = null;
                }
            }
        }
        return new PagedResult<T>(results, nextKey);
    }

    private int setStatementParameters(PreparedStatement ps) throws Exception {
        int index = 1;
        for (Object param : getFilters().values()) {
            ps.setObject(index, param);
            index++;
        }
        return index;
    }
}
