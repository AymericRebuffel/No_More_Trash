package com.example.no_more_trash.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.example.no_more_trash.R;

public class Param_activity extends AppCompatActivity {

    Button t7;
    Intent i = new Intent(Intent.ACTION_SEND);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.param_activity);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void onClick(View v) {
        i.setType("message/rfc822");
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"nomoretrash@mail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Ton Sujet");
        startActivity(Intent.createChooser(i, "Titre:"));
    };
}
