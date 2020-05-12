package com.example.no_more_trash.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.ScriptGroup;
import android.widget.ImageView;

import com.example.no_more_trash.R;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Date;

public class ModelDechet implements Parcelable {
    //public ImageView image;
    //public Location location;
    public   String id;
    public double latitude;
    public double longitude;
    public Date date;
    public String taille;
    public String type;
    private boolean clean;

    @JsonCreator
    public ModelDechet(/*ImageView image,*/ @JsonProperty("latitude") double latitude,
                                            @JsonProperty("longitude") double longitude,
                                            @JsonProperty("date") Date date,
                                            @JsonProperty("taille") String taille,
                                            @JsonProperty("type") String type,
                                            @JsonProperty("clean") boolean clean,@JsonProperty("id") String id) {
        //this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.taille = taille;
        this.type = type;
        this.clean=clean;
        this.id=id;
    }

    protected ModelDechet(Parcel in) {
        id = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        taille = in.readString();
        type = in.readString();
        clean= in.readByte() != 0;
    }

    public ModelDechet (String type,String taille,String id,double loc,double lat){
        this.type=type;
        this.id=id;
        this.latitude=lat;
        this.longitude=loc;
        this.taille=taille;
    }

    public static final Creator<ModelDechet> CREATOR = new Creator<ModelDechet>() {
        @Override
        public ModelDechet createFromParcel(Parcel in) {
            return new ModelDechet(in);
        }

        @Override
        public ModelDechet[] newArray(int size) {
            return new ModelDechet[size];
        }
    };

    public void setId(String id){
        this.id=id;
    }

    public boolean isClean() {
        return clean;
    }

    public void setCleaned(boolean selected) {
        this.clean = selected;
    }

    public String getId(){
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(taille);
        dest.writeString(type);
        dest.writeByte((byte) (clean? 1 : 0));
    }
}
