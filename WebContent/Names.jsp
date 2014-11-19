<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="Resources/Module1/CSS/NewFile.css" rel="stylesheet" type="text/css" />
<title>User Names</title>
</head>
<body>
<div class="wrapper">
<p class="seached-name">Search Name: ${searchname}</p>
<p>Total: ${count} results found.</p>
<table class="searched-reasult">
  <c:forEach var="item" items="${list}">
    <tr>
      <td>
<a href="UserInfo.jsp">
      ${item.firstName} ${item.lastName}
      </a>
      </td>
    </tr>
  </c:forEach>
</table>
<p>
Page: ${page_id}
</p>
<div class="pagination">
<c:if test="${page_id != 1}">
<a href="Search?name=${searchname}&page_id=${page_id-1}">Previous</a>
</c:if>
<span> | </span>
<c:if test="${remainingcount > 0}">
<a href="Search?name=${searchname}&page_id=${page_id+1}">Next</a>
</c:if>
</div>
</div>
</body>
</html>