package org.xbot.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xbot.core.basement.ParamChecker;
import org.xbot.core.bean.CategoryView;
import org.xbot.core.bean.KPIView;
import org.xbot.core.bean.RegionView;
import org.xbot.core.dao.Project;
import org.xbot.core.dao.Record;
import org.xbot.core.dao.Test;
import org.xbot.core.service.RecordService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class DashboardController extends GenericController{


	@Autowired
	private RecordService recordService;

	/**
	 * For adding the project
	 * @param projectCode
	 * @param leader
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dashboards")
	public ModelAndView getDashboard(@RequestParam(value="projectCode", required=false) String projectCode,
									 @RequestParam(value="projectName", required=false) String projectName,
								   @RequestParam(value="region", required=false) String region,
								   @RequestParam(value="country", required=false) String country,
								   @RequestParam(value="leader", required=false) String leader,
								   @RequestParam(value="category", required=false) String category,
								   @RequestParam(value="status", required=false) String status,
								   @RequestParam(value="projectCategory", required=false) String projectCategory,
									 @RequestParam(value="startDate", required=false) String startDate,
									 @RequestParam(value="endDate", required=false) String endDate,
									 ModelAndView model) {
		ParamChecker pc = new ParamChecker();
		model.setViewName(Dashboard_View);
		Calendar c = Calendar.getInstance();

		Timestamp startDt = null;
		Timestamp endDt = null;
		//System.out.println("startDate:"+startDate+" endDate:"+endDate+"|"+pc.isNotEmpty(endDate));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {

			if (pc.isNotEmpty(endDate)){
				endDt = new Timestamp(sdf.parse(endDate).getTime());
			} else {
				endDt =new Timestamp(c.getTimeInMillis());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (pc.isNotEmpty(startDate)){
				startDt = new Timestamp(sdf.parse(startDate).getTime());
			} else {
				c.add(Calendar.MONDAY, -6);
				startDt = new Timestamp(c.getTimeInMillis());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, CategoryView> categoryMap = new HashMap<>();
		Map<String, RegionView> regionMap = new HashMap<>();

		model.addObject("startDate", startDate);
		model.addObject("endDate", endDate);
		model.addObject("projectCode", projectCode);
		model.addObject("projectName", projectName);
		model.addObject("projectCategory", projectCategory);
		model.addObject("leader", leader);
		model.addObject("status", status);
		model.addObject("region", region);
		model.addObject("country", country);
		model.addObject("activeTab", "1");
		model.addObject("categoryMap", categoryMap);
		model.addObject("regionMap", regionMap);
//		if (country!=null && country.length()>0  && !pc.isFollowPattern(pc.COUNTRY_CODE, country)){
//			model.addObject(RESULT, false);
//			model.addObject(MESSAGE, "Country code should be 2 capital characters, such as HK, UK, US...");
//			return model;
//		} else
		if (status!=null && status.length()>0  && !Project.STATUS.Active.toString().equals(status) && !Project.STATUS.Inactive.toString().equals(status) && !Project.STATUS.ToBeDeleted.toString().equals(status)) {
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Project status is incorrect. If this keeps happening, please find system admin.");
			return model;
		}
		Project p = new Project();
		if (pc.isNotEmpty(country)){
			p.setCountry(country);
		}
		if (pc.isNotEmpty(leader)){
			p.setLeader(leader);
		}
		if (pc.isNotEmpty(projectCode)){
			p.setProjectCode(projectCode);
		}
		if (pc.isNotEmpty(category)){
			p.setCategory(category);
		}
		Test t = new Test();
		t.setProject(p);
		Record r = new Record();
		r.setTest(t);
		//r.setResult(Record.Result.Success);

		List<Record> result = recordService.listRecord(r, startDt, endDt);
		if (result == null || (result!=null && result.size()==0)){
			return model;
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDt);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDt);


		long totalAutoExecutionTimeInMs = 0l;//ms, the total time
		long totalManualExecutionTime = 0l;//minutes, the manual total time

		String curCategory;
		String curProject;
		String curRegion;
		String curCountry;

		KPIView curProjectKpiView = null;
		KPIView curCountryKpiView = null;
		KPIView curMonthKpiView = null;
		CategoryView curCategoryView = null;
		RegionView curRegionView = null;
		String curMonthStr = null;
		Calendar curMonthCalendar = Calendar.getInstance();
		for (Record cur : result){
			curMonthStr = null;
			//successful records
			curCategory = cur.getTest().getProject().getCategory();
			curProject = cur.getTest().getProject().getProjectCode();
			curRegion = cur.getTest().getProject().getRegion();
			curCountry = cur.getTest().getProject().getCountry();
			//init category container
			if (categoryMap.get(curCategory)==null){
				//new a category
				CategoryView cv = new CategoryView();
				cv.setCategory(curCategory);
				categoryMap.put(curCategory, cv);
			}
			curCategoryView = categoryMap.get(curCategory);

			if (curCategoryView.getProjects().get(curProject)==null){
				curProjectKpiView = new KPIView();
				curCategoryView.getProjects().put(curProject, curProjectKpiView);
			}
			curProjectKpiView = curCategoryView.getProjects().get(curProject);

			//init region container
			if (regionMap.get(curRegion)==null){
				//new a category
				RegionView rv = new RegionView();
				Map<String, KPIView> monthsTemplate = new LinkedHashMap<>();
				//init the month KPIView
				Calendar curC = Calendar.getInstance();
				for (curC.setTime(startCalendar.getTime()); !curC.after(endCalendar); curC.add(Calendar.MONTH,1)){
					monthsTemplate.put(curC.get(Calendar.YEAR)+"-"+(curC.get(Calendar.MONTH)+1), new KPIView());
				}
				rv.setMonths(monthsTemplate);
				rv.setRegion(curRegion);
				regionMap.put(curRegion, rv);
			}
			curRegionView = regionMap.get(curRegion);

			if (curRegionView.getCountries().get(curCountry)==null){
				curCountryKpiView = new KPIView();
				curRegionView.getCountries().put(curCountry, curCountryKpiView);
			}
			curCountryKpiView = curRegionView.getCountries().get(curCountry);

			//process the report data
			//update test case count
			curProjectKpiView.setTestcaseNumber(curProjectKpiView.getTestcaseNumber()+1);
			curCountryKpiView.setTestcaseNumber(curCountryKpiView.getTestcaseNumber()+1);


			if (Record.Result.Success==cur.getResult()){
				if (cur.getEndTime()!=null){
					//handle the month KPIview
					curMonthCalendar.setTime(cur.getEndTime());
					curMonthStr = curMonthCalendar.get(Calendar.YEAR)+"-"+(curMonthCalendar.get(Calendar.MONTH)+1);

					curMonthKpiView = curRegionView.getMonths().get(curMonthStr);

					curMonthKpiView.setSuccessTestcaseNumber(curMonthKpiView.getSuccessTestcaseNumber()+1);
					curMonthKpiView.setTestcaseNumber(curMonthKpiView.getTestcaseNumber()+1);
					if (curMonthKpiView.getTestcaseNumber()>0) {
						curMonthKpiView.setSuccessRate(curMonthKpiView.getSuccessTestcaseNumber()*100 / curMonthKpiView.getTestcaseNumber());
					}
					if (cur.getStartTime()!=null) {
						curMonthKpiView.setAutoExecutionTime(curMonthKpiView.getAutoExecutionTime() + (cur.getEndTime().getTime() - cur.getStartTime().getTime()) / 60000);
					}
					curMonthKpiView.setManualExecutionTime(curMonthKpiView.getManualExecutionTime()+cur.getTest().getManualExecutionTime());

					curRegionView.getMonths().put(curMonthStr, curMonthKpiView);

				}
				//update auto time
				if (cur.getEndTime()!=null && cur.getStartTime()!=null && (cur.getEndTime().getTime()-cur.getStartTime().getTime()>0)){
					curProjectKpiView.setAutoExecutionTime(curProjectKpiView.getAutoExecutionTime()+(cur.getEndTime().getTime()-cur.getStartTime().getTime()) / 60000);
					curCountryKpiView.setAutoExecutionTime(curCountryKpiView.getAutoExecutionTime()+(cur.getEndTime().getTime()-cur.getStartTime().getTime()) / 60000);
					totalAutoExecutionTimeInMs += cur.getEndTime().getTime()-cur.getStartTime().getTime();
				}
				//update manual time
				if (cur.getTest()!=null){
					totalManualExecutionTime += cur.getTest().getManualExecutionTime();
					curProjectKpiView.setManualExecutionTime(curProjectKpiView.getManualExecutionTime()+cur.getTest().getManualExecutionTime());
					curCountryKpiView.setManualExecutionTime(curCountryKpiView.getManualExecutionTime()+cur.getTest().getManualExecutionTime());
				}
				//update the success test case count
				curProjectKpiView.setSuccessTestcaseNumber(curProjectKpiView.getSuccessTestcaseNumber()+1);
				curCountryKpiView.setSuccessTestcaseNumber(curCountryKpiView.getSuccessTestcaseNumber()+1);


			}
			//update success rate
			curProjectKpiView.setSuccessRate(curProjectKpiView.getSuccessTestcaseNumber()*100 / curProjectKpiView.getTestcaseNumber());
			curCountryKpiView.setSuccessRate(curCountryKpiView.getSuccessTestcaseNumber()*100 / curCountryKpiView.getTestcaseNumber());

			//update region list
			curCategoryView.getProjects().put(curProject, curProjectKpiView);
			curRegionView.getCountries().put(curCountry, curCountryKpiView);
			categoryMap.put(curCategory, curCategoryView);

			regionMap.put(curRegion, curRegionView);


			if (cur.getTest()!=null){
				totalManualExecutionTime += cur.getTest().getManualExecutionTime();
			}

		}
		//summarize for Category and Region
		CategoryView curC;
		KPIView categoryKpi;
		RegionView curR;
		KPIView regionKpi;
		KPIView curKpi;
		KPIView monthKpi;
		for (String key : categoryMap.keySet()){
			categoryKpi = new KPIView();
			curC = categoryMap.get(key);
			for (String projectKey : curC.getProjects().keySet()){
				curKpi = curC.getProjects().get(projectKey);
				categoryKpi.setAutoExecutionTime(categoryKpi.getAutoExecutionTime()+curKpi.getAutoExecutionTime());
				categoryKpi.setManualExecutionTime(categoryKpi.getManualExecutionTime()+curKpi.getManualExecutionTime());
				categoryKpi.setTestcaseNumber(categoryKpi.getTestcaseNumber()+curKpi.getTestcaseNumber());
				categoryKpi.setSuccessTestcaseNumber(categoryKpi.getSuccessTestcaseNumber()+curKpi.getSuccessTestcaseNumber());
				//categoryKpi.setDefectNumner();
				//categoryKpi.setTeamConfidence();
			}
			if (categoryKpi.getTestcaseNumber()>0){
				categoryKpi.setSuccessRate(categoryKpi.getSuccessTestcaseNumber()/categoryKpi.getTestcaseNumber());
			}
			categoryMap.get(key).setKpiView(categoryKpi);
		}
		for (String key : regionMap.keySet()){
			regionKpi = new KPIView();
			curR = regionMap.get(key);
			//curR.setMonths(new LinkedHashMap<>(monthsTemplate));
			for (String countryKey : curR.getCountries().keySet()){
				curKpi = curR.getCountries().get(countryKey);
				regionKpi.setAutoExecutionTime(regionKpi.getAutoExecutionTime()+curKpi.getAutoExecutionTime());
				regionKpi.setManualExecutionTime(regionKpi.getManualExecutionTime()+curKpi.getManualExecutionTime());
				regionKpi.setTestcaseNumber(regionKpi.getTestcaseNumber()+curKpi.getTestcaseNumber());
				regionKpi.setSuccessTestcaseNumber(regionKpi.getSuccessTestcaseNumber()+curKpi.getSuccessTestcaseNumber());
				//regionKpi.setDefectNumner();
				//regionKpi.setTeamConfidence();
			}
			if (regionKpi.getTestcaseNumber()>0){
				regionKpi.setSuccessRate(regionKpi.getSuccessTestcaseNumber()/regionKpi.getTestcaseNumber());
			}
			regionMap.get(key).setKpiView(regionKpi);
		}

		model.addObject("categoryMap", categoryMap);
		model.addObject("regionMap", regionMap);


		long savedTime = 0;
		if (totalAutoExecutionTimeInMs>0){
			model.addObject("totalAutoExecutionTime", totalAutoExecutionTimeInMs/60000);
			savedTime = totalManualExecutionTime - totalAutoExecutionTimeInMs/60000;
		} else {
			model.addObject("totalAutoExecutionTime", 0);
		}
		model.addObject("totalManualExecutionTime", totalManualExecutionTime);
		model.addObject("savedTime", savedTime);
		System.out.println("Total Saved Time:"+totalAutoExecutionTimeInMs+" | totalManualExecutionTime:"+totalManualExecutionTime);


		return model;
	}

}
