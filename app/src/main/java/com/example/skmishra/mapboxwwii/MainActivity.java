package com.example.skmishra.mapboxwwii;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Scene;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolygonOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private  MapboxMap map;
    RecyclerView mrecyler;
    ArrayList<Scenes> masterScenes = new ArrayList<>();
    private MapAdapter madap;
    TextView narrative;

    /*--*/
    TextView firstTextIntro;
    TextView alliesText;
    TextView axisText;
    Button firstButton;
    RelativeLayout firstRel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.activity_main);
        narrative = (TextView)findViewById(R.id.narrativeText);
        mapView = (MapView)findViewById(R.id.mapView);

        firstTextIntro = (TextView)findViewById(R.id.firstTextIntro);
        alliesText =(TextView)findViewById(R.id.aliesText);
        axisText = (TextView)findViewById(R.id.axisText);
        firstButton = (Button)findViewById(R.id.buttonFirst);
        firstRel = (RelativeLayout)findViewById(R.id.relFirst);


        setAMButton(firstButton);
        setAMReg(firstTextIntro);
        setAMReg(alliesText);
        setAMReg(axisText);

        setAMReg(narrative);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {

                     map = mapboxMap;
                     ArrayList<Events> mArrayEvents = new ArrayList<>();
                     Events firstEvent = new Events();
                     firstEvent.title = "Invasion of Poland";
                     firstEvent.latitude = (float) 51.9194;
                     firstEvent.longitude = (float)19.1451;
                     firstEvent.sceneID =1;
                     firstEvent.description = "The Invasion of Poland was a joint invasion of Poland by Germany, the Soviet Union, the Free City of Danzig, and a small Slovak contingent that marked the beginning of World War II.";
                     mArrayEvents.add(firstEvent);

                     Events secondEvent = new Events();
                        secondEvent.title = "Phoney War";
                        secondEvent.latitude = (float) 49.39964;
                        secondEvent.longitude = (float)7.023;
                        secondEvent.sceneID =1;
                        secondEvent.description = "The Phoney War was an eight-month period at the start of World War II, during which the French troops invaded Germany's Saar district.";
                        mArrayEvents.add(secondEvent);

                    // Add the custom icon marker to the map
                map.clear();
                for (int i=0;i<mArrayEvents.size();i++)

                {
                    Events singleEvent = mArrayEvents.get(i);

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(singleEvent.latitude, singleEvent.longitude))
                            .title(singleEvent.title)
                            .snippet(singleEvent.description));



                }


            }
        });




        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mrecyler = (RecyclerView)findViewById(R.id.recycler);
        madap = new MapAdapter(this, new clickHandle() {
            @Override
            public void onClick(int position) {


                layoutManager.scrollToPosition(position);
                Scenes selected = masterScenes.get(position);
                mapView.setStyleUrl(selected.style_url);
                CameraPosition positionX = new CameraPosition.Builder()
                        .target(new LatLng(selected.latitude, selected.longitude)) // Sets the new camera position
                        .zoom(selected.zl) // Sets the zoom
                        // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(positionX), 3000);
                narrative.setText(selected.narrative);
                map.clear();

                Log.e("Selected",selected.eventsYear.size()+"");

                for (int i=0;i<selected.eventsYear.size();i++)

                {
                    Events singleEvent = selected.eventsYear.get(i);

                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(singleEvent.latitude, singleEvent.longitude))
                            .title(singleEvent.title)
                            .snippet(singleEvent.description));



                }
            }
        });


        mrecyler.setLayoutManager(layoutManager);
        mrecyler.setAdapter(madap);


       makeRequest("http://graph.platestheapp.com/wwii/");


    }

    public  void makeRequest(String url)
    {
        volleyHandler mVh=new volleyHandler();
        mVh.makeRequest(url, new VolleyResponse() {
            @Override
            public void onSuccess(String resp) throws JSONException {

                Log.e("Resp",resp);
                JSONArray mJSON = new JSONArray(resp);
                for(int i=0;i<mJSON.length();i++)
                {
                    JSONObject mSingObs = mJSON.getJSONObject(i);
                    Scenes singleScene = new Scenes();
                    singleScene.id = mSingObs.getInt("id");
                    singleScene.latitude = mSingObs.getInt("latitude");
                    singleScene.longitude = mSingObs.getInt("longitude");
                    singleScene.narrative = mSingObs.getString("narrative");
                    singleScene.style_url = mSingObs.getString("style_url");
                    singleScene.year = mSingObs.getInt("year");
                    singleScene.zl = mSingObs.getInt("zl");

                    JSONArray events = mSingObs.getJSONArray("events");
                    ArrayList<Events> mArrayEvents = new ArrayList<>();
                    for (int  k=0 ;k<events.length();k++)
                    {
                        JSONObject eventJSON = events.getJSONObject(k);
                         Events singleEvent = new Events();
                         singleEvent.description = eventJSON.getString("description");
                         singleEvent.title = eventJSON.getString("title");
                         singleEvent.latitude = eventJSON.getInt("latitude");
                         singleEvent.longitude =  eventJSON.getInt("longitude");
                        mArrayEvents.add(singleEvent);
                    }

                    singleScene.eventsYear = mArrayEvents;

                    masterScenes.add(singleScene);

                }



                setData();
            }

            @Override
            public void onError() {

            }
        });


    }
   public void setAMReg(TextView textView)
    {
        Typeface type = Typeface.createFromAsset(getAssets(),"AmericanTypewriter.ttc");
        textView.setTypeface(type);
    }
    public  void setAMButton(Button btn)
    {
        Typeface type = Typeface.createFromAsset(getAssets(),"AmericanTypewriter.ttc");
        btn.setTypeface(type);
    }
    public void setAMBold(TextView textView)
    {
        Typeface type = Typeface.createFromAsset(getAssets(),"am2.ttf");
        textView.setTypeface(type);
    }

    public void setData()

    {
        madap.setData(masterScenes);

    }



        @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    public void removeIntro(View view) {
        firstRel.setVisibility(View.GONE);
    }
}

