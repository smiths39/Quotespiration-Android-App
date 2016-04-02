package secretcoder.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // ====== GLOBAL VARIABLES ======
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favouriteQuotes";
    private static final String TABLE_QUOTES = "quotes";

    private static final String KEY_ID = "id";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_QUOTE = "quote";

    /**
     *  Default constructor.
     *
     *  @param  context     Context of the activity calling the class
     *  @see                DatabaseHandler
     *  @see                Context
     *  @since              1.0
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *  Initialises the database when the application starts.
     *
     *  @param  database    The SQLite database to be initialised
     *  @see                SQLiteDatabase
     *  @see                String
     *  @since              1.0
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_QUOTES_TABLE = "CREATE TABLE " + TABLE_QUOTES + "("
                                                     + KEY_ID + " INTEGER PRIMARY KEY,"
                                                     + KEY_AUTHOR + " TEXT,"
                                                     + KEY_QUOTE + " TEXT)";
        database.execSQL(CREATE_QUOTES_TABLE);
    }

    /**
     *  Upgrades the table if changes are invoked.
     *
     *  @param  database    The SQLite database being invoked
     *  @param  oldVersion  The old version of the database
     *  @param  newVersion  The new version of the database
     *  @see                Void
     *  @see                SQLiteDatabase
     *  @since              1.0
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTES);
        onCreate(database);
    }

    /**
     *  Adds a quote and author to the database.
     *
     *  @param  quote   The quote and the corresponding author to be added
     *  @see            Void
     *  @see            Quote
     *  @see            SQLiteDatabase
     *  @see            ContentValues
     *  @since          1.0
     */
    public void addQuote(Quote quote) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AUTHOR, quote.getAuthor());
        values.put(KEY_QUOTE, quote.getQuote());

        database.insert(TABLE_QUOTES, null, values);
        database.close();
    }

    /**
     *  Determines if the quote already exists within the database.
     *
     *  @param  quote   The quote to be verified
     *  @see            Boolean
     *  @see            String
     *  @see            SQLiteDatabase
     *  @see            Cursor
     *  @return         True or False on whether the quote already exists
     *  @since          1.0
     */
    public boolean quoteExists(String quote) {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_QUOTES, new String[]{KEY_ID, KEY_AUTHOR, KEY_QUOTE}, KEY_QUOTE + "=?", new String[]{String.valueOf(quote)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    /**
     *  Returns a list containing all quotes contained within the database.
     *
     *  @see        List
     *  @see        Quote
     *  @see        String
     *  @see        SQLiteDatabase
     *  @see        Cursor
     *  @return     The list containing all existing quotes
     *  @since      1.0
     */
    public List<Quote> getAllQuotes() {
        List<Quote> quoteList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_QUOTES;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Quote quote = new Quote();
                quote.setID(Integer.parseInt(cursor.getString(0)));
                quote.setAuthor(cursor.getString(1));
                quote.setQuote(cursor.getString(2));

                quoteList.add(quote);
            } while (cursor.moveToNext());
        }
        return quoteList;
    }

    /**
     *  Delete a quote from the database.
     *
     *  @param  quote   The quote to be deleted
     *  @see            Void
     *  @see            Quote
     *  @see            String
     *  @see            SQLiteDatabase
     *  @since          1.0
     */
    public void deleteQuote(Quote quote) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_QUOTES, KEY_ID + "=?", new String[]{String.valueOf(quote.getID())});
        database.close();
    }
}
