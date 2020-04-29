package com.example.no_more_trash.fragment.formulaire;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.no_more_trash.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class FragmentPhoto extends Fragment {
    private ImageView photo;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    public FragmentPhoto(){
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View v=inflater.inflate(R.layout.fragment_photo,container,false);

        Button boutonPhoto = v.findViewById(R.id.Photo);
        boutonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendrePhoto();
            }
        });

        this.photo = v.findViewById(R.id.imageView);
        return v;
    }

    private void prendrePhoto() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start camera and wait for the results.
        getActivity().startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.photo.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getContext(), "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }
}
