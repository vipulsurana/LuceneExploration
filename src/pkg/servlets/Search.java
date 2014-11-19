package pkg.servlets;

import pkg.classes.User;
import pkg.database.JdbcConstants;
import pkg.lucene.LuceneConstants;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.PriorityQueue;
import org.apache.lucene.util.Version;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
		response.setContentType("text/html");
		//PrintWriter out = response.getWriter();
		
		String page_id = request.getParameter("page_id");
		System.out.println(page_id);
		String name = request.getParameter("name");
		System.out.println(name);
		
		
		Directory d2 = FSDirectory.open(new File(LuceneConstants.INDEX_DIR));
	    DirectoryReader ireader = DirectoryReader.open(d2);
	    
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    
	    QueryParser parser = new QueryParser("firstname", new StandardAnalyzer());
	    
	    Query queryParsing = parser.parse(name);
	    System.out.println(queryParsing.toString());
	    
	    ScoreDoc[] hits = isearcher.search(queryParsing, null, 1000).scoreDocs;
	    System.out.println("Total hits: " + hits.length);
	    
	    int count = (Integer.parseInt(page_id) - 1) * 10;
	    int i = 0;
	    int iterate = count + 10;
	    ArrayList<User> list = new ArrayList<User>();
	    // Iterate through the results:
	    if(hits.length > 0){
		    if(hits.length > iterate){	    
			    for (i = count; i < iterate; i++) {
			      Document hitDoc = isearcher.doc(hits[i].doc);
			      int docId = hits[i].doc;
			      
			      Document d = isearcher.doc(docId);
			      System.out.println("Name: " +d.get("firstname") + " " + d.get("lastname"));
			      
			      System.out.println("UserId: " + hitDoc.get("userid"));
			      
			      User user = new User();
			      user.setUserid(d.get("userid"));
			      user.setFirstName(d.get("firstname"));
			      user.setLastName(d.get("lastname"));
			      
			      list.add(user);			     
			    }
			    request.setAttribute("remainingcount", (hits.length - iterate));
			    request.setAttribute("page_id",page_id);
			    request.setAttribute("count",hits.length);
			    request.setAttribute("searchname",name);
			    request.setAttribute("list", list);
		        RequestDispatcher rd = request.getRequestDispatcher("Names.jsp");
		        rd.forward(request, response);
		    }
		    else
		    {
		    	iterate = hits.length - count;
		    	System.out.println("i= " + count + ", iterate=" + iterate);
			    for (i = count; i < count+iterate; i++) {
				      Document hitDoc = isearcher.doc(hits[i].doc);
				      int docId = hits[i].doc;
				      
				      Document d = isearcher.doc(docId);
				      System.out.println("Name: " +d.get("firstname") + " " + d.get("lastname"));
				      
				      System.out.println("UserId: " + hitDoc.get("userid"));
				      
				      User user = new User();
				      user.setUserid(d.get("userid"));
				      user.setFirstName(d.get("firstname"));
				      user.setLastName(d.get("lastname"));
				      
				      list.add(user);			     
				    }
			    request.setAttribute("remainingcount", (hits.length - (count+iterate)));
			    request.setAttribute("page_id",page_id);
			    request.setAttribute("count",hits.length);
			    request.setAttribute("searchname",name);
			    request.setAttribute("list", list);
		        RequestDispatcher rd = request.getRequestDispatcher("Names.jsp");
		        rd.forward(request, response);
		    }
	    }	
		}
		    
		catch(Exception e)
		{
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// direct fetch from database (without Lucene)
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String searchBox = request.getParameter("txtSearch");
		String query = "SELECT * FROM tbl_users WHERE firstname ='"+ searchBox +"'";
		Connection con = null;
		IndexWriter iwriter = null;
		try
		{
			Class.forName(JdbcConstants.driversName);
			
			con = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
			//Statement s1 = con.createStatement();
			/*
			ResultSet rs1 = s1.executeQuery(query);

			while(rs1.next()){
				String userid = rs1.getString("userid");
				String firstname = rs1.getString("firstname");
				String lastname = rs1.getString("lastname");
				out.println(userid +", "+ firstname +" "+ lastname);
				System.out.println(userid +", "+ firstname +" "+ lastname);
			}
			*/
			
			// basic select query data to index 
			//String luceneQuery = "SELECT * FROM tbl_users";
			//Statement s2 = con.createStatement();
			//ResultSet rs2 = s2.executeQuery(luceneQuery);
			
			// now indexing the data in lucene
			
		    // Store the index in memory:
		    //Directory d1 = new RAMDirectory();

		    // To store an index on disk, use this instead:
		    Directory d2 = FSDirectory.open(new File(LuceneConstants.INDEX_DIR));
		    
		    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
		    iwriter = new IndexWriter(d2, config);
		    /*
			while(rs2.next()){
				Document doc = new Document();
			    doc.add(new StringField("userid",rs2.getString("userid"), Store.YES));
			    doc.add(new StringField("firstname",rs2.getString("firstname"), Store.YES));
			    doc.add(new StringField("lastname",rs2.getString("lastname"), Store.YES));
			    iwriter.addDocument(doc);
			}

			while(rs2.next()){
				Document doc = new Document();
				doc.add(new StringField("userid",rs2.getString(1), Store.YES));
				doc.add(new TextField("firstname",rs2.getString(2), Store.YES));
			    doc.add(new TextField("lastname",rs2.getString(3), Store.YES));
			    iwriter.addDocument(doc);
			}	
				*/
			// Now search the index:
		    DirectoryReader ireader = DirectoryReader.open(d2);
		    
		    IndexSearcher isearcher = new IndexSearcher(ireader);
		    // Parse a simple query that searches for "text":
		    
		    //single level query parser
		    //QueryParser parser = new QueryParser("firstname", new StandardAnalyzer());

		    //multi level query parser		    
		    MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] {"firstname", "lastname"}, new StandardAnalyzer());
		    Query queryParsing = parser.parse(searchBox);
		    System.out.println(queryParsing.toString());
		    
		    ScoreDoc[] hits = isearcher.search(queryParsing, null, 1000).scoreDocs;
		    
		    out.println("Total hits: " + hits.length);
		    System.out.println("Total hits: " + hits.length);
		    int i = 0;
		    ArrayList<User> list = new ArrayList<User>();
		    // Iterate through the results:
		    if(hits.length > 0){
			    if(hits.length > 10){
			    for (i = 0; i < 10; i++) {
				      Document hitDoc = isearcher.doc(hits[i].doc);
				      int docId = hits[i].doc;
				      
				      Document d = isearcher.doc(docId);
				      System.out.println("Name: " +d.get("firstname") + " " + d.get("lastname"));			      
				      System.out.println("UserId: " + hitDoc.get("userid"));
				      
				      User user = new User();
				      user.setUserid(d.get("userid"));
				      user.setFirstName(d.get("firstname"));
				      user.setLastName(d.get("lastname"));
				      
				      list.add(user);
			    	}
			    }
			    else
			    {
				    for (i = 0; i < hits.length; i++) {
					      Document hitDoc = isearcher.doc(hits[i].doc);
					      int docId = hits[i].doc;
					      
					      Document d = isearcher.doc(docId);
					      System.out.println("Name: " +d.get("firstname") + " " + d.get("lastname"));
					      
					      System.out.println("UserId: " + hitDoc.get("userid"));
					      
					      User user = new User();
					      user.setUserid(d.get("userid"));
					      user.setFirstName(d.get("firstname"));
					      user.setLastName(d.get("lastname"));
					      
					      list.add(user);
					    }
			    }

			    request.setAttribute("remainingcount", (hits.length - (i)));
			    request.setAttribute("page_id",1);
			    request.setAttribute("count",hits.length);
			    request.setAttribute("searchname",searchBox);
			    request.setAttribute("list", list);
		        RequestDispatcher rd = request.getRequestDispatcher("Names.jsp");
		        rd.forward(request, response);
			    
			    //ireader.close();
			    //d2.close();			    
		    }
		    else
		    {
		    	out.println("NO records to display.");
		    }
		}
		catch(Exception e)
		{
		}
		finally{
		    iwriter.close();
		}
	}
}
