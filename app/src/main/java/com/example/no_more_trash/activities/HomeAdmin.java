package com.example.no_more_trash.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.no_more_trash.R;

import org.w3c.dom.Text;

public class HomeAdmin extends AppCompatActivity {
    Map_Activity map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home_admin);

        //utilisation du fragment pour indiquer session administrateur
        View frag = findViewById(R.id.FragmentInterface);
        TextView infoSession = frag.findViewById(R.id.infoSession);
        infoSession.setText("Session Administrateur");

        //Boutton de paramètre
        ImageView param = findViewById(R.id.icon_param);
        param.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeAdmin.this, Param_activity.class));
            }
        });
    }
}
