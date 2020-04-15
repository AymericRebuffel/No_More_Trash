package com.example.no_more_trash.activities;

import android.media.Image;
import android.widget.ImageView;

import org.osmdroid.util.GeoPoint;

public class ModelDecheterie {
    private GeoPoint localisation;
    private String nom;
    private ImageView image;
    private String description;

    public ModelDecheterie(GeoPoint localisation, String nom, ImageView image, String description) {
        this.localisation = localisation;
        this.nom = nom;
        this.image = image;
        this.description = description;
    }

    public ModelDecheterie(GeoPoint localisation, String nom, String description) {
        this.localisation = localisation;
        this.nom = nom;
        this.description = description;
    }

    public GeoPoint getLocalisation() {
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

    public void setLocalisation(GeoPoint localisation) {
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
