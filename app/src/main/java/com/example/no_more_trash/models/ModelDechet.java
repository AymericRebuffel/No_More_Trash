package com.example.no_more_trash.models;

import android.location.Location;
import android.renderscript.ScriptGroup;
import android.widget.ImageView;

import com.example.no_more_trash.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Date;

public class ModelDechet {
    //public ImageView image;
    //public Location location;
    public double latitude;
    public double longitude;
    public Date date;
    public String taille;
    public String type;

    @JsonCreator
    public ModelDechet(/*ImageView image,*/ @JsonProperty("latitude") double latitude,
                                            @JsonProperty("longitude") double longitude,
                                            @JsonProperty("date") Date date,
                                            @JsonProperty("taille") String taille,
                                            @JsonProperty("type") String type) {
        //this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.taille = taille;
        this.type = type;
    }


}
