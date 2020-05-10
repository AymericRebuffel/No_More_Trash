package com.example.no_more_trash.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.no_more_trash.fragment.formulaire.FragmentPhoto;
import com.example.no_more_trash.models.ModelDecheterie;
import com.example.no_more_trash.models.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.no_more_trash.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.no_more_trash.activities.DeclarationDechet.CHANNEL_1_ID;

public class FormulaireDecheterieActivity extends AppCompatActivity {
    private ImageView photo;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    String titre;
    String description;
    Location myLoc;
   // private String fournisseur;
    private LocationManager locationManager ;
    private LocationListener locationListener;
    public static final  String CHANNEL_1_ID = "channel2";
    int notificationId =1;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  initialiserLocalisation();
        getSupportActionBar().hide();
        setContentView(R.layout.declaration_decheterie);
        createNotificationChannel();
        //Boutton de validation du formulaire
        Button valider=findViewById(R.id.ValidationD);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nom=findViewById(R.id.iddecharge);
                titre=nom.getText().toString();
                Valider(v);
            }
        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLoc=location;
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
        locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);
        EditText nom=findViewById(R.id.iddecharge);
        titre=nom.getText().toString();

        //Gestion de la prise de photo de la décharge
        View frag = findViewById(R.id.FragmentPhoto);
        this.photo = frag.findViewById(R.id.imageView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.photo.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void prendrePhoto() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    public void Valider(View view){
        addNotification();
        ModelDecheterie tmp=new ModelDecheterie();
        tmp.setLocalisation(myLoc.getLatitude(),myLoc.getLongitude());
        tmp.setNom(titre);
        // tmp.setImage(photo);
        try {
            writeJson(tmp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent gameActivity = new Intent(this, HomeUser.class);
        startActivity(gameActivity);
    }

    private void addNotification() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.trash);
        Intent intent = new Intent(this, FormulaireDecheterieActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.icon_param)
                .setContentTitle("Alert")
                .setContentText("Vous avez déclaré une déchetterie : "+titre)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, builder.build());

        notificationId++;

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("Channel_1");
            String description =("Channel_1_description");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_1_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
   /* @SuppressLint("MissingPermission")
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
            myLoc = locationManager.getLastKnownLocation(fournisseur);
            String coordonnees;
            if(myLoc!=null) {
                coordonnees = String.format("Latitude : %f - Longitude : %f\n", myLoc.getLatitude(), myLoc.getLongitude());
                Log.d("GPS", "coordonnees : " + coordonnees);
            }
        }
    }

    private void createGpsDisabledAlert() {
        Log.d("gps", "test");
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
    }*/

    public void writeJson(ModelDecheterie decheterie) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String res = "";
        try {
            res = mapper.writeValueAsString(decheterie);
            System.out.println(mapper.writeValueAsString(decheterie));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FileOutputStream fos = openFileOutput("database_Dechetteries.json",MODE_APPEND);
        if(fos!=null){
            fos.write((""+res+"\n").getBytes());
            fos.close();
        }
    }
}
