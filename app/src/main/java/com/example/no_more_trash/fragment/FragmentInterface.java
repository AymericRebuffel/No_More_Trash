package com.example.no_more_trash.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.no_more_trash.R;


public class FragmentInterface extends Fragment {
    public FragmentInterface(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View v=inflater.inflate(R.layout.fragment_interface,container,false);
        return v;
    }
}
