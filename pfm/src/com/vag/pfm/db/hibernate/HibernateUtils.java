/**
 * REVISION         DATE        NAME     DESCRIPTION
 * 511.101       Aug 7, 2016	GOBI      Initial Code
 */

package com.vag.pfm.db.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import net.sf.ehcache.hibernate.management.api.HibernateStats;
import net.sf.ehcache.hibernate.management.impl.HibernateStatsImpl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.query.NativeQuery;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.hibernate5.SessionFactoryUtils;

import com.vag.pfm.db.jdbc.JDBConnection;


/**
 * The Class HibernateUtils.
 *
 * @author GOBINATH A
 */
@SuppressWarnings("deprecation")
public class HibernateUtils {

	/** The hibernate utils. */
	private static HibernateUtils hibernateUtils = null;

	/** The cache queries. */
	private boolean cacheQueries = false;

	/** The query cache region. */
	private String queryCacheRegion;

	/** The fetch size. */
	private int fetchSize = 0;

	/** The first result. */
	private int firstResult = 0;

	/** The max results. */
	private int maxResults = 0;

	/**
	 * Instantiates a new hibernate utils.
	 */
	private HibernateUtils() {
	}

	/**
	 * Gets the single instance of HibernateUtils.
	 *
	 * @return single instance of HibernateUtils
	 */
	public static HibernateUtils getInstance() {
		if (hibernateUtils == null) {
			hibernateUtils = new HibernateUtils();
		}
		return hibernateUtils;
	}

	/**
	 * Gets the session.
	 *
	 * @return the session
	 */
	public Session getSession() {
		Session hibSession = null;
		HibernateSession dbSession = null;
		HibernateStats hibernateStats = null;
		SessionFactory sessionFactory = null;
		try {
			sessionFactory = HSessionFactory.getSessionFactory();
			hibernateStats = new HibernateStatsImpl(sessionFactory);
			hibernateStats.enableStats();
			hibSession = sessionFactory.openSession();
			dbSession = new HibernateSession(hibSession, hibernateStats);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbSession;
	}

	/**
	 * Gets the new open session.
	 *
	 * @return the new open session
	 */
	public Session getNewOpenSession() {
		return HSessionFactory.getSessionFactory().openSession();
	}
	
	/**
	 * Gets the data source.
	 *
	 * @return the data source
	 */
	public DataSource getDataSource() {
		return SessionFactoryUtils.getDataSource(HSessionFactory.getSessionFactory());
	}
	
	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection() {
		return new JDBConnection(DataSourceUtils.getConnection(getDataSource()));
	}

	/**
	 * Do hibernate.
	 *
	 * @param <T> the generic type
	 * @param action the action
	 * @return the t
	 * @throws HibernateException the hibernate exception
	 * @throws SQLException the SQL exception
	 */
	@SuppressWarnings("unchecked")
	private <T> T doHibernate(HibernateCallback action)
			throws HibernateException, SQLException {
		Session session = null;
		T result = null;
		try {
			session = getSession();
			result = (T) action.execute(session);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	/**
	 * Do trans hibernate.
	 *
	 * @param <T> the generic type
	 * @param action the action
	 * @return the t
	 * @throws HibernateException the hibernate exception
	 * @throws SQLException the SQL exception
	 */
	@SuppressWarnings("unchecked")
	private <T> T doHibernate(HibernateTransCallback action)
			throws HibernateException, SQLException {
		Session session = null;
		Transaction trans = null;
		T result = null;
		try {
			session = getSession();
			trans = session.beginTransaction();
			result = (T) action.execute(session);
			session.flush();
			trans.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (trans != null && trans.isActive()) {
				trans.rollback();
			}

			if (session != null) {
				session.close();
			}
		}
		return result;
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @param id the id
	 * @return the t
	 * @throws Exception the exception
	 */
	public <T> T get(Class<T> entityClass, Serializable id) throws Exception {
		return get(entityClass, id, null);
	}

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @param id the id
	 * @param lockMode the lock mode
	 * @return the t
	 * @throws Exception the exception
	 */
	public <T> T get(final Class<T> entityClass, final Serializable id,
			final LockMode lockMode) throws Exception {
		return doHibernate(new HibernateCallback() {
			@Override
			public T execute(Session session) throws HibernateException {
				if (lockMode != null) {
					return (T) session.get(entityClass, id, new LockOptions(
							lockMode));
				} else {
					return (T) session.get(entityClass, id);
				}
			}

		});
	}

	/**
	 * Gets the.
	 *
	 * @param entityName the entity name
	 * @param id the id
	 * @return the object
	 * @throws Exception the exception
	 */
	public Object get(String entityName, Serializable id) throws Exception {
		return get(entityName, id, null);
	}

	/**
	 * Gets the.
	 *
	 * @param entityName the entity name
	 * @param id the id
	 * @param lockMode the lock mode
	 * @return the object
	 * @throws Exception the exception
	 */
	public Object get(final String entityName, final Serializable id,
			final LockMode lockMode) throws Exception {
		return doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.get(entityName, id,
							new LockOptions(lockMode));
				} else {
					return session.get(entityName, id);
				}
			}
		});
	}

	/**
	 * Load.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @param id the id
	 * @return the t
	 * @throws Exception the exception
	 */
	public <T> T load(Class<T> entityClass, Serializable id) throws Exception {
		return load(entityClass, id, null);
	}

	/**
	 * Load.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @param id the id
	 * @param lockMode the lock mode
	 * @return the t
	 * @throws Exception the exception
	 */
	public <T> T load(final Class<T> entityClass, final Serializable id,
			final LockMode lockMode) throws Exception {
		return doHibernate(new HibernateCallback() {
			@Override
			public T execute(Session session) throws HibernateException {
				if (lockMode != null) {
					return (T) session.load(entityClass, id, new LockOptions(
							lockMode));
				} else {
					return (T) session.load(entityClass, id);
				}
			}
		});
	}

	/**
	 * Load.
	 *
	 * @param entityName the entity name
	 * @param id the id
	 * @return the object
	 * @throws Exception the exception
	 */
	public Object load(String entityName, Serializable id) throws Exception {
		return load(entityName, id, null);
	}

	/**
	 * Load.
	 *
	 * @param entityName the entity name
	 * @param id the id
	 * @param lockMode the lock mode
	 * @return the object
	 * @throws Exception the exception
	 */
	public Object load(final String entityName, final Serializable id,
			final LockMode lockMode) throws Exception {
		return doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					return session.load(entityName, id, new LockOptions(
							lockMode));
				} else {
					return session.load(entityName, id);
				}
			}
		});
	}

	/**
	 * Load all.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @return the list
	 * @throws Exception the exception
	 */
	public <T> List<T> loadAll(final Class<T> entityClass) throws Exception {
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({"unchecked" })
			@Override
			public List<T> execute(Session session) throws HibernateException {
				Criteria criteria = session.createCriteria(entityClass);
				criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
				prepareCriteria(criteria);
				return criteria.list();
			}
		});
	}

	/**
	 * Load.
	 *
	 * @param entity the entity
	 * @param id the id
	 * @throws Exception the exception
	 */
	public void load(final Object entity, final Serializable id)
			throws Exception {
		doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.load(entity, id);
				return null;
			}
		});
	}

	/**
	 * Refresh.
	 *
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void refresh(final Object entity) throws Exception {
		refresh(entity, null);
	}

	/**
	 * Refresh.
	 *
	 * @param entity the entity
	 * @param lockMode the lock mode
	 * @throws Exception the exception
	 */
	public void refresh(final Object entity, final LockMode lockMode)
			throws Exception {
		doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				if (lockMode != null) {
					session.refresh(entity, new LockOptions(lockMode));
				} else {
					session.refresh(entity);
				}
				return null;
			}
		});
	}

	/**
	 * Contains.
	 *
	 * @param entity the entity
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean contains(final Object entity) throws Exception {
		return doHibernate(new HibernateCallback() {
			@Override
			public Boolean execute(Session session) {
				return session.contains(entity);
			}
		});
	}

	/**
	 * Evict.
	 *
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void evict(final Object entity) throws Exception {
		doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.evict(entity);
				return null;
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience methods for storing individual objects
	// -------------------------------------------------------------------------

	/**
	 * Lock.
	 *
	 * @param entity the entity
	 * @param lockMode the lock mode
	 * @throws Exception the exception
	 */
	public void lock(final Object entity, final LockMode lockMode)
			throws Exception {
		doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.buildLockRequest(new LockOptions(lockMode))
						.lock(entity);
				return null;
			}
		});
	}

	/**
	 * Lock.
	 *
	 * @param entityName the entity name
	 * @param entity the entity
	 * @param lockMode the lock mode
	 * @throws Exception the exception
	 */
	public void lock(final String entityName, final Object entity,
			final LockMode lockMode) throws Exception {
		doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.buildLockRequest(new LockOptions(lockMode)).lock(
						entityName, entity);
				return null;
			}
		});
	}

	/**
	 * Save.
	 *
	 * @param entity the entity
	 * @return the serializable
	 * @throws Exception the exception
	 */
	public Serializable save(final Object entity) throws Exception {
		return doHibernate(new HibernateTransCallback() {
			@Override
			public Serializable execute(Session session)
					throws HibernateException {
				return session.save(entity);
			}
		});
	}

	/**
	 * Save.
	 *
	 * @param entityName the entity name
	 * @param entity the entity
	 * @return the serializable
	 * @throws Exception the exception
	 */
	public Serializable save(final String entityName, final Object entity)
			throws Exception {
		return doHibernate(new HibernateTransCallback() {
			@Override
			public Serializable execute(Session session)
					throws HibernateException {
				return session.save(entityName, entity);
			}
		});
	}

	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void update(Object entity) throws Exception {
		update(entity, null);
	}

	/**
	 * Update.
	 *
	 * @param entity the entity
	 * @param lockMode the lock mode
	 * @throws Exception the exception
	 */
	public void update(final Object entity, final LockMode lockMode)
			throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.update(entity);
				if (lockMode != null) {
					session.buildLockRequest(new LockOptions(lockMode)).lock(
							entity);
				}
				return null;
			}
		});
	}

	/**
	 * Update.
	 *
	 * @param entityName the entity name
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void update(String entityName, Object entity) throws Exception {
		update(entityName, entity, null);
	}

	/**
	 * Update.
	 *
	 * @param entityName the entity name
	 * @param entity the entity
	 * @param lockMode the lock mode
	 * @throws Exception the exception
	 */
	public void update(final String entityName, final Object entity,
			final LockMode lockMode) throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.update(entityName, entity);
				if (lockMode != null) {
					session.buildLockRequest(new LockOptions(lockMode)).lock(
							entityName, entity);
				}
				return null;
			}
		});
	}

	/**
	 * Save or update.
	 *
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void saveOrUpdate(final Object entity) throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.saveOrUpdate(entity);
				return null;
			}
		});
	}

	/**
	 * Save or update.
	 *
	 * @param entityName the entity name
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void saveOrUpdate(final String entityName, final Object entity)
			throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.saveOrUpdate(entityName, entity);
				return null;
			}
		});
	}

	/**
	 * Replicate.
	 *
	 * @param entity the entity
	 * @param replicationMode the replication mode
	 * @throws Exception the exception
	 */
	public void replicate(final Object entity,
			final ReplicationMode replicationMode) throws Exception {
		doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.replicate(entity, replicationMode);
				return null;
			}
		});
	}

	/**
	 * Replicate.
	 *
	 * @param entityName the entity name
	 * @param entity the entity
	 * @param replicationMode the replication mode
	 * @throws Exception the exception
	 */
	public void replicate(final String entityName, final Object entity,
			final ReplicationMode replicationMode) throws Exception {
		doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.replicate(entityName, entity, replicationMode);
				return null;
			}
		});
	}

	/**
	 * Persist.
	 *
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void persist(final Object entity) throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.persist(entity);
				return null;
			}
		});
	}

	/**
	 * Persist.
	 *
	 * @param entityName the entity name
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void persist(final String entityName, final Object entity)
			throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.persist(entityName, entity);
				return null;
			}
		});
	}

	/**
	 * Merge.
	 *
	 * @param <T> the generic type
	 * @param entity the entity
	 * @return the t
	 * @throws Exception the exception
	 */
	public <T> T merge(final Object entity) throws Exception {
		return doHibernate(new HibernateTransCallback() {
			public Object execute(Session session) throws HibernateException {
				return session.merge(entity);
			}
		});
	}

	/**
	 * Merge.
	 *
	 * @param <T> the generic type
	 * @param entityName the entity name
	 * @param entity the entity
	 * @return the t
	 * @throws Exception the exception
	 */
	public <T> T merge(final String entityName, final T entity)
			throws Exception {
		return doHibernate(new HibernateTransCallback() {
			@Override
			@SuppressWarnings("unchecked")
			public T execute(Session session) throws HibernateException {
				return (T) session.merge(entityName, entity);
			}
		});
	}

	/**
	 * Delete.
	 *
	 * @param entity the entity
	 * @throws Exception the exception
	 */
	public void delete(Object entity) throws Exception {
		delete(entity, null);
	}

	/**
	 * Delete.
	 *
	 * @param entity
	 *            the entity
	 * @param lockMode
	 *            the lock mode
	 * @throws Exception
	 *             the exception
	 */
	public void delete(final Object entity, final LockMode lockMode)
			throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.delete(entity);
				if (lockMode != null) {
					session.buildLockRequest(new LockOptions(lockMode)).lock(
							entity);
				}
				return null;
			}
		});
	}

	/**
	 * Delete.
	 *
	 * @param entityName
	 *            the entity name
	 * @param entity
	 *            the entity
	 * @throws Exception
	 *             the exception
	 */
	public void delete(String entityName, Object entity) throws Exception {
		delete(entityName, entity, null);
	}

	/**
	 * Delete.
	 *
	 * @param entityName
	 *            the entity name
	 * @param entity
	 *            the entity
	 * @param lockMode
	 *            the lock mode
	 * @throws Exception
	 *             the exception
	 */
	public void delete(final String entityName, final Object entity,
			final LockMode lockMode) throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.delete(entityName, entity);
				if (lockMode != null) {
					session.buildLockRequest(new LockOptions(lockMode)).lock(
							entityName, entity);
				}
				return null;
			}
		});
	}

	/**
	 * Delete all.
	 *
	 * @param entities
	 *            the entities
	 * @throws Exception
	 *             the exception
	 */
	public void deleteAll(final Collection<?> entities) throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				for (Object entity : entities) {
					session.delete(entity);
				}
				return null;
			}
		});
	}

	/**
	 * Flush.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public void flush() throws Exception {
		doHibernate(new HibernateTransCallback() {
			@Override
			public Object execute(Session session) throws HibernateException {
				session.flush();
				return null;
			}
		});
	}

	/**
	 * Clear.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public void clear() throws Exception {
		doHibernate(new HibernateCallback() {
			@Override
			public Object execute(Session session) {
				session.clear();
				return null;
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for SQL strings
	// -------------------------------------------------------------------------

	/**
	 * Find.
	 *
	 * @param queryString
	 *            the query string
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> find(final String queryString) throws Exception {
		return find(queryString, false, (Object[]) null);
	}
	
	/**
	 * Find.
	 *
	 * @param queryString the query string
	 * @param isListOfMap the is list of map
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<?> find(final String queryString, final boolean isListOfMap) throws Exception {
		return find(queryString, isListOfMap, (Object[]) null);
	}

	/**
	 * Find.
	 *
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> find(final String queryString, final Object... values)
			throws Exception {
		return find(queryString, false, values);
	}

	/**
	 * Find.
	 *
	 * @param queryString
	 *            the query string
	 * @param isListOfMap
	 *            the is list of map
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> find(final String queryString, final boolean isListOfMap,
			final Object... values) throws Exception {
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings("rawtypes")
			@Override
			public List<?> execute(Session session) throws HibernateException {
				NativeQuery queryObject = session.createNativeQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				if (isListOfMap) {
					queryObject.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				}
				return queryObject.list();
			}
		});
	}

	/**
	 * Find sql.
	 *
	 * @param queryString
	 *            the query string
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findSQL(final String queryString) throws Exception {
		return findSQL(queryString, false, (Object[]) null);
	}

	/**
	 * Find sql.
	 *
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findSQL(final String queryString, final Object... values)
			throws Exception {
		return findSQL(queryString, false, values);
	}

	/**
	 * Find sql.
	 *
	 * @param queryString
	 *            the query string
	 * @param isListOfMap
	 *            the is list of map
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findSQL(final String queryString, final boolean isListOfMap,
			final Object... values) throws Exception {
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({  "rawtypes" })
			@Override
			public List<?> execute(Session session) throws HibernateException {
				NativeQuery queryObject = session.createNativeQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				if (isListOfMap) {
					queryObject
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				}
				return queryObject.list();
			}
		});
	}

	/**
	 * Find by named param.
	 *
	 * @param queryString
	 *            the query string
	 * @param paramName
	 *            the param name
	 * @param value
	 *            the value
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedParam(String queryString, String paramName,
			Object value) throws Exception {
		return findByNamedParam(queryString, new String[] { paramName },
				new Object[] { value });
	}

	/**
	 * Find by named param.
	 *
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedParam(final String queryString,
			final String[] paramNames, final Object[] values) throws Exception {
		return findByNamedParam(queryString, paramNames, false, values);
	}

	/**
	 * Find by named param.
	 *
	 * @param queryString
	 *            the query string
	 * @param paramNames
	 *            the param names
	 * @param isListOfMap
	 *            the is list of map
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedParam(final String queryString,
			final String[] paramNames, final boolean isListOfMap,
			final Object[] values) throws Exception {
		if (paramNames.length != values.length) {
			throw new IllegalArgumentException(
					"Length of paramNames array must match length of values array");
		}
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({  "rawtypes" })
			@Override
			public List<?> execute(Session session) throws HibernateException {
				NativeQuery queryObject = session.createNativeQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						applyNamedParameterToQuery(queryObject, paramNames[i],
								values[i]);
					}
				}
				if (isListOfMap) {
					queryObject
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				}
				return queryObject.list();
			}
		});
	}

	/**
	 * Find by value bean.
	 *
	 * @param queryString
	 *            the query string
	 * @param valueBean
	 *            the value bean
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByValueBean(final String queryString,
			final Object valueBean) throws Exception {
		return findByValueBean(queryString, valueBean, false);
	}

	/**
	 * Find by value bean.
	 *
	 * @param queryString
	 *            the query string
	 * @param valueBean
	 *            the value bean
	 * @param isListOfMap
	 *            the is list of map
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByValueBean(final String queryString,
			final Object valueBean, final boolean isListOfMap) throws Exception {
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({  "rawtypes" })
			@Override
			public List<?> execute(Session session) throws HibernateException {
				NativeQuery queryObject = session.createNativeQuery(queryString);
				prepareQuery(queryObject);
				queryObject.setProperties(valueBean);
				if (isListOfMap) {
					queryObject
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				}
				return queryObject.list();
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for named queries
	// -------------------------------------------------------------------------

	/**
	 * Find by named query.
	 *
	 * @param queryName
	 *            the query name
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedQuery(final String queryName,
			final Object... values) throws Exception {
		return findByNamedQuery(queryName, false, values);
	}

	/**
	 * Find by named query.
	 *
	 * @param queryName
	 *            the query name
	 * @param isListOfMap
	 *            the is list of map
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedQuery(final String queryName,
			final boolean isListOfMap, final Object... values) throws Exception {
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public List<?> execute(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				if (isListOfMap) {
					queryObject
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				}
				return queryObject.list();
			}
		});
	}

	/**
	 * Find by named query and named param.
	 *
	 * @param queryName
	 *            the query name
	 * @param paramName
	 *            the param name
	 * @param value
	 *            the value
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedQueryAndNamedParam(String queryName,
			String paramName, Object value) throws Exception {
		return findByNamedQueryAndNamedParam(queryName,
				new String[] { paramName }, new Object[] { value });
	}

	/**
	 * Find by named query and named param.
	 *
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedQueryAndNamedParam(final String queryName,
			final String[] paramNames, final Object[] values) throws Exception {
		return findByNamedQueryAndNamedParam(queryName, paramNames, values,
				false);
	}

	/**
	 * Find by named query and named param.
	 *
	 * @param queryName
	 *            the query name
	 * @param paramNames
	 *            the param names
	 * @param values
	 *            the values
	 * @param isListOfMap
	 *            the is list of map
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedQueryAndNamedParam(final String queryName,
			final String[] paramNames, final Object[] values,
			final boolean isListOfMap) throws Exception {

		if (values != null
				&& (paramNames == null || paramNames.length != values.length)) {
			throw new IllegalArgumentException(
					"Length of paramNames array must match length of values array");
		}
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public List<?> execute(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						applyNamedParameterToQuery(queryObject, paramNames[i],
								values[i]);
					}
				}
				if (isListOfMap) {
					queryObject
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				}
				return queryObject.list();
			}
		});
	}

	/**
	 * Find by named query and value bean.
	 *
	 * @param queryName
	 *            the query name
	 * @param valueBean
	 *            the value bean
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedQueryAndValueBean(final String queryName,
			final Object valueBean) throws Exception {
		return findByNamedQueryAndValueBean(queryName, valueBean, false);
	}

	/**
	 * Find by named query and value bean.
	 *
	 * @param queryName
	 *            the query name
	 * @param valueBean
	 *            the value bean
	 * @param isListOfMap
	 *            the is list of map
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByNamedQueryAndValueBean(final String queryName,
			final Object valueBean, final boolean isListOfMap) throws Exception {
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public List<?> execute(Session session) throws HibernateException {
				Query queryObject = session.getNamedQuery(queryName);
				prepareQuery(queryObject);
				queryObject.setProperties(valueBean);
				if (isListOfMap) {
					queryObject
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
				}
				return queryObject.list();
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience finder methods for detached criteria
	// -------------------------------------------------------------------------

	/**
	 * Find by criteria.
	 *
	 * @param criteria
	 *            the criteria
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByCriteria(DetachedCriteria criteria) throws Exception {
		return findByCriteria(criteria, -1, -1);
	}

	/**
	 * Find by criteria.
	 *
	 * @param criteria
	 *            the criteria
	 * @param firstResult
	 *            the first result
	 * @param maxResults
	 *            the max results
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public List<?> findByCriteria(final DetachedCriteria criteria,
			final int firstResult, final int maxResults) throws Exception {
		if (criteria == null) {
			throw new NullPointerException("DetachedCriteria must not be null");
		}
		return doHibernate(new HibernateCallback() {
			@Override
			public List<?> execute(Session session) throws HibernateException {
				Criteria executableCriteria = criteria
						.getExecutableCriteria(session);
				prepareCriteria(executableCriteria);
				if (firstResult >= 0) {
					executableCriteria.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					executableCriteria.setMaxResults(maxResults);
				}
				return executableCriteria.list();
			}
		});
	}

	/**
	 * Find by example.
	 *
	 * @param <T>
	 *            the generic type
	 * @param exampleEntity
	 *            the example entity
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public <T> List<T> findByExample(T exampleEntity) throws Exception {
		return findByExample(null, exampleEntity, -1, -1);
	}

	/**
	 * Find by example.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entityName
	 *            the entity name
	 * @param exampleEntity
	 *            the example entity
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public <T> List<T> findByExample(String entityName, T exampleEntity)
			throws Exception {
		return findByExample(entityName, exampleEntity, -1, -1);
	}

	/**
	 * Find by example.
	 *
	 * @param <T>
	 *            the generic type
	 * @param exampleEntity
	 *            the example entity
	 * @param firstResult
	 *            the first result
	 * @param maxResults
	 *            the max results
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public <T> List<T> findByExample(T exampleEntity, int firstResult,
			int maxResults) throws Exception {
		return findByExample(null, exampleEntity, firstResult, maxResults);
	}

	/**
	 * Find by example.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entityName
	 *            the entity name
	 * @param exampleEntity
	 *            the example entity
	 * @param firstResult
	 *            the first result
	 * @param maxResults
	 *            the max results
	 * @return the list
	 * @throws Exception
	 *             the exception
	 */
	public <T> List<T> findByExample(final String entityName,
			final T exampleEntity, final int firstResult, final int maxResults)
			throws Exception {
		if (exampleEntity == null) {
			throw new NullPointerException("Example entity must not be null");
		}
		return doHibernate(new HibernateCallback() {
			@Override
			@SuppressWarnings({ "unchecked" })
			public List<T> execute(Session session) throws HibernateException {
				Criteria executableCriteria = (entityName != null ? session
						.createCriteria(entityName) : session
						.createCriteria(exampleEntity.getClass()));
				executableCriteria.add(Example.create(exampleEntity));
				prepareCriteria(executableCriteria);
				if (firstResult >= 0) {
					executableCriteria.setFirstResult(firstResult);
				}
				if (maxResults > 0) {
					executableCriteria.setMaxResults(maxResults);
				}
				return executableCriteria.list();
			}
		});
	}

	// -------------------------------------------------------------------------
	// Convenience query methods for iteration and bulk updates/deletes
	// -------------------------------------------------------------------------

	/**
	 * Iterate.
	 *
	 * @param queryString
	 *            the query string
	 * @return the iterator
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("rawtypes")
	public Iterator iterate(String queryString) throws Exception {
		return iterate(queryString, (Object[]) null);
	}

	/**
	 * Iterate.
	 *
	 * @param queryString
	 *            the query string
	 * @param value
	 *            the value
	 * @return the iterator
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("rawtypes")
	public Iterator iterate(String queryString, Object value) throws Exception {
		return iterate(queryString, new Object[] { value });
	}

	/**
	 * Iterate.
	 *
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the iterator
	 * @throws Exception
	 *             the exception
	 */
	public Iterator<?> iterate(final String queryString, final Object... values)
			throws Exception {
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public Iterator<?> execute(Session session)
					throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.iterate();
			}
		});
	}

	/**
	 * Bulk update.
	 *
	 * @param queryString
	 *            the query string
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	public int bulkUpdate(String queryString) throws Exception {
		return bulkUpdate(queryString, (Object[]) null);
	}

	/**
	 * Bulk update.
	 *
	 * @param queryString
	 *            the query string
	 * @param value
	 *            the value
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	public int bulkUpdate(String queryString, Object value) throws Exception {
		return bulkUpdate(queryString, new Object[] { value });
	}

	/**
	 * Bulk update.
	 *
	 * @param queryString
	 *            the query string
	 * @param values
	 *            the values
	 * @return the int
	 * @throws Exception
	 *             the exception
	 */
	public int bulkUpdate(final String queryString, final Object... values)
			throws Exception {
		return doHibernate(new HibernateCallback() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public Integer execute(Session session) throws HibernateException {
				Query queryObject = session.createQuery(queryString);
				prepareQuery(queryObject);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						queryObject.setParameter(i, values[i]);
					}
				}
				return queryObject.executeUpdate();
			}
		});
	}

	/**
	 * Prepare query.
	 *
	 * @param queryObject the query object
	 */
	@SuppressWarnings({ "rawtypes" })
	protected void prepareQuery(Query queryObject) {
		if (isCacheQueries()) {
			queryObject.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				queryObject.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			queryObject.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			queryObject.setMaxResults(getMaxResults());
		}

		if (getFirstResult() > 0) {
			queryObject.setFirstResult(getFirstResult());
		}
	}

	/**
	 * Apply named parameter to query.
	 *
	 * @param queryObject the query object
	 * @param paramName the param name
	 * @param value the value
	 * @throws HibernateException the hibernate exception
	 */
	@SuppressWarnings({ "rawtypes" })
	protected void applyNamedParameterToQuery(Query queryObject,
			String paramName, Object value) throws HibernateException {

		if (value instanceof Collection) {
			queryObject.setParameterList(paramName, (Collection) value);
		} else if (value instanceof Object[]) {
			queryObject.setParameterList(paramName, (Object[]) value);
		} else {
			queryObject.setParameter(paramName, value);
		}
	}

	/**
	 * Prepare criteria.
	 *
	 * @param criteria the criteria
	 */
	protected void prepareCriteria(Criteria criteria) {
		if (isCacheQueries()) {
			criteria.setCacheable(true);
			if (getQueryCacheRegion() != null) {
				criteria.setCacheRegion(getQueryCacheRegion());
			}
		}
		if (getFetchSize() > 0) {
			criteria.setFetchSize(getFetchSize());
		}
		if (getMaxResults() > 0) {
			criteria.setMaxResults(getMaxResults());
		}
	}

	/**
	 * Checks if is cache queries.
	 *
	 * @return true, if is cache queries
	 */
	public boolean isCacheQueries() {
		return cacheQueries;
	}

	/**
	 * Sets the cache queries.
	 *
	 * @param cacheQueries the new cache queries
	 */
	public void setCacheQueries(boolean cacheQueries) {
		this.cacheQueries = cacheQueries;
	}

	/**
	 * Gets the query cache region.
	 *
	 * @return the query cache region
	 */
	public String getQueryCacheRegion() {
		return queryCacheRegion;
	}

	/**
	 * Sets the query cache region.
	 *
	 * @param queryCacheRegion the new query cache region
	 */
	public void setQueryCacheRegion(String queryCacheRegion) {
		this.queryCacheRegion = queryCacheRegion;
	}

	/**
	 * Gets the fetch size.
	 *
	 * @return the fetch size
	 */
	public int getFetchSize() {
		return fetchSize;
	}

	/**
	 * Sets the fetch size.
	 *
	 * @param fetchSize the new fetch size
	 */
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}

	/**
	 * Gets the first result.
	 *
	 * @return the first result
	 */
	public int getFirstResult() {
		return firstResult;
	}

	/**
	 * Sets the first result.
	 *
	 * @param firstResult the new first result
	 */
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	/**
	 * Gets the max results.
	 *
	 * @return the max results
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * Sets the max results.
	 *
	 * @param maxResults the new max results
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
