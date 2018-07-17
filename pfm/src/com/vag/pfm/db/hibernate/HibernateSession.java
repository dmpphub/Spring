/**
 * REVISION         DATE        NAME     DESCRIPTION
 * 511.101       Aug 7, 2016	GOBI      Initial Code
 */
package com.vag.pfm.db.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import net.sf.ehcache.hibernate.management.api.HibernateStats;

import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.LobHelper;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionEventListener;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionBuilder;
import org.hibernate.SimpleNaturalIdLoadAccess;
import org.hibernate.Transaction;
import org.hibernate.TypeHelper;
import org.hibernate.UnknownProfileException;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.stat.SessionStatistics;

/**
 * The Class HibernateSession.
 *
 * @author GOBINATH A
 */
@SuppressWarnings("rawtypes")
public class HibernateSession implements Session {

	/** The Constant connectionLog. */
	static final Logger connectionLog = Logger.getLogger("connectionLogger");

	/** The session. */
	Session session = null;

	/** The hibernate stats. */
	HibernateStats hibernateStats = null;

	/*** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7117923378660899624L;

	/**
	 * *
	 * Instantiates a new hibernate session.
	 *
	 * @param session the session
	 * @param hibernateStats the hibernate stats
	 */
	public HibernateSession(Session session, HibernateStats hibernateStats) {
		this.session = session;
		this.hibernateStats = hibernateStats;
		int hash = session.hashCode();
		connectionLog.debug("SessionOpenCount= "+this.hibernateStats.getSessionOpenCount() + " @" + Integer.toHexString(hash));
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the tenant identifier
	 * @see org.hibernate.SharedSessionContract#getTenantIdentifier()
	 */
	@Override
	public String getTenantIdentifier() {
		return this.session.getTenantIdentifier();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @throws HibernateException the hibernate exception
	 * @see org.hibernate.SharedSessionContract#close()
	 */
	@Override
	public void close() throws HibernateException {
        clear();
        int hash = session.hashCode();
		this.session.close();
		connectionLog.debug("SessionCloseCount= "+this.hibernateStats.getSessionCloseCount() + " @" + Integer.toHexString(hash));
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return true, if is open
	 * @see org.hibernate.SharedSessionContract#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return this.session.isOpen();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return true, if is connected
	 * @see org.hibernate.SharedSessionContract#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return this.session.isConnected();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the transaction
	 * @see org.hibernate.SharedSessionContract#beginTransaction()
	 */
	@Override
	public Transaction beginTransaction() {
		return this.session.beginTransaction();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the transaction
	 * @see org.hibernate.SharedSessionContract#getTransaction()
	 */
	@Override
	public Transaction getTransaction() {
		return this.session.getTransaction();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the named procedure call
	 * @see org.hibernate.SharedSessionContract#getNamedProcedureCall(java.lang.String)
	 */
	@Override
	public ProcedureCall getNamedProcedureCall(String paramString) {
		return this.session.getNamedProcedureCall(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the procedure call
	 * @see org.hibernate.SharedSessionContract#createStoredProcedureCall(java.lang.String)
	 */
	@Override
	public ProcedureCall createStoredProcedureCall(String paramString) {
		return this.session.createStoredProcedureCall(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramVarArgs the param var args
	 * @return the procedure call
	 * @see org.hibernate.SharedSessionContract#createStoredProcedureCall(java.lang.String, java.lang.Class[])
	 */
	@Override
	public ProcedureCall createStoredProcedureCall(String paramString,
			Class... paramVarArgs) {
		return this.session.createStoredProcedureCall(paramString, paramVarArgs);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramVarArgs the param var args
	 * @return the procedure call
	 * @see org.hibernate.SharedSessionContract#createStoredProcedureCall(java.lang.String, java.lang.String[])
	 */
	@Override
	public ProcedureCall createStoredProcedureCall(String paramString,
			String... paramVarArgs) {
		return this.session.createStoredProcedureCall(paramString, paramVarArgs);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramClass the param class
	 * @return the criteria
	 * @see org.hibernate.SharedSessionContract#createCriteria(java.lang.Class)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Criteria createCriteria(Class paramClass) {
		return this.session.createCriteria(paramClass);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramClass the param class
	 * @param paramString the param string
	 * @return the criteria
	 * @see org.hibernate.SharedSessionContract#createCriteria(java.lang.Class, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Criteria createCriteria(Class paramClass, String paramString) {
		return this.session.createCriteria(paramClass, paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the criteria
	 * @see org.hibernate.SharedSessionContract#createCriteria(java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Criteria createCriteria(String paramString) {
		return this.session.createCriteria(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString1 the param string1
	 * @param paramString2 the param string2
	 * @return the criteria
	 * @see org.hibernate.SharedSessionContract#createCriteria(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Criteria createCriteria(String paramString1, String paramString2) {
		return this.session.createCriteria(paramString1, paramString2);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the jdbc batch size
	 * @see org.hibernate.SharedSessionContract#getJdbcBatchSize()
	 */
	@Override
	public Integer getJdbcBatchSize() {
		return this.session.getJdbcBatchSize();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramInteger the new jdbc batch size
	 * @see org.hibernate.SharedSessionContract#setJdbcBatchSize(java.lang.Integer)
	 */
	@Override
	public void setJdbcBatchSize(Integer paramInteger) {
		this.session.setJdbcBatchSize(paramInteger);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the named query
	 * @see org.hibernate.query.QueryProducer#getNamedQuery(java.lang.String)
	 */
	@Override
	public Query getNamedQuery(String paramString) {
		return this.session.getNamedQuery(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the query
	 * @see org.hibernate.query.QueryProducer#createNamedQuery(java.lang.String)
	 */
	@Override
	public Query createNamedQuery(String paramString) {
		return this.session.createNamedQuery(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the native query
	 * @see org.hibernate.query.QueryProducer#createNativeQuery(java.lang.String)
	 */
	@Override
	public NativeQuery createNativeQuery(String paramString) {
		return this.session.createNativeQuery(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString1 the param string1
	 * @param paramString2 the param string2
	 * @return the native query
	 * @see org.hibernate.query.QueryProducer#createNativeQuery(java.lang.String, java.lang.String)
	 */
	@Override
	public NativeQuery createNativeQuery(String paramString1,
			String paramString2) {
		return this.session.createNativeQuery(paramString1,
				paramString2);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the named native query
	 * @see org.hibernate.query.QueryProducer#getNamedNativeQuery(java.lang.String)
	 */
	@Override
	public NativeQuery getNamedNativeQuery(String paramString) {
		return this.session.getNamedNativeQuery(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @return true, if successful
	 * @see javax.persistence.EntityManager#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object arg0) {
		return this.session.contains(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param arg0 the arg0
	 * @return the entity graph
	 * @see javax.persistence.EntityManager#createEntityGraph(java.lang.Class)
	 */
	@Override
	public <T> EntityGraph<T> createEntityGraph(Class<T> arg0) {
		return this.session.createEntityGraph(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @return the entity graph
	 * @see javax.persistence.EntityManager#createEntityGraph(java.lang.String)
	 */
	@Override
	public EntityGraph<?> createEntityGraph(String arg0) {
		return this.session.createEntityGraph(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @return the stored procedure query
	 * @see javax.persistence.EntityManager#createNamedStoredProcedureQuery(java.lang.String)
	 */
	@Override
	public StoredProcedureQuery createNamedStoredProcedureQuery(String arg0) {
		return this.session.createNamedStoredProcedureQuery(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @return the stored procedure query
	 * @see javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String)
	 */
	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String arg0) {
		return this.session.createStoredProcedureQuery(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @return the stored procedure query
	 * @see javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String, java.lang.Class[])
	 */
	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String arg0,
			Class... arg1) {
		return this.session.createStoredProcedureQuery(arg0,arg1);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @return the stored procedure query
	 * @see javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String, java.lang.String[])
	 */
	@Override
	public StoredProcedureQuery createStoredProcedureQuery(String arg0,
			String... arg1) {
		return this.session.createStoredProcedureQuery(arg0,arg1);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @see javax.persistence.EntityManager#detach(java.lang.Object)
	 */
	@Override
	public void detach(Object arg0) {
		this.session.detach(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @return the t
	 * @see javax.persistence.EntityManager#find(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T find(Class<T> arg0, Object arg1) {
		return this.session.find(arg0, arg1);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * @return the t
	 * @see javax.persistence.EntityManager#find(java.lang.Class, java.lang.Object, java.util.Map)
	 */
	@Override
	public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
		return this.session.find(arg0, arg1, arg2);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * @return the t
	 * @see javax.persistence.EntityManager#find(java.lang.Class, java.lang.Object, javax.persistence.LockModeType)
	 */
	@Override
	public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2) {
		return this.session.find(arg0, arg1, arg2);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * @param arg3 the arg3
	 * @return the t
	 * @see javax.persistence.EntityManager#find(java.lang.Class, java.lang.Object, javax.persistence.LockModeType, java.util.Map)
	 */
	@Override
	public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2,
			Map<String, Object> arg3) {
		return this.session.find(arg0, arg1, arg2, arg3);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the criteria builder
	 * @see javax.persistence.EntityManager#getCriteriaBuilder()
	 */
	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return this.session.getCriteriaBuilder();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the delegate
	 * @see javax.persistence.EntityManager#getDelegate()
	 */
	@Override
	public Object getDelegate() {
		return this.session.getDelegate();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @return the entity graph
	 * @see javax.persistence.EntityManager#getEntityGraph(java.lang.String)
	 */
	@Override
	public EntityGraph<?> getEntityGraph(String arg0) {
		return this.session.getEntityGraph(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param arg0 the arg0
	 * @return the entity graphs
	 * @see javax.persistence.EntityManager#getEntityGraphs(java.lang.Class)
	 */
	@Override
	public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> arg0) {
		return this.session.getEntityGraphs(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the entity manager factory
	 * @see javax.persistence.EntityManager#getEntityManagerFactory()
	 */
	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		return this.session.getEntityManagerFactory();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @return the lock mode
	 * @see javax.persistence.EntityManager#getLockMode(java.lang.Object)
	 */
	@Override
	public LockModeType getLockMode(Object arg0) {
		return this.session.getLockMode(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the metamodel
	 * @see javax.persistence.EntityManager#getMetamodel()
	 */
	@Override
	public Metamodel getMetamodel() {
		return this.session.getMetamodel();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the properties
	 * @see javax.persistence.EntityManager#getProperties()
	 */
	@Override
	public Map<String, Object> getProperties() {
		return this.session.getProperties();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @return the reference
	 * @see javax.persistence.EntityManager#getReference(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T> T getReference(Class<T> arg0, Object arg1) {
		return this.session.getReference(arg0, arg1);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return true, if is joined to transaction
	 * @see javax.persistence.EntityManager#isJoinedToTransaction()
	 */
	@Override
	public boolean isJoinedToTransaction() {
		return this.session.isJoinedToTransaction();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @see javax.persistence.EntityManager#joinTransaction()
	 */
	@Override
	public void joinTransaction() {
		this.session.joinTransaction();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @see javax.persistence.EntityManager#lock(java.lang.Object, javax.persistence.LockModeType)
	 */
	@Override
	public void lock(Object arg0, LockModeType arg1) {
		this.session.lock(arg0, arg1);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * @see javax.persistence.EntityManager#lock(java.lang.Object, javax.persistence.LockModeType, java.util.Map)
	 */
	@Override
	public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
		this.session.lock(arg0, arg1, arg2);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @see javax.persistence.EntityManager#refresh(java.lang.Object, java.util.Map)
	 */
	@Override
	public void refresh(Object arg0, Map<String, Object> arg1) {
		this.session.refresh(arg0, arg1);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @see javax.persistence.EntityManager#refresh(java.lang.Object, javax.persistence.LockModeType)
	 */
	@Override
	public void refresh(Object arg0, LockModeType arg1) {
		this.session.refresh(arg0, arg1);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @param arg2 the arg2
	 * @see javax.persistence.EntityManager#refresh(java.lang.Object, javax.persistence.LockModeType, java.util.Map)
	 */
	@Override
	public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2) {
		this.session.refresh(arg0, arg1, arg2);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @see javax.persistence.EntityManager#remove(java.lang.Object)
	 */
	@Override
	public void remove(Object arg0) {
		this.session.remove(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the new flush mode
	 * @see javax.persistence.EntityManager#setFlushMode(javax.persistence.FlushModeType)
	 */
	@Override
	public void setFlushMode(FlushModeType arg0) {
		this.session.setFlushMode(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @see javax.persistence.EntityManager#setProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setProperty(String arg0, Object arg1) {
		this.session.setProperty(arg0, arg1);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param arg0 the arg0
	 * @return the t
	 * @see javax.persistence.EntityManager#unwrap(java.lang.Class)
	 */
	@Override
	public <T> T unwrap(Class<T> arg0) {
		return this.session.unwrap(arg0);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the session
	 * @see org.hibernate.jpa.HibernateEntityManager#getSession()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Session getSession() {
		return this.session.getSession();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the shared session builder
	 * @see org.hibernate.Session#sessionWithOptions()
	 */
	@Override
	public SharedSessionBuilder sessionWithOptions() {
		return this.session.sessionWithOptions();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @throws HibernateException the hibernate exception
	 * @see org.hibernate.Session#flush()
	 */
	@Override
	public void flush() throws HibernateException {
		this.session.flush();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramFlushMode the new flush mode
	 * @see org.hibernate.Session#setFlushMode(org.hibernate.FlushMode)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void setFlushMode(FlushMode paramFlushMode) {
		this.session.setFlushMode(paramFlushMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the flush mode
	 * @see org.hibernate.Session#getFlushMode()
	 */
	@Override
	public FlushModeType getFlushMode() {
		return this.session.getFlushMode();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramFlushMode the new hibernate flush mode
	 * @see org.hibernate.Session#setHibernateFlushMode(org.hibernate.FlushMode)
	 */
	@Override
	public void setHibernateFlushMode(FlushMode paramFlushMode) {
		this.session.setHibernateFlushMode(paramFlushMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the hibernate flush mode
	 * @see org.hibernate.Session#getHibernateFlushMode()
	 */
	@Override
	public FlushMode getHibernateFlushMode() {
		return this.session.getHibernateFlushMode();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramCacheMode the new cache mode
	 * @see org.hibernate.Session#setCacheMode(org.hibernate.CacheMode)
	 */
	@Override
	public void setCacheMode(CacheMode paramCacheMode) {
		this.session.setCacheMode(paramCacheMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the cache mode
	 * @see org.hibernate.Session#getCacheMode()
	 */
	@Override
	public CacheMode getCacheMode() {
		return this.session.getCacheMode();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the session factory
	 * @see org.hibernate.Session#getSessionFactory()
	 */
	@Override
	public SessionFactory getSessionFactory() {
		return this.session.getSessionFactory();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @throws HibernateException the hibernate exception
	 * @see org.hibernate.Session#cancelQuery()
	 */
	@Override
	public void cancelQuery() throws HibernateException {
		this.session.cancelQuery();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return true, if is dirty
	 * @throws HibernateException the hibernate exception
	 * @see org.hibernate.Session#isDirty()
	 */
	@Override
	public boolean isDirty() throws HibernateException {
		return this.session.isDirty();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return true, if is default read only
	 * @see org.hibernate.Session#isDefaultReadOnly()
	 */
	@Override
	public boolean isDefaultReadOnly() {
		return this.session.isDefaultReadOnly();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramBoolean the new default read only
	 * @see org.hibernate.Session#setDefaultReadOnly(boolean)
	 */
	@Override
	public void setDefaultReadOnly(boolean paramBoolean) {
		this.session.setDefaultReadOnly(paramBoolean);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @return the identifier
	 * @see org.hibernate.Session#getIdentifier(java.lang.Object)
	 */
	@Override
	public Serializable getIdentifier(Object paramObject) {
		return this.session.getIdentifier(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return true, if successful
	 * @see org.hibernate.Session#contains(java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean contains(String paramString, Object paramObject) {
		return this.session.contains(paramString, paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @see org.hibernate.Session#evict(java.lang.Object)
	 */
	@Override
	public void evict(Object paramObject) {
		this.session.evict(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @param paramSerializable the param serializable
	 * @param paramLockMode the param lock mode
	 * @return the t
	 * @see org.hibernate.Session#load(java.lang.Class, java.io.Serializable, org.hibernate.LockMode)
	 */
	@Override
	public <T> T load(Class<T> paramClass, Serializable paramSerializable,
			LockMode paramLockMode) {
		return this.session.load(paramClass, paramSerializable, paramLockMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @param paramSerializable the param serializable
	 * @param paramLockOptions the param lock options
	 * @return the t
	 * @see org.hibernate.Session#load(java.lang.Class, java.io.Serializable, org.hibernate.LockOptions)
	 */
	@Override
	public <T> T load(Class<T> paramClass, Serializable paramSerializable,
			LockOptions paramLockOptions) {
		return this.session.load(paramClass, paramSerializable, paramLockOptions);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramSerializable the param serializable
	 * @param paramLockMode the param lock mode
	 * @return the object
	 * @see org.hibernate.Session#load(java.lang.String, java.io.Serializable, org.hibernate.LockMode)
	 */
	@Override
	public Object load(String paramString, Serializable paramSerializable,
			LockMode paramLockMode) {
		return this.session.load(paramString, paramSerializable, paramLockMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramSerializable the param serializable
	 * @param paramLockOptions the param lock options
	 * @return the object
	 * @see org.hibernate.Session#load(java.lang.String, java.io.Serializable, org.hibernate.LockOptions)
	 */
	@Override
	public Object load(String paramString, Serializable paramSerializable,
			LockOptions paramLockOptions) {
		return this.session.load(paramString, paramSerializable, paramLockOptions);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @param paramSerializable the param serializable
	 * @return the t
	 * @see org.hibernate.Session#load(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <T> T load(Class<T> paramClass, Serializable paramSerializable) {
		return this.session.load(paramClass, paramSerializable);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramSerializable the param serializable
	 * @return the object
	 * @see org.hibernate.Session#load(java.lang.String, java.io.Serializable)
	 */
	@Override
	public Object load(String paramString, Serializable paramSerializable) {
		return this.session.load(paramString, paramSerializable);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @param paramSerializable the param serializable
	 * @see org.hibernate.Session#load(java.lang.Object, java.io.Serializable)
	 */
	@Override
	public void load(Object paramObject, Serializable paramSerializable) {
		this.session.load(paramObject, paramSerializable);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @param paramReplicationMode the param replication mode
	 * @see org.hibernate.Session#replicate(java.lang.Object, org.hibernate.ReplicationMode)
	 */
	@Override
	public void replicate(Object paramObject,
			ReplicationMode paramReplicationMode) {
		this.session.replicate(paramObject, paramReplicationMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @param paramReplicationMode the param replication mode
	 * @see org.hibernate.Session#replicate(java.lang.String, java.lang.Object, org.hibernate.ReplicationMode)
	 */
	@Override
	public void replicate(String paramString, Object paramObject,
			ReplicationMode paramReplicationMode) {
		this.session.replicate(paramString, paramObject, paramReplicationMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @return the serializable
	 * @see org.hibernate.Session#save(java.lang.Object)
	 */
	@Override
	public Serializable save(Object paramObject) {
		return this.session.save(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the serializable
	 * @see org.hibernate.Session#save(java.lang.String, java.lang.Object)
	 */
	@Override
	public Serializable save(String paramString, Object paramObject) {
		return this.session.save(paramString, paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @see org.hibernate.Session#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(Object paramObject) {
		this.session.saveOrUpdate(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @see org.hibernate.Session#saveOrUpdate(java.lang.String, java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(String paramString, Object paramObject) {
		this.session.saveOrUpdate(paramString, paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @see org.hibernate.Session#update(java.lang.Object)
	 */
	@Override
	public void update(Object paramObject) {
		this.session.update(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @see org.hibernate.Session#update(java.lang.String, java.lang.Object)
	 */
	@Override
	public void update(String paramString, Object paramObject) {
		this.session.update(paramString, paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @return the object
	 * @see org.hibernate.Session#merge(java.lang.Object)
	 */
	@Override
	public Object merge(Object paramObject) {
		return this.session.merge(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @return the object
	 * @see org.hibernate.Session#merge(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object merge(String paramString, Object paramObject) {
		return this.session.merge(paramString, paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @see org.hibernate.Session#persist(java.lang.Object)
	 */
	@Override
	public void persist(Object paramObject) {
		this.session.persist(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @see org.hibernate.Session#persist(java.lang.String, java.lang.Object)
	 */
	@Override
	public void persist(String paramString, Object paramObject) {
		this.session.persist(paramString, paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @see org.hibernate.Session#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object paramObject) {
		this.session.delete(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @see org.hibernate.Session#delete(java.lang.String, java.lang.Object)
	 */
	@Override
	public void delete(String paramString, Object paramObject) {
		this.session.delete(paramString, paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @param paramLockMode the param lock mode
	 * @see org.hibernate.Session#lock(java.lang.Object, org.hibernate.LockMode)
	 */
	@Override
	public void lock(Object paramObject, LockMode paramLockMode) {
		this.session.lock(paramObject, paramLockMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @param paramLockMode the param lock mode
	 * @see org.hibernate.Session#lock(java.lang.String, java.lang.Object, org.hibernate.LockMode)
	 */
	@Override
	public void lock(String paramString, Object paramObject,
			LockMode paramLockMode) {
		this.session.lock(paramString, paramObject, paramLockMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramLockOptions the param lock options
	 * @return the lock request
	 * @see org.hibernate.Session#buildLockRequest(org.hibernate.LockOptions)
	 */
	@Override
	public LockRequest buildLockRequest(LockOptions paramLockOptions) {
		return this.session.buildLockRequest(paramLockOptions);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @see org.hibernate.Session#refresh(java.lang.Object)
	 */
	@Override
	public void refresh(Object paramObject) {
		this.session.refresh(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @see org.hibernate.Session#refresh(java.lang.String, java.lang.Object)
	 */
	@Override
	public void refresh(String paramString, Object paramObject) {
		this.session.refresh(paramString, paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @param paramLockMode the param lock mode
	 * @see org.hibernate.Session#refresh(java.lang.Object, org.hibernate.LockMode)
	 */
	@Override
	public void refresh(Object paramObject, LockMode paramLockMode) {
		this.session.refresh(paramObject, paramLockMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @param paramLockOptions the param lock options
	 * @see org.hibernate.Session#refresh(java.lang.Object, org.hibernate.LockOptions)
	 */
	@Override
	public void refresh(Object paramObject, LockOptions paramLockOptions) {
		this.session.refresh(paramObject, paramLockOptions);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramObject the param object
	 * @param paramLockOptions the param lock options
	 * @see org.hibernate.Session#refresh(java.lang.String, java.lang.Object, org.hibernate.LockOptions)
	 */
	@Override
	public void refresh(String paramString, Object paramObject,
			LockOptions paramLockOptions) {
		this.session.refresh(paramString, paramObject, paramLockOptions);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @return the current lock mode
	 * @see org.hibernate.Session#getCurrentLockMode(java.lang.Object)
	 */
	@Override
	public LockMode getCurrentLockMode(Object paramObject) {
		return this.session.getCurrentLockMode(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @param paramString the param string
	 * @return the query
	 * @see org.hibernate.Session#createFilter(java.lang.Object, java.lang.String)
	 */
	@Override
	public Query createFilter(Object paramObject, String paramString) {
		return this.session.createFilter(paramObject, paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @see org.hibernate.Session#clear()
	 */
	@Override
	public void clear() {
		this.session.clear();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @param paramSerializable the param serializable
	 * @return the t
	 * @see org.hibernate.Session#get(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <T> T get(Class<T> paramClass, Serializable paramSerializable) {
		return this.session.get(paramClass, paramSerializable);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @param paramSerializable the param serializable
	 * @param paramLockMode the param lock mode
	 * @return the t
	 * @see org.hibernate.Session#get(java.lang.Class, java.io.Serializable, org.hibernate.LockMode)
	 */
	@Override
	public <T> T get(Class<T> paramClass, Serializable paramSerializable,
			LockMode paramLockMode) {
		return this.session.get( paramClass, paramSerializable, paramLockMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @param paramSerializable the param serializable
	 * @param paramLockOptions the param lock options
	 * @return the t
	 * @see org.hibernate.Session#get(java.lang.Class, java.io.Serializable, org.hibernate.LockOptions)
	 */
	@Override
	public <T> T get(Class<T> paramClass, Serializable paramSerializable,
			LockOptions paramLockOptions) {
		return this.session.get( paramClass, paramSerializable, paramLockOptions);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramSerializable the param serializable
	 * @return the object
	 * @see org.hibernate.Session#get(java.lang.String, java.io.Serializable)
	 */
	@Override
	public Object get(String paramString, Serializable paramSerializable) {
		return this.session.get(paramString, paramSerializable);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramSerializable the param serializable
	 * @param paramLockMode the param lock mode
	 * @return the object
	 * @see org.hibernate.Session#get(java.lang.String, java.io.Serializable, org.hibernate.LockMode)
	 */
	@Override
	public Object get(String paramString, Serializable paramSerializable,
			LockMode paramLockMode) {
		return this.session.get(paramString, paramSerializable, paramLockMode);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @param paramSerializable the param serializable
	 * @param paramLockOptions the param lock options
	 * @return the object
	 * @see org.hibernate.Session#get(java.lang.String, java.io.Serializable, org.hibernate.LockOptions)
	 */
	@Override
	public Object get(String paramString, Serializable paramSerializable,
			LockOptions paramLockOptions) {
		return this.session.get(paramString, paramSerializable, paramLockOptions);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @return the entity name
	 * @see org.hibernate.Session#getEntityName(java.lang.Object)
	 */
	@Override
	public String getEntityName(Object paramObject) {
		return this.session.getEntityName(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the identifier load access
	 * @see org.hibernate.Session#byId(java.lang.String)
	 */
	@Override
	public IdentifierLoadAccess byId(String paramString) {
		return this.session.byId(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @return the multi identifier load access
	 * @see org.hibernate.Session#byMultipleIds(java.lang.Class)
	 */
	@Override
	public <T> MultiIdentifierLoadAccess<T> byMultipleIds(Class<T> paramClass) {
		return this.session.byMultipleIds(paramClass);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the multi identifier load access
	 * @see org.hibernate.Session#byMultipleIds(java.lang.String)
	 */
	@Override
	public MultiIdentifierLoadAccess byMultipleIds(String paramString) {
		return this.session.byMultipleIds(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @return the identifier load access
	 * @see org.hibernate.Session#byId(java.lang.Class)
	 */
	@Override
	public <T> IdentifierLoadAccess<T> byId(Class<T> paramClass) {
		return this.session.byId(paramClass);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the natural id load access
	 * @see org.hibernate.Session#byNaturalId(java.lang.String)
	 */
	@Override
	public NaturalIdLoadAccess byNaturalId(String paramString) {
		return this.session.byNaturalId(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @return the natural id load access
	 * @see org.hibernate.Session#byNaturalId(java.lang.Class)
	 */
	@Override
	public <T> NaturalIdLoadAccess<T> byNaturalId(Class<T> paramClass) {
		return this.session.byNaturalId(paramClass);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the simple natural id load access
	 * @see org.hibernate.Session#bySimpleNaturalId(java.lang.String)
	 */
	@Override
	public SimpleNaturalIdLoadAccess bySimpleNaturalId(String paramString) {
		return this.session.bySimpleNaturalId(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramClass the param class
	 * @return the simple natural id load access
	 * @see org.hibernate.Session#bySimpleNaturalId(java.lang.Class)
	 */
	@Override
	public <T> SimpleNaturalIdLoadAccess<T> bySimpleNaturalId(
			Class<T> paramClass) {
		return this.session.bySimpleNaturalId(paramClass);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the filter
	 * @see org.hibernate.Session#enableFilter(java.lang.String)
	 */
	@Override
	public Filter enableFilter(String paramString) {
		return this.session.enableFilter(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the enabled filter
	 * @see org.hibernate.Session#getEnabledFilter(java.lang.String)
	 */
	@Override
	public Filter getEnabledFilter(String paramString) {
		return this.session.getEnabledFilter(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @see org.hibernate.Session#disableFilter(java.lang.String)
	 */
	@Override
	public void disableFilter(String paramString) {
		this.session.disableFilter(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the statistics
	 * @see org.hibernate.Session#getStatistics()
	 */
	@Override
	public SessionStatistics getStatistics() {
		return this.session.getStatistics();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @return true, if is read only
	 * @see org.hibernate.Session#isReadOnly(java.lang.Object)
	 */
	@Override
	public boolean isReadOnly(Object paramObject) {
		return this.session.isReadOnly(paramObject);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramObject the param object
	 * @param paramBoolean the param boolean
	 * @see org.hibernate.Session#setReadOnly(java.lang.Object, boolean)
	 */
	@Override
	public void setReadOnly(Object paramObject, boolean paramBoolean) {
		this.session.setReadOnly(paramObject, paramBoolean);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramWork the param work
	 * @throws HibernateException the hibernate exception
	 * @see org.hibernate.Session#doWork(org.hibernate.jdbc.Work)
	 */
	@Override
	public void doWork(Work paramWork) throws HibernateException {
		this.session.doWork(paramWork);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramReturningWork the param returning work
	 * @return the t
	 * @throws HibernateException the hibernate exception
	 * @see org.hibernate.Session#doReturningWork(org.hibernate.jdbc.ReturningWork)
	 */
	@Override
	public <T> T doReturningWork(ReturningWork<T> paramReturningWork)
			throws HibernateException {
		return this.session.doReturningWork(paramReturningWork);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the connection
	 * @see org.hibernate.Session#disconnect()
	 */
	@Override
	public Connection disconnect() {
		return this.session.disconnect();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramConnection the param connection
	 * @see org.hibernate.Session#reconnect(java.sql.Connection)
	 */
	@Override
	public void reconnect(Connection paramConnection) {
		this.session.reconnect(paramConnection);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return true, if is fetch profile enabled
	 * @throws UnknownProfileException the unknown profile exception
	 * @see org.hibernate.Session#isFetchProfileEnabled(java.lang.String)
	 */
	@Override
	public boolean isFetchProfileEnabled(String paramString)
			throws UnknownProfileException {
		return this.session.isFetchProfileEnabled(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @throws UnknownProfileException the unknown profile exception
	 * @see org.hibernate.Session#enableFetchProfile(java.lang.String)
	 */
	@Override
	public void enableFetchProfile(String paramString)
			throws UnknownProfileException {
		this.session.enableFetchProfile(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @throws UnknownProfileException the unknown profile exception
	 * @see org.hibernate.Session#disableFetchProfile(java.lang.String)
	 */
	@Override
	public void disableFetchProfile(String paramString)
			throws UnknownProfileException {
		this.session.disableFetchProfile(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the type helper
	 * @see org.hibernate.Session#getTypeHelper()
	 */
	@Override
	public TypeHelper getTypeHelper() {
		return this.session.getTypeHelper();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @return the lob helper
	 * @see org.hibernate.Session#getLobHelper()
	 */
	@Override
	public LobHelper getLobHelper() {
		return this.session.getLobHelper();
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramVarArgs the param var args
	 * @see org.hibernate.Session#addEventListeners(org.hibernate.SessionEventListener[])
	 */
	@Override
	public void addEventListeners(SessionEventListener... paramVarArgs) {
		this.session.addEventListeners(paramVarArgs);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramString the param string
	 * @return the query
	 * @see org.hibernate.Session#createQuery(java.lang.String)
	 */
	@Override
	public Query createQuery(String paramString) {
		return this.session.createQuery(paramString);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramString the param string
	 * @param paramClass the param class
	 * @return the query
	 * @see org.hibernate.Session#createQuery(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> Query<T> createQuery(String paramString, Class<T> paramClass) {
		return this.session.createQuery(paramString, paramClass);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramCriteriaQuery the param criteria query
	 * @return the query
	 * @see org.hibernate.Session#createQuery(javax.persistence.criteria.CriteriaQuery)
	 */
	@Override
	public <T> Query<T> createQuery(CriteriaQuery<T> paramCriteriaQuery) {
		return this.session.createQuery(paramCriteriaQuery);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramCriteriaUpdate the param criteria update
	 * @return the query
	 * @see org.hibernate.Session#createQuery(javax.persistence.criteria.CriteriaUpdate)
	 */
	@Override
	public Query createQuery(CriteriaUpdate paramCriteriaUpdate) {
		return this.session.createQuery(paramCriteriaUpdate);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param paramCriteriaDelete the param criteria delete
	 * @return the query
	 * @see org.hibernate.Session#createQuery(javax.persistence.criteria.CriteriaDelete)
	 */
	@Override
	public Query createQuery(CriteriaDelete paramCriteriaDelete) {
		return this.session.createQuery(paramCriteriaDelete);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param <T> the generic type
	 * @param paramString the param string
	 * @param paramClass the param class
	 * @return the query
	 * @see org.hibernate.Session#createNamedQuery(java.lang.String, java.lang.Class)
	 */
	@Override
	public <T> Query<T> createNamedQuery(String paramString, Class<T> paramClass) {
		return this.session.createNamedQuery(paramString, paramClass);
	}

	/**
	 *  (non-Javadoc).
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @return the native query
	 * @see org.hibernate.query.QueryProducer#createNativeQuery(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NativeQuery createNativeQuery(String arg0, Class arg1) {
		return this.session.createNativeQuery(arg0, arg1);
	}

}
