package com.example.no_more_trash.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.example.no_more_trash.R;

import org.osmdroid.util.GeoPoint;

public class FormulaireDecheterieActivity extends AppCompatActivity {
    private ImageView photo;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    String titre;
    GeoPoint geoPoint;
    private String fournisseur;
    private LocationManager locationManager = null;
    private Location localisation = null;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ModelDecheterie tmp=new ModelDecheterie();
        initialiserLocalisation();

        setContentView(R.layout.declaration_decheterie);

        //Boutton de validation du formulaire
        Button valider=findViewById(R.id.ValidationD);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Valider(v);
            }
        });

        EditText nom=findViewById(R.id.iddecharge);
        titre=nom.getText().toString();

        //Gestion de la prise de photo de la décharge
        View frag = findViewById(R.id.FragmentPhoto);
        this.photo = frag.findViewById(R.id.imageView);
        //Iniatilisation de la déchetterie
        tmp.setLocalisation(geoPoint);
        tmp.setNom(titre);
        tmp.setImage(photo);
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
        Intent gameActivity = new Intent(this, HomeUser.class);
        startActivity(gameActivity);
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

   public ModelDecheterie createDecheterie(){
        if(photo!=null){
            return new ModelDecheterie(geoPoint,titre,photo,"");
        }
        return new ModelDecheterie(geoPoint,titre,"");
   }
}
