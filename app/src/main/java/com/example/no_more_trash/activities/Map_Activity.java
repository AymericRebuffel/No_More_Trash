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
import android.os.Parcelable;
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
import java.util.Random;
import java.util.Scanner;

public class Map_Activity extends AppCompatActivity {
    private MapView map;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Button center_map;
    private double longitude;
    private double lattitude;
    private MyLocationNewOverlay mLocationOverlay;
    private ArrayList items;
    private ArrayList<ModelDecheterie> decheteries;
    private ArrayList<GeoPoint> trajet;
    private ArrayList<ModelDechet> dechets ;
    private ArrayList<ModelDechet> clenedDechets;

    @Override
    public void onCreate(Bundle saveInstantState) {
        super.onCreate(saveInstantState);
        decheteries = new ArrayList<ModelDecheterie>();
        dechets=new ArrayList<ModelDechet>();
        clenedDechets=new ArrayList<ModelDechet>();
        try {
            initDechets();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            initDechetteries();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            initCleanedGarbage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getSupportActionBar().hide();
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.map_decheterie);
        items = new ArrayList<OverlayItem>();
        textView = findViewById(R.id.textView2);
        map = findViewById(R.id.map);
        center_map = findViewById(R.id.center_map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        trajet = new ArrayList<GeoPoint>();
        OverlayItem point = new OverlayItem("dechet_organique", "moyen", new GeoPoint(45.31765771762817, 5.922782763890293));
        trajet.add(new GeoPoint(point.getPoint().getLatitude(), point.getPoint().getLongitude()));
      //  items.add(point);
        decheteries.add(new ModelDecheterie(45.80828947812799,2.789005057397027,"oui","test"));
        if(dechets.size()!=0){
            placeDechet();
        }
        if(decheteries.size()!=0){
            placeDecheterie();
        }
        final IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        mapController.setCenter(new GeoPoint(45.469727002862086,5.972142037934329));
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                lattitude = location.getLatitude();
                textView.append("\n " + longitude + " " + lattitude);
                mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), map);
                mLocationOverlay.enableMyLocation();
                map.setMultiTouchControls(true);
                map.getOverlays().add(mLocationOverlay);
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
        } else {
            setpos();
        }
        locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);
        Polyline line = new Polyline();
        line.setTitle("Un trajet");
        line.setSubDescription(Polyline.class.getCanonicalName());
        line.setWidth(10f);
        line.setId("-1");
        line.setColor(Color.RED);
        line.setPoints(trajet);
        line.setGeodesic(true);
        line.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, map));
        map.getOverlayManager().add(line) ;
        map.invalidate();
        /*ModelDechet D=new ModelDechet("organique","petit","test",2.789005057397027,45.80828947812799);
        dechets.add(D);
        Marker s=new Marker(map);
        s.setId("test");
        s.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                goTomarker("test");
                return false;
            }
        });
        s.setPosition(new GeoPoint(45.80828947812799,2.789005057397027));
       // mapController.setCenter(s.getPosition());
        map.getOverlays().add(s);
        map.invalidate();*/
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
    public void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        try {
            initDechets();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            initDechetteries();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            initCleanedGarbage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.getOverlays().clear();
        placeDecheterie();
        placeDechet();
        map.onResume();
       // map.getController().setCenter(new GeoPoint(45.31765771762817, 5.922782763890293));
    }

    @Override
    public void onPause(){
        super.onPause();
        map.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==1){
            clenedDechets.add((ModelDechet) data.getParcelableExtra("result"));
        }
        super.onActivityResult(requestCode,resultCode,data);
        map.getOverlays().clear();
        placeDechet();
        placeDecheterie();
    }


    @SuppressLint("MissingPermission")
    private void setpos(){
        center_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 500, 3, locationListener);

            }

            });
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
        //intiID();
        System.out.println(dechets);
    }

    private void intiID() {
        for(int i=0;i<dechets.size();i++){
            Random t=new Random();
            int tmp=t.nextInt();
            dechets.get(i).setId(tmp+"");
            System.out.println(dechets.get(i).getId());
        }
    }

    private void placeDechet(){
        for(int i=0;i<dechets.size();i++){
            if(!contient(dechets.get(i))){
            Marker tmp=new Marker(map);
            tmp.setPosition(new GeoPoint(dechets.get(i).latitude, dechets.get(i).longitude));
            tmp.setId(dechets.get(i).getId());
            tmp.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    goTomarker(marker.getId());
                    return false;
                }
            });
            map.getOverlays().add(tmp);
           /* OverlayItem tmp=new OverlayItem("dechet "+dechets.get(i).type, dechets.get(i).taille, new GeoPoint(dechets.get(i).latitude, dechets.get(i).longitude));
            items.add(tmp);*/
        }}
        enlevepin();
    }

    private boolean contient(ModelDechet dechet) {
        for(int i=0;i<clenedDechets.size();i++){
            if(dechet.getId().equals(clenedDechets.get(i).getId())){
                return true;
            }
        }
        return false;
    }
    private boolean markerCleaned(Marker m){
        for(int i=0;i<clenedDechets.size();i++){
                System.out.println(m.getTitle()+"----------------------------------------------------");
                if(m.getId().equals(clenedDechets.get(i).getId())){
                    return true;
            }
        }
        return false;
    }
    private void enlevepin(){
        for(int i=0;i<map.getOverlays().size();i++){
            Overlay tmp=map.getOverlays().get(i);
            if(tmp instanceof Marker){
                if(markerCleaned((Marker)tmp)){
                    map.getOverlays().remove(i);
                }
            }
        }
    }
    private void placeDecheterie(){
        for(int i=0;i<decheteries.size();i++){
            Marker tmp=new Marker(map);
            tmp.setTitle(decheteries.get(i).nom);
            tmp.setPosition(new GeoPoint(decheteries.get(i).latitude, decheteries.get(i).longitude));
            tmp.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            tmp.setId(decheteries.get(i).ID);
           // tmp.setIcon(getResources().getDrawable(R.drawable.trash));
            map.getOverlays().add(tmp);
            map.invalidate();
        }
    }

    private void initDechetteries() throws IOException {
        FileInputStream fis = openFileInput("database_Dechetteries.json");
        Scanner scanner = new Scanner(fis);
        String object;
        ObjectMapper objectMapper = new ObjectMapper();
        while (scanner.hasNextLine()){
            object = scanner.nextLine();
            decheteries.add(objectMapper.readValue(object,ModelDecheterie.class));
        }
        System.out.println(dechets);
    }
    private void goTomarker(String iddechet){
        ModelDechet tmp=selectioneDechet(iddechet);
        Intent gameActivity = new Intent(this, MarkerDechet.class);
        gameActivity.putExtra("Cdechet",(Parcelable) tmp);
        startActivityForResult(gameActivity,0);
    }

    private ModelDechet selectioneDechet(String id){
        for(int i=0;i<dechets.size();i++){
                if(id.equals(dechets.get(i).getId())){
                    return dechets.get(i);
                }
        }
        return null;
    }
    private void initCleanedGarbage() throws IOException {
        FileInputStream fis = openFileInput("database_CleanedGarbage.json");
        Scanner scanner = new Scanner(fis);
        String object;
        ObjectMapper objectMapper = new ObjectMapper();
        while (scanner.hasNextLine()){
            object = scanner.nextLine();
            clenedDechets.add(objectMapper.readValue(object,ModelDechet.class));
        }
        System.out.println(clenedDechets.size()+"________-_-_-_-_-_-_");
    }
}
