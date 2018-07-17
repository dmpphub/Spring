/** ********************************************************************************
 * @author  Gobinath A Created on Sep 24, 2016
 * *********************************************************************************
 * REVISIONS
 * -----------
 * DATE			VERSION		NAME		COMMENTS
 * Sep 24, 2016	101.1		GOBI		Initial Code
 *********************************************************************************/
package com.vag.pfm.db.jdbc;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;


/**
 * The Class JDBCTemplate.
 *
 * @author GOBINATH A
 */
public class JDBCUtils {
	
	/** The jdbc utils. */
	private static JDBCUtils jdbcUtils = null;

    /** The jdbc temp. */
    private JdbcTemplate jdbcTemp = null;

    /**
     * Instantiates a new JDBC template.
     */
    private JDBCUtils() {
    	jdbcTemp = new JdbcTemplate(DatabaseUtils.getDataSource());
    }

    /**
     * Instantiates a new JDBC template.
     *
     * @param ds the ds
     */
    private JDBCUtils(DataSource ds) {
    	if (ds == null) {
    		new JDBCUtils();
    	}
    	jdbcTemp = new JdbcTemplate(ds);
    }
    
    /**
     * Gets the single instance of JDBCUtils.
     *
     * @return single instance of JDBCUtils
     */
    public static JDBCUtils getInstance() {
		if (jdbcUtils == null) {
			jdbcUtils = new JDBCUtils();
		}
		return jdbcUtils;
	}
    
    /**
     * Gets the single instance of JDBCUtils.
     *
     * @param ds the ds
     * @return single instance of JDBCUtils
     */
    public static JDBCUtils getInstance(DataSource ds) {
		if (jdbcUtils == null || ds != null) {
			jdbcUtils = new JDBCUtils(ds);
		}
		return jdbcUtils;
	}

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param rowMapper the row mapper
     * @return the list
     */
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
		return jdbcTemp.query(sql, rowMapper);
    }

    /**
     * Query.
     *
     * @param sql the sql
     * @param rch the rch
     */
    public void query(String sql, RowCallbackHandler rch) {
    	jdbcTemp.query(sql, rch);
    }

    /**
     * Query for map.
     *
     * @param sql the sql
     * @return the map
     */
    public Map<String, Object> queryForMap(String sql) {
		return jdbcTemp.queryForMap(sql);
    }

    /**
     * Query for object.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param rowMapper the row mapper
     * @return the t
     */
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
    	return jdbcTemp.queryForObject(sql, rowMapper);
    }

    /**
     * Query for object.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param requiredType the required type
     * @return the t
     */
    public <T> T queryForObject(String sql, Class<T> requiredType) {
    	return jdbcTemp.queryForObject(sql, requiredType);
    }

    /**
     * Query for list.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param elementType the element type
     * @return the list
     */
    public <T> List<T> queryForList(String sql, Class<T> elementType) {
    	return jdbcTemp.queryForList(sql, elementType);
    }

    /**
     * Query for list.
     *
     * @param sql the sql
     * @return the list
     */
    public List<Map<String, Object>> queryForList(String sql) {
    	return jdbcTemp.queryForList(sql);
    }

    /**
     * Query for row set.
     *
     * @param sql the sql
     * @return the sql row set
     */
    public SqlRowSet queryForRowSet(String sql) {
    	return jdbcTemp.queryForRowSet(sql);
    }

    /**
     * Update.
     *
     * @param sql the sql
     * @return the int
     */
    public int update(final String sql) {
    	return jdbcTemp.update(sql);
    }

    /**
     * Batch update.
     *
     * @param sql the sql
     * @return the int[]
     */
    public int[] batchUpdate(final String... sql) {
    	return jdbcTemp.batchUpdate(sql);
    }

    /**
     * Execute.
     *
     * @param <T> the generic type
     * @param psc the psc
     * @param action the action
     * @return the t
     */
    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) {
		return jdbcTemp.execute(psc, action);
    }

    /**
     * Execute.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param action the action
     * @return the t
     */
    public <T> T execute(String sql, PreparedStatementCallback<T> action) {
    	return jdbcTemp.execute(sql, action);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param psc the psc
     * @param pss the pss
     * @param rse the rse
     * @return the t
     */
    public <T> T query(PreparedStatementCreator psc, final PreparedStatementSetter pss, final ResultSetExtractor<T> rse) {
    	return jdbcTemp.query(psc, rse);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param psc the psc
     * @param rse the rse
     * @return the t
     */
    public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) {
    	return jdbcTemp.query(psc, rse);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param pss the pss
     * @param rse the rse
     * @return the t
     */
    public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) {
    	return jdbcTemp.query(sql, pss, rse);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @param rse the rse
     * @return the t
     */
    public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) {
    	return jdbcTemp.query(sql, args, argTypes, rse);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param rse the rse
     * @return the t
     */
    public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) {
    	return jdbcTemp.query(sql, args, rse);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param rse the rse
     * @param args the args
     * @return the t
     */
    public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) {
    	return jdbcTemp.query(sql, rse, args);
    }

    /**
     * Query.
     *
     * @param psc the psc
     * @param rch the rch
     */
    public void query(PreparedStatementCreator psc, RowCallbackHandler rch) {
    	jdbcTemp.query(psc, rch);
    }

    /**
     * Query.
     *
     * @param sql the sql
     * @param pss the pss
     * @param rch the rch
     */
    public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) {
    	jdbcTemp.query(sql, pss, rch);
    }

    /**
     * Query.
     *
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @param rch the rch
     */
    public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) {
    	jdbcTemp.query(sql, args, argTypes, rch);
    }

    /**
     * Query.
     *
     * @param sql the sql
     * @param args the args
     * @param rch the rch
     */
    public void query(String sql, Object[] args, RowCallbackHandler rch) {
    	jdbcTemp.query(sql, args, rch);
    }

    /**
     * Query.
     *
     * @param sql the sql
     * @param rch the rch
     * @param args the args
     */
    public void query(String sql, RowCallbackHandler rch, Object... args) {
    	jdbcTemp.query(sql, rch, args);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param psc the psc
     * @param rowMapper the row mapper
     * @return the list
     */
    public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) {
    	return jdbcTemp.query(psc, rowMapper);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param pss the pss
     * @param rowMapper the row mapper
     * @return the list
     */
    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) {
    	return jdbcTemp.query(sql, pss, rowMapper);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @param rowMapper the row mapper
     * @return the list
     */
    public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
    	return jdbcTemp.query(sql, args, argTypes, rowMapper);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param rowMapper the row mapper
     * @return the list
     */
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
    	return jdbcTemp.query(sql, args, rowMapper);
    }

    /**
     * Query.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param rowMapper the row mapper
     * @param args the args
     * @return the list
     */
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
    	return jdbcTemp.query(sql, rowMapper, args);
    }

    /**
     * Query for object.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @param rowMapper the row mapper
     * @return the t
     */
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
    	return jdbcTemp.queryForObject(sql, args, argTypes, rowMapper);
    }

    /**
     * Query for object.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param rowMapper the row mapper
     * @return the t
     */
    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
    	return jdbcTemp.queryForObject(sql, args, rowMapper);
    }

    /**
     * Query for object.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param rowMapper the row mapper
     * @param args the args
     * @return the t
     */
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
    	return jdbcTemp.queryForObject(sql, rowMapper, args);
    }

    /**
     * Query for object.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @param requiredType the required type
     * @return the t
     */
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType) {
    	return jdbcTemp.queryForObject(sql, args, argTypes, requiredType);
    }

    /**
     * Query for object.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param requiredType the required type
     * @return the t
     */
    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) {
    	return jdbcTemp.queryForObject(sql, args, requiredType);
    }

    /**
     * Query for object.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param requiredType the required type
     * @param args the args
     * @return the t
     */
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) {
    	return jdbcTemp.queryForObject(sql, requiredType, args);
    }

    /**
     * Query for map.
     *
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @return the map
     */
    public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) {
    	return jdbcTemp.queryForMap(sql, args, argTypes);
    }

    /**
     * Query for map.
     *
     * @param sql the sql
     * @param args the args
     * @return the map
     */
    public Map<String, Object> queryForMap(String sql, Object... args) {
    	return jdbcTemp.queryForMap(sql, args);
    }

    /**
     * Query for list.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @param elementType the element type
     * @return the list
     */
    public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType) {
    	return jdbcTemp.queryForList(sql, args, argTypes, elementType);
    }

    /**
     * Query for list.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param args the args
     * @param elementType the element type
     * @return the list
     */
    public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) {
    	return jdbcTemp.queryForList(sql, args, elementType);
    }

    /**
     * Query for list.
     *
     * @param <T> the generic type
     * @param sql the sql
     * @param elementType the element type
     * @param args the args
     * @return the list
     */
    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) {
    	return jdbcTemp.queryForList(sql, elementType, args);
    }

    /**
     * Query for list.
     *
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @return the list
     */
    public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) {
    	return jdbcTemp.queryForList(sql, args, argTypes);
    }

    /**
     * Query for list.
     *
     * @param sql the sql
     * @param args the args
     * @return the list
     */
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
    	return jdbcTemp.queryForList(sql, args);
    }

    /**
     * Query for row set.
     *
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @return the sql row set
     */
    public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) {
    	return jdbcTemp.queryForRowSet(sql, args, argTypes);
    }

    /**
     * Query for row set.
     *
     * @param sql the sql
     * @param args the args
     * @return the sql row set
     */
    public SqlRowSet queryForRowSet(String sql, Object... args) {
    	return jdbcTemp.queryForRowSet(sql, args);
    }

    /**
     * Update.
     *
     * @param sql the sql
     * @param args the args
     * @param argTypes the arg types
     * @return the int
     */
    public int update(String sql, Object[] args, int[] argTypes) {
    	return jdbcTemp.update(sql, args, argTypes);
    }

    /**
     * Batch update.
     *
     * @param sql the sql
     * @param pss the pss
     * @return the int[]
     */
    public int[] batchUpdate(String sql, final BatchPreparedStatementSetter pss) {
    	return jdbcTemp.batchUpdate(sql, pss);
    }

    /**
     * Batch update.
     *
     * @param sql the sql
     * @param batchArgs the batch args
     * @return the int[]
     */
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
    	return jdbcTemp.batchUpdate(sql, batchArgs);
    }

    /**
     * Batch update.
     *
     * @param sql the sql
     * @param batchArgs the batch args
     * @param argTypes the arg types
     * @return the int[]
     */
    public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) {
    	return jdbcTemp.batchUpdate(sql, batchArgs, argTypes);
    }

    /**
     * Execute.
     *
     * @param <T> the generic type
     * @param callString the call string
     * @param action the action
     * @return the t
     */
    public <T> T execute(String callString, CallableStatementCallback<T> action) {
    	return jdbcTemp.execute(callString, action);
    }
    /**
     * Update.
     *
     * @param sql the sql
     * @param args the args
     * @return the int
     */
    public int update(String sql, Object[] args) {
        return jdbcTemp.update(sql, args);
    }

    /**
     * Execute.
     *
     * @param sql the sql
     */
    public void execute(String sql) {
        jdbcTemp.execute(sql);
    }
}
