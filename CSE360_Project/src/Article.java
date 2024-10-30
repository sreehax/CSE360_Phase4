import java.util.ArrayList;

public class Article {
  private String title;
  private String body;
  private ArrayList<String> references;
  private int id;
  private String header;
  private String grouping;
  private String description;
  private ArrayList<String> keywords;

  public Article(String title, String body, ArrayList<String> references, int id, String header, String grouping, String description, ArrayList<String> keywords) {
    this.title = title;
    this.body = body;
    this.references = new ArrayList<String>();
    for (String reference : references) {
      this.references.add(reference);
    }
    this.id = id;
    this.header = header;
    this.grouping = grouping;
    this.description = description;
    this.keywords = new ArrayList<String>();
    for (String keyword : keywords) {
      this.keywords = keywords;
    }
  }

  public Article() {
    this.title = "";
    this.body = "";
    this.references = new ArrayList<String>();
    this.id = -1;
    this.header = "";
    this.grouping = "";
    this.description = "";
    this.keywords = new ArrayList<String>();
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return this.body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public ArrayList<String> getReferences() {
    return this.references;
  }
  
  public String getReferencesStr() {
	  String ret = this.references.toString();
	  ret.substring(1, ret.length() - 1);
	  return ret;
  }

  public void addReference(String reference) {
    this.references.add(reference);
  }

  public int getID() {
    return this.id;
  }

  public void setID(int id) {
    this.id = id;
  }

  public String getHeader() {
    return this.header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public String getGrouping() {
    return this.grouping;
  }

  public void setGrouping(String grouping) {
    this.grouping = grouping;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ArrayList<String> getKeywords() {
    return this.keywords;
  }
  
  public String getKeywordsStr() {
	  String ret = this.keywords.toString();
	  ret.substring(1, ret.length() - 1);
	  return ret;
  }

  public void addKeyword(String keyword) {
    this.keywords.add(keyword);
  }
}
