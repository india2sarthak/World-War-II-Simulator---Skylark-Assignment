package com.example.skmishra.mapboxwwii;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;

class MapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{


    Context mcontext;
    LayoutInflater mlay;
    clickHandle mclick;
    ArrayList<Scenes> mArrayScenes = new ArrayList<>();
    int selectedPosition;

    public MapAdapter(Context mcontext, clickHandle clik) {

        mlay = LayoutInflater.from(mcontext);
        this.mcontext = mcontext;
        this.mclick = clik;

    }
    public void setData(ArrayList<Scenes> array)
    {
        mArrayScenes = array;
        notifyItemRangeChanged(0, mArrayScenes.size());

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = mlay.inflate(R.layout.custom_age, parent, false);
        ageHolder VH=new ageHolder(v);

        return VH;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof  ageHolder) {

            Scenes singleScene = mArrayScenes.get(position);

            Log.e("TDDD", singleScene.year + "   " + singleScene.narrative);

            ((ageHolder) holder).agelabel.setText(singleScene.year+"");
            ((ageHolder) holder).clcik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mclick.onClick(position);


                }
            });


        }


    }

    @Override
    public int getItemCount() {
        return mArrayScenes.size();
    }

    class ageHolder extends RecyclerView.ViewHolder {


        TextView agelabel;
        RelativeLayout clcik;
        public ageHolder(View itemView) {
            super(itemView);
            agelabel = (TextView)itemView.findViewById(R.id.textAll);
            clcik =(RelativeLayout)itemView.findViewById(R.id.clck);
            Typeface type = Typeface.createFromAsset(MyApplication.getAppContext().getAssets(),"AmericanTypewriter.ttc");
            agelabel.setTypeface(type);

        }




    }
}