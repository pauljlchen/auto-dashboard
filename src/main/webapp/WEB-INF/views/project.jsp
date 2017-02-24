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


<title>Project Management</title>

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
            //alert($(this).attr("id"));
			//fill the value

           	$("#updateForm").children("input[name='id']").val($(this).children("td[class='id']").html());
            $("#updateForm").children("input[name='projectId']").val($(this).children("td[class='id']").html());
            $("#updateForm").children("input[name='region']").val($(this).children("td[class='region']").html());
            $("#updateForm").children("input[name='country']").val($(this).children("td[class='country']").html());
            $("#updateForm").children("input[name='projectCode']").val($(this).children("td[class='projectCode']").html());
            $("#updateForm").children("input[name='projectName']").val($(this).children("td[class='projectName']").html());
            $("#updateForm").children("input[name='projectCategory']").val($(this).children("td[class='projectCategory']").html());
            $("#updateForm").children("input[name='leader']").val($(this).children("td[class='leader']").html());
            $("#updateForm").children("select[name='status']").val($(this).children("td[class='status']").html());
            $("#dialog").dialog("open");
        });


	});
</script>
</head>

<body>

	<main>
		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Search Project</a></li>
				<li><a href="#tabs-2">Add Project</a></li>
			</ul>

			<div id="tabs-1">

				<h2>Search Project</h2>
				<form action="<%=path%>/projects/search" method="POST" >
					<label>Project Code:</label><input type="text" name="projectCode" value="${projectCode}"/><br/>
					<label>Project Name:</label><input type="text" name="projectName" value="${projectName}"/><br/>
					<label>Project Category:</label><input type="text" name="projectCategory" value="${projectCategory}"/><br/>
					<label>Leader:</label><input type="text" name="leader" value="${leader}"/><br/>
					<label>Region:</label><input type="text" name="region" value="${region}"/><br/>
					<label>Country Code:</label><input type="text" name="country" value="${country}"/><br/>
					<label>Status:</label><select type="text" name="status" value="${status}">
					<option></option>
					<option value="Active">Active</option>
					<option value="Inactive">Inactive</option>
					<option value="ToBeDeleted">ToBeDeleted</option>

				</select>
					<input id="search" type="submit" value="Search"/><input type="reset" value="Reset"/>
				</form>
				<br/>
				<table class="history_div">
					<tr><th>Project ID</th><th>Region</th><th>Country Code</th><th>Project Code</th><th>Project Name</th><th>Project Category</th><th>Leader</th><th>Status</th><th>Created Time</th></tr>
				<c:forEach var="project" items="${projectList}">
					<tr>

						<td class="id">${project.id}</td>
						<td class="region">${project.region}</td>
						<td class="country">${project.country}</td>
						<td class="projectCode">${project.projectCode}</td>
						<td class="projectName">${project.projectName}</td>
						<td class="projectCategory">${project.category}</td>
						<td class="leader">${project.leader}</td>
						<td class="status">${project.status}</td>
						<td><fmt:formatDate value="${project.createdTime}" type="both"	pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</c:forEach>
				</table>
			</div>
			<div id="tabs-2">
				<h2>Add new Project</h2>
				<form id="addForm" action="<%=path%>/projects/add" method="POST">

					<label>Project Code:</label><input type="text" name="projectCode" value="${projectCode}"/><br/>
					<label>Region:</label><input type="text" name="region" value="${region}"/><br/>
					<label>Country Code:</label><input type="text" name="country" value="${country}"/><br/>
					<label>Project Name:</label><input type="text" name="projectName" value="${projectName}"/><br/>
					<label>Project Category:</label><input type="text" name="projectCategory" value="${projectCategory}"/><br/>
					<label>Leader:</label><input type="text" name="leader" value="${leader}"/><br/>
					<label>Status:</label><select type="text" name="status" value="${status}">
					<option value="Active">Active</option>
					<option value="Inactive">Inactive</option>
					<option value="ToBeDeleted">ToBeDeleted</option>
				</select>
					<input type="submit" value="Submit"/><input type="reset" value="Reset"/>
				</form>
			</div>
		<jsp:include page="footer.jsp" />
	</main>
	<div id="dialog" title="Update Project" class="display: none; z-index:200;">
		<form id="updateForm" action="<%=path%>/projects/update" method="POST" >
			<label>Project ID:</label><input name="id" readonly/><input type="hidden" name="projectId"/><br/>
			<label>Region:</label><input type="text" name="region" /><br/>
			<label>Country Code:</label><input type="text" name="country" /><br/>
			<label>Project Code:</label><input type="text" name="projectCode" /><br/>
			<label>Project Name:</label><input type="text" name="projectName" /><br/>
			<label>Project Category:</label><input type="text" name="projectCategory"/><br/>
			<label>Leader:</label><input type="text" name="leader"/><br/>
			<label>Status:</label><select type="text" name="status">
			<option value="Active">Active</option>
			<option value="Inactive">Inactive</option>
			<option value="ToBeDeleted">ToBeDeleted</option>
		</select><br/>
			<input type="submit" value="Update"/><input type="reset" value="Reset"/>
		</form>

	</div>
</body>
</html>
