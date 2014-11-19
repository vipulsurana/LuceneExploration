package pkg.experiment;

import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.lucene.index.IndexWriter;

import pkg.database.JdbcConstants;

public class IndexMultipleTables {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String query = "select A.userid, firstname, lastname, image, basic_info, dob from TBL_USERS A JOIN TBL_USER_INFO B ON (A.USERID = B.USERID)";
		
		Class.forName(JdbcConstants.driversName);		
		Connection conn = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
		
		Statement s1 = conn.createStatement();
		ResultSet rs1 = s1.executeQuery(query);
		
		while(rs1.next()){
			String userid = rs1.getString("userid");
			String firstname = rs1.getString("firstname");
			String lastname = rs1.getString("lastname");
			Blob image = rs1.getBlob("image");
			Date dob = rs1.getDate("dob");
			String basic_info = rs1.getString("basic_info");
			System.out.println(userid +", "+ firstname +" "+ lastname+", "+ dob +", "+ basic_info);
			//out.println(image);
		}
		
	}

}
