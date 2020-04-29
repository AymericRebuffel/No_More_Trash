package com.example.no_more_trash.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.no_more_trash.R;
import com.example.no_more_trash.activities.DeclarationDechet;
import com.example.no_more_trash.activities.FormulaireDecheterieActivity;
import com.example.no_more_trash.activities.HomeAdmin;
import com.example.no_more_trash.activities.Map_Activity;


public class FragmentButton extends Fragment {
    public FragmentButton(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_button, container, false);
        Button dechetterie = v.findViewById(R.id.dechetterie);

        Button decharge = v.findViewById(R.id.decharge);
        Button dechette = v.findViewById(R.id.buttonDechetterie);

        dechetterie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declarationDechet(v);
            }
        });
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
        return v;
    }
    public void declarationDechet(View view){
        Intent gameActivity = new Intent(getContext(), DeclarationDechet.class);
        startActivity(gameActivity);
    }
    public void pageMap(){
        startActivity(new Intent(getContext(), Map_Activity.class));
    }

    public void declarationDecheterie(){
        startActivity(new Intent(getContext(), FormulaireDecheterieActivity.class));
    }
}