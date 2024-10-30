import java.util.ArrayList;

/**
 * The Article class represents an article with attributes like title, body, references, 
 * unique identifier, header, grouping, description, and keywords. 
 * It provides methods to retrieve, modify, and display information related to the article.
 */
public class Article {
  private String title;
  private String body;
  private ArrayList<String> references;
  private int id;
  private String header;
  private String grouping;
  private String description;
  private ArrayList<String> keywords;

	 /**
     * Constructs an Article object with the specified title, body, references, ID, header, 
     * grouping, description, and keywords.
     *
     * @param title       The title of the article
     * @param body        The main content or body of the article
     * @param references  A list of references related to the article
     * @param id          A unique identifier for the article
     * @param header      The header of the article
     * @param grouping    The grouping category for the article
     * @param description A brief description of the article
     * @param keywords    A list of keywords associated with the article
     */
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
  
  /**
     * Constructs an empty Article object with default values.
     * Fields like title, body, header, grouping, and description are initialized as empty strings.
     * The ID is initialized to -1, and empty ArrayLists are assigned to references and keywords.
     */
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
  
   /**
     * Displays all information about the article, including title, references, header, 
     * grouping, description, keywords, and body.
     */
  public void printInfo() {
	  System.out.println("==========");
	  System.out.println("Article ID#" + this.id);
	  System.out.println("Title: " + this.title);
	  System.out.println("References: " + this.getReferencesStr());
	  System.out.println("Header: " + this.header);
	  System.out.println("Grouping: " + this.grouping);
	  System.out.println("Description: " + this.description);
	  System.out.println("Keywords: " + this.getKeywordsStr());
	  System.out.println("Body: ");
	  System.out.println(this.body);
	  System.out.println("==========");
  }
  /**
     * Retrieves the title of the article.
     *
     * @return The title of the article
     */
  public String getTitle() {
    return this.title;
  }
  /**
     * Sets the title of the article.
     *
     * @param title The new title to be set
     */
  public void setTitle(String title) {
    this.title = title;
  }
 /**
     * Retrieves the body content of the article.
     *
     * @return The body content of the article
     */
  public String getBody() {
    return this.body;
  }
  /**
     * Sets the body content of the article.
     *
     * @param body The new body content to be set
     */
  public void setBody(String body) {
    this.body = body;
  }
 /**
     * Retrieves the list of references associated with the article.
     *
     * @return A list of references
     */
  public ArrayList<String> getReferences() {
    return this.references;
  }
   /**
     * Returns the references as a comma-separated string.
     *
     * @return A string representation of the references
     */
  public String getReferencesStr() {
	  String ret = this.references.toString();
	  ret = ret.substring(1, ret.length() - 1);
	  return ret;
  }
   /**
     * Adds a reference to the article's list of references.
     *
     * @param reference The reference to be added
     */
  public void addReference(String reference) {
    this.references.add(reference);
  }
  /**
     * Retrieves the unique identifier (ID) of the article.
     *
     * @return The ID of the article
     */
  public int getID() {
    return this.id;
  }

    /**
     * Sets the unique identifier (ID) of the article.
     *
     * @param id The new ID to be set
     */
  public void setID(int id) {
    this.id = id;
  }
 /**
     * Retrieves the header of the article.
     *
     * @return The header of the article
     */
  public String getHeader() {
    return this.header;
  }
  /**
     * Sets the header of the article.
     *
     * @param header The new header to be set
     */
  public void setHeader(String header) {
    this.header = header;
  }
 /**
     * Retrieves the grouping category of the article.
     *
     * @return The grouping category of the article
     */
  public String getGrouping() {
    return this.grouping;
  }

    /**
     * Sets the grouping category of the article.
     *
     * @param grouping The new grouping category to be set
     */
  public void setGrouping(String grouping) {
    this.grouping = grouping;
  }
    /**
     * Retrieves the description of the article.
     *
     * @return The description of the article
     */
  public String getDescription() {
    return this.description;
  }
 /**
     * Sets the description of the article.
     *
     * @param description The new description to be set
     */
  public void setDescription(String description) {
    this.description = description;
  }
 /**
     * Retrieves the list of keywords associated with the article.
     *
     * @return A list of keywords
     */
  public ArrayList<String> getKeywords() {
    return this.keywords;
  }
   /**
     * Returns the keywords as a comma-separated string.
     *
     * @return A string representation of the keywords
     */
  public String getKeywordsStr() {
	  String ret = this.keywords.toString();
	  ret = ret.substring(1, ret.length() - 1);
	  return ret;
  }
 /**
     * Adds a keyword to the article's list of keywords.
     *
     * @param keyword The keyword to be added
     */
  public void addKeyword(String keyword) {
    this.keywords.add(keyword);
  }
}
