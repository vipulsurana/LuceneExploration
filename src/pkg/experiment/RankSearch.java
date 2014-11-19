package pkg.experiment;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Weight;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import pkg.lucene.LuceneConstants;

public class RankSearch {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String query = "select * from tbl_users";
		
		Directory d2 = FSDirectory.open(new File(LuceneConstants.INDEX_DIR));
		DirectoryReader ireader = DirectoryReader.open(d2);
	    IndexSearcher isearcher = new IndexSearcher(ireader);
	    Query tq = new QueryParser("firstname", new StandardAnalyzer()).parse("Aaima"); //27 hits
		TopDocs hits=isearcher.search(tq, 100);
		
		//TopDocs hits = searcher.search(q,maxHits);
        ScoreDoc[] scoreDocs = hits.scoreDocs;
        System.out.println("hits=" + scoreDocs.length);
        System.out.println("Hits (rank,score,docId)");
        for (int n = 0; n < scoreDocs.length; ++n) {
            ScoreDoc sd = scoreDocs[n];
            float score = sd.score;
            int docId = sd.doc;
            Document d = isearcher.doc(docId);
            String fname = d.get("firstname");
            String lname = d.get("lastname");
            System.out.printf(n + " " + score + " " + docId + " - " + fname + " " + lname + "\n");
        }
        
        
	}
}
