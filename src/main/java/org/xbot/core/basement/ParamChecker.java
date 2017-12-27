package org.xbot.core.basement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParamChecker {
	
 
	
	public final String FORMAT_CHECK_MEG = " format is incorrect. Should be ";
	public final String STAFF_ID="[A-Za-z0-9_\\-]{3,20}";
	public final String USER_NAME="[A-Za-z0-9_ ]{3,200}";
	public final String PASSWORD="[\\S]{4,24}";
	public final String EMAIL="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	public final String PROJECT_NAME="[A-Za-z0-9_ ]{1,255}";
	public final String TESTPLAN_NAME="[A-Za-z0-9_ ]{1,255}";
	public final String TESTCASE_NAME="[A-Za-z0-9_ ]{1,255}";
	public final String TESTCASE_SNAPSHOT_NAME="[A-Za-z0-9_ ]{1,255}";
	public final String GROUP_NAME="[A-Za-z0-9_ ]{1,255}";
	public final String ROLE_NAME="[A-Za-z0-9_ ]{1,255}";
	public final String SCHEDULE_NAME="[A-Za-z0-9_ ]{1,255}";
	public final String UUID="[A-Za-z0-9-]{8}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{12}";
	public final String UUIDs="[A-Za-z0-9-]{8}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{12}(,[A-Za-z0-9-]{8}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{4}-[A-Za-z0-9-]{12})*";
	public final String USER_ID="[A-Za-z0-9-]{4,36}";
	public final String Role_UUID="[A-Za-z0-9- ]{4,36}";
	public final String DESC = "[\\s\\S]{0,1000}";
	public final String LABEL = "[A-Za-z0-9_\\- ]{0,255}";
	public final String TIMEOUT = "[0-9]{0,12}";
	public final String NUMERIC = "[0-9]+";
	public final String CALCULABLE = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)";
	public final String DATE = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
	public final String TIME = "(([0-1][0-9])|2[0-3]):[0-6][0-9](,(([0-1][0-9])|2[0-3]):[0-6][0-9])*";
	public final String IMAGE_URL="(?<projectId>"+UUID+")/"+DATE+"/"+UUID+".png";

	public final String COUNTRY_CODE="[\\s\\S]{0,50}";

	 //public final String STAFF_ID="[0-9]{8}";
	//public final String STAFF_ID="[0-9]{8}"; 
	public String isEmpty(String field, String fieldName){
		if (field == null || (field!=null && field.trim().length()==0)){
			return "Field "+fieldName+" is not provided or empty!";
		}
		return null;
	}
	public boolean isNotEmpty(String field){
		if (field == null || (field!=null && field.trim().length()==0)){
			return false;
		}
		return true;
	}
	
	public boolean isFollowPattern(String patternStr, String field){
		if (field==null){
			return false;
		}
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(field);
		 
		if (matcher.matches()){
			return true;
		} else {
			return false;
		}
		
	}
	
	
}
