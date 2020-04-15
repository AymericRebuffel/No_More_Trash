package com.example.no_more_trash.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.no_more_trash.R;

public class HomeAdmin extends AppCompatActivity {
    Map_Activity map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_admin);
        //Bouton pour chercher un déchetterie
        ImageView exit = findViewById(R.id.icon_param);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeAdmin.this, Param_activity.class));
            }
        });
        Button dechetterie = findViewById(R.id.dechtterie);
        dechetterie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declarationDechet(v);
            }
        });
        Button decharge = findViewById(R.id.decharge);
        Button dechette=findViewById(R.id.buttonDecchete);
        decharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageMap();
            }
        });

        dechette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declarationDecheterie();
            }
        });
    }

    public void declarationDechet(View view){
        Intent gameActivity = new Intent(HomeAdmin.this, DeclarationDechet.class);
        startActivity(gameActivity);
    }
    public void pageMap(){
        startActivity(new Intent(this,Map_Activity.class));
    }

    public void declarationDecheterie(){
        startActivity(new Intent(HomeAdmin.this, FormulaireDecheterieActivity.class));
    }
}
