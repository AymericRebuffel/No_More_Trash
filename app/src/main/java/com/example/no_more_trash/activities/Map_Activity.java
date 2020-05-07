package com.example.no_more_trash.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.no_more_trash.R;

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
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

public class Map_Activity extends AppCompatActivity {
    private MapView map;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double longitude;
    private  double lattitude;
   // private Location mylocation;
    private ArrayList items;

    @Override
    public void onCreate(Bundle saveInstantState) {
        super.onCreate(saveInstantState);
        getSupportActionBar().hide();
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.map_decheterie);
        items = new ArrayList<OverlayItem>();
        textView = findViewById(R.id.textView2);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        final IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(43.65020, 7.00517);
        mapController.setCenter(startPoint);



        OverlayItem home = new OverlayItem("Une decheterie", "quelque part", new GeoPoint(43.65020, 7.00517));
        Drawable m = home.getMarker(0);

        items.add(home);
        items.add(new OverlayItem("Resto", "chez babar", new GeoPoint(43.64950, 7.00517)));

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
                Marker me=new Marker(map);
                me.setPosition(new GeoPoint(lattitude,longitude));
                me.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                me.setIcon(getDrawable(R.drawable.gpson));
                map.getOverlays().add(me);
                mapController.setCenter(me.getPosition());
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
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
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
        if(findPos(items,"me")!=null){
            mapController.setCenter(findPos(items,"me"));
        }



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

}
