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
<link rel="shortcut icon" href="<%=path%>/css/images/xbot_small.ico" type="image/x-icon"/>
<script src="<%=path%>/js/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/js/jquery-ui.min.js"></script>


<title>Test Management</title>

<script type="text/javascript">


    $(function(){
        $("#dialog").hide();
        $("#dialog").dialog({
            autoOpen: false,
            height : "480",
            width : "530"
        });
        $("#tabs").tabs({event:"mouseover",active:${activeTab}});

        $(".history_div tr").click(function(){
			//fill the value
           	$("#updateForm").children("input[name='id']").val($(this).children("td[class='id']").html());
            $("#updateForm").children("input[name='idDisplay']").val($(this).children("td[class='id']").html());
            $("#updateForm").children("input[name='projectId']").val($(this).children().children("input[name='projectId']").val());
            $("#updateForm").children("input[name='projectIdDisplay']").val($(this).children().children("input[name='projectId']").val());
            $("#updateForm").children("input[name='name']").val($(this).children("td[class='name']").html());
            $("#updateForm").children("textarea[name='description']").html($(this).children("td[class='description']").html());
            $("#updateForm").children("input[name='manualExecutionTime']").val($(this).children("td[class='manualExecutionTime']").html());

            $("#dialog").dialog("open");
        });


	});
</script>
</head>

<body>
	<jsp:include page="header.jsp" />
	<main>

		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Search Test Case</a></li>
				<li><a href="#tabs-2">Add Test Case</a></li>
			</ul>

			<div id="tabs-1">
				<h2>Search Test Case</h2>
				<form action="<%=path%>/tests/search" method="POST" >
					<label>Project:</label>
					<select name="projectId">
						<option></option>
						<c:forEach var="project" items="${projectList}">
							<option value="${project.id}" <c:if test="${project.id == projectId}">selected</c:if>>[${project.projectCode}]${project.projectName}</option>
						</c:forEach>
					</select><br/>

					<label>Test Case Name:</label><input type="text" name="name" value="${name}"/><br/>
					<label>Description:</label><textarea type="text" name="description">${description}</textarea><br/>

					<input id="search" type="submit" value="Search"/><input type="reset" value="Reset"/>
				</form>
				<br/>
				<table class="history_div">
					<tr><th>Project</th><th>Test Case ID</th><th>Test Case Name</th><th>Description</th><th>Manual Execution Time(Minutes)</th><th>Created Time</th></tr>
				<c:forEach var="item" items="${objList}">
					<tr>
						<td>

							<c:forEach var="project" items="${projectList}">
								<c:if test="${project.id == item.project.id}">[${project.projectCode}]${project.projectName}<input type="hidden" name="projectId" value="${project.id}"/></c:if>
							</c:forEach>
						</td>
						<td class="id">${item.id}</td>
						<td class="name">${item.name}</td>
						<td class="description">${item.description}</td>
						<td class="manualExecutionTime">${item.manualExecutionTime}</td>
						<td><fmt:formatDate value="${item.createdTime}" type="both"	pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</c:forEach>
				</table>
			</div>
			<div id="tabs-2">
				<h2>Add new Test Case</h2>
				<form id="addForm" action="<%=path%>/tests/add" method="POST">
					<label>Project:</label><select name="projectId">
					<c:forEach var="project" items="${projectList}">
						<option value="${project.id}" <c:if test="${project.id == projectId}">selected</c:if>>[${project.projectCode}]${project.projectName}</option>
					</c:forEach>
				</select><br/>
					<label>Test Case Name:</label><input type="text" name="name" value="${name}"/><br/>
					<label>Description:</label><textarea type="text" name="description">${description}</textarea><br/>
					<label>Manual Execution Time(Minutes):</label><input type="text" name="manualExecutionTime" value="${manualExecutionTime}"/><br/>
					<input type="submit" value="Submit"/><input type="reset" value="Reset"/>
				</form>
			</div>
		<jsp:include page="footer.jsp" />
	</main>
	<div id="dialog" title="Update Test" class="display: none; z-index:200;">
		<form id="updateForm" action="<%=path%>/tests/update" method="POST" >
			<label>Project:</label><input name="projectIdDisplay" readonly/><input type="hidden" name="projectId"/><br/>
			<label>Test Case ID:</label><input name="idDisplay" readonly/><input type="hidden" name="id"/><br/>
			<label>Test Case Name:</label><input type="text" name="name" /><br/>
			<label>Description:</label><textarea type="text" name="description"></textarea><br/>
			<label>Manual Execution Time(Minutes):</label><input type="text" name="manualExecutionTime"/><br/>
			<input type="submit" value="Update"/><input type="reset" value="Reset"/>
		</form>
	</div>
</body>
</html>
