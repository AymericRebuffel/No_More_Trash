package com.example.no_more_trash.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.no_more_trash.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class Map_Activity extends AppCompatActivity {
    private MapView map;
    private LocationManager locationManager = null;
    private Location localisation = null;
    private String fournisseur;
    @Override
    public void onCreate(Bundle saveInstantState){
        super.onCreate(saveInstantState);
        getSupportActionBar().hide();
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.map_decheterie);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
        mapController.setCenter(startPoint);



        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        OverlayItem home = new OverlayItem("Une decheterie", "quelque part", new GeoPoint(43.65020,7.00517));
        Drawable m = home.getMarker(0);

        items.add(home);
        items.add(new OverlayItem("Resto", "chez babar", new GeoPoint(43.64950,7.00517)));

        Marker mymark=new Marker(map);
        mymark.setPosition( new GeoPoint(43.64950, 7.00517));
        mymark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mymark.setIcon(getResources().getDrawable(R.drawable.trash));
        map.getOverlays().add(mymark);
        map.invalidate();


        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this, items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
                        return true;
                    }
                    @Override
                    public boolean onItemLongPress(final int index, final OverlayItem item) {
                        return false;
                    }
                });


        mOverlay.setFocusItemsOnTap(true);
        map.getOverlays().add(mOverlay);
    }
    @Override
    public void onResume(){
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        map.onPause();
    }

    public void addpin(String titre,String desription,GeoPoint g){
        OverlayItem tmp= new OverlayItem(titre,desription,g);
        List<OverlayItem> ltmp = new ArrayList<>();
        ltmp.add(tmp);
        map.getOverlays().add((Overlay) ltmp);
    }
    @SuppressLint("MissingPermission")
    private void initialiserLocalisation()
    {
        if(locationManager == null)
        {
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            assert locationManager != null;
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                createGpsDisabledAlert();
            Criteria criteres = new Criteria();
            criteres.setAccuracy(Criteria.ACCURACY_FINE);
            criteres.setPowerRequirement(Criteria.POWER_HIGH);
            fournisseur = locationManager.getBestProvider(criteres, true);
        }

        if(fournisseur != null)
        {
            localisation = locationManager.getLastKnownLocation(fournisseur);
            assert localisation != null;
            @SuppressLint("DefaultLocale") String coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
            Log.d("GPS", "coordonnees : " + coordonnees);
        }
    }

    private void createGpsDisabledAlert() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder
                .setMessage("Le GPS est désactivé et il est nécessaire pour connaître la position du déchet, voulez-vous l'activer ?")
                .setCancelable(false)
                .setPositiveButton("Oui ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                showGpsOptions();
                            }
                        }
                );
        localBuilder.setNegativeButton("Non ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        paramDialogInterface.cancel();
                        finish();
                    }
                }
        );
        localBuilder.create().show();
    }
    private void showGpsOptions() {
        startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
        //finish();
    }
}
