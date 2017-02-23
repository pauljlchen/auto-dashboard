<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html "about:legacy-compat">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="<%=path%>/css/images/xbot_small.ico" type="image/x-icon"/>

<link rel="stylesheet" href="<%=path%>/css/hsbc.css">
<script src="<%=path%>/js/jquery-2.1.4.min.js.js"></script>
<script src="<%=path%>/js/jquery-ui.min.js"></script>
<script src="<%=path%>/js/common.js"></script>
<script> 
  $(function() {
  	$("#dialog").hide();
  	$("#dialog").dialog({
  		autoOpen: false,
		height : "380",
		width : "330"
		});
  	$("#contact").click(function (){
  		$("#dialog").dialog("open");
  	});
    
  });
  </script>
 </head>
<body>
	<footer class="footer">
	<c:if test="${RESULT==false }"></c:if>
	<div class="ui-widget" <c:if test="${RESULT!=true && RESULT!=false}">style="display: none"</c:if>>
		<a name="err_msg"></a><div class="<c:if test="${RESULT==true }">ui-state-passed</c:if><c:if test="${RESULT==false }">ui-state-error</c:if> ui-corner-all" style="margin-top: 10px; padding: 0 .7em;">
	    <p><span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>Process Result: ${MSG}</p>
		</div>
	</div> 

	</footer>
</body>
</html>