package com.example.no_more_trash.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.no_more_trash.R;
import com.example.no_more_trash.models.ModelDechet;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.no_more_trash.models.ModelDecheterie;

import org.osmdroid.api.IGeoPoint;
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
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map_Activity extends AppCompatActivity {
    private MapView map;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button center_map;
    private double longitude;
    private  double lattitude;
    private  MyLocationNewOverlay mLocationOverlay;
    private ArrayList items;
    private ArrayList<ModelDecheterie> decheteries;
    private ArrayList<GeoPoint> trajet;
    private Location localisation = null;
    private String fournisseur;
    private ArrayList<ModelDechet> dechets = new ArrayList<>();
    @Override
    public void onCreate(Bundle saveInstantState) {
        super.onCreate(saveInstantState);
        try {
            initDechets();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getSupportActionBar().hide();
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.map_decheterie);
        items = new ArrayList<OverlayItem>();
        textView = findViewById(R.id.textView2);
        decheteries=new ArrayList<ModelDecheterie>();
        map = findViewById(R.id.map);
        center_map=findViewById(R.id.center_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        trajet=new ArrayList<GeoPoint>();
        OverlayItem point =new OverlayItem("titre","descri",new GeoPoint(45.31765771762817,5.922782763890293));
        trajet.add(new GeoPoint(point.getPoint().getLatitude(),point.getPoint().getLongitude()));
        items.add(point);
        final IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
        mapController.setCenter(startPoint);
        ModelDecheterie dechet1=new ModelDecheterie(new Marker(map),"dechet1","");
        dechet1.getLocalisation().setPosition(new GeoPoint(45.42521728609235,6.015727887003348));
        decheteries.add(dechet1);
        final Marker mymark = new Marker(map);
        mymark.setPosition(new GeoPoint(43.64950, 7.00517));
        mymark.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mymark.setIcon(getResources().getDrawable(R.drawable.trash));
       // map.getOverlays().add(mymark);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude=location.getLongitude();
                lattitude=location.getLatitude();
                textView.append("\n " + longitude + " " + lattitude);
                mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()),map);
                mLocationOverlay.enableMyLocation();
                map.setMultiTouchControls(true);
                map.getOverlays().add(mLocationOverlay);
                mapController.setCenter(mLocationOverlay.getMyLocation());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET}, 10);
            return;
        }else{
            setpos();
        }
        locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);
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

        Polyline line = new Polyline();
        line.setTitle("Un trajet");
        line.setSubDescription(Polyline.class.getCanonicalName());
        line.setWidth(10f);
        line.setColor(Color.RED);
        line.setPoints(trajet);
        line.setGeodesic(true);
        line.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, map));

       map.invalidate();



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

    public IGeoPoint findPos(ArrayList<OverlayItem> list,String tag){
        for(int i=0;i<list.size();i++){
            if(list.get(i).getTitle().equals(tag)){
                return list.get(i).getPoint();
            }
        }
        return null;
    }
    @SuppressLint("MissingPermission")
    private void setpos(){
        center_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                map.getController().setCenter(mLocationOverlay.getMyLocation());
            }

            });
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

    private void initDechets() throws IOException {
        FileInputStream fis = openFileInput("database_Dechets.json");
        Scanner scanner = new Scanner(fis);
        String object;
        ObjectMapper objectMapper = new ObjectMapper();
        while (scanner.hasNextLine()){
            object = scanner.nextLine();
            dechets.add(objectMapper.readValue(object,ModelDechet.class));
        }
        System.out.println(dechets);
    }
}
