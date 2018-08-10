package org.cmucreatelab.flutter_android.helpers;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by mike on 5/2/17.
 *
 * HttpRequestHandler
 *
 * A class for helping with sending an http request for sending an email through the data log tab.
 */
public class HttpRequestHandler {

    private GlobalHandler parent;
    private static final String VOLLEY_TAG = "email_server";
    public RequestQueue volleyRequestQueue;


    public HttpRequestHandler(GlobalHandler parent) {
        this.parent = parent;
        this.volleyRequestQueue = Volley.newRequestQueue(parent.appContext);
    }


    public void addRequestToRequestQueue(Request request) {
        request.setTag(VOLLEY_TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volleyRequestQueue.add(request);
    }


    public void clearRequestsFromRequestQueue() {
        volleyRequestQueue.cancelAll(VOLLEY_TAG);
    }

}
