package com.example.no_more_trash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.no_more_trash.R;
import com.example.no_more_trash.models.ModelDechet;

import java.util.ArrayList;

public class MarkerDechet extends AppCompatActivity {
    private Button oui;
    private Button non;
    private TextView textView;
    ArrayList<ModelDechet> dechets;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_dechet);
        oui=findViewById(R.id.oui_marker);
        non=findViewById(R.id.non_marker);
        textView=findViewById(R.id.textView3);
        dechets=new ArrayList<ModelDechet>();
        oui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nettoyer();
            }
        });
        non.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pNettoyer();
            }
        });
    }

    private void nettoyer(){
        Intent swap=new Intent(this,Map_Activity.class);
        startActivity(swap);
    }

    private void pNettoyer(){
        Intent swap=new Intent(this,Map_Activity.class);
        startActivity(swap);
    }

}
