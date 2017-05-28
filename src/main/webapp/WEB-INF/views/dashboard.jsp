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
<script src="<%=path%>/js/echarts.min.js"></script>
<script src="<%=path%>/js/jquery.waypoints.min.js"></script>
<script src="<%=path%>/js/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/js/jquery-ui.min.js"></script>
<script src="<%=path%>/js/jquery.countup.min.js"></script>
<title>Dashboard</title>
	<script type="text/javascript">
        var autoSwitchFlag = true;
        var refreshInteval = setInterval(function(){
            location.reload();
        },600000);
        $(function(){
            $(document).mousemove(function(){
                clearInterval(controller);
                clearInterval(refreshInteval);
                autoSwitchFlag=false;
                var controller = setInterval(function(){
                    autoSwitchFlag=true;
                	clearInterval(controller);
                    refreshInteval = setInterval(function(){
                        location.reload();
                    },600000);
                },20000);
			});
            $("#startDate").datepicker();


            $("#startDate").datepicker("option", "dateFormat", "yy-mm-dd");
            $("#startDate").val("${startDate}");
            $("#endDate").datepicker();
            $("#endDate").datepicker( "option", "dateFormat", "yy-mm-dd");
            $("#endDate").val("${endDate}");


            $("#dialog1").hide();
            $("#dialog1").dialog({
                autoOpen: false,
                width: 600,
                height:400
            });

            $("#filter").click(function(){
                $("#dialog1").dialog("open");
			});
			//below command must be placed in the last
            $('.counter').countUp();
		});


	</script>

</head>

<body>
<jsp:include page="header.jsp" />

<div class="right"><div class="counter" data-counter-time="5000" data-counter-delay="50">${totalManualExecutionTime}</div><div class="subtitle">Total saved Minutes...<button id="filter" value="Filter">Filter Charts Data</button></div></div>


	<div class="left" id="theDashboard2" style="width: 40%;height:45%;"></div>
	<div class="left" id="theDashboard1" style="width: 40%;height:45%;"></div><br/>
	<div class="left" id="theDashboard4" style="width: 40%;height:45%;"></div>
	<div class="left" id="theDashboard5" style="width: 40%;height:45%;"></div><br/>
	<div class="left" id="theDashboard3" style="width: 40%;height:45%;"></div>
		<div class="left">
		<main>

			<script type="text/javascript">

                var myChart1 = echarts.init(document.getElementById('theDashboard1'));
                var myChart2 = echarts.init(document.getElementById('theDashboard2'));
                var myChart3 = echarts.init(document.getElementById('theDashboard3'));
                var myChart4 = echarts.init(document.getElementById('theDashboard4'));
                var myChart5 = echarts.init(document.getElementById('theDashboard5'));


                // 指定图表的配置项和数据
                var option1 = {
                    title: {
                        text: 'Saved Time(minutes)'
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
                            name:'Domain',
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

                            ]
                        },
                        {
                            name:'Product',
                            type:'pie',
                            radius: ['50%', '65%'],

                            data:[
                                <c:forEach var="category" items="${categoryMap.keySet()}">
									<c:forEach var="project" items="${categoryMap.get(category).projects.keySet()}">
									{value:${categoryMap.get(category).projects.get(project).manualExecutionTime}, name:'${project}'},
									</c:forEach>
                                </c:forEach>

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
                        data: [
                            <c:forEach var="each" items="${dateList}">
                            	'${each}',
                            </c:forEach>
						]
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [

                        <c:forEach var="pod" items="${confidenceMap.keySet()}">
                        <c:set var="lastScore" value="0"/>
                        	{name:'${pod}',type:'line',data:[
							<c:forEach var="confidenceView" items="${confidenceMap.get(pod).keySet()}">
                                <c:if test="${confidenceMap.get(pod).get(confidenceView).score==0}"><c:out value="${lastScore}"/>,</c:if>
                                <c:if test="${confidenceMap.get(pod).get(confidenceView).score>0}">${confidenceMap.get(pod).get(confidenceView).score},<c:set var="lastScore" value="${confidenceMap.get(pod).get(confidenceView).score}"/></c:if>
							</c:forEach>
							]},
                        </c:forEach>
                    ]
                };

                // testcase完成个数
                var option4 = {
                    title: {
                        text: 'Test Case Count'
                    },
                    tooltip : {
                        trigger: 'axis',
                        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        }
                    },
                    legend: {
                        data: ['Completed','To be implemented']
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
                            <c:forEach var="podKey" items="${testcaseCountMap.keySet()}" >
                            	'${podKey}',
                            </c:forEach>
                        ]
                    },
                    series: [
                        {   name: 'Completed',
                            type: 'bar',
                            stack: '总量',
                            label: {
                                normal: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            },
                            data: [
                                <c:forEach var="podKey" items="${testcaseCountMap.keySet()}" >
								<c:if test="${empty testcaseCountMap.get(podKey).executedTestcases}">
									0,
								</c:if>
                                <c:if test="${not empty testcaseCountMap.get(podKey).executedTestcases}">
                                	${testcaseCountMap.get(podKey).executedTestcases.size()},
                                </c:if>
                                </c:forEach>
                            ]

                        },
                        {   name: 'To be implemented',
                            type: 'bar',
                            stack: '总量',
                            label: {
                                normal: {
                                    show: true,
                                    position: 'insideRight'
                                }
                            },
                            data: [
                                <c:forEach var="podKey" items="${testcaseCountMap.keySet()}" >
                                <c:if test="${empty testcaseCountMap.get(podKey).testcaseNumber}">
                                0,
                                </c:if>
                                <c:if test="${not empty testcaseCountMap.get(podKey).testcaseNumber}">
                                ${testcaseCountMap.get(podKey).testcaseNumber - testcaseCountMap.get(podKey).executedTestcases.size()},
                                </c:if>
                                </c:forEach>
                            ]

                        },

                    ]
                };

                function getPassRateData(){
                    var data = [];
                    <c:forEach var="heatKey" items="${heatMap.keySet()}" >
                    	data.push(['${heatKey}', '${heatMap.get(heatKey).passRate}']);
                    </c:forEach>
                    console.log(data[data.length - 1]);
                    return data;
                }
                function getExecutionRateData() {
                    var data = [];
                    <c:forEach var="heatKey" items="${heatMap.keySet()}" >
                    data.push(['${heatKey}', '${heatMap.get(heatKey).executionRate}']);
                    </c:forEach>
                    console.log(data[data.length - 1]);
                    return data;
                }

				var option5 = {
                    tooltip: {
                        position: 'top'
                    },

                    visualMap: [{
                        text: ['Execution Rate'],
                        min: 0,
                        max: 100,
                        inRange: {
                            color: ['#921aff'],
                            opacity: [0, 0.8]
                        },
                        calculable: true,
                        seriesIndex: [1],
                        orient: 'horizontal',
                        left: '55%',
                        bottom: 0,

						padding: 0
                    }, {
                        text: ['Pass Rate'],
                        min: 0,
                        max: 100,
                        inRange: {
                            color: ['#9ACD32'],
                            opacity: [0,0.8]
                        },
                        controller: {
                            inRange: {
                                opacity: [0.3, 0.6]
                            }
                        },
                        calculable: true,
                        seriesIndex: [0],
                        orient: 'horizontal',
                        left: '5%',
                        bottom: 0,
                        padding: 0


                    }],

                    calendar: [

                        {
                            orient: 'horizontal',
                            yearLabel: {
                                margin: 40
                            },
                            monthLabel: {
                                margin: 20
                            },
                            cellSize: 'auto',
                            left: 66,
							bottom: 50,
                            range: ['${heatStartDate}','${heatEndDate}']
                        } ],

                    series: [ {
                        type: 'heatmap',
                        coordinateSystem: 'calendar',
                        calendarIndex: 0,
                        data:getPassRateData()
                    }, {
                        type: 'effectScatter',
                        coordinateSystem: 'calendar',
                        calendarIndex: 0,
                        symbolSize: function (val) {
                            return val[1] / 10;
                        },
                        data: getExecutionRateData()
                    } ]
                };




                // 使用刚指定的配置项和数据显示图表。
                myChart1.setOption(option1);
                myChart2.setOption(option2);
                myChart3.setOption(option3);
                myChart4.setOption(option4);
                myChart5.setOption(option5);

                var app1 = -1;
                var lastApp1 = -1;
                var index1 = 0;
                var lastIndex1 = 0;
                setInterval(function(){
                    if (autoSwitchFlag){
						if (app1<option1.series[index1].data.length){
							app1++;
						} else {
							app1 = 0;
							index1 = (index1 + 1) % option1.series.length;
						}
						// 取消之前高亮的图形
						myChart1.dispatchAction({
							type: 'downplay',
							seriesIndex: lastIndex1,
							dataIndex: lastApp1
						});
						// 高亮当前图形
						myChart1.dispatchAction({
							type: 'highlight',
							seriesIndex: index1,
							dataIndex: app2
						});
						// 显示 tooltip
						myChart1.dispatchAction({
							type: 'showTip',
							seriesIndex: index1,
							dataIndex: app1
						});
						lastIndex1 = index1;
						lastApp1 = app1	;
                    }
                }, 2000);



                var app2 = -1;
                var lastApp2 = -1;
                var index2 = 0;
                var lastIndex2 = 0;

                setInterval(function(){
                    if (autoSwitchFlag) {
                        if (app2 < option2.series[index2].data.length) {
                            app2++;
                        } else {
                            app2 = 0;
                            index2 = (index2 + 1) % option2.series.length;
                        }
                        // 取消之前高亮的图形
                        myChart2.dispatchAction({
                            type: 'downplay',
                            seriesIndex: lastIndex2,
                            dataIndex: lastApp2
                        });
                        // 高亮当前图形
                        myChart2.dispatchAction({
                            type: 'highlight',
                            seriesIndex: index2,
                            dataIndex: app2
                        });
                        // 显示 tooltip
                        myChart2.dispatchAction({
                            type: 'showTip',
                            seriesIndex: index2,
                            dataIndex: app2
                        });
                        lastIndex2 = index2;
                        lastApp2 = app2;
                    }
				}, 2000);
			</script>

		</main>
		</div>
<div id="dialog1" title="Filter Charts Data" class="display: none; z-index:200;">
	<form action="<%=path%>/dashboards" method="POST" class="inputform">
		<label>Start Date:</label><input type="text" id="startDate" name="startDate" value="${startDate}"/><br/>
		<label>End Date:</label><input type="text" id="endDate" name="endDate" value="${endDate}"/><br/>
		<label>POD:</label><input type="text" name="pod" value="${pod}"/><br/>
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
	</select><br/>
		<label></label><input id="search" type="submit" value="Search"/><input type="reset" value="Reset"/>
		<!--div id="savedTime">${savedTime}</div><div id="totalAutoExecutionTime">${totalAutoExecutionTime}</div><div id="totalManualExecutionTime">${totalManualExecutionTime}</div-->
	</form>
</div>
		<jsp:include page="footer.jsp" />
</body>
</html>
