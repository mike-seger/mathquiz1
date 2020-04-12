package com.net128.application.mathquiz.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.spi.PersistenceProviderResolverHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.net128.application.mathquiz.persistence.PersistenceManagerBean;

public class BaseDao<T> implements Dao<T> {
	private final static Logger logger = LoggerFactory.getLogger(BaseDao.class);
	protected final static EntityManagerFactory EMF;
	private Class<T> clazz;
	static {
		logger.info("Initializing EntityManagerFactory...");
		logger.info("providers:"  + PersistenceProviderResolverHolder
			.getPersistenceProviderResolver()
			.getPersistenceProviders());
		EMF = PersistenceManagerBean.getInstance().getEntityManagerFactory();
	}

	public BaseDao(Class<T> clazz) {
		this.clazz=clazz;
	}
	
	public static EntityManager getEntityManager() {
		return EMF.createEntityManager();
	}
	
	public static List<Object> genericFindAll(String query) {
		return getEntityManager().createQuery(query, Object.class).getResultList();
	}

	public int deleteAll() {
		return update("DELETE FROM "+clazz.getSimpleName()+" t");
	}

	public int update(String queryString) {
		return update(queryString, null);
	}
	
	public int update(String queryString, Map<String, Object> parameters) {
		EntityManager e = getEntityManager();
		int result=0;
		EntityTransaction transaction=null;
		try {
			transaction = e.getTransaction();
			transaction.begin();
			Query query = createTypedQuery(e, queryString, parameters);
			result = query.executeUpdate();
			transaction.commit();
		} catch (Exception ex) {
			if (transaction != null && transaction.isActive())
				transaction.rollback();
			logger.error(ex.getMessage(), ex);
		} finally {
			e.close();
		}
		return result;
	}

	public boolean delete(Long id) {
		EntityManager e = getEntityManager();
		EntityTransaction transaction = null;
		try {
			transaction = e.getTransaction();
			transaction.begin();
			T item = e.find(clazz, id);
			e.remove(item);
			transaction.commit();
			return true;
		} catch (Exception ex) {
			if (transaction != null && transaction.isActive())
				transaction.rollback();
			logger.error(ex.getMessage(), ex);
			return false;
		} finally {
			e.close();
		}
	}

	public T save(T object) {
		return save(object, null);
	}

	public T save(T object, SaveHook saveHook) {
		EntityManager entityManager = getEntityManager();
		entityManager.clear();
		entityManager.setFlushMode(FlushModeType.COMMIT);
		EntityTransaction transaction = null;
		T savedObject;
		try {
			transaction = entityManager.getTransaction();
			transaction.begin();
			if(saveHook!=null) {
				saveHook.process(entityManager, object);
			}
			if(object instanceof SingleRefEntity) {
				SingleRefEntity entity=(SingleRefEntity)object;
				entity.setReference(entityManager.merge(entity.getReference()));
			}
			savedObject=entityManager.merge(object);
	        entityManager.flush();
			transaction.commit();
		} catch (Exception ex) {
			if (transaction != null && transaction.isActive())
				transaction.rollback();
			logger.error("Problem saving "+object, ex);
			savedObject=null;
		} finally {
			entityManager.close();
		}
		return savedObject;
	}

	public T findById(Long id) {
		return getEntityManager().find(clazz, id);
	}
	
	public List<T> findAll(String queryString) {
		return findAll(queryString, null);
	}
	
	public List<T> findAll(String queryString, Map<String, Object> parameters) {
		return findFirst(queryString, parameters, 0);
	}
	
	public List<T> findFirst(String queryString, Map<String, Object> parameters, int numberOfRecords) {
		TypedQuery<T> query=createTypedQuery(getEntityManager(), queryString, parameters);
		if(numberOfRecords>0) {
			query.setMaxResults(numberOfRecords);
		}
		return query.getResultList();
	}

	public Double calculate(String query) {
		return calculate(query, null);
	}

	public Double calculate(String queryString, Map<String, Object> parameters) {
		TypedQuery<Double> query=getEntityManager().createQuery(queryString, Double.class);
		addQueryParameters(query, parameters);
		return query.getSingleResult();
	}

	public Long countAll(String query) {
		return countAll(query, null);
	}

	public Long countAll(String queryString, Map<String, Object> parameters) {
		TypedQuery<Long> query=getEntityManager().createQuery(queryString, Long.class);
		addQueryParameters(query, parameters);
		return query.getSingleResult();
	}

	private TypedQuery<T> createTypedQuery(EntityManager e, String queryString, Map<String, Object> parameters) {
		TypedQuery<T> query=e.createQuery(queryString, clazz);
		addQueryParameters(query, parameters);
		return query;
	}
	
	private void addQueryParameters(Query query,  Map<String, Object> parameters) {
		if(parameters!=null) {
			for(String name : parameters.keySet()) {
				query.setParameter(name, parameters.get(name));			
			}		
		}		
	}
}
