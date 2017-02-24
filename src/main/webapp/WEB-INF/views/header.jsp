<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html "about:legacy-compat">
 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta http-equiv="pragma" content="cache">
	<meta http-equiv="cache-control" content="cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="description" content="Header">
<link rel="shortcut icon" href="<%=path%>/css/images/xbot_small.ico" type="image/x-icon"/>

<script src="<%=path%>/js/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/js/jquery-ui.min.js"></script>

 </head>
<body>
	<nav>
		<span>
		<ul id="showmenu2">
			<li><img src="<%=path%>/css/images/HSBC.jpg"/></li><li><a href="<%=path%>/home.html">Home</a></li><li>|</li>
			<li><a href="<%=path%>/router?target=project">Product</a></li><li>|</li>
			<li><a href="<%=path%>/router?target=test">Test Case</a></li><li>|</li>
			<li><a href="<%=path%>/router?target=dashboard">Dashboard</a></li><li>|</li>
		</ul>
	 </span><br/>


	 </nav>
	  <div class="headerBottom"></div>
	  <br class="clear:both"/>


</body>
</html>