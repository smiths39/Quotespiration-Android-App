package secretcoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import secretcoder.CustomListView.CustomListAdapter;
import secretcoder.SQLite.DatabaseHandler;
import secretcoder.SQLite.Quote;

public class FavouriteQuotes extends Activity {

    // ====== GLOBAL VARIABLES ======
    private static ListView listView;
    private static CustomListAdapter adapter;
    private static ProgressDialog progressDialog;
    private static List<Quote> quoteList = new ArrayList<>();

    /**
     *  Called when activity is starting.
     *
     *  @param  savedInstanceState  The data of the activity re-initialized after previously being shut down
     *  @see                        R
     *  @see                        Bundle
     *  @see                        Void
     *  @since                      1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        initialiseCustomListView();
        initialiseProgressDialog();

        populateQuoteListView();
        setListViewListeners();
    }

    /**
     *  Initialises all components corresponding to custom listview.
     *
     *  @see    R
     *  @see    Void
     *  @see    CustomListAdapter
     *  @since  1.0
     */
    private void initialiseCustomListView() {
        listView = (ListView) findViewById(R.id.lvFavourites);
        adapter = new CustomListAdapter(this, quoteList);
        listView.setAdapter(adapter);
    }

    /**
     *  Initialises all components corresponding to progress dialog.
     *
     *  @see    R
     *  @see    Void
     *  @see    ProgressDialog
     *  @since  1.0
     */
    private void initialiseProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
        progressDialogVisibility();
    }

    /**
     *  Toggles the visibility of the progress dialog.
     *
     *  @see    Void
     *  @see    ProgressDialog
     *  @since  1.0
     */
    private void progressDialogVisibility() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
            progressDialog.show();
        }
    }

    /**
     *  Populates the favourites quote list with data from SQLite database.
     *
     *  @see    Void
     *  @see    DatabaseHandler
     *  @see    List
     *  @see    Quote
     *  @see    CustomListAdapter
     *  @since  1.0
     */
    private void populateQuoteListView() {
        DatabaseHandler database = new DatabaseHandler(this);
        List<Quote> quotes = database.getAllQuotes();

        for (int index = 0; index < quotes.size(); index++) {
            quoteList.add(quotes.get(index));
        }
        adapter.notifyDataSetChanged();
        progressDialogVisibility();
    }

    /**
     *  Initialises click listeners for the custom listview.
     *
     *  @see    R
     *  @see    Void
     *  @see    Boolean
     *  @see    View
     *  @see    ListView
     *  @see    List
     *  @see    AdapterView
     *  @see    AlertDialog
     *  @see    DialogInterface
     *  @see    DatabaseHandler
     *  @see    FavouriteQuotes
     *  @see    android.widget.AdapterView.OnItemClickListener
     *  @since  1.0
     */
    private void setListViewListeners() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Quote selectedQuote = quoteList.get(position);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FavouriteQuotes.this);
                alertDialogBuilder.setTitle(selectedQuote.getAuthor().toString())
                        .setMessage(selectedQuote.getQuote().toString())
                        .setPositiveButton(getResources().getString(R.string.done).toUpperCase().toUpperCase(), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FavouriteQuotes.this);
                alertDialogBuilder.setTitle(R.string.remove_quote_title)
                        .setMessage(R.string.remove_quote_message)
                        .setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseHandler database = new DatabaseHandler(FavouriteQuotes.this);
                                database.deleteQuote(quoteList.get(position));
                                quoteList.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getResources().getString(android.R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    /**
     *  Performs final cleanup before activity is destroyed.
     *
     *  @see    Void
     *  @since  1.0
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialogVisibility();
    }

    /**
     *  Clears the listview when the back button is pressed.
     *
     *  @see    Void
     *  @since  1.0
     */
    @Override
    public void onBackPressed() {
        quoteList.clear();
        finish();
    }

    /**
     *  Initializes the contents of the activity's standard options menu.
     *
     *  @param  menu    Menu to invoke in actionbar
     *  @see            R
     *  @see            Boolean
     *  @see            Menu
     *  @return         True
     *  @since          1.0
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favourites, menu);
        return true;
    }

    /**
     *  Called when an item in the options menu is selected.
     *
     *  @param  item    Item in the actionbar menu
     *  @see            R
     *  @see            Boolean
     *  @see            Void
     *  @see            List
     *  @see            MenuItem
     *  @see            AlertDialog
     *  @see            DialogInterface
     *  @see            DatabaseHandler
     *  @see            CustomListAdapter
     *  @return         False to allow normal menu processing to proceed, True to consume it
     *  @since          1.0
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(R.string.remove_all_quotes_title)
                        .setMessage(R.string.remove_all_quotes_message)
                        .setPositiveButton(getResources().getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DatabaseHandler database = new DatabaseHandler(FavouriteQuotes.this);
                                for (int i = 0; i < quoteList.size(); i++) {
                                    database.deleteQuote(quoteList.get(i));
                                }
                                quoteList.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getResources().getString(android.R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
