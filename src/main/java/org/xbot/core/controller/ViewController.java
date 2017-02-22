package org.xbot.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xbot.core.basement.ParamChecker;
import org.xbot.core.basement.ServiceResult;
import org.xbot.core.dao.CommonDAO;
import org.xbot.core.dao.Project;
import org.xbot.core.dao.Test;
import org.xbot.core.service.RecordService;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ViewController extends GenericController{



	@Autowired
	private RecordService recordService;

	/**
	 * Route the requests
	 * @param target
	 * @param model
	 * @return
	 */
	@RequestMapping("/router")
	public ModelAndView route(@RequestParam(value="target", required=false) String target, ModelAndView model) {

		 if ("project".equals(target)){
			 model.setViewName("redirect:/projects/search");
			 return model;
		 } else if ("test".equals(target)){
		 	model.setViewName("redirect:/tests/search");
		 	return model;
		 } else if ("record".equals(target)){
			 model.setViewName("redirect:"+Record_View);
			 return model;
		 } else if ("dashboard".equals(target)){
			 model.setViewName("redirect:"+Dashboard_Controller);
			 return model;
		 }
		model.addObject(MESSAGE, "Incorrect page requested!");
		return model;
	}
	//, )



	/**
	 * For adding the project
	 * @param projectCode
	 * @param projectName
	 * @param leader
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/projects/add" /*, method= RequestMethod.POST*/)
	public ModelAndView addProject(@RequestParam(value="projectCode", required=false) String projectCode,
								   @RequestParam(value="projectName", required=false) String projectName,
								   @RequestParam(value="projectCategory", required=false) String projectCategory,
								   @RequestParam(value="leader", required=false) String leader,
								   @RequestParam(value="status", required=false) String status,
								   @RequestParam(value="region", required=false) String region,
								   @RequestParam(value="country", required=false) String country,
								   ModelAndView model) {
		ParamChecker pc = new ParamChecker();
		model.setViewName(Project_View);
		model.addObject("projectCode", projectCode);
		model.addObject("projectName", projectName);
		model.addObject("projectCategory", projectCategory);
		model.addObject("leader", leader);
		model.addObject("status", status);
		model.addObject("region", region);
		model.addObject("country", country);
		model.addObject("activeTab", "1");
		if (!pc.isNotEmpty(projectCode)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project Code is not provided");
			return model;
		} else if (!pc.isNotEmpty(region)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Region is not provided");
			return model;
		} else if (!pc.isFollowPattern(pc.COUNTRY_CODE, country)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Country code should be 2 capital characters, such as HK, UK, US...");
			return model;
		} else if (!pc.isNotEmpty(projectName)){

			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project Name is not provided");
			return model;
		} else if (!pc.isNotEmpty(leader)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Leader LN is not provided");
			return model;
		} else if (!pc.isNotEmpty(projectCategory)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project Category is not provided");
			return model;
		} else if (!Project.STATUS.Active.toString().equals(status) && !Project.STATUS.Inactive.toString().equals(status) && !Project.STATUS.ToBeDeleted.toString().equals(status)) {
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project status is incorrect. If this keeps happening, please find system admin.");
			return model;
		}
		Project p = new Project();

		p.setProjectCode(projectCode);
		//check if project code is unique in DB
		ServiceResult result = recordService.exists(p);
		if (result.getResult()){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "The project code had been registered before. "+((Project)result.getReturnObject()));
			return model;
		}
		p.setCategory(projectCategory);
		p.setCountry(country);
		p.setRegion(region);
		p.setProjectName(projectName);
		p.setLeader(leader);
		p.setCreatedTime(new Timestamp(System.currentTimeMillis()));
		p.setStatus(Project.STATUS.valueOf(status));
		result = recordService.save(p);
 		if (result.getResult()){
			model.addObject(MESSAGE, "Project have been created");
			model.addObject(RESULT, true);
		} else {
			model.addObject(MESSAGE, "Project creation failed. Error: " + result.getMessage());
			model.addObject(RESULT, false);
		}

		return model;
	}

	@RequestMapping(value = "/projects/search")
	public ModelAndView searchProject(
			@RequestParam(value="projectId", required=false) String projectId,
			@RequestParam(value="projectCode", required=false) String projectCode,
			@RequestParam(value="projectName", required=false) String projectName,
			@RequestParam(value="projectCategory", required=false) String projectCategory,
			@RequestParam(value="leader", required=false) String leader,
			@RequestParam(value="status", required=false) String status,
			@RequestParam(value="region", required=false) String region,
			@RequestParam(value="country", required=false) String country,
			ModelAndView model) {

		ParamChecker pc = new ParamChecker();
		model.setViewName(Project_View);
		model.addObject("projectId", projectId);
		model.addObject("projectCode", projectCode);
		model.addObject("projectName", projectName);
		model.addObject("leader", leader);
		model.addObject("status", status);
		model.addObject("region", region);
		model.addObject("country", country);
		List<Project> projectList = new ArrayList<>();
		model.addObject("projectList", projectList);
		model.addObject("activeTab", "0");
		if (!pc.isNotEmpty(projectCode) && !pc.isNotEmpty(projectName) && !pc.isNotEmpty(leader)  && !pc.isNotEmpty(projectCategory) && (!Project.STATUS.Active.toString().equals(status) && !Project.STATUS.Inactive.toString().equals(status) && !Project.STATUS.ToBeDeleted.toString().equals(status))){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Please provide information of project to search.");
			return model;
		}
		if (status!=null && status.length()>0 &&  !Project.STATUS.Active.toString().equals(status) && !Project.STATUS.Inactive.toString().equals(status) && !Project.STATUS.ToBeDeleted.toString().equals(status)) {
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project status is incorrect. If this keeps happening, please find system admin.");
			return model;
		}

		Project reference = new Project();
		if (pc.isNotEmpty(projectId)){
			reference.setId(projectId);
		}
		if (pc.isNotEmpty(leader)){
			reference.setLeader(leader);
		}
		if (pc.isNotEmpty(projectCode)){
			reference.setProjectCode(projectCode);
		}
		if (pc.isNotEmpty(projectName)){
			reference.setProjectName(projectName);
		}
		if (pc.isNotEmpty(projectCategory)){
			reference.setCategory(projectCategory);
		}
		if (pc.isNotEmpty(region)){
			reference.setRegion(region);
		}
		if (pc.isNotEmpty(country)){
			reference.setCountry(country);
		}
		if (pc.isNotEmpty(status)){
			reference.setStatus(Project.STATUS.valueOf(status));
		} else {
			reference.setStatus(null);
		}

		ServiceResult result = recordService.searchByInstance(reference, CommonDAO.OP_MODE.LIKE);
		if (result.getResult()){
			//passed
			projectList = (List<Project>) result.getReturnObject();
			if (projectList!=null && projectList.size()==0) {
				model.addObject(MESSAGE, "No result matched.");
			}
			model.addObject("projectList", projectList);
			model.addObject(RESULT, true);

		} else {
			//failed
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project list is empty. "+result.getMessage());
		}
		return model;
	}
	/**
	 * For updating the project
	 * @param projectCode
	 * @param projectName
	 * @param leader
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/projects/update" , method= RequestMethod.POST)
	public ModelAndView updateProject(
			@RequestParam(value="projectId", required=false) String projectId,
		   @RequestParam(value="projectCode", required=false) String projectCode,
		   @RequestParam(value="projectName", required=false) String projectName,
			@RequestParam(value="projectCategory", required=false) String projectCategory,
		   @RequestParam(value="leader", required=false) String leader,
			@RequestParam(value="region", required=false) String region,
			@RequestParam(value="country", required=false) String country,
		   @RequestParam(value="status", required=false) String status,ModelAndView model) {
		ParamChecker pc = new ParamChecker();
		model.setViewName(Project_View);
		model.addObject("projectCode", projectCode);
		model.addObject("projectName", projectName);
		model.addObject("leader", leader);
		model.addObject("status", status);
		model.addObject("activeTab", "0");
		if (!pc.isFollowPattern(pc.UUID, projectId)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project ID is not provided or not following format.");
			return model;
		} else if (!pc.isNotEmpty(projectCode)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project Code is not provided");
			return model;
		} else if (!pc.isNotEmpty(projectName)){

			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project Name is not provided");
			return model;
		} else if (!pc.isNotEmpty(leader)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Leader LN is not provided");
			return model;
		} else if (!Project.STATUS.Active.toString().equals(status) && !Project.STATUS.Inactive.toString().equals(status) && !Project.STATUS.ToBeDeleted.toString().equals(status)) {
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project status is incorrect. If this keeps happening, please find system admin.");
			return model;
		} else if (!pc.isNotEmpty(projectCategory)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project category is not provided. It should describe the main technology of this project.");
			return model;
		}
		Project p = new Project();
		p.setProjectCode(projectCode);

		//check if project code is unique in DB
		ServiceResult result = recordService.exists(p);
		if (!result.getResult()){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "The project code had not been registered before. "+((Project)result.getReturnObject()));
			return model;
		}
		p = (Project) result.getReturnObject();
		p.setProjectName(projectName);
		p.setProjectCode(projectCode);
		p.setCategory(projectCategory);
		p.setCountry(country);
		p.setRegion(region);
		p.setLeader(leader);
		p.setCreatedTime(new Timestamp(System.currentTimeMillis()));
		p.setStatus(Project.STATUS.valueOf(status));
		result = recordService.update(p);
		if (result.getResult()){
			model.addObject(MESSAGE, "Project have been created");
			model.addObject(RESULT, true);
		} else {
			model.addObject(MESSAGE, "Project creation failed. Error: "+ result.getMessage());
			model.addObject(RESULT, false);
		}

		return model;
	}



	/**
	 * For adding the test

	 * @return
	 */
	@RequestMapping(value = "/tests/add" /*, method= RequestMethod.POST*/)
	public ModelAndView addTest(@RequestParam(value="name", required=false) String name,
								   @RequestParam(value="description", required=false) String description,
								   @RequestParam(value="manualExecutionTime", required=false) String manualExecutionTime,
								   @RequestParam(value="projectId", required=false) String projectId,
								   ModelAndView model) {
		ParamChecker pc = new ParamChecker();
		model.setViewName(Test_View);
		model.addObject("name", name);
		model.addObject("description", description);
		model.addObject("manualExecutionTime", manualExecutionTime);
		model.addObject("projectId", projectId);
		model.addObject("activeTab", "1");
		Project p = new Project();
		p.setStatus(Project.STATUS.Active);
		ServiceResult result = recordService.searchByInstance(p, CommonDAO.OP_MODE.EQUALS);
		List<Project> projectList;
		if (result.getResult()){
			projectList = (List<Project>) result.getReturnObject();
		} else {
			projectList = new ArrayList<>();
		}
		model.addObject("projectList", projectList);
		if (!pc.isNotEmpty(name)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Test Name is not provided");
			return model;
		} else if (!pc.isNotEmpty(manualExecutionTime)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Estimated manual execution time of this test case is not provided.");
			return model;
		} else if (!pc.isNotEmpty(projectId)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project is not provided");
			return model;
		}
		Project objProject = recordService.getById(Project.class, projectId);
		Test obj = new Test();
		obj.setName(name);
		obj.setProject(objProject);
		//check if test.name of same project is defined in DB
		result = recordService.exists(obj);
		if (result.getResult()){
			model.addObject(RESULT, false);
			if (result.getReturnObject()!=null) {
				model.addObject(MESSAGE, "The test name had been registered before. " + ((Test) result.getReturnObject()));
			} else {
				model.addObject(MESSAGE, "The test name had been registered before.");
			}
			return model;
		}
		if (description != null){
			obj.setDescription(description);
		}
		long manualExecutionTimeLong = 0;
		try {
			manualExecutionTimeLong = Long.valueOf(manualExecutionTime.trim());
			obj.setManualExecutionTime(manualExecutionTimeLong);
		} catch (Exception e){
			log.error("Failed to parse manualExecutionTime: "+manualExecutionTime +" Error:",e);
		}
		obj.setCreatedTime(new Timestamp(System.currentTimeMillis()));

		result = recordService.save(obj);

		if (result.getResult()){
			model.addObject(MESSAGE, "Test has been created");
			model.addObject(RESULT, true);
		} else {
			model.addObject(MESSAGE, "Test creation failed. Error: " + result.getMessage());
			model.addObject(RESULT, false);
		}
		return model;
	}

	@RequestMapping(value = "/tests/search")
	public ModelAndView searchProject(
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="description", required=false) String description,
			@RequestParam(value="manualExecutionTime", required=false) String manualExecutionTime,
			@RequestParam(value="projectId", required=false) String projectId, ModelAndView model) {

		ParamChecker pc = new ParamChecker();
		model.setViewName(Test_View);
		model.addObject("id", id);
		model.addObject("name", name);
		model.addObject("description", description);
		model.addObject("manualExecutionTime", manualExecutionTime);
		model.addObject("projectId", projectId);
		model.addObject("activeTab", "1");
		List<Test> objList = new ArrayList<>();
		model.addObject("objList", objList);
		Project p = new Project();
		p.setStatus(Project.STATUS.Active);
		ServiceResult result = recordService.searchByInstance(p, CommonDAO.OP_MODE.EQUALS);
		List<Project> projectList;
		if (result.getResult()){
			projectList = (List<Project>) result.getReturnObject();
		} else {
			projectList = new ArrayList<>();
		}
		model.addObject("projectList", projectList);
		model.addObject("activeTab", "0");
		if (!pc.isNotEmpty(id) && !pc.isNotEmpty(name) && !pc.isNotEmpty(description) && !pc.isNotEmpty(manualExecutionTime)  && !pc.isNotEmpty(projectId)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Please provide information of project to search.");
			return model;
		}

		Test obj = new Test();
		if (pc.isNotEmpty(id)){
			obj.setId(id);
		}
		if (pc.isNotEmpty(name)){
			obj.setName(name);
		}
		if (pc.isNotEmpty(description)){
			obj.setDescription(description);
		}
		obj.setManualExecutionTime(null);
		if (pc.isNotEmpty(manualExecutionTime)){
			Long manualExecutionTimeLong = 0L;
			try {
				manualExecutionTimeLong = Long.valueOf(manualExecutionTime.trim());
				obj.setManualExecutionTime(manualExecutionTimeLong);
			} catch (Exception e){
				e.printStackTrace();
				log.error("Failed to parse manualExecutionTime: "+manualExecutionTime +" Error:",e);
			}
		}
		Project objProject=null;
		if (pc.isNotEmpty(projectId)){
			objProject = recordService.getById(Project.class, projectId);
			obj.setProject(objProject);
		} else {
			obj.setProject(null);
		}
		objList = recordService.searchTest(obj);
		if (objList != null && objList.size()>0){


			model.addObject("objList", objList);
			model.addObject(RESULT, true);

		} else if (objList!=null && objList.size()==0) {
			model.addObject(MESSAGE, "No result matched.");
		} else {
			//failed
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Test list is empty. "+result.getMessage()==null?"":result.getMessage());
		}
		return model;
	}

	/**
	 * * For updating the test
	 * @param id
	 * @param name
	 * @param description
	 * @param manualExecutionTime
	 * @param projectId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/tests/update" , method= RequestMethod.POST)
	public ModelAndView updateTest(
			@RequestParam(value="id", required=false) String id,
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="description", required=false) String description,
			@RequestParam(value="manualExecutionTime", required=false) String manualExecutionTime,
			@RequestParam(value="projectId", required=false) String projectId,ModelAndView model) {
		ParamChecker pc = new ParamChecker();
		model.setViewName(Test_View);
		model.addObject("id", id);
		model.addObject("name", name);
		model.addObject("description", description);
		model.addObject("manualExecutionTime", manualExecutionTime);
		model.addObject("projectId", projectId);
		model.addObject("activeTab", "0");
		Project p = new Project();
		p.setStatus(Project.STATUS.Active);
		ServiceResult result = recordService.searchByInstance(p, CommonDAO.OP_MODE.EQUALS);
		List<Project> projectList;
		if (result.getResult()){
			projectList = (List<Project>) result.getReturnObject();
		} else {
			projectList = new ArrayList<>();
		}
		//System.out.println("projectLId:"+projectId);
		model.addObject("projectList", projectList);
		if (!pc.isFollowPattern(pc.UUID, projectId)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project ID is not provided or not following format.");
			return model;
		} if (!pc.isFollowPattern(pc.UUID, id)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Test ID is not provided or not following format.");
			return model;
		} else if (!pc.isNotEmpty(name)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Test Name is not provided");
			return model;
		} else if (!pc.isNotEmpty(manualExecutionTime)){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Test case manual execution minute is not provided");
			return model;
		}
		Test obj = new Test();

		Project objProject = recordService.getById(Project.class, projectId);
		obj.setProject(objProject);
		obj.setId(id);
		//check if project code is unique in DB
		result = recordService.exists(obj);
		if (!result.getResult()){
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "The test had not been registered before. "+((Project)result.getReturnObject()));
			return model;
		}
		obj = (Test) result.getReturnObject();
		obj.setDescription(description);
		if (pc.isNotEmpty(manualExecutionTime)){
			long manualExecutionTimeLong = 0;
			try {
				manualExecutionTimeLong = Long.valueOf(manualExecutionTime.trim());
				obj.setManualExecutionTime(manualExecutionTimeLong);
			} catch (Exception e){
				log.error("Failed to parse manualExecutionTime: "+manualExecutionTime +" Error:",e);
			}
		}
		obj.setName(name);
		obj.setDescription(description);
		result = recordService.update(obj);
		if (result.getResult()){
			model.addObject(MESSAGE, "Test has been created");
			model.addObject(RESULT, true);
		} else {
			model.addObject(MESSAGE, "Test creation failed. Error: "+ result.getMessage());
			model.addObject(RESULT, false);
		}

		return model;
	}
}
