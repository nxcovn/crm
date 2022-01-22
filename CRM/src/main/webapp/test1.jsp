<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <title>mytitle</title>
</head>
<body>
$.ajax({
    url:"",
    data:{},
    type:"",
    dataType:"json",
    success:function (data) {

    }
})
</body>
</html>
