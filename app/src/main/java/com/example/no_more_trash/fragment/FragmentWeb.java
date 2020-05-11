package com.example.no_more_trash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.example.no_more_trash.R;


public class FragmentWeb extends Fragment {
    public FragmentWeb(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View v=inflater.inflate(R.layout.webfragment,container,false);
        WebView page=(WebView)v.findViewById(R.id.pageweb);
        page.getSettings().setJavaScriptEnabled(true);
        page.setWebViewClient(new WebViewClient());
        page.loadUrl("https://twitter.com/Dechets_Infos");
        return v;
    }
}
