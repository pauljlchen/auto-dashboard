package org.xbot.core.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.sql.JoinType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Repository("commonDAO")
public class CommonDAO {
	
	public static enum OP_MODE {EQUALS, LIKE, LE, LT, GE, GT, NOT_EQUALS, CASE_SENSITIVE_LIKE};
 
	
	private Logger log = LogManager.getLogger("dashboard");
	
  
	@Autowired
    protected SessionFactory sessionFactory;
    
  
    public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


	public Session getSession() {
    	//return sessionFactory.openSession();
       return sessionFactory.getCurrentSession();
    } 
    
	
    public <T extends Object> T save(T model) {
    	
    	log.debug("saving object:"+model);
    
    	//System.out.println(model+" is SF null? "+sessionFactory==null +" session null?"+(sessionFactory.getCurrentSession()==null));
    	getSession().save(model);
     
    	//sessionFactory.getCurrentSession().getTransaction().commit();
    	return model;
    }

    public <T extends Object> void saveOrUpdate(T model) {
    	log.debug("saveOrUpdateing object:"+model);
        getSession().saveOrUpdate(model);
        
    }
    
    public <T extends Object> void update(T model) {
    	log.debug("updating object:"+model);
        getSession().update(model);
    }
    
    public <T extends Object> void merge(T model) {
    	log.debug("merging object:"+model);
        getSession().merge(model);
    }

    public <T extends Object, PK extends Serializable> void delete(Class<T> entityClass, PK id) {
    	log.debug("deleting class:"+entityClass.getSimpleName()+" ID:"+id);
    	getSession().delete(get(entityClass, id));
    }

    public <T extends Object> void delete(T model) {
    	log.debug("deleting object:"+model);
    	getSession().delete(model);
    }
    @SuppressWarnings("unchecked")
	public <T extends Object, PK extends Serializable> T load(Class<T> entityClass, PK id) {
    	
    	log.debug("getting class:"+entityClass.getSimpleName()+" ID:"+id);
    	return (T) getSession().load(entityClass, id); 
    }
    /**
     * Get the instance from DB immediately 
     * @param entityClass
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
	public <T extends Object, PK extends Serializable> T get(Class<T> entityClass, PK id) {
    	
    	log.debug("getting class:"+entityClass.getSimpleName()+" ID:"+id);
    	return (T) getSession().get(entityClass, id); 
    }
    
    public <T> T isExisted(T model) {
    	return isExisted(model, OP_MODE.EQUALS);
    }
    
   
    /**
     * check whether provided element is existed
     * @param <T>
     * @return
     */
    public <T> T isExisted(T model, OP_MODE opMode) {
    	log.debug("Checking if object is existed :"+model.getClass().getSimpleName()+" OP_MODE:"+opMode);
    	Criteria criteria = getSession().createCriteria(model.getClass());
    	
    	ClassMetadata catMeta = sessionFactory.getClassMetadata(model.getClass());  
		String[] propertyNames = catMeta.getPropertyNames();  
		Type[] propertyTypes = catMeta.getPropertyTypes();
		Object value;

		for (int i = 0; i < propertyNames.length; i++) {  
			value = catMeta.getPropertyValue(model, propertyNames[i]);
			if (value == null || (value instanceof Set && ((Set) value).size()==0)){
				continue;
			}
			
			if (value!=null && OP_MODE.EQUALS==opMode){
				criteria.add(Restrictions.eq(propertyNames[i], value));
			} else if (value!=null && OP_MODE.LIKE==opMode){
				criteria.add(Restrictions.like(propertyNames[i], value));	
			} else if (value!=null && OP_MODE.LE==opMode){
				criteria.add(Restrictions.le(propertyNames[i], value));	
			} else if (value!=null && OP_MODE.LT==opMode){
				criteria.add(Restrictions.lt(propertyNames[i], value));	
			} else if (value!=null && OP_MODE.GE==opMode){
				criteria.add(Restrictions.ge(propertyNames[i], value));	
			} else if (value!=null && OP_MODE.GT==opMode){
				criteria.add(Restrictions.gt(propertyNames[i], value));	
			} else if (value!=null && OP_MODE.NOT_EQUALS==opMode){
				criteria.add(Restrictions.ne(propertyNames[i], value));	
			} else if (value!=null && OP_MODE.CASE_SENSITIVE_LIKE==opMode){
				criteria.add(Restrictions.ilike(propertyNames[i], value));	
			}  
			//System.out.println("name=="+propertyNames[i] + ", type=="+propertyTypes[i]);  
		}
    	
		T result = (T) criteria.setMaxResults(1).setReadOnly(true).uniqueResult();
        if (result==null){
        	return null;
        } else {
        	return result;
        }

    }
    
    public <T extends Object> int countAll(Class<T> entityClass) {
    	log.debug("Counting all for class:"+entityClass.getSimpleName());
    	Criteria criteria = getSession().createCriteria(entityClass);
        criteria.setProjection(Projections.rowCount());
        return ((Long) criteria.uniqueResult()).intValue();
    }

    
    @SuppressWarnings("unchecked")
    public <T extends Object> List<T> listAll(Class<T> entityClass) {
    	log.debug("Listing all for class:"+entityClass.getSimpleName());
    	Criteria criteria = getSession().createCriteria(entityClass);
      
        return criteria.list();
    }
    


	/**
	 * This method will list all object that =/like/</> the attributes of this object provided. Criteria are connected with "AND". Filter by ID is not supported
	 * @param model
	 * @param opMode
	 * @return
	 */
	public <T> Long countAllByObject(T model, OP_MODE opMode) {
		log.debug("Counting all by object:"+model.getClass().getSimpleName()+" OP_MODE:"+opMode);
		Criteria criteria = getSession().createCriteria(model.getClass());

		ClassMetadata catMeta = sessionFactory.getClassMetadata(model.getClass());

		String[] propertyNames = catMeta.getPropertyNames();

		Object value;
//		if ( (OP_MODE.EQUALS_AND == opMode || OP_MODE.LIKE_AND)){
//			criteria.add(criterion)
//		}

		//below logic will not handle the id and value criteria
		for (int i = 0; i < propertyNames.length; i++) {
			value = catMeta.getPropertyValue(model, propertyNames[i]);
			//System.out.println("name=="+propertyNames[i] + ", type=="+propertyTypes[i]+" value="+value+" value==null"+(value==null));
			if (value == null || (value instanceof Set && ((Set) value).size()==0)){
				continue;
			}

			if (value!=null && OP_MODE.EQUALS==opMode){
				criteria.add(Restrictions.eq(propertyNames[i], value));
			} else if (value!=null && OP_MODE.LIKE==opMode){
				criteria.add(Restrictions.like(propertyNames[i], "%"+value+"%"));
			} else if (value!=null && OP_MODE.LE==opMode){
				criteria.add(Restrictions.le(propertyNames[i], value));
			} else if (value!=null && OP_MODE.LT==opMode){
				criteria.add(Restrictions.lt(propertyNames[i], value));
			} else if (value!=null && OP_MODE.GE==opMode){
				criteria.add(Restrictions.ge(propertyNames[i], value));
			} else if (value!=null && OP_MODE.GT==opMode){
				criteria.add(Restrictions.gt(propertyNames[i], value));
			} else if (value!=null && OP_MODE.NOT_EQUALS==opMode){
				criteria.add(Restrictions.ne(propertyNames[i], value));
			} else if (value!=null && OP_MODE.CASE_SENSITIVE_LIKE==opMode){
				criteria.add(Restrictions.ilike(propertyNames[i], "%"+value+"%"));
			}

		}


		return (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();

	}


    /**
     * Load list by property
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> List<T> hqlQuery(String hql, Map<String, Object> properties) {
    	 
    	return hqlQuery(hql, properties, 10000) ;
    }
    
    /**
     * Load list by property
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> List<T> hqlQuery(String hql, Map<String, Object> properties, int maxResult) {
    	if (maxResult<=0){
    		return new ArrayList<T>();
    	}
    	Query q = getSession().createQuery(hql).setMaxResults(maxResult);
    	for (String key : properties.keySet()){
    		q.setParameter(key, properties.get(key));
    	}
    	
    	return q.list();
    }

	/**
	 * Load list by property, will lock the result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> hqlQueryForUpdate(String hql, String lockTarget, Map<String, Object> properties, int maxResult) {
		if (maxResult<=0){
			return new ArrayList<T>();
		}
		Query q = getSession().createQuery(hql).setMaxResults(maxResult);
		q.setLockMode(lockTarget, LockMode.PESSIMISTIC_WRITE);
		for (String key : properties.keySet()){
			q.setParameter(key, properties.get(key));
		}

		return q.list();
	}
    
    /**
     * Execute Update or Delete. Return number of row being affected
     * @return
     */
    @SuppressWarnings("unchecked")
    public int hqlExecute(String hql, Map<String, Object> properties) {
    	 
    	Query q = getSession().createQuery(hql);
    	for (String key : properties.keySet()){
    		q.setParameter(key, properties.get(key));
    	}
    	
    	return q.executeUpdate(); 	
    }
   
    /**
     * Execute Update or Delete. Return number of row being affected for native SQL
     * @return
     */
    @SuppressWarnings("unchecked")
    public int sqlExecute(String sql, Map<Integer, Object> properties) {
    	SQLQuery q = getSession().createSQLQuery(sql);  
   
    	for (Integer key : properties.keySet()){
    		q.setParameter(key, properties.get(key));
    	}
    	
    	return q.executeUpdate(); 	
    }

    
    
    
    /**
     * Run delete HQL
     * @return
     */
    public <T extends Object> int hqlUpdate(String hql, Map<String, Object> properties) {
    	 
    	Query q = getSession().createQuery(hql);
    	for (String key : properties.keySet()){
    		q.setParameter(key, properties.get(key));
    	}
    	 
    	return q.executeUpdate();
    }

	/**
	 * This method will list all object that =/like/</> the attributes of this object provided. Criteria are connected with "AND". Filter by ID is not supported
	 * @param model
	 * @param opMode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Object> List<T> listAllByInstance(T model, OP_MODE opMode, Page page) {
		log.debug("Listing all by object:"+model.getClass().getSimpleName()+" OP_MODE:"+opMode);
		Criteria criteria = getSession().createCriteria(model.getClass());

		ClassMetadata catMeta = sessionFactory.getClassMetadata(model.getClass());

		String[] propertyNames = catMeta.getPropertyNames();
		Type[] propertyTypes = catMeta.getPropertyTypes();
		Object value;
//		if ( (OP_MODE.EQUALS_AND == opMode || OP_MODE.LIKE_AND)){
//			criteria.add(criterion)
//		}

		//below logic will not handle the id and value criteria
		for (int i = 0; i < propertyNames.length; i++) {
			value = catMeta.getPropertyValue(model, propertyNames[i]);

			//System.out.println("name:"+propertyNames[i]+" type:"+propertyTypes[i].getName());
			//System.out.println("name=="+propertyNames[i] + ", type=="+propertyTypes[i]+" value="+value+" value==null"+(value==null));
			if (value == null || (value instanceof Set && ((Set) value).size()==0)){
				continue;
			}
			if ("string".equals(propertyNames[i])) {
				if (value != null && OP_MODE.EQUALS == opMode) {
					criteria.add(Restrictions.eq(propertyNames[i], value));
				} else if (value != null && OP_MODE.LIKE == opMode) {
					criteria.add(Restrictions.like(propertyNames[i], "%" + value + "%"));
				} else if (value != null && OP_MODE.LE == opMode) {
					criteria.add(Restrictions.le(propertyNames[i], value));
				} else if (value != null && OP_MODE.LT == opMode) {
					criteria.add(Restrictions.lt(propertyNames[i], value));
				} else if (value != null && OP_MODE.GE == opMode) {
					criteria.add(Restrictions.ge(propertyNames[i], value));
				} else if (value != null && OP_MODE.GT == opMode) {
					criteria.add(Restrictions.gt(propertyNames[i], value));
				} else if (value != null && OP_MODE.NOT_EQUALS == opMode) {
					criteria.add(Restrictions.ne(propertyNames[i], value));
				} else if (value != null && OP_MODE.CASE_SENSITIVE_LIKE == opMode) {
					criteria.add(Restrictions.ilike(propertyNames[i], "%" + value + "%"));
				}
			} else {
				if (value != null) {
					criteria.add(Restrictions.eq(propertyNames[i], value));
				}
			}

		}
		criteria.setMaxResults(page.getRecordsPerPage().intValue());
		criteria.setFirstResult((page.getRequestedPage().intValue() - 1)*page.getRecordsPerPage().intValue());
		//set the order
		for (String cur: page.getOrder().keySet()){
			if (Page.ORDER.ASC == page.getOrder().get(cur)){
				criteria.addOrder(Order.asc(cur));
			} else {
				criteria.addOrder(Order.desc(cur));
			}
		}
		return criteria.list();
	}


}
