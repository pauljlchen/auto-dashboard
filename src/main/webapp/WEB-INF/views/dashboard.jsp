<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<script src="<%=path%>/js/echarts.min.js"></script>
<script src="<%=path%>/js/jquery.waypoints.min.js"></script>
<script src="<%=path%>/js/jquery.countup.min.js"></script>

<title>Dashboard</title>
	<script type="text/javascript">
        $(function(){
            $('.counter').countUp();
            $("#startDate").datepicker();
            $("#startDate").datepicker("option", "dateFormat", "yy-mm-dd");
            $("#startDate").val("${startDate}");
            $("#endDate").datepicker();
            $("#endDate").datepicker( "option", "dateFormat", "yy-mm-dd");
            $("#endDate").val("${endDate}");
		});


	</script>

</head>

<body>
<div class="right"><div class="counter" data-counter-time="5000" data-counter-delay="50">${totalManualExecutionTime}</div><div class="subtitle">Total saved Minutes...</div></div>


	<div class="left" id="theDashboard2" style="width: 600px;height:400px;"></div>
<div class="left" id="theDashboard1" style="width: 600px;height:400px;"></div><br/>
	<div class="left" id="theDashboard3" style="width: 600px;height:400px;"></div>
		<div class="left">
		<main>

			<script type="text/javascript">

                var myChart1 = echarts.init(document.getElementById('theDashboard1'));
                var myChart2 = echarts.init(document.getElementById('theDashboard2'));
                var myChart3 = echarts.init(document.getElementById('theDashboard3'));
                // 指定图表的配置项和数据
                var option1 = {
                    title: {
                        text: 'Saved Time'
                    },
                    tooltip : {
                        trigger: 'axis',
                        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        data: [
						<c:forEach var="region" items="${regionMap.keySet()}">
							 '${region}',
						</c:forEach>
                        ]
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis:  {
                        type: 'value'
                    },
                    yAxis: {
                        type: 'category',
                        data: [

							<c:forEach var="region" items="${regionMap.keySet()}" end="0">


                            	<c:forEach var="month" items="${regionMap.get(region).months.keySet()}">
									'${month}',
								</c:forEach>

                            </c:forEach>
						]
                    },
                    series: [
                        <c:forEach var="region" items="${regionMap.keySet()}">

								{
									name: '${region}',
									type: 'bar',
									stack: '总量',
									label: {
										normal: {
											show: true,
											position: 'insideRight'
										}
									},
									data: [
                                        <c:forEach var="month" items="${regionMap.get(region).months.keySet()}">
											${regionMap.get(region).months.get(month).manualExecutionTime},
                                   		</c:forEach>
                                    ]
								},

                        </c:forEach>
                    ]
                };


                var option2 = {
                    title: {
                        text: 'Saved Time(minutes) by Domain/Product'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b}: {c} ({d}%)"
                    },
                    legend: {
                        show: 'true',
                        orient: 'vertical',
                        x: 'left',
                        data:['直达']
                    },
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 200,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    series: [
                        {
                            name:'Category',
                            type:'pie',
                            selectedMode: 'single',
                            radius: [0, '40%'],

                            label: {
                                normal: {
                                    position: 'inner'
                                }
                            },
                            labelLine: {
                                normal: {
                                    show: false
                                }
                            },
                            data:[
                                <c:forEach var="item" items="${categoryMap.keySet()}">
                                	{value:${categoryMap.get(item).kpiView.manualExecutionTime}, name:'${categoryMap.get(item).category}'},
								</c:forEach>
                                {}
                            ]
                        },
                        {
                            name:'Projects',
                            type:'pie',
                            radius: ['50%', '65%'],

                            data:[
                                <c:forEach var="category" items="${categoryMap.keySet()}">
									<c:forEach var="project" items="${categoryMap.get(category).projects.keySet()}">
									{value:${categoryMap.get(category).projects.get(project).manualExecutionTime}, name:'${project}'},
									</c:forEach>
                                </c:forEach>
                                {}
                            ]
                        }
                    ]
                };
                var option3 = {
                    title: {
                        text: 'Team Confidence',
                        subtext: 'To measure the confidence of test coverage and result'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        data:['邮件营销','联盟广告','视频广告','直接访问','搜索引擎']
                    },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: ['周一','周二','周三','周四','周五','周六','周日']
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [
                        {
                            name:'iUpload',
                            type:'line',
                            data:[2, 2.2, 2.4, 2.6, 2.8, 3, 2.4]
                        },
                        {
                            name:'iQueue',
                            type:'line',
                            data:[3, 3.4, 3.2, 4, 4.1, 3.4, 4.3]
                        },
                        {
                            name:'ICCM',
                            type:'line',
                            data:[3.2, 2.4, 2.2, 2.4, 2.1, 3.4, 4.3]
                        },
                        {
                            name:'FileNet',
                            type:'line',
                            data:[2.5, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9]
                        },
                        {
                            name:'WDM',
                            type:'line',
                            data:[3.2, 3.4, 3.6, 3.8, 3.6, 3.4, 3.8]
                        }
                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart1.setOption(option1);
                myChart2.setOption(option2);
                myChart3.setOption(option3);
			</script>

			<form action="/dashboards" method="POST" >
				<label>Start Date:</label><input type="text" id="startDate" name="startDate" value="${startDate}"/><br/>
				<label>End Date:</label><input type="text" id="endDate" name="endDate" value="${endDate}"/><br/>
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
			<!--div id="savedTime">${savedTime}</div><div id="totalAutoExecutionTime">${totalAutoExecutionTime}</div><div id="totalManualExecutionTime">${totalManualExecutionTime}</div-->
		</form>

			<c:forEach var="item" items="${categoryMap.keySet()}">
				hello: ${categoryMap.get(item).category}
			</c:forEach>
		</main>
		</div>
		<jsp:include page="footer.jsp" />
</body>
</html>
