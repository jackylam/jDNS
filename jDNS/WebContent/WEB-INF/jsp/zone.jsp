<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="${pageContext.request.contextPath}">
<link rel="stylesheet" type="text/css" href="/jdns/css/index.css" /> 
<!-- <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"> -->
<title>Zone Details</title>
</head>
<body>
<%
//allow access only if session exists
String user = null;
	if(session.getAttribute("user") == null){
		request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
		
}else user = (String) session.getAttribute("user");
%>

<div id="container" style="width:auto">

<div id="header">
<h1>jDNS</h1>
<span style="float:right">testing</span>
</div>

<div id="menu">
<h3><a href="/jdns/main.jsp">Zones</a></h3>
<h3><a href="/jdns/stats.jsp">Statistics</a></h3>
</div>

<div id="content">
<table>
	<tr>
		<th>ID</th>
		<th>Domain ID</th>
		<th>Name</th>
		<th>Type</th>
		<th>Content</th>
		<th>TTL</th>
		<th>Priority</th>
		<th>Change Date</th>
		<th>Disabled</th>
		<th>Order Name</th>
		<th>Authority</th>
	</tr>
	<c:forEach var="entry" items="${requestScope.records}">
  	<tr>
  		<td><c:out value="${entry.getId()}"/></td>
  		<td><c:out value="${entry.getDomainId()}"/></td>
  		<td><c:out value="${entry.getName()}"/></td>
  		<td><c:out value="${entry.getType()}"/></td>
  		<td><c:out value="${entry.getContent()}"/></td>
  		<td><c:out value="${entry.getTtl()}"/></td>
  		<td><c:out value="${entry.getPriority()}"/></td>
  		<td><c:out value="${entry.getChangeDate()}"/></td>
  		<td><c:out value="${entry.getDisabled()}"/></td>
  		<td><c:out value="${entry.getOrderName()}"/></td>
  		<td><c:out value="${entry.getAuth()}"/></td>
  	</tr>
</c:forEach>
</table>
<br/>
<br/>
<br/>
</div>

<div id="footer" style="background-color:#1E90FF;clear:both;text-align:center;">
Copyright © jaks.me</div>

</div>

</body>
</html>

</html>