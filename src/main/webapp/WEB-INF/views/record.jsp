<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
<link rel="shortcut icon" href="css/images/xbot_small.ico" type="image/x-icon"/>
<script src="js/jquery-2.1.4.min.js"></script>
<script src="js/jquery-ui.min.js"></script>
<script src="js/common.js"></script>


<title>Group Management</title>

<script type="text/javascript">

	$(function(){
		 
		$("#tabs").tabs({event:"mouseover", active:${activeTab}});
	 

		
		$("#multiSource3").dblclick(function(){
			addUser();
		});
 
		$("#addUser").click(function(){
			addUser();
		});
 
		$("#multiTarget3").dblclick(function(){
			removeUser();
		});
		$("#removeUser").click(function(){
			removeUser();
		});
		function addUser(){
		var found = false; 
			$("#multiSource3 option:selected").each(function(){
				var source =$(this);
				if (source.val()==""){
					found = true;
				}
				$("#multiTarget3 option").each(function (){  
		            var target = $(this); 		            
		            if(source.val()==target.val() ){  
		                found = true;
		           	}  
         		}); 
				if (!found){
				 	$("<option>").val(source.val()).text(source.text()).appendTo($("#multiTarget3"));
				}
			});
		}
		function removeUser(){
			$("#multiTarget3").find("option:selected").remove();
		}
		
		$("#groupId3").change(function(){
			//if this value = "" then disable the search button;
			$("#mapGroupUserSubmit").attr("disabled",true);
			$("#multiTarget3").find("option").remove();
			if (""==$(this).val()){
				$("#searchUserName").attr("disabled",true);
			} else {
				$("#searchUserName").attr("disabled", false);
			}
			
			var target = $("#multiTarget3");
			 $.get("listUserByGroup.do", {
					groupId: $("#groupId3").val()
				}, function(response){
					if (target!=null){
						target.find("option").remove();
						$.each($.parseJSON(response), function(index, item){
							$("<option>").val(item.value).text(item.desc).appendTo(target);
						});
						  
					}
					$("#mapGroupUserSubmit").attr("disabled",false);
			});	
		});
		$("#searchUserName").click(function(){
			var child = $("#multiSource3");
			$.get("listUserByName.do", {
					userName: $("#userName").val()
				}, function(response){
					if (child!=null){
						child.find("option").remove();
						$.each($.parseJSON(response), function(index, item){
							$("<option>").val(item.value).text(item.desc).appendTo(child);
						});
						  
					}
			});	
			
		 });
		 
		 $("#mapGroupUserSubmit").click(function(){
		 	var target = $("#multiTarget3");
		 	target.find("option").attr("selected",true);
			
		 });
		 
		 
		 
		 
		 $("#multiSource4").dblclick(function(){
			addProject();
		});
 
		$("#addProject").click(function(){
			addProject();
		});
 
		$("#multiTarget4").dblclick(function(){
			removeProject();
		});
		$("#removeProject").click(function(){
			removeProject();
		});
		
		function addProject(){
		var found = false; 
			$("#multiSource4 option:selected").each(function(){
				var source =$(this);
				if (source.val()==""){
					found = true;
				}
				$("#multiTarget4 option").each(function (){  
		            var target = $(this); 		            
		            if(source.val()==target.val()){  
		                found = true;
		           	}  
         		}); 
				if (!found){
				 	$("<option>").val(source.val()).text(source.text()).appendTo($("#multiTarget4"));
				}
			});
		}
		function removeProject(){
			$("#multiTarget4").find("option:selected").remove();
		}
		$("#groupId4").change(function(){
			//if this value = "" then disable the search button;
			$("#mapGroupProjectSubmit").attr("disabled",true);
			$("#multiTarget4").find("option").remove();
			if (""==$(this).val()){
				$("#searchProjectName").attr("disabled",true);
			} else {
				$("#searchProjectName").attr("disabled", false);
			}
			
			var target = $("#multiTarget4");
			 $.get("listProjectByGroup.do", {
					groupId: $("#groupId4").val()
				}, function(response){
					if (target!=null){
						target.find("option").remove();
						$.each($.parseJSON(response), function(index, item){
							$("<option>").val(item.value).text(item.desc).appendTo(target);
						});
						  
					}
					$("#mapGroupProjectSubmit").attr("disabled",false);
			});	
		});
		$("#searchProjectName").click(function(){
			var child = $("#multiSource4");
			$.get("listProjectByName.do", {
					projectName: $("#projectName").val()
				}, function(response){
					if (child!=null){
						child.find("option").remove();
						$.each($.parseJSON(response), function(index, item){
							$("<option>").val(item.value).text(item.desc).appendTo(child);
						});
						  
					}
			});	
			
		 });
		 
		 $("#mapGroupProjectSubmit").click(function(){
		 	var target = $("#multiTarget4");
		 	target.find("option").attr("selected",true); 
		 });
	});
	
</script>
</head>

<body style="display:none">

	<main>
		<form action="project" method="POST">
			Project Code:<input type="text" name="projectCode"/>
			Project Name:<input type="text" name="projectName"/>
			Leader:<input type="text" name="leader"/>
			Status:<select type="text" name="leader">
						<option>Active</option>
						<option>Inactive</option>
					</select>
			<button type="submit" value="submit"/><button type="reset" value="reset"/>
		</form>
	</main>
</body>
</html>
