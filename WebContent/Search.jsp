 <%@ page import="java.sql.*" %>
 <%@ page import="pkg.database.JdbcConstants" %>
 <%@ page import="java.io.PrintWriter" %> 
 <%@ page import="java.sql.Connection" %>
 <%@ page import="java.sql.DriverManager" %>
 <%@ page import="java.sql.ResultSet" %>
 <%@ page import="java.io.IOException" %>
    <html>
    <form>
    <table>
    <%
	response.setContentType("text/html");  
	PrintWriter pw = response.getWriter();  
	String searchBox = request.getParameter("txtSearch");
	//String searchBox = request.getParameter("txtSearch");

	String query = "SELECT * FROM tbl_users WHERE firstname ='"+ searchBox +"'";
	Class.forName(JdbcConstants.driversName);
	Connection con = null;
	Statement s = null;
	ResultSet rs = null;
	con = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
	
	s = con.createStatement();

	rs = s.executeQuery(query);
	while(rs.next()){
		String userId = rs.getString("userid");
		String firstName = rs.getString("firstname");
		String lastName = rs.getString("lastname");
		out.println(userId +", "+ firstName +" "+ lastName);
		System.out.println(userId +", "+ firstName +" "+ lastName);
	}
    %>
    </table>
    </form>
</html>

<%--
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
</html>
--%>