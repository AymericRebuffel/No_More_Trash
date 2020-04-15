package com.example.no_more_trash.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.no_more_trash.R;

import org.osmdroid.util.GeoPoint;

public class FormulaireDecheterieActivity extends AppCompatActivity {
    private ImageView photo;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    String titre;
    GeoPoint geoPoint;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.declaration_decheterie);
        Button valider=findViewById(R.id.ValidationD);
        EditText nom=findViewById(R.id.iddecharge);
        EditText longi=findViewById(R.id.longitude);
        EditText latti=findViewById(R.id.latitude);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Valider(v);
            }
        });
        Button boutonPhoto = findViewById(R.id.PhotoD);
        boutonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendrePhoto();
            }
        });
        this.photo = findViewById(R.id.imageViewD);
        titre=nom.getText().toString();
        geoPoint=new GeoPoint(Integer.parseInt(longi.getText().toString()),Integer.parseInt(latti.getText().toString()));

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

   public ModelDecheterie createDecheterie(){
        if(photo!=null){
            return new ModelDecheterie(geoPoint,titre,photo,"");
        }
        return new ModelDecheterie(geoPoint,titre,"");
   }
}
