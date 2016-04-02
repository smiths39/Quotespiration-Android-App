package secretcoder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import secretcoder.SQLite.DatabaseHandler;
import secretcoder.SQLite.Quote;
import secretcoder.Volley.AppController;

public class FamousQuote extends Activity implements View.OnClickListener {

    // ====== GLOBAL VARIABLES ======
    private static ProgressDialog progressDialog;
    private static TextView tvFamousQuote, tvAuthor;
    private static Button btnFavourites, btnTryAgain;

    private final static String TAG = FamousQuote.class.getSimpleName();
    private final static String URL = "http://api.forismatic.com/api/1.0/?method=getQuote&lang=en&format=json";

    /**
     *  Called when activity is starting.
     *
     *  @param  savedInstanceState  The data of the activity re-initialized after previously being shut down
     *  @see                        R
     *  @see                        Bundle
     *  @see                        Void
     *  @see                        android.app.ActionBar
     *  @since                      1.0
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().show();

        if (isNetworkConnected()) {
            initialiseRetrievedDataScreen();
        } else {
            initialiseNoDataRetrievedScreen();
        }
    }

    /**
     *  Initialises screen to retrieve and display quote data.
     *
     *  @see    R
     *  @see    Void
     *  @since  1.0
     */
    private void initialiseRetrievedDataScreen() {
        setContentView(R.layout.activity_famous_quote);
        initialiseOnScreenComponents();
        initialiseProgressDialog();

        setQuoteFont();
        makeJsonObjectRequest();
    }

    /**
     *  Initialises screen display message that no data was retrieved.
     *
     *  @see    R
     *  @see    Void
     *  @see    Button
     *  @since  1.0
     */
    private void initialiseNoDataRetrievedScreen() {
        setContentView(R.layout.no_internet_connection);
        btnTryAgain = (Button) findViewById(R.id.btnNetworkTryAgain);
        btnTryAgain.setOnClickListener(this);
    }

    /**
     *  Determines whether internet connectivity is detected on user's phone.
     *
     *  @see        Boolean
     *  @see        ConnectivityManager
     *  @see        Context
     *  @see        NetworkInfo
     *  @return     True or False depending on network connectivity enabled
     *  @since      1.0
     */
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *  Initialises all components displayed on screen.
     *
     *  @see    R
     *  @see    Void
     *  @see    TextView
     *  @see    Button
     *  @since  1.0
     */
    private void initialiseOnScreenComponents() {
        tvFamousQuote = (TextView) findViewById(R.id.tvFamousQuote);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        btnFavourites = (Button) findViewById(R.id.btnFavourites);

        btnFavourites.setOnClickListener(this);
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
        progressDialog.setMessage(getResources().getString(R.string.progress_dialog_wait_message));
        progressDialog.setCancelable(false);
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
     *  Sets the font of the quote.
     *
     *  @see    R
     *  @see    TextView
     *  @see    Typeface
     *  @see    Void
     *  @since  1.0
     */
    private void setQuoteFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.famous_quote_font));
        tvFamousQuote.setTypeface(font);
    }

    /**
     *  Retrieves JSON data from API via HTTP Request supported by Volley service.
     *
     *  @see    R
     *  @see    Void
     *  @see    String
     *  @see    Request
     *  @see    com.android.volley.Request.Method
     *  @see    JSONObject
     *  @see    JsonObjectRequest
     *  @see    JSONException
     *  @see    Response
     *  @see    AppController
     *  @see    VolleyError
     *  @see    VolleyLog
     *  @see    Log
     *  @see    Toast
     *  @since  1.0
     */
    private void makeJsonObjectRequest() {
        progressDialogVisibility();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String jsonQuoteResponse = "";
                    String jsonAuthorResponse = "";

                    for (int i = 0; i < response.length(); i++) {
                        jsonQuoteResponse = response.getString(getResources().getString(R.string.json_quote_tag));
                        jsonAuthorResponse = response.getString(getResources().getString(R.string.json_author_tag));
                    }
                    tvFamousQuote.setText(jsonQuoteResponse);

                    if (jsonAuthorResponse.length() == 0) {
                        tvAuthor.setText(R.string.unknown_author);
                    } else {
                        tvAuthor.setText("- " + jsonAuthorResponse);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialogVisibility();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, R.string.error_title + error.getMessage());
                Toast.makeText(getApplicationContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
                progressDialogVisibility();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    /**
     *  Calls any attached OnClickListener.
     *
     *  @param  view    The View that is making the OnClickListener call
     *  @see            R
     *  @see            Void
     *  @see            View
     *  @see            Intent
     *  @see            FavouriteQuotes
     *  @see            Toast
     *  @since          1.0
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnFavourites:
                Intent favouriteQuotesIntent = new Intent(getBaseContext(), FavouriteQuotes.class);
                startActivity(favouriteQuotesIntent);
                break;

            case R.id.btnNetworkTryAgain:
                if (isNetworkConnected()) {
                    invalidateOptionsMenu();
                    initialiseRetrievedDataScreen();
                } else {
                    Toast.makeText(FamousQuote.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     *  Closes the application when the back button is pressed.
     *
     *  @see    Void
     *  @since  1.0
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     *  Prepare the Screen's standard options menu to be displayed.
     *
     *  @param      menu    The options menu as last shown or first initialized by onCreateOptionsMenu()
     *  @see                R
     *  @see                Boolean
     *  @see                Menu
     *  @see                MenuItem
     *  @return             Returns true for the menu to be displayed
     *  @since              1.0
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemRefresh = menu.findItem(R.id.action_refresh);
        MenuItem menuItemFavourite = menu.findItem(R.id.action_favourite);

        if (isNetworkConnected()) {
            menuItemRefresh.setEnabled(true).setVisible(true);
            menuItemFavourite.setEnabled(true).setVisible(true);
        } else {
            menuItemRefresh.setEnabled(false).setVisible(false);
            menuItemFavourite.setEnabled(false).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
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
        getMenuInflater().inflate(R.menu.menu_quotes, menu);
        return true;
    }

    /**
     *  Called when an item in the options menu is selected.
     *
     *  @param  item    Item in the actionbar menu
     *  @see            R
     *  @see            Boolean
     *  @see            DatabaseHandler
     *  @see            Toast
     *  @return         False to allow normal menu processing to proceed, True to consume it
     *  @since          1.0
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (isNetworkConnected()) {
                    invalidateOptionsMenu();
                    makeJsonObjectRequest();
                } else {
                    invalidateOptionsMenu();
                    initialiseNoDataRetrievedScreen();
                }
                return true;

            case R.id.action_favourite:
                DatabaseHandler database = new DatabaseHandler(this);

                if (database.quoteExists(tvFamousQuote.getText().toString())) {
                    Toast.makeText(this, R.string.quote_exists, Toast.LENGTH_SHORT).show();
                } else {
                    database.addQuote(new Quote(tvAuthor.getText().toString().substring(2, tvAuthor.length()), tvFamousQuote.getText().toString()));
                    Toast.makeText(this, R.string.quote_added, Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
