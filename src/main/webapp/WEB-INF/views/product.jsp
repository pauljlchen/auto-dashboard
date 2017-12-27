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


<title>Product Management</title>

<script type="text/javascript">


    $(function(){
        $("#dialog1").hide();
        $("#dialog1").dialog({
            autoOpen: false,
            width: 600,
			height:500
        });
        $("#tabs").tabs({event:"mouseover",active:${activeTab}});

        $(".history_div tr").click(function(){
            //alert($(this).attr("id"));
			//fill the value

           	$("#updateForm").children("input[name='id']").val($(this).attr("id"));
            $("#updateForm").children("input[name='productId']").val($(this).attr("id"));
            $("#updateForm").children("input[name='region']").val($(this).children("td[class='region']").html());
            $("#updateForm").children("input[name='country']").val($(this).children("td[class='country']").html());
            $("#updateForm").children("input[name='productCode']").val($(this).children("td[class='productCode']").html());
            $("#updateForm").children("input[name='productName']").val($(this).children("td[class='productName']").html());
            $("#updateForm").children("input[name='productCategory']").val($(this).children("td[class='productCategory']").html());
            $("#updateForm").children("input[name='leader']").val($(this).children("td[class='leader']").html());
            $("#updateForm").children("input[name='manager']").val($(this).children("td[class='manager']").html());
            $("#updateForm").children("select[name='status']").val($(this).children("td[class='status']").html());
            $("#updateForm").children("input[name='targetTestcaseNumber']").val($(this).children("td[class='targetTestcaseNumber']").html());
            $("#updateForm").children("input[name='phaseTestcaseNumber']").val($(this).children("td[class='phaseTestcaseNumber']").html());
            $("#updateForm").children("input[name='isAllTestCaseAutomated']").val($(this).children("td[class='isAllTestCaseAutomated']").html());
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
				<li><a href="#tabs-1">Search Product</a></li>
				<li><a href="#tabs-2">Add Product</a></li>
			</ul>

			<div id="tabs-1">

				<form action="<%=path%>/product/search" method="POST"  class="inputform">
					<label>POD:</label><input type="text" name="pod" value="${pod}"/><br/>
					<label>Product Code:</label><input type="text" name="productCode" value="${productCode}"/><br/>
					<label>Product Name:</label><input type="text" name="productName" value="${productName}"/><br/>
					<label>Product Category:</label><input type="text" name="productCategory" value="${productCategory}"/><br/>
					<label>Product Manager:</label><input type="text" name="manager" value="${manager}"/><br/>
					<label>Test Lead:</label><input type="text" name="leader" value="${leader}"/><br/>
					<label>Region:</label><input type="text" name="region" value="${region}"/><br/>
					<label>Country Code:</label><input type="text" name="country" value="${country}"/><br/>
					<label>Testing Tools:</label><input type="text" name="testingTools" value="${testingTools}"/><br/>
					<label>Target Testcase Number:</label><input type="text" name="targetTestcaseNumber" value="${targetTestcaseNumber}"/><br/>
					<label>Phase Testcase Number:</label><input type="text" name="phaseTestcaseNumber" value="${phaseTestcaseNumber}"/><br/>
					<label>Status:</label><select type="text" name="status" value="${status}">
					<option></option>
					<option value="Active" <c:if test="${status == 'Active'}">selected</c:if>>Active</option>
					<option value="Inactive" <c:if test="${status == 'Inactive'}">selected</c:if>>Inactive</option>
					<option value="ToBeDeleted" <c:if test="${status == 'ToBeDeleted'}">selected</c:if>>ToBeDeleted</option>

				</select><br/><br/>
					<input id="search" type="submit" value="Search"/><input type="reset" value="Reset"/>
				</form>
				<br/>
				<table class="history_div">
					<tr><th>POD</th><th>Product Code</th><th>Product Name</th><th>Product Category</th><th>Product Manager</th><th>Test Lead</th><th>Region</th><th>Country Code</th><th>Status</th><th>Target Testcase Number</th><th>Phase Target Testcase Number</th><th>Testing Tools</th><th>Created Time</th></tr>
				<c:forEach var="product" items="${productList}">
					<tr id="${product.id}">

						<td class="pod">${product.pod}</td>
						<td class="productCode">${product.productCode}</td>
						<td class="productName">${product.productName}</td>
						<td class="productCategory">${product.category}</td>
						<td class="manager">${product.manager}</td>
						<td class="leader">${product.leader}</td>
						<td class="region">${product.region}</td>
						<td class="country">${product.country}</td>
						<td class="status">${product.status}</td>
						<td class="targetTestcaseNumber">${product.targetTestcaseNumber}</td>
						<td class="phaseTestcaseNumber">${product.phaseTestcaseNumber}</td>
						<td class="testingTools">${product.testingTools}</td>
						<td><fmt:formatDate value="${product.createdTime}" type="both"	pattern="yyyy-MM-dd HH:mm:ss" /></td>

					</tr>
				</c:forEach>
				</table>
			</div>
			<div id="tabs-2">
				<form id="addForm" action="<%=path%>/product/add" method="POST" class="inputform">
					<label>POD:</label><input type="text" name="pod" value="${pod}"/><br/>
					<label>Product Code:</label><input type="text" name="productCode" value="${productCode}"/><br/>
					<label>Product Name:</label><input type="text" name="productName" value="${productName}"/><br/>
					<label>Product Category:</label><input type="text" name="productCategory" value="${productCategory}"/><br/>
					<label>Product Manager:</label><input type="text" name="manager" value="${manager}"/><br/>
					<label>Test Lead:</label><input type="text" name="leader" value="${leader}"/><br/>
					<label>Region:</label><input type="text" name="region" value="${region}"/><br/>
					<label>Country Code:</label><input type="text" name="country" value="${country}"/><br/>
					<label>Target Testcase Number:</label><input type="text" name="targetTestcaseNumber" value="${targetTestcaseNumber}"/><br/>
					<label>Phase Target Testcase Number:</label><input type="text" name="phaseTestcaseNumber" value="${phaseTestcaseNumber}"/><br/>

					<label>Testing Tools:</label><input type="text" name="testingTools" value="${testingTools}"/><br/>
					<label>Status:</label><select type="text" name="status" value="${status}">
					<option value="Active">Active</option>
					<option value="Inactive">Inactive</option>
					<option value="ToBeDeleted">ToBeDeleted</option>
				</select><br/>
					<br/>
					<input type="submit" value="Submit"/><input type="reset" value="Reset"/>
				</form>
			</div>
		<jsp:include page="footer.jsp" />
	</main>
	<div id="dialog1" title="Update Product" class="display: none; z-index:200;">
		<form id="updateForm" action="<%=path%>/product/update" method="POST"  class="inputform">
			<label>POD:</label><input type="text" name="pod" /><br/>
			<label>Region:</label><input type="text" name="region" /><br/>
			<label>Country Code:</label><input type="text" name="country" /><br/>
			<label>Product Code:</label><input type="text" name="productCode" /><br/>
			<label>Product Name:</label><input type="text" name="productName" /><br/>
			<label>Product Category:</label><input type="text" name="productCategory"/><br/>
			<label>Target Testcase Number:</label><input type="text" name="targetTestcaseNumber"/><br/>
			<label>Phase Testcase Number:</label><input type="text" name="phaseTestcaseNumber" /><br/>

			<label>Testing Tools:</label><input type="text" name="testingTools"/><br/>
			<label>Product Manager:</label><input type="text" name="manager" /><br/>
			<label>Test Lead:</label><input type="text" name="leader"/><br/>
			<label>Status:</label><select type="text" name="status">
			<option value="Active">Active</option>
			<option value="Inactive">Inactive</option>
			<option value="ToBeDeleted">ToBeDeleted</option>
		</select><br/>
			<br/>
			<label>Product ID:</label><input type="text" disabled="disabled" name="id"/><br/><br/>
			<input type="submit" value="Update"/><input type="reset" value="Reset"/><input type="hidden" name="productId"/>
		</form>

	</div>
</body>
</html>
