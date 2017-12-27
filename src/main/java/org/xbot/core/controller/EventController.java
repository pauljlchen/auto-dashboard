package org.xbot.core.controller;

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
public class EventController extends GenericController{
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
	@RequestMapping(value = "/events")
	public ModelAndView getEvents(@RequestParam(value="productCode", required=false) String productCode,
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
		model.setViewName(Events_View);
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
				c.add(Calendar.HOUR, -2);
				startDt = new Timestamp(c.getTimeInMillis());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDt);
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endDt);

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
		model.addObject("records", null);

		if (status!=null && status.length()>0  && !Project.STATUS.Active.toString().equals(status) && !Project.STATUS.Inactive.toString().equals(status) && !Project.STATUS.ToBeDeleted.toString().equals(status)) {
			model.addObject(RESULT, false);
			model.addObject(MESSAGE, "Product status is incorrect. If this keeps happening, please find system admin.");
			return model;
		}
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





		Test t = new Test();
		t.setProject(p);
		Record r = new Record();
		r.setTest(t);
		//r.setResult(Record.Result.Success);
		List<Record> result = recordService.listRecord(r, startDt, endDt);
		if (result == null || (result!=null && result.size()==0)){
			return model;
		}
		System.out.println("results:"+result);
		//test case count map
		model.addObject("records", result);

		return model;
	}

}
