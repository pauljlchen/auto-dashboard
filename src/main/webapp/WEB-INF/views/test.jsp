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


<title>Test Management</title>

<script type="text/javascript">


    $(function(){
        $("#dialog1").hide();
        $("#dialog1").dialog({
            autoOpen: false,
            width: 600,
            height:300
        });
        $("#tabs").tabs({event:"mouseover",active:${activeTab}});

        $(".history_div tr").click(function(){

			//fill the value
           	$("#updateForm").children("input[name='id']").val($(this).children("td[class='id']").html());
            $("#updateForm").children("input[name='idDisplay']").val($(this).children("td[class='id']").html());
            $("#updateForm").children("input[name='productId']").val($(this).children().children("input[name='productId']").val());
            $("#updateForm").children("input[name='productIdDisplay']").val($.trim($(this).children("td[class='productName']").text()));
            $("#updateForm").children("input[name='name']").val($(this).children("td[class='name']").html());
            $("#updateForm").children("textarea[name='description']").html($(this).children("td[class='description']").html());
            $("#updateForm").children("input[name='manualExecutionTime']").val($(this).children("td[class='manualExecutionTime']").html());

            $("#dialog1").dialog("open");
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
				<form action="<%=path%>/tests/search" method="POST"  class="inputform">
					<label>Product:</label>
					<select name="productId">
						<option></option>
						<c:forEach var="product" items="${productList}">
							<option value="${product.id}" <c:if test="${product.id == productId}">selected</c:if>>[${product.productCode}]${product.productName}</option>
						</c:forEach>
					</select><br/>

					<label>Test Case Name:</label><input type="text" name="name" value=""/><br/>
					<label>Description:</label><textarea type="text" name="description"></textarea><br/>

					<input id="search" type="submit" value="Search"/><input type="reset" value="Reset"/>
				</form>
				<br/>
				<table class="history_div">
					<tr><th>Product</th><th>Test Case ID</th><th>Test Case Name</th><th>Description</th><th>Manual Execution Time(Minutes)</th><th>Created Time</th></tr>
				<c:forEach var="item" items="${objList}">
					<tr>
						<td class="productName">
							<c:forEach var="product" items="${productList}">
								<c:if test="${product.id == item.project.id}">[${product.productCode}]${product.productName}<input type="hidden" name="productId" value="${product.id}"/></c:if>
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
				<form id="addForm" action="<%=path%>/tests/add" method="POST"  class="inputform">
					<label>Product:</label><select name="productId">
					<c:forEach var="product" items="${productList}">
						<option value="${product.id}" <c:if test="${product.id == productId}">selected</c:if>>[${product.productCode}]${product.productName}</option>
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
	<div id="dialog1" title="Update Test" class="display: none; z-index:200;">
		<form id="updateForm" action="<%=path%>/tests/update" method="POST"  class="inputform">
			<label>Product:</label><input  type="text"  name="productIdDisplay" disabled="true"/> <br/>
			<label>Test Case ID:</label><input type="text" name="idDisplay" disabled="true"/> <br/>
			<label>Test Case Name:</label><input type="text" name="name" /><br/>
			<label>Description:</label><textarea type="text" name="description"></textarea><br/>
			<label>Manual Execution Time(Minutes):</label><input type="text" name="manualExecutionTime"/><br/>
			<input type="submit" value="Update"/><input type="reset" value="Reset"/>
			<input type="hidden" name="productId"/><input type="hidden" name="id"/>
		</form>
	</div>

</body>
</html>
