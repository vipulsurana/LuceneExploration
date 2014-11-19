package pkg.experiment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pkg.database.JdbcConstants;

public class ImageRetreive {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		try {
			Class.forName(JdbcConstants.driversName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			Connection con;
			try {
				System.out.println("Start");
				con = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
				PreparedStatement ps=con.prepareStatement("select * from TBL_USER_INFO");  
				ResultSet rs=ps.executeQuery();  
				if(rs.next()){//now on 1st row  
				              
				//InputStream is = rs.getBlob("3").getBinaryStream();
				Blob b = rs.getBlob("image");//2 means 2nd column data  
				byte barr[]=b.getBytes(1,(int)b.length());//1 means first image
				
				FileOutputStream fout;
				try {
					//takes image from database and creates the same image 2.jpg
					fout = new FileOutputStream("D:\\Health Care\\Images\\2.jpg");
					fout.write(barr);  		              
					fout.close(); 
					System.out.println("End");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}
}
