package com.example.skmishra.mapboxwwii;

import org.json.JSONException;

public interface VolleyResponse {
    void onSuccess(String resp) throws JSONException;
    void onError();

}

