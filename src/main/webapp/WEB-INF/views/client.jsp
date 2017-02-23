<%--
  User: Paul Chan
  Date: 2016/11/25
  Time: 0:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>加载客户端</title>
    <script src="<%=path%>/js/jquery-2.1.4.min.js"></script>
    <% String userId=request.getParameter("userId");%>
    <% String tokenId=request.getParameter("tokenId");%>
    <% String serverIp=request.getParameter("serverIp");%>
    <% String remoteUserId=request.getParameter("remoteUserId");%>
    <% String remoteUserPassword=request.getParameter("remoteUserPassword");%>
    <% String remotePort=request.getParameter("remotePort");%>
    <% String hubRegisterLink=request.getParameter("hubRegisterLink");%>
    <% String version=request.getParameter("version");%>
    <% String message=request.getParameter("message");%>
    <script type="text/javascript">
        $(function(){
            window.open('creepers://userId=${staffId}&tokenId=${tokenId}&serverIp=${serverIp}&remoteUserId=${remoteUserId}&remoteUserPassword=${remoteUserPassword}&remotePort=${remotePort}&hubRegisterLink=${hubRegisterLink}&version=${version}&message=${message}','_self');
            setTimeout(function(){window.close()}, 2000);
        });
    </script>
</head>
<body>
请点击【是】切换到应用。此页面将自动关闭。

</body>
</html>
