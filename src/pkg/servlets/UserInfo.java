package pkg.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pkg.database.JdbcConstants;

/**
 * Servlet implementation class UserInfo
 */
@WebServlet("/UserInfo")
public class UserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		String id = request.getParameter("userid");
		try {
			Class.forName(JdbcConstants.driversName);
			
			try {
				String query = "select A.userid, firstname, lastname, image, basic_info, dob from TBL_USERS A JOIN TBL_USER_INFO B ON (A.USERID = B.USERID)";
				
				Class.forName(JdbcConstants.driversName);		
				Connection conn = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
				
				Statement s1 = conn.createStatement();
				ResultSet rs1 = s1.executeQuery(query);
				
				while(rs1.next()){
					String userid = rs1.getString("userid");
					String firstname = rs1.getString("firstname");
					String lastname = rs1.getString("lastname");
					
					Blob b1=rs1.getBlob("image");
			        byte x[]=b1.getBytes(1, (int)b1.length());
			        OutputStream o=response.getOutputStream();
			        o.write(x);
			        
					//Blob image = rs1.getBlob("image");
					//byte[] imgData = image.getBytes(1,(int)image.length());
					Date dob = rs1.getDate("dob");
					String basic_info = rs1.getString("basic_info");
					System.out.println(userid +", "+ firstname +" "+ lastname+", "+ dob +", "+ basic_info);
					out.println(o);
				}
				/*
				Connection conn = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
				String userInfoQuery = "SELECT * FROM tbl_users where userid ='"+ id +"'";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(userInfoQuery);
				User user = null;
				while(rs.next())
				{
					  user = new User();
				      user.setUserid(rs.getString("userid"));
				      user.setFirstName(rs.getString("firstname"));
				      user.setLastName(rs.getString("lastname"));
				}
			    request.setAttribute("user", user);
		        RequestDispatcher rd = request.getRequestDispatcher("UserInfo.jsp");
		        rd.forward(request, response);
		        */
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
