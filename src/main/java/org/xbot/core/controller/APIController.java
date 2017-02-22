package org.xbot.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xbot.core.basement.ParamChecker;
import org.xbot.core.basement.ServiceResult;
import org.xbot.core.dao.Record;
import org.xbot.core.dao.Test;
import org.xbot.core.service.RecordService;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;


@Controller
@RestController
public class APIController extends GenericController{
	@Autowired
	private RecordService recordService;

	/**
	 * start record sample below. tokenId, source are optional
		 * localhost/records/18b3493b-503c-450f-b547-0c71d2efbde4?action=start
	 *http://localhost/records/18b3493b-503c-450f-b547-0c71d2efbde4?action=start
	 *
	 * end record sample below. tokenId, source are optional
	 * ?action=end&
	 *http://localhost/records/18b3493b-503c-450f-b547-0c71d2efbde4?action=end
	 * @param testId
	 * @param source
	 * @param tokenId
	 * @param action
	 * @param result
	 * @return
	 */
	@RequestMapping("/records/{testId}")
    public ResponseEntity<?> doLoginRequest(@PathVariable String testId, @RequestParam(value="source", required=false) String source,
											@RequestParam(value="tokenId", required=false) String tokenId,
											@RequestParam(value="action", required=false) String action,
											@RequestParam(value="result", required=false) String result
											) {
		ParamChecker pc = new ParamChecker();
		if (!pc.isFollowPattern(pc.UUID ,testId)){
			//if the test id is not following the pattern
			return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("\"testId\" should be uuid length 36. Resource url is /records/<testId>?action=<action>");
		} else if (!"start".equals(action) && !"end".equals(action)){
			return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("action can be \"start\" or \"end\" only. Resource url is /records/<testId>?action=<action>");
		} else if (pc.isNotEmpty(source) && source.length()>128){
			return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("source length must be less then 128. Resource url is /records/<testId>?action=<action>&source=<source>");
		}
		Record record = null;
		ServiceResult serviceRresult = null;

		if ("start".equals(action)){
			record = new Record();
			Test t = recordService.getById(Test.class, testId);
			if (t!=null){
				record.setTest(t);
			} else {
				return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Provided test object not found. Pls double check if the test ID is valid");
			}
			record.setSource(source);
			record.setToken(tokenId);
			//create a new one
			record.setStartTime(new Timestamp(System.currentTimeMillis()));
			serviceRresult = recordService.save(record);
		} else if ("end".equals(action)){
			record = recordService.findIncompletedRecordById(testId, tokenId, source);
			if (record == null){
				return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("Corresponding start record not found. Failed to update");
			}
			if ("success".equalsIgnoreCase(result) || result ==null){
				record.setResult(Record.Result.Success);
			} else if ("fail".equalsIgnoreCase(result)){
				record.setResult(Record.Result.Fail);
			} else if ("other".equalsIgnoreCase(result)){
				record.setResult(Record.Result.Other);
			}
			record.setEndTime(new Timestamp(System.currentTimeMillis()));
			serviceRresult = recordService.update(record);
		}

		if (serviceRresult.getResult()){
			return ResponseEntity.status(HttpServletResponse.SC_OK).body("Recorded");
		} else {
			return ResponseEntity.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR).body("Please notify server admin to check. "+serviceRresult.getMessage());
		}
    }

}
