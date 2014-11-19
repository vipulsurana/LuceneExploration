package pkg.experiment;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import pkg.database.JdbcConstants;
import pkg.lucene.LuceneConstants;

public class SearchUpdateSearchLucene {

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException {
		// TODO Auto-generated method stub
		String query = "update tbl_users set firstname='Vipul' where firstname='Aaima' AND lastname='Aaby'";
		//System.out.println("Start");
		Class.forName(JdbcConstants.driversName);
		Connection conn = DriverManager.getConnection(JdbcConstants.connectionString,JdbcConstants.userName,JdbcConstants.passWord);
		
		PreparedStatement s1 = conn.prepareStatement(query);
		s1.executeUpdate();
		//System.out.println("End & Start again");
		
		String query2 = "select * from tbl_users where firstname='Vipul'";
		Statement s2 = conn.createStatement();
		ResultSet rs1 = s2.executeQuery(query2);
		
		while(rs1.next())
		{
			String userid = rs1.getString("userid");
			String firstname = rs1.getString("firstname");
			String lastname = rs1.getString("lastname");			
			//System.out.println(userid + " - " + firstname + " " + lastname);
		}
		
		//System.out.println("End again");
		
		Directory d2 = FSDirectory.open(new File(LuceneConstants.INDEX_DIR));
		DirectoryReader ireader = DirectoryReader.open(d2);	    
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
	    IndexWriter iwriter = new IndexWriter(d2, config);
	    //update Document
	  //QueryParser parser = new QueryParser("firstname", new StandardAnalyzer());

	    //multi level query parser		    
	    //MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] {"firstname", "lastname"}, new StandardAnalyzer());
		
		String query3 = "select * from tbl_users where firstname='Vipul'";
		Statement s3 = conn.createStatement();
		ResultSet rs2 = s3.executeQuery(query3);
		
		while(rs2.next())
		{
			String userid = rs2.getString("userid");
			String firstname = rs2.getString("firstname");
			String lastname = rs2.getString("lastname");			
			//System.out.println(userid + " - " + firstname + " " + lastname);
			
			Document doc = new Document();
			doc.add(new StringField("userid",rs2.getString("userid"), Store.YES));
			doc.add(new TextField("firstname",rs2.getString("firstname"), Store.YES));
			doc.add(new TextField("lastname",rs2.getString("lastname"), Store.YES));
			System.out.println(rs2.getString("userid") + " - " + rs2.getString("firstname") + " " + rs2.getString("lastname"));
			
			iwriter.updateDocument(new Term("userid","1"),doc);
		}
		iwriter.close();
		
		Query tq = new QueryParser("firstname", new StandardAnalyzer()).parse("Vipul");
	    //Query queryParsing = MultiFieldQueryParser.parse(new String[] {"Vipul", "Aaby"},new String[] {"firstname", "lastname"}, new StandardAnalyzer());
		ScoreDoc[] hits = isearcher.search(tq, null, 1000).scoreDocs;
		System.out.println(hits.length);
		
		for (int i = 0; i < hits.length; i++) {
			Document hitDoc = isearcher.doc(hits[i].doc);
			int docId = hits[i].doc;			  
			Document d = isearcher.doc(docId);
			System.out.println("Doc Id:    " + docId + ", " + "UserId: " + hitDoc.get("userid") + ", " + "Name: " +d.get("firstname") + " " + d.get("lastname"));
		}
		    
	    // Query Parser and Similarity to rank documents
	    //Query tq = new QueryParser("firstname", new StandardAnalyzer()).parse("Vipul"); //0 hits
	    //Query tq = new QueryParser("firstname", new StandardAnalyzer()).parse("Aaima"); //27 hits
	    //Query tq = new QueryParser("lastname", new StandardAnalyzer()).parse("Aaby"); // 2 hits
	    //Query tq = new MultiFieldQueryParser(new String[] {"firstname", "lastname"}, new StandardAnalyzer()).parse("Vipul");
	    //TopDocs docs=isearcher.search(tq, 100);
	    
	    //dont know what this line does
	    //isearcher.setSimilarity(new BM25Similarity());
	    
	    
	    //System.out.println("Total hits: " + docs.totalHits);
	    
		//IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
	    //IndexWriter iwriter = new IndexWriter(d2, config);
	    //what is field and text in below line
	    //Term term = new Term("", "");
	    
	    //create a document and update it from previous
	    //Document doc = new Document();
	    //iwriter.updateDocument(term, doc);
	}

}
