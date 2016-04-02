package secretcoder.SQLite;

public class Quote {

    // ====== GLOBAL VARIABLES ======
    private int id;
    private String author, quote;

    /**
     *  Default empty constructor.
     *
     *  @see    Quote
     *  @since  1.0
     */
    public Quote() {}

    /**
     *  Default constructor.
     *
     *  @param  author  Author to be passed in
     *  @param  quote   Quote to be passed in
     *  @see            Quote
     *  @see            String
     *  @since          1.0
     */
    public Quote(String author, String quote){
        this.author = author;
        this.quote = quote;
    }

    /**
     *  Default constructor.
     *
     *  @param  id      ID to be passed in
     *  @param  author  Author to be passed in
     *  @param  quote   Quote to be passed in
     *  @see            Quote
     *  @see            String
     *  @since          1.0
     */
    public Quote(int id, String author, String quote){
        this.id = id;
        this.author = author;
        this.quote = quote;
    }

    // ====== GETTER METHODS ======
    public int getID() { return this.id; }
    public String getAuthor() { return this.author; }
    public String getQuote() { return this.quote; }

    // ====== SETTER METHODS ======
    public void setID(int id) { this.id = id; }
    public void setAuthor(String author) { this.author = author; }
    public void setQuote(String quote) { this.quote = quote; }
}