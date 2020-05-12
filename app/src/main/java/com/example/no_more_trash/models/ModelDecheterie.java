package com.example.no_more_trash.models;

import android.location.Location;
import android.media.Image;
import android.view.Display;
import android.widget.ImageView;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class ModelDecheterie {
    public double latitude;
    public double longitude;
    public String nom;
    public static  final String ID="-2";
    /*private ImageView image;
    public String description;*/

    public ModelDecheterie(@JsonProperty("latitude") double latitude,
                           @JsonProperty("longitude") double longitude,
                           @JsonProperty("nom") String nom/*, ImageView image, String description*/) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.nom = nom;
       /* this.image = image;
        this.description = description;*/
    }

    private void lancevue() {
    }


    public ModelDecheterie(){

    }

    public ModelDecheterie(double latitude, double longitude, String nom, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.nom = nom;
       // this.description = description;
    }

    public void setLocalisation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNom() {
        return nom;
    }

  /*  public ImageView getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }*/

    public void setNom(String nom) {
        this.nom = nom;
    }

  /*  public void setImage(ImageView image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }*/


}
