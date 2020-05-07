package com.example.no_more_trash.models;

import android.location.Location;
import android.media.Image;
import android.view.Display;
import android.widget.ImageView;

import org.osmdroid.util.GeoPoint;

public class ModelDecheterie {
    private Location localisation;
    private String nom;
    private ImageView image;
    private String description;

    public ModelDecheterie(Location localisation, String nom, ImageView image, String description) {
        this.localisation = localisation;
        this.nom = nom;
        this.image = image;
        this.description = description;
    }
    public ModelDecheterie(){

    }

    public ModelDecheterie(Location localisation, String nom, String description) {
        this.localisation = localisation;
        this.nom = nom;
        this.description = description;
    }

    public Location getLocalisation() {
        return localisation;
    }

    public String getNom() {
        return nom;
    }

    public ImageView getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setLocalisation(Location localisation) {
        this.localisation = localisation;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
