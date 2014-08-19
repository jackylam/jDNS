<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="${pageContext.request.contextPath}">
<link rel="stylesheet" type="text/css" href="/jdns/css/index.css" /> 
<title>Login Page</title>
</head>
<body>

<div id="container" style="width:auto">

<div id="header">
<h1 style="margin-bottom:0;">jDNS</h1></div>

<div id="menu">
</div>

<div id="content">
<br/>
<br/>
<br/>
<form action="/jdns/login" method="POST">
   Email:<input type="text" name="email" style="margin:25px">
<br/>
Password:<input type="password" name="password" />
<br/>
<br/>
<input type="submit" value="Submit" />
</form></div>

<div id="footer" style="background-color:#1E90FF;clear:both;text-align:center;">
Copyright © jaks.me</div>

</div>

</body>
</html>

</html>