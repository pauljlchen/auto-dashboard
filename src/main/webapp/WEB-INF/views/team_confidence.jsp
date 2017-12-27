<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib  prefix="spring" uri="http://www.springframework.org/tags"%>
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
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<link rel="shortcut icon" href="<%=path%>/css/images/xbot_small.ico" type="image/x-icon"/>
<script src="<%=path%>/js/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/js/jquery-ui.min.js"></script>


<title>Team Confluence</title>

<script type="text/javascript">


    $(function(){
        $("#tabs").tabs({event:"mouseover",active:${activeTab}});
	});
</script>
</head>

<body>
	<jsp:include page="header.jsp" />
	<main>

		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Add Team Confidence Rating</a></li>
			</ul>

			<div id="tabs-1">

				<form action="<%=path%>/teamConfidence" method="POST"  class="inputform">
					<label>Team Confidence Rating:</label><input type="text" name="score" value="${score}"/><br/>
					<label>Description:</label><input type="text" name="description" value="${description}"/><br/>
					<fieldset>
						<legend>Update Single Project</legend>
						<label>Product Code:</label><input type="text" name="productCode" value="${productCode}"/><br/>
						<label>Product Name:</label><input type="text" name="productName" value="${productName}"/><br/>
					</fieldset>
					<fieldset>
						<legend>Bulk Update(passcode is required)</legend>
						<label>Passcode:</label><input type="text" name="code" value="${code}"/><br/>
						<label>POD:</label><input type="text" name="pod" value="${pod}"/><br/>
						<label>Product Category:</label><input type="text" name="productCategory" value="${productCategory}"/><br/>
						<label>Manager:</label><input type="text" name="manager" value="${manager}"/><br/>
						<label>Leader:</label><input type="text" name="leader" value="${leader}"/><br/>
					</fieldset>
					 <br/>
					<input id="search" type="submit" value="Add"/><input type="reset" value="Reset"/>
					<br/>
				</form>
				<br/>
			</div>
		<jsp:include page="footer.jsp" />
	</main>

</body>
</html>
