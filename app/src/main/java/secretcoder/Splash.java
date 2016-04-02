package secretcoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Splash extends Activity {

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
        setContentView(R.layout.activity_splash);

        setTypeFont();
        launchFamousQuotePage();
    }

    /**
     *  Sets the font of the application title.
     *
     *  @see    R
     *  @see    TextView
     *  @see    Typeface
     *  @see    Void
     *  @since  1.0
     */
    private void setTypeFont() {
        TextView tvAppTitle = (TextView) findViewById(R.id.tvSplashTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.splash_font));
        tvAppTitle.setTypeface(font);
    }

    /**
     *  Launches famous quote page after 3 seconds.
     *
     *  @see    Void
     *  @see    Handler
     *  @see    Runnable
     *  @see    Intent
     *  @see    FamousQuote
     *  @since  1.0
     */
    private void launchFamousQuotePage() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent famousQuotePage = new Intent(getBaseContext(), FamousQuote.class);
                startActivity(famousQuotePage);
                finish();
            }
        }, 3000);
    }

    /**
     *  Disabled the back press key functionality for the activity.
     *
     *  @see    Void
     *  @since  1.0
     */
    @Override
    public void onBackPressed() {}
}
