package com.example.no_more_trash;

import android.media.Image;

import org.osmdroid.util.GeoPoint;

public class ModelDecheterie {
    private GeoPoint localisation;
    private String nom;
    private Image image;
    private String description;

    public ModelDecheterie(GeoPoint localisation, String nom, Image image, String description) {
        this.localisation = localisation;
        this.nom = nom;
        this.image = image;
        this.description = description;
    }
    public GeoPoint getLocalisation() {
        return localisation;
    }

    public String getNom() {
        return nom;
    }

    public Image getImage() {
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

    public void setImage(Image image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
