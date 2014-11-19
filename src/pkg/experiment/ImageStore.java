package pkg.experiment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;

import pkg.database.JdbcConstants;

public class ImageStore {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Class.forName(JdbcConstants.driversName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Connection con = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
			
			PreparedStatement ps=con.prepareStatement("insert into TBL_USER_INFO values(2,2,TO_DATE('1989-12-09','YYYY-MM-DD'),'Basic Information of User 2',?)");
			FileInputStream fin;
			try {
				fin = new FileInputStream("D:\\Health Care\\Images\\1.jpg");
				try {
					ps.setBinaryStream(1,fin,fin.available());
					//ps.setBlob(2, fin,fin.available());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				int i=ps.executeUpdate();  
				System.out.println(i+" records affected");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
