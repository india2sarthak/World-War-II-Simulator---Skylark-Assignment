package com.example.skmishra.mapboxwwii;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

   public static MyApplication ins;


    @Override
    public void onCreate() {
        super.onCreate();
        ins=this;





    }



    public static MyApplication getInstance()
    {
        return ins;
    }

    public static Context getAppContext()
    {
        return ins.getApplicationContext();
    }
}
