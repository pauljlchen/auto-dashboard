package org.xbot.core.controller;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.xbot.core.basement.ConfigConst;
import org.xbot.core.basement.ParamChecker;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class DropdownController extends GenericController {

	/**
	 *
	 */
	private static final long serialVersionUID = -2248841405936829433L;

	@Autowired
	private ConfigConst configConst;
	
	@RequestMapping(value="/autoComplete.do")
	public void getDropdownDetailsById(@RequestParam(value="objId", required=false) String objId, @RequestParam(value="operation", required=false) String operation,
    		HttpServletRequest httpRequest, HttpServletResponse response, ModelAndView model){
			try {
				httpRequest.setCharacterEncoding("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	
			ParamChecker pc = new ParamChecker();
			String name = null;
			int status = -99 ;
			String desc = null;
			String createdAt = null;
			String createdBy = null;
			String updatedAt = null;
			String updatedBy = null;
			String label = null;
			

			JsonArrayBuilder jab  = Json.createArrayBuilder();
			JsonArray  ja = null;
			
			if ((objId==null && operation==null) || (objId!=null && !pc.isFollowPattern(pc.UUID, objId))){
				//command is null, ignore
				response.setContentType("text/plain;charset=UTF-8");
				response.setCharacterEncoding("utf-8");
				PrintWriter out = null;
				try {
					out = response.getWriter();
					out.append("");
					//System.out.println("doGet method called:"+request.getParameter("term"));
					out.flush();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (out!=null){
						out.close();
					}
				}
				return;
			}
			ja = jab.build();

			response.setContentType("text/plain;charset=UTF-8");
			response.setCharacterEncoding("utf-8");
			PrintWriter out = null;
			try {
				out = response.getWriter();
				out.append(ja.toString());
				//System.out.println("doGet method called:"+request.getParameter("term"));
				out.flush();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (out!=null){
					out.close();
				}
			}
	}


	
}
