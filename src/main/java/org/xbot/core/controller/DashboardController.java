package org.xbot.core.controller;

import junit.framework.TestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xbot.core.basement.ParamChecker;
import org.xbot.core.basement.ServiceResult;
import org.xbot.core.bean.*;
import org.xbot.core.dao.*;
import org.xbot.core.service.RecordService;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class DashboardController extends GenericController{
	public final static String DATE_FORMAT = "yyyy-M-d";

	@Autowired
	private RecordService recordService;

	/**
	 * For adding the project
	 * @param productCode
	 * @param leader
	 * @param status
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/dashboards")
	public ModelAndView getDashboard(@RequestParam(value="productCode", required=false) String productCode,
									 @RequestParam(value="productName", required=false) String productName,
								   @RequestParam(value="region", required=false) String region,
								   @RequestParam(value="country", required=false) String country,
								   @RequestParam(value="leader", required=false) String leader,
								   @RequestParam(value="category", required=false) String category,
								   @RequestParam(value="status", required=false) String status,
								   @RequestParam(value="productCategory", required=false) String productCategory,
									 @RequestParam(value="startDate", required=false) String startDate,
									 @RequestParam(value="endDate", required=false) String endDate,
									 @RequestParam(value="manager", required=false) String manager,
									 @RequestParam(value="pod", required=false) String pod,
									 @RequestParam(value="isShowHeat", required=false, defaultValue="true") Boolean isShowHeat,
									 ModelAndView model) {

		ParamChecker pc = new ParamChecker();
		model.setViewName(Dashboard_View);
		Calendar c = Calendar.getInstance();

		Timestamp startDt = null;
		Timestamp endDt = null;
		//System.out.println("startDate:"+startDate+" endDate:"+endDate+"|"+pc.isNotEmpty(endDate));
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
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
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDt);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDt);

		if (true==isShowHeat){
			String heatStartDate = startDate;
			String heatEndDate = endDate;
			if (heatStartDate==null || (heatStartDate!=null && heatStartDate.length()==0)){
				heatStartDate = startCalendar.get(Calendar.YEAR)+"-"+(startCalendar.get(Calendar.MONTH)+1)+"-"+(startCalendar.get(Calendar.DATE)+1);
			}
			if (heatEndDate==null || (heatEndDate!=null && heatEndDate.length()==0)){
				heatEndDate = endCalendar.get(Calendar.YEAR)+"-"+(endCalendar.get(Calendar.MONTH)+1)+"-"+(endCalendar.get(Calendar.DATE)+1);
			}
			model.addObject("heatStartDate", heatStartDate);
			model.addObject("heatEndDate", heatEndDate);
		}
		Map<String, CategoryView> categoryMap = new HashMap<>();
		Map<String, RegionView> regionMap = new HashMap<>();
		//Testcase count map
		Map<String, TestCaseView> testcaseCountMap = new HashMap<>();
		// POD                     Date
		Map<String,  LinkedHashMap<String, TeamConfidenceView>> confidenceMap = new HashMap<>();
		List<String> dateList = new ArrayList<String>();
		LinkedHashMap<String, TeamConfidenceView> confidenceTemplate = new LinkedHashMap<String, TeamConfidenceView>();
		model.addObject("startDate", startDate);
		model.addObject("endDate", endDate);

		model.addObject("productCode", productCode);
		model.addObject("productName", productName);
		model.addObject("productCategory", productCategory);
		model.addObject("manager", manager);
		model.addObject("leader", leader);
		model.addObject("pod", pod);
		model.addObject("status", status);
		model.addObject("region", region);
		model.addObject("country", country);
		model.addObject("activeTab", "1");
		model.addObject("categoryMap", categoryMap);
		model.addObject("regionMap", regionMap);
		model.addObject("confidenceMap", confidenceMap);
		model.addObject("testcaseCountMap", testcaseCountMap);
		model.addObject("isShowHeat", isShowHeat);

		//init the date list for pass rate heatmap to use
		LinkedHashMap<String, PassrateView> heatmapData = new LinkedHashMap<String, PassrateView>();
		model.addObject("heatMap", heatmapData);

//		if (country!=null && country.length()>0  && !pc.isFollowPattern(pc.COUNTRY_CODE, country)){
//			model.addObject(RESULT, false);
//			model.addObject(MESSAGE, "Country code should be 2 capital characters, such as HK, UK, US...");
//			return model;
//		} else
		if (status!=null && status.length()>0  && !Project.STATUS.Active.toString().equals(status) && !Project.STATUS.Inactive.toString().equals(status) && !Project.STATUS.ToBeDeleted.toString().equals(status)) {
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Product status is incorrect. If this keeps happening, please find system admin.");
			return model;
		}
		TeamConfidence refTeamConfidence = new TeamConfidence();
		Project p = new Project();
		if (pc.isNotEmpty(country)){
			p.setCountry(country);
		}
		if (pc.isNotEmpty(region)){
			p.setRegion(region);
		}
		if (pc.isNotEmpty(leader)){
			p.setLeader(leader);
		}
		if (pc.isNotEmpty(pod)){
			p.setPod(pod);
		}
		if (pc.isNotEmpty(manager)){
			p.setManager(manager);
		}
		if (pc.isNotEmpty(productCode)){
			p.setProductCode(productCode);
		}
		if (pc.isNotEmpty(productName)){
			p.setProductName(productName);
		}
		if (pc.isNotEmpty(category)){
			p.setCategory(category);
		}
		if (pc.isNotEmpty(status)){
			p.setStatus(Project.STATUS.valueOf(status));
		}
		//s
		//search for projects
		ServiceResult serviceResult = recordService.searchByInstance(p, CommonDAO.OP_MODE.EQUALS);
		List<Project> projects = new ArrayList<>();
		if (serviceResult.getResult()){
			projects = (List<Project>) serviceResult.getReturnObject();
			for (Project cur : projects){
				//try to get the tcv first
				if (cur.getPod()!=null && cur.getPod().length()>0){
					TestCaseView tcv = null;
					if((tcv=testcaseCountMap.get(cur.getPod()))!=null){
						//update existing one
						if (cur.getTargetTestcaseNumber()!=null){
							tcv.setTestcaseNumber(tcv.getTestcaseNumber()+cur.getTargetTestcaseNumber());
						}
					} else {
						//create new one
						tcv = new TestCaseView();
						tcv.setPODName(cur.getPod());

						tcv.setProjectName(cur.getProductName());
						tcv.setTestcaseNumber(cur.getTargetTestcaseNumber());
						//init the testcaseCountMap
					}
					testcaseCountMap.put(cur.getPod(), tcv);
				}

			}
			projects = null;
			serviceResult.setReturnObject(null);
			serviceResult = null;
		}




		Test t = new Test();
		t.setProject(p);
		Record r = new Record();
		r.setTest(t);
		//r.setResult(Record.Result.Success);
		refTeamConfidence.setProject(p);
		List<Record> result = recordService.listRecord(r, startDt, endDt);
		if (result == null || (result!=null && result.size()==0)){
			return model;
		}


		//init the date list for team confidence use
		Calendar curCalendar = Calendar.getInstance();

		int passCount=0;
		int executionCount=0;

		List<Test> tests = recordService.listTest(t);
		int totalTestCase=0;
		if (tests!=null && tests.size()>0){
			totalTestCase = tests.size();
		}

		Calendar curDateEndCalendar = Calendar.getInstance();

		int[] records = null;
		for (curCalendar.setTime(startCalendar.getTime()); !curCalendar.after(endCalendar); curCalendar.add(Calendar.DATE,1)){
			curCalendar.set(Calendar.HOUR,0);
			curCalendar.set(Calendar.HOUR_OF_DAY,0);
			curCalendar.set(Calendar.MINUTE,0);
			curCalendar.set(Calendar.SECOND,0);
			dateList.add(curCalendar.get(Calendar.YEAR)+"-"+(curCalendar.get(Calendar.MONTH)+1)+"-"+(curCalendar.get(Calendar.DATE)+1));
			TeamConfidenceView tc = new TeamConfidenceView();
			tc.setScore(0d);
			confidenceTemplate.put(curCalendar.get(Calendar.YEAR)+"-"+(curCalendar.get(Calendar.MONTH)+1)+"-"+(curCalendar.get(Calendar.DATE)+1),tc);

			if (isShowHeat){
				//for the passrate heatmapp calendar
				PassrateView pv = new PassrateView();
				//calculate the data
				passCount=0;
				executionCount=0;
				curDateEndCalendar.setTime(curCalendar.getTime());
				curDateEndCalendar.add(Calendar.DATE,1);
				curDateEndCalendar.set(Calendar.HOUR_OF_DAY,0);
				curDateEndCalendar.set(Calendar.HOUR,0);
				curDateEndCalendar.set(Calendar.MINUTE,0);
				curDateEndCalendar.set(Calendar.SECOND,0);

				records = recordService.countRecords(r, new Timestamp(curCalendar.getTimeInMillis()), new Timestamp(curDateEndCalendar.getTimeInMillis()));
				if (records!=null && records.length==2){
					passCount = records[0];
					executionCount = records[1];
				}

				if (executionCount>0 && passCount>0){
					pv.setPassRate(passCount*100/executionCount);
				} else {
					pv.setPassRate(0);
				}
				if (totalTestCase>0 && executionCount>0){
					pv.setExecutionRate(executionCount*100/totalTestCase);
				} else {
					pv.setExecutionRate(0);
				}
				heatmapData.put(curCalendar.get(Calendar.YEAR)+"-"+(curCalendar.get(Calendar.MONTH)+1)+"-"+(curCalendar.get(Calendar.DATE)+1),pv);
			}


		}
		model.addObject("dateList", dateList);
		model.addObject("heatMap", heatmapData);

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
		//for setting the map key of Month
		Calendar curMonthCalendar = Calendar.getInstance();


		for (Record cur : result){
			//calculate for the pod test case count
			if (cur.getTest()!=null && cur.getTest().getProject()!=null && cur.getTest().getProject().getPod()!=null){
				TestCaseView tcv = testcaseCountMap.get(cur.getTest().getProject().getPod());
				if (tcv!=null) {
					Test curTest = tcv.getExecutedTestcases().get(cur.getTest().getId());
					if (curTest == null) {
						//not existed in the map yet, add it
						tcv.getExecutedTestcases().put(cur.getTest().getId(), cur.getTest());
					}
				}
			}


			//end calculating for pod test case count



			curMonthStr = null;
			//successful records
			curCategory = cur.getTest().getProject().getCategory();
			curProject = cur.getTest().getProject().getProductCode();
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
				curProjectKpiView.setName(curProject+"("+cur.getTest().getProject().getProductName()+")");
			}
			curProjectKpiView = curCategoryView.getProjects().get(curProject);

			//init region container
			if (regionMap.get(curRegion)==null){
				//new a category
				RegionView rv = new RegionView();
				Map<String, KPIView> monthsTemplate = new LinkedHashMap<>();
				//init the month KPIView
				curCalendar = Calendar.getInstance();
				for (curCalendar.setTime(startCalendar.getTime()); !curCalendar.after(endCalendar); curCalendar.add(Calendar.MONTH,1)){
					monthsTemplate.put(curCalendar.get(Calendar.YEAR)+"-"+(curCalendar.get(Calendar.MONTH)+1), new KPIView());
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
					curMonthKpiView.setManualExecutionTime(curMonthKpiView.getManualExecutionTime()+cur.getManualExecutionTime());

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
					totalManualExecutionTime += cur.getManualExecutionTime();
					curProjectKpiView.setManualExecutionTime(curProjectKpiView.getManualExecutionTime()+cur.getManualExecutionTime());
					curCountryKpiView.setManualExecutionTime(curCountryKpiView.getManualExecutionTime()+cur.getManualExecutionTime());
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
		//System.out.println("Total Saved Time:"+totalAutoExecutionTimeInMs+" | totalManualExecutionTime:"+totalManualExecutionTime);


		//for team confidence
		model.addObject("confidenceMap",getTeamConfident(confidenceTemplate,refTeamConfidence , confidenceMap, startDt, endDt));
		//end for team confidence

		//for passrate

		//end for passrate



		//test case count map
		model.addObject("testcaseCountMap", testcaseCountMap);
		result.clear();
		result=null;
		return model;
	}
	private Map<String,  LinkedHashMap<String, TeamConfidenceView>> getTeamConfident(LinkedHashMap<String, TeamConfidenceView> confidenceTemplate, TeamConfidence refTeamConfidence, Map<String,  LinkedHashMap<String, TeamConfidenceView>> confidenceMap, Timestamp startDt,Timestamp endDt){
		//for team confidence
		ServiceResult teamConfidenceResult = recordService.listTeamConfidence(refTeamConfidence, startDt, endDt);

		Calendar temp = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		LinkedHashMap<String, TeamConfidenceView> curConfidenceScoreMap;
		TeamConfidenceView confidenceView;
		if (teamConfidenceResult.getResult()){
			List<TeamConfidence> confidenceList = (ArrayList<TeamConfidence>) teamConfidenceResult.getReturnObject();
			if (confidenceList!=null && confidenceList.size()>0){
				for (TeamConfidence cur: confidenceList){
					if (cur.getProject()!=null && cur.getProject().getPod()!=null){
						curConfidenceScoreMap = confidenceMap.get(cur.getProject().getPod());
						if (curConfidenceScoreMap==null){
							//define the new one

							curConfidenceScoreMap = new LinkedHashMap<String, TeamConfidenceView> ();
							curConfidenceScoreMap.putAll(confidenceTemplate);
							for (String key : curConfidenceScoreMap.keySet()){
								curConfidenceScoreMap.put(key, confidenceTemplate.get(key).clone());
							}
						}

						confidenceView = curConfidenceScoreMap.get(sdf.format(cur.getCreatedTime()));
						if (confidenceView!=null){
							confidenceView.setScore(cur.getScore());

							confidenceView.setCreatedBy(cur.getCreatedBy());
							confidenceView.setDescription(cur.getDescription());
							curConfidenceScoreMap.put(sdf.format(cur.getCreatedTime()), confidenceView);
							confidenceMap.put(cur.getProject().getPod(), curConfidenceScoreMap);
						}

					}
				}

			}
		}//end for team confidence
		return confidenceMap;
	}
}
