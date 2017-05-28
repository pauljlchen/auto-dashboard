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
        $("#dialog1").hide();
        $("#dialog1").dialog({
            autoOpen: false,
            width: 600,
			height:450
        });
        $("#tabs").tabs({event:"mouseover",active:${activeTab}});

        $(".history_div tr").click(function(){
            //alert($(this).attr("id"));
			//fill the value

           	$("#updateForm").children("span[name='id']").html($(this).children("td[class='id']").html());
            $("#updateForm").children("input[name='projectId']").val($(this).children("td[class='id']").html());
            $("#updateForm").children("input[name='region']").val($(this).children("td[class='region']").html());
            $("#updateForm").children("input[name='country']").val($(this).children("td[class='country']").html());
            $("#updateForm").children("input[name='projectCode']").val($(this).children("td[class='projectCode']").html());
            $("#updateForm").children("input[name='projectName']").val($(this).children("td[class='projectName']").html());
            $("#updateForm").children("input[name='projectCategory']").val($(this).children("td[class='projectCategory']").html());
            $("#updateForm").children("input[name='leader']").val($(this).children("td[class='leader']").html());
            $("#updateForm").children("input[name='manager']").val($(this).children("td[class='manager']").html());
            $("#updateForm").children("select[name='status']").val($(this).children("td[class='status']").html());
            $("#updateForm").children("input[name='targetTestcaseNumber']").val($(this).children("td[class='targetTestcaseNumber']").html());
            $("#updateForm").children("input[name='testingTools']").val($(this).children("td[class='testingTools']").html());
            $("#updateForm").children("input[name='pod']").val($(this).children("td[class='pod']").html());

            $("#dialog1").dialog("open").css("width:800");
        });


	});
</script>
</head>

<body>
	<jsp:include page="header.jsp" />
	<main>

		<div id="tabs">
			<ul>
				<li><a href="#tabs-1">Search Project</a></li>
				<li><a href="#tabs-2">Add Project</a></li>
			</ul>

			<div id="tabs-1">

				<form action="<%=path%>/projects/search" method="POST"  class="inputform">
					<label>POD:</label><input type="text" name="pod" value="${pod}"/><br/>
					<label>Project Code:</label><input type="text" name="projectCode" value="${projectCode}"/><br/>
					<label>Project Name:</label><input type="text" name="projectName" value="${projectName}"/><br/>
					<label>Project Category:</label><input type="text" name="projectCategory" value="${projectCategory}"/><br/>
					<label>Manager:</label><input type="text" name="manager" value="${manager}"/><br/>
					<label>Leader:</label><input type="text" name="leader" value="${leader}"/><br/>
					<label>Region:</label><input type="text" name="region" value="${region}"/><br/>
					<label>Country Code:</label><input type="text" name="country" value="${country}"/><br/>
					<label>Testing Tools:</label><input type="text" name="testingTools" value="${testingTools}"/><br/>
					<label>Target Testcase Number:</label><input type="text" name="targetTestcaseNumber" value="${targetTestcaseNumber}"/><br/>
					<label>Status:</label><select type="text" name="status" value="${status}">
					<option></option>
					<option value="Active" <c:if test="${status == 'Active'}">selected</c:if>>Active</option>
					<option value="Inactive" <c:if test="${status == 'Inactive'}">selected</c:if>>Inactive</option>
					<option value="ToBeDeleted" <c:if test="${status == 'ToBeDeleted'}">selected</c:if>>ToBeDeleted</option>

				</select><br/>
					<input id="search" type="submit" value="Search"/><input type="reset" value="Reset"/>
				</form>
				<br/>
				<table class="history_div">
					<tr><th>Project ID</th><th>POD</th><th>Project Code</th><th>Project Name</th><th>Project Category</th><th>Manager</th><th>Leader</th><th>Region</th><th>Country Code</th><th>Status</th><th>Target Testcase Number</th><th>Testing Tools</th><th>Created Time</th></tr>
				<c:forEach var="project" items="${projectList}">
					<tr>
						<td class="id">${project.id}</td>
						<td class="pod">${project.pod}</td>
						<td class="projectCode">${project.projectCode}</td>
						<td class="projectName">${project.projectName}</td>
						<td class="projectCategory">${project.category}</td>
						<td class="manager">${project.manager}</td>
						<td class="leader">${project.leader}</td>
						<td class="region">${project.region}</td>
						<td class="country">${project.country}</td>
						<td class="status">${project.status}</td>
						<td class="targetTestcaseNumber">${project.targetTestcaseNumber}</td>
						<td class="testingTools">${project.testingTools}</td>
						<td><fmt:formatDate value="${project.createdTime}" type="both"	pattern="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
				</c:forEach>
				</table>
			</div>
			<div id="tabs-2">
				<form id="addForm" action="<%=path%>/projects/add" method="POST" class="inputform">
					<label>POD:</label><input type="text" name="pod" value="${pod}"/><br/>
					<label>Project Code:</label><input type="text" name="projectCode" value="${projectCode}"/><br/>
					<label>Project Name:</label><input type="text" name="projectName" value="${projectName}"/><br/>
					<label>Project Category:</label><input type="text" name="projectCategory" value="${projectCategory}"/><br/>
					<label>Manager:</label><input type="text" name="manager" value="${manager}"/><br/>
					<label>Leader:</label><input type="text" name="leader" value="${leader}"/><br/>
					<label>Region:</label><input type="text" name="region" value="${region}"/><br/>
					<label>Country Code:</label><input type="text" name="country" value="${country}"/><br/>
					<label>Target Testcase Number:</label><input type="text" name="targetTestcaseNumber" value="${targetTestcaseNumber}"/><br/>
					<label>Testing Tools:</label><input type="text" name="testingTools" value="${testingTools}"/><br/>
					<label>Status:</label><select type="text" name="status" value="${status}">
					<option value="Active">Active</option>
					<option value="Inactive">Inactive</option>
					<option value="ToBeDeleted">ToBeDeleted</option>
				</select><br/>
					<input type="submit" value="Submit"/><input type="reset" value="Reset"/>
				</form>
			</div>
		<jsp:include page="footer.jsp" />
	</main>
	<div id="dialog1" title="Update Project" class="display: none; z-index:200;">
		<form id="updateForm" action="<%=path%>/projects/update" method="POST"  class="inputform">
			<label>POD:</label><input type="text" name="pod" /><br/>
			<label>Region:</label><input type="text" name="region" /><br/>
			<label>Country Code:</label><input type="text" name="country" /><br/>
			<label>Project Code:</label><input type="text" name="projectCode" /><br/>
			<label>Project Name:</label><input type="text" name="projectName" /><br/>
			<label>Project Category:</label><input type="text" name="projectCategory"/><br/>
			<label>Target Testcase Number:</label><input type="text" name="targetTestcaseNumber"/><br/>
			<label>Testing Tools:</label><input type="text" name="testingTools"/><br/>
			<label>Manager:</label><input type="text" name="manager" /><br/>
			<label>Leader:</label><input type="text" name="leader"/><br/>
			<label>Status:</label><select type="text" name="status">
			<option value="Active">Active</option>
			<option value="Inactive">Inactive</option>
			<option value="ToBeDeleted">ToBeDeleted</option>
		</select><br/>
			<label>Project ID:</label><span  name="id" width="500px"></span><br/><br/>
			<input type="submit" value="Update"/><input type="reset" value="Reset"/><input type="hidden" name="projectId"/>
		</form>

	</div>
</body>
</html>
