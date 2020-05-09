package com.example.no_more_trash.activities;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.no_more_trash.R;
import com.example.no_more_trash.models.ModelDechet;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class DeclarationDechet extends AppCompatActivity {
    private ImageView photo;
    private LocationManager locationManager = null;
    private Location localisation = null;
    private String fournisseur;
    private Date date;
    private String taille;
    private String type;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    public static final  String CHANNEL_1_ID = "channel1";
    int notificationId =1;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        Log.d("Start","Declaration dechet");
        initialiserLocalisation();
        createNotificationChannel();
        //Log.d("Localisation : ", localisation.toString());
        setContentView(R.layout.declaration_dechet);
        Button valider = findViewById(R.id.Validation);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Valider(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Button boutonPhoto = findViewById(R.id.Photo);
        boutonPhoto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendrePhoto();
            }
        });
        this.photo = findViewById(R.id.imageView);
    }

    public void Valider(View view) throws IOException {
        if(localisation!=null)
            date = new Date(localisation.getTime());
        Spinner spinnerTaille = findViewById(R.id.spinnerTaille);
        taille = spinnerTaille.getSelectedItem().toString();
        Spinner spinnerType = findViewById(R.id.spinnerType);
        type = spinnerType.getSelectedItem().toString();
        addNotification();
        ModelDechet modelDechet = new ModelDechet(/*photo,*/localisation,date,taille,type);
        writeJson(modelDechet);
        Intent gameActivity = new Intent(DeclarationDechet.this, HomeUser.class);
        startActivity(gameActivity);
    }

    private void addNotification() {

        Intent intent = new Intent(this, DeclarationDechet.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.icon_param)
                .setContentTitle("Alert")
                .setContentText("Vous avez déclaré un "+taille+" déchet de type "+type)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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

    public void writeJson(ModelDechet dechet) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String res = "";
        try {
            res = mapper.writeValueAsString(dechet);
            System.out.println(mapper.writeValueAsString(dechet));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FileOutputStream fos = openFileOutput("database_Dechets.json",MODE_APPEND);
        if(fos!=null){
            fos.write((""+res+",\n").getBytes());
            fos.close();
        }
    }

    private void prendrePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
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
            String coordonnees;
            if(localisation!=null) {
                coordonnees = String.format("Latitude : %f - Longitude : %f\n", localisation.getLatitude(), localisation.getLongitude());
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
    }

}
