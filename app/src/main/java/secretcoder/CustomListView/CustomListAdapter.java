package secretcoder.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import secretcoder.R;
import secretcoder.SQLite.Quote;

public class CustomListAdapter extends BaseAdapter {

    // ====== GLOBAL VARIABLES ======
    private static Activity activity;
    private static LayoutInflater inflater;
    private static List<Quote> quoteItems;

    /**
     *  Class constructor to initialise variables.
     *
     *  @param  activity    Activity being called from
     *  @param  quoteItems  List items to populate within listview
     *  @see                Activity
     *  @see                List
     *  @see                Quote
     *  @since              1.0
     */
    public CustomListAdapter(Activity activity, List<Quote> quoteItems) {
        this.activity = activity;
        this.quoteItems = quoteItems;
    }

    /**
     *  Retrieves the number of items populated within the listview
     *
     *  @see                Integer
     *  @see                List
     *  @return             The number of items within the listview
     *  @since              1.0
     */
    @Override
    public int getCount() {
        return quoteItems.size();
    }

    /**
     *  Returns the object within the listview at a specified position.
     *
     *  @param  position    The index position within the listview
     *  @see                Object
     *  @see                List
     *  @return             The object at the specified position
     *  @since              1.0
     */
    @Override
    public Object getItem(int position) {
        return quoteItems.get(position);
    }

    /**
     *  Returns the position of the specified object within the listview.
     *
     *  @param  position    The index position within the listview
     *  @see                Object
     *  @see                List
     *  @return             The object at the specified position
     *  @since              1.0
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     *  Get a View that displays the data at the specified position in the listview.
     *
     *  @param      position        The position of the item within the adapter's data set
     *  @param      convertView     The old view to reuse
     *  @param      parent          The parent that the view be attached to
     *  @see                        R
     *  @see                        View
     *  @see                        ViewGroup
     *  @see                        LayoutInflater
     *  @see                        Activity
     *  @see                        Context
     *  @see                        TextView
     *  @see                        Quote
     *  @see                        String
     *  @return                     A View corresponding to the data at the specified position
     *  @since                      1.0
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
        }

        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvFavouriteAuthor);
        TextView tvQuote = (TextView) convertView.findViewById(R.id.tvFavouriteQuote);

        Quote quote = quoteItems.get(position);
        String fullQuote = quote.getQuote();

        if (fullQuote.length() >= 100) {
            fullQuote = fullQuote.substring(0, 100) + "...";
        }

        tvQuote.setText(fullQuote);
        tvAuthor.setText(quote.getAuthor());

        return convertView;
    }
}