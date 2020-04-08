package com.example.no_more_trash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //Bouton pour chercher un déchetterie
        ImageView exit = findViewById(R.id.icon_param);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Param_activity.class));
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
        decharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageMap();
            }
        });
    }

    public void declarationDechet(View view){
        Intent gameActivity = new Intent(MainActivity.this, DeclarationDechet.class);
        startActivity(gameActivity);
    }
    public void pageMap(){
        startActivity(new Intent(this,Map_Activity.class));
    }

}
