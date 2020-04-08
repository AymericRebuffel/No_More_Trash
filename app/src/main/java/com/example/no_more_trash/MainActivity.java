package com.example.no_more_trash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Map_Activity map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //Bouton pour chercher un déchetterie
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
        Intent gameActivity = new Intent(MainActivity.this, DeclarationDechet.class);
        startActivity(gameActivity);
    }
    public void pageMap(){
        startActivity(new Intent(this,Map_Activity.class));
    }

    public void declarationDecheterie(){
        startActivity(new Intent(MainActivity.this,FormulaireDecheterieActivity.class));
    }
}
