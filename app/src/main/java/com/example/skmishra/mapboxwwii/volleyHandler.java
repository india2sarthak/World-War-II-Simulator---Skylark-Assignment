package com.example.skmishra.mapboxwwii;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;

import java.net.MalformedURLException;

public class volleyHandler {

    VolleySingleton VS;
    com.android.volley.toolbox.ImageLoader mImageLoader;
    MyApplication mApp;
    public Boolean isNots = false;

    private StringRequest sRq;
    public boolean isSearch;
    public boolean isPlatesSerch;



    public void makeRequest(String url, final VolleyResponse callback) {



        VS = VolleySingleton.getInstance();
        RequestQueue rq = VS.getRequestQueue();
        Log.e("URLS", url);
        sRq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                try {
                    callback.onSuccess(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (!isNots) {
                    Toast.makeText(MyApplication.getAppContext(), "No internet , please try again later", Toast.LENGTH_LONG).show();
                    callback.onError();
                    Log.e("Error",volleyError.getMessage());
                }
            }
        });


        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sRq.setRetryPolicy(retryPolicy);


        sRq.setShouldCache(false);
        rq.add(sRq);
    }
}
