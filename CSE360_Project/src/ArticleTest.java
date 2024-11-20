import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ArticleTest {

	@Test
	void testArticleStringStringArrayListOfStringIntStringStringStringArrayListOfString() {
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		
	}

	@Test
	void testArticle() {
		Article a = new Article();
	}

	@Test
	void testPrintInfo() {
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		a.printInfo();
	}

	@Test
	void testGetTitle() {
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		a.getTitle();
	}

	@Test
	void testSetTitle() {
		Article a = new Article();
		a.setTitle("testTitle");
	}

	@Test
	void testGetBody() {
		Article a = new Article();
		a.getBody();
	}

	@Test
	void testSetBody() {
		Article a = new Article();
		a.setBody("testBody");
	}

	@Test
	void testGetReferences() {
		Article a = new Article();
		a.getReferences();
	}

	@Test
	void testGetReferencesStr() {
		Article a = new Article();
		a.getReferencesStr();
	}

	@Test
	void testAddReference() {
		Article a = new Article();
		a.addReference("testReference");
	}

	@Test
	void testGetID() {
		Article a = new Article();
		a.getID();
	}

	@Test
	void testSetID() {
		Article a = new Article();
		a.setID(1);
	}

	@Test
	void testGetHeader() {
		Article a = new Article();
		a.getHeader();
	}

	@Test
	void testSetHeader() {
		Article a = new Article();
		a.setHeader("testHeader");
	}

	@Test
	void testGetGrouping() {
		Article a = new Article();
		a.getGrouping();
	}

	@Test
	void testSetGrouping() {
		Article a = new Article();
		a.setGrouping("testGroup");
	}

	@Test
	void testGetDescription() {
		Article a = new Article();
		a.getDescription();
	}

	@Test
	void testSetDescription() {
		Article a = new Article();
		a.setDescription("testDescription");
	}

	@Test
	void testGetKeywords() {
		Article a = new Article();
		a.getKeywords();
	}

	@Test
	void testGetKeywordsStr() {
		Article a = new Article();
		a.getKeywords();
	}

	@Test
	void testAddKeyword() {
		Article a = new Article();
		a.addKeyword("testKeyword");
	}

}
