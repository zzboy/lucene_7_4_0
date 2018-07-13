import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.TextField;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

public class DocValueFieldTest {

	public static void main(String[] args) throws IOException {
		String dirstr = "/tmp/test/";
		Directory dir = FSDirectory.open(Paths.get(dirstr));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setMaxBufferedDocs(10000);
		final IndexWriter indexWrt = new IndexWriter(dir, iwc);
		Random rand = new Random();
		for (int d = 0; d < 10240; ++d) {
			Document doc = new Document();
			/*
			int min = rand.nextInt(), max = rand.nextInt();
			if (min > max) {
				int a = min;
				min = max;
				max = a;
			}
			*/
			doc.add(new TextField("ha", d + " " + (d + 1) + " " + (d + 2)+ " " + (d + 3)+ " " + (d + 4), Field.Store.NO));
			//doc.add(new IntRange("xrange", new int[] { min }, new int[] { max }));
			//doc.add(new IntPoint("xint", rand.nextInt()));
			//doc.add(new StringField("xx", "hahah" + rand.nextDouble(), Store.YES));
			//doc.add(new SortedDocValuesField("xx", new BytesRef("hello" + rand.nextDouble())));
			//doc.add(new BinaryDocValuesField("xx", new BytesRef("hahahah".getBytes())));
			//doc.add(new NumericDocValuesField("xx", rand.nextLong()));
			indexWrt.addDocument(doc);
		}
		indexWrt.close();
		dir.close();
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(dirstr)));
		IndexSearcher searcher = new IndexSearcher(reader);
		int min = rand.nextInt(), max = rand.nextInt();
		if (min > max) {
			int a = min;
			min = max;
			max = a;
		}
		//Query query = new PhraseQuery("ha", new String[]{"" + 100, "" + 101}); //IntRange.newWithinQuery("xrange", new int[] {min}, new int[] {max});
		//Query query = new QueryParser("ha", analyzer).parse("ha:100 AND ha:103");
		Query query = new WildcardQuery(new Term("ha", "10*"));
		TopDocs hits = searcher.search(query, 10);

		System.out.println(hits.totalHits);
		reader.close();
	}

}
