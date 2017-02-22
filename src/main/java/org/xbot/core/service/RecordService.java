package org.xbot.core.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xbot.core.basement.ConfigConst;
import org.xbot.core.basement.ParamChecker;
import org.xbot.core.basement.ServiceResult;
import org.xbot.core.dao.*;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("taskService")
public class RecordService {

	
	@Resource(name="configConst")
	private ConfigConst configConst;

	@Resource(name="commonDAO")
	private CommonDAO dao;
	
	private Logger log = LogManager.getLogger("dashboard");


	/*
         * This service will save the instance in database
         */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ServiceResult exists(final Object obj){
		ServiceResult result = new ServiceResult();
		Object temp;
		try{
			temp = dao.isExisted(obj);
			if (temp != null){
				result.setResult(true);
				result.setReturnObject(temp);
			} else {
				result.setResult(false);
			}
		} catch (Exception e){
			result.setResult(false);
			result.setMessage("Error:"+e);
			log.error("Error checking if object is unique: ", e);
		}
		return result;
	}


		/*
         * This service will save the instance in database
         */
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResult save(final Object obj){
		ServiceResult result = new ServiceResult();
		log.debug("Saving :"+obj);
		try{
			dao.save(obj);
			result.setResult(true);
			result.setReturnObject(obj);
		} catch (Exception e){
			result.setResult(false);
			result.setMessage("Error:"+e);
			log.error("Error saving the object: ", e);
		}
		return result;
	}
	/*
             * This service will update the instance in database
             */
	@Transactional(propagation = Propagation.REQUIRED)
	public ServiceResult update(final Object obj){
		ServiceResult result = new ServiceResult();
		log.debug("Updating :"+obj);
		try{
			dao.update(obj);
			result.setResult(true);
			result.setReturnObject(obj);
		} catch (Exception e){
			result.setResult(false);
			result.setMessage("Error:"+e);
			log.error("Error updating the object: ", e);
		}
		return result;
	}


	/*
         * List all object by the reference model
         */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public ServiceResult searchByInstance(final Object obj, CommonDAO.OP_MODE opMode){
		ServiceResult result = new ServiceResult();
		List temp = new ArrayList<>();
		Page page = new Page();
		page.setFirstPage(1l);
		page.setLastPage(1l);
		page.setCurrentPage(1L);
		page.setRecordsPerPage(10000L);
		page.setRequestedPage(1l);

		try{

			temp = dao.listAllByInstance(obj, opMode, page);

			if (temp != null && temp.size()>0){
				result.setResult(true);
				result.setReturnObject(temp);
			} else {
				result.setResult(false);
			}
		} catch (Exception e){
			result.setResult(false);
			result.setMessage("Error:"+e);
			log.error("Error searching for object list: ", e);
			e.printStackTrace();
		}
		return result;
	}

	/*
         * Find the latest incompleted record
         */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Record findIncompletedRecordById(final String testId, String tokenId, String source){
		List<Record> temp = null;

		try{
			Criteria criteria = dao.getSession().createCriteria(Record.class);
			criteria.add(Restrictions.eq("test.id", testId));
			if (tokenId!=null && tokenId.length()>0){
				criteria.add(Restrictions.eq("tokenId", tokenId));
			}
			if (source!=null && source.length()>0){
				criteria.add(Restrictions.eq("source", source));
			}
			criteria.add(Restrictions.isNull("endTime"));
			criteria.add(Restrictions.isNotNull("startTime"));
			criteria.addOrder(Order.desc("startTime"));
			//criteria.setMaxResults(1);

			temp = criteria.list();
			if (temp != null && temp.size()>0){
				//update all subsequent as incompleted
				for (int i=1; i< temp.size(); i++){
					temp.get(i).setEndTime(new Timestamp(System.currentTimeMillis()));
					temp.get(i).setResult(Record.Result.Other);
					dao.update(temp.get(i));
				}
				return temp.get(0);
			}
		} catch (Exception e){

			log.error("Error searching for incompleted record ", e);

		}
		return null;
	}


	/*
         * Find the latest incompleted record
         */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Record> listRecord(Record record, Timestamp startDate, Timestamp endDate){
		List<Record> result = null;

		try{

			Criteria c = dao.getSession().createCriteria(Record.class,"r");
			c.createAlias("r.test", "t").createAlias("r.test.project", "p");
			c.add(Restrictions.ge("r.endTime", startDate));
			c.add(Restrictions.le("r.endTime", endDate));
			//Criteria c = tempC2.createCriteria("Test");

					//.add(Restrictions.eqProperty("r.testId","t.id"))
					//.add(Restrictions.eqProperty("t.projectId","p.id"));
			//for project properties
			ParamChecker pc = new ParamChecker();
			if (record!=null && record.getTest()!=null && record.getTest().getProject()!=null){
				Project project = record.getTest().getProject();
				if (pc.isFollowPattern(pc.UUID, project.getId())){
					c.add(Restrictions.eq("p.id", project.getId()));
				}
				if (pc.isNotEmpty(project.getCategory())){
					c.add(Restrictions.eq("p.category", project.getCategory()));
				}
				if (pc.isNotEmpty(project.getCountry())){
					c.add(Restrictions.eq("p.country", project.getCountry()));
				}
				if (pc.isNotEmpty(project.getLeader())){
					c.add(Restrictions.eq("p.leader", project.getLeader()));
				}
				if (pc.isNotEmpty(project.getProjectCode())){
					c.add(Restrictions.eq("p.projectCode", project.getProjectCode()));
				}
				if (pc.isNotEmpty(project.getProjectName())){
					c.add(Restrictions.eq("p.projectName", project.getProjectName()));
				}
				if (pc.isNotEmpty(project.getRegion())){
					c.add(Restrictions.eq("p.region", project.getRegion()));
				}
				if (project.getStatus()!=null){
					c.add(Restrictions.eq("p.status", project.getStatus()));
				}
			}
			if (record!=null && record.getTest()!=null){
				Test t = record.getTest();
				if (pc.isFollowPattern(pc.UUID, t.getId())){
					c.add(Restrictions.eq("t.id", t.getId()));
				}
				if (pc.isNotEmpty(t.getName())){
					c.add(Restrictions.eq("t.name", t.getName()));
				}
				if (pc.isNotEmpty(t.getDescription())){
					c.add(Restrictions.eq("t.description", t.getDescription()));
				}
				if (t.getManualExecutionTime()!=null){
					c.add(Restrictions.eq("t.manualExecutionTime", t.getManualExecutionTime()));
				}
			}
//			c.add(Restrictions.isNull("endTime"));
//			c.add(Restrictions.isNotNull("startTime"));
//			c.addOrder(Order.desc("startTime"));
			//criteria.setMaxResults(1);

			result = c.list();
			if (result != null && result.size()>0){
				return result;
			}
		} catch (Exception e){
			e.printStackTrace();
			log.error("Error searching for incompleted record ", e);

		}
		return null;
	}
	/*
         * load the instance by id
         */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public <T> T getById(final Class<T> obj, final String id){
		return dao.get(obj, id);
	}


	/*
         * Find the test
         */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Test> searchTest(final Test test){
		List<Test> result = null;
		try{
			Criteria c = dao.getSession().createCriteria(Test.class,"t");
			//Criteria c = tempC2.createCriteria("Test");

			//.add(Restrictions.eqProperty("r.testId","t.id"))
			//.add(Restrictions.eqProperty("t.projectId","p.id"));
			//for project properties
			ParamChecker pc = new ParamChecker();
			Project project = test.getProject();
			if (project != null) {
				c.createAlias("t.project", "p");

				if (pc.isFollowPattern(pc.UUID, project.getId())) {
					c.add(Restrictions.eq("p.id", project.getId()));
				}
				if (pc.isNotEmpty(project.getCategory())) {
					c.add(Restrictions.eq("p.category", project.getCategory()));
				}
				if (pc.isNotEmpty(project.getCountry())) {
					c.add(Restrictions.eq("p.country", project.getCountry()));
				}
				if (pc.isNotEmpty(project.getLeader())) {
					c.add(Restrictions.eq("p.leader", project.getLeader()));
				}
				if (pc.isNotEmpty(project.getProjectCode())) {
					c.add(Restrictions.eq("p.projectCode", project.getProjectCode()));
				}
			}
			if (pc.isFollowPattern(pc.UUID, test.getId())){
				c.add(Restrictions.ilike("id", "%"+test.getId()+"%"));
			}
			if (pc.isNotEmpty(test.getName())){
				c.add(Restrictions.ilike("name", "%"+test.getName()+"%"));
			}
			if (pc.isNotEmpty(test.getDescription())){
				c.add(Restrictions.ilike("description", "%"+test.getDescription()+"%"));
			}

//			c.add(Restrictions.isNull("endTime"));
//			c.add(Restrictions.isNotNull("startTime"));
//			c.addOrder(Order.desc("startTime"));
			//criteria.setMaxResults(1);

			result = c.list();
			if (result != null && result.size()>0){
				return result;
			}
		} catch (Exception e){
			e.printStackTrace();
			log.error("Error searching for test ", e);

		}
		return null;
	}


}
