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
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<link rel="shortcut icon" href="<%=path%>/css/images/xbot_small.ico" type="image/x-icon"/>
<script src="<%=path%>/js/echarts.min.js"></script>
<script src="<%=path%>/js/jquery.waypoints.min.js"></script>
<script src="<%=path%>/js/jquery-2.1.4.min.js"></script>
<script src="<%=path%>/js/jquery-ui.min.js"></script>
<script src="<%=path%>/js/jquery.countup.min.js"></script>
<title>Events</title>
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

		});
        /*新闻滚动*/
        $(function(){
            //将$(".scrollNews")对象作为参数传递给scollNews()函数的参数
            var $this = $(".scrollNews");
            //滚动定时器变量
            var scrollTimer;
            //hover()方法的含义是鼠标滑入滑出，它对应着两个事件，即mouseenter和mouseleave，因此可通过trigger("mouseleave")来触发hover事件的第二个函数
            $this.hover(
                function(){
                    clearInterval(scrollTimer);
                },
                function(){
                    scrollTimer = setInterval(function(){
                        scrollNews( $this );//每3秒执行一次scrollNews函数
                    }, 3000 );
                }
            ).trigger("mouseleave");//当用户进入页面后就会触发hover事件的第二个函数，从而使内容滚动起来

        });
        function scrollNews(obj){
            var $self = obj.find("ul:first"); //找到第一个ul元素
            var lineHeight = $self.find("li:first").height(); //获取第一个li元素的行高
            $self.animate({ "marginTop" : -lineHeight +"px" }, 600 , function(){
                //alert($self.css({marginTop:0}).find("li:first").text());//逐条获取
                //把所有匹配的元素追加到$self元素的后面,所以才出现这种周而复始滚动的效果
                $self.css({marginTop:0}).find("li:first").appendTo($self); //appendTo能直接移动元素
            })
        }

	</script>

</head>

<body>

<jsp:include page="header.jsp" />

<h3>Activities in last 2 hours ...</h3>
<c:if test="${records==null}">
	There is no activity in last 2 hours.
</c:if>
<div class="scrollNews" >
	<ul>
	<c:forEach var="record" items="${records}">
		<li style="list-style-type:none;">
		<c:if test="${record.result=='Success'}">
			<div class="ui-widget" style="padding: 0.1em;">
				<div class="ui-state-passed ui-corner-all" style="padding: 0 .7em;">
					<p><span class="ui-icon ui-icon-check" style="float: left; margin-right: .3em;"></span>
						[${record.test.project.category}]${record.test.project.productName} - <img src="css/images/time.jpg" style="width:1em;height:1em;"/>${record.endTime} - ${record.test.name} - ${record.test.description} - Saved: ${record.test.manualExecutionTime}</p>
				</div>
			</div>
		</c:if>
		<c:if test="${record.result!='Success'}">
			<div class="ui-widget" style="padding: 0.1em;">
				<div class="ui-state-error ui-corner-all" style="padding: 0 .7em;">
					<p><span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
						[${record.test.project.category}]${record.test.project.productName} - <img src="css/images/time.jpg" style="width:1em;height:1em;"/>${record.endTime} - ${record.test.name} - ${record.test.description} </p>
				</div>
			</div>
		</c:if>
		</li>
	</c:forEach>
	</ul>
</div>


</div>
		<jsp:include page="footer.jsp" />
</body>
</html>
