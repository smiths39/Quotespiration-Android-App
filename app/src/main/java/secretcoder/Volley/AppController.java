package secretcoder.Volley;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    // ====== GLOBAL VARIABLES ======
    private RequestQueue requestQueue;
    private static AppController instance;
    private static final String TAG = AppController.class.getSimpleName();

    /**
     *  Called when the application is starting.
     *
     *  @see    Void
     *  @see    AppController
     *  @since  1.0
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     *  Returns application controller's instance.
     *
     *  @see        AppController
     *  @return     Instance of AppController
     *  @since      1.0
     */
    public static synchronized AppController getInstance() {
        return instance;
    }

    /**
     *  Retrieves the request queue of data returned from API.
     *
     *  @see        RequestQueue
     *  @see        Volley
     *  @return     Request queue populated from API via Volley HTTP call
     *  @since      1.0
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    /**
     *  Adds API Volley call to request queue.
     *
     *  @param   request    Collection of json object requests called via an API
     *  @param   <T>        Type of data requests to be returned
     *  @see                Void
     *  @see                Request
     *  @since              1.0
     */
    public <T> void addToRequestQueue(Request<T> request) {
        request.setTag(TAG);
        getRequestQueue().add(request);
    }
}