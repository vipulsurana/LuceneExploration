<!-- 
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Information</title>
</head>
<body>
<p><b>
Basic Information:
</b>
</p>
<p>
ID: ${user.userid}
</p>
<p>
FirstName: ${user.firstName}
</p>
<p>
LastName: ${user.lastName}
</p>
</body>
</html>
 -->
 
<%@ page language="java" contentType="text/html; charset=windows-1256"
        pageEncoding="windows-1256"%>
        <%@ page import="java.sql.*" %>
         <%@ page import="java.io.*" %>
         <%@ page import="pkg.database.JdbcConstants" %>
         <%@ page trimDirectiveWhitespaces="true" %>
    <!DOCTYPE>
    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Image show example</title>
    </head>
    <body>
     <H1>Fetching Data From a Database</H1>
    <%

    Blob image = null;  
    byte[] imgData = null;  
    
	String query = "select A.userid, firstname, lastname, image, basic_info, dob from TBL_USERS A JOIN TBL_USER_INFO B ON (A.USERID = B.USERID)";
	
	Class.forName(JdbcConstants.driversName);
	Connection conn = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
        
        Statement s1 = conn.createStatement();
		ResultSet rs1 = s1.executeQuery(query);
		
		while(rs1.next()){
			String userid = rs1.getString("userid");
			String firstname = rs1.getString("firstname");
			String lastname = rs1.getString("lastname");
			
			Date dob = rs1.getDate("dob");
			String basic_info = rs1.getString("basic_info");
			System.out.println(userid +", "+ firstname +" "+ lastname+", "+ dob +", "+ basic_info);
			
			Blob b1=rs1.getBlob("image");
	        byte x[]=b1.getBytes(1, (int)b1.length());
	        response.setContentType("image/jpg");
	        OutputStream o=response.getOutputStream();
    %>
    <TABLE BORDER="1">
    <TR>

    <td>Image</td>
    <td><img src="<% o.write(x); %>" alt="image1" title="image1" /></td>
    
        <% o.flush();%>
    </TR>
    </TABLE>
    <BR>
    <%
    o.close();
    }
    %>
    </body>
    </html>