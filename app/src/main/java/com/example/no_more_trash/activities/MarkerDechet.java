package com.example.no_more_trash.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.no_more_trash.R;
import com.example.no_more_trash.models.ModelDechet;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MarkerDechet extends AppCompatActivity {
    private Button oui;
    private Button non;
    private TextView textView;
    ModelDechet dechet;
    private Intent intent;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_dechet);
        intent=getIntent();
        oui=findViewById(R.id.oui_marker);
        non=findViewById(R.id.non_marker);
        textView=findViewById(R.id.textView3);
        if(intent.hasExtra("Cdechet")){
            dechet=intent.getParcelableExtra("Cdechet");
        }
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
        dechet.setCleaned(true);
        try {
            writeJson();
        }catch (IOException e){
            e.printStackTrace();
        }
        Intent swap=new Intent(this,Map_Activity.class);
        swap.putExtra("result",(Parcelable) dechet);
        MarkerDechet.this.setResult(1,swap);
        MarkerDechet.this.finish();
    }

    private void pNettoyer(){
        Intent swap=new Intent(this,Map_Activity.class);
        MarkerDechet.this.setResult(0,swap);
        MarkerDechet.this.finish();
    }
    public void writeJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String res = "";
        try {
            res = mapper.writeValueAsString(dechet);
            System.out.println(mapper.writeValueAsString(dechet));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        FileOutputStream fos = openFileOutput("database_CleanedGarbage.json",MODE_APPEND);
        if(fos!=null){
            fos.write((""+res+"\n").getBytes());
            fos.close();
        }
    }

}
