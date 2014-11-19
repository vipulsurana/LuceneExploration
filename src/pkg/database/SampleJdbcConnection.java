package pkg.database;

import java.sql.*;

public class SampleJdbcConnection {
	
	public static void main(String[] args) throws Exception {
		
		final String query = "select * from tbl_users where firstname='Aahana'";
		Class.forName(JdbcConstants.driversName);
		//Connection with Oracle 12c
		Connection con = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
		Statement s=con.createStatement();
		ResultSet rs=s.executeQuery(query);
		while(rs.next()){
		System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3));
		}
		con.close();
	}
}
