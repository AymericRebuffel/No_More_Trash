package com.example.no_more_trash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    public void declarationDechet(View view){
        Intent gameActivity = new Intent(MainActivity.this, DeclarationDechet.class);
        startActivity(gameActivity);
    }
}
