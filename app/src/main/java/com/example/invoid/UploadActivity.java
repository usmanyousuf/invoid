package com.example.invoid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mylibrary.captureimage1;

public class UploadActivity extends AppCompatActivity {
     Button button ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().hide();
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UploadActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
         button =findViewById(R.id.button);
         button.setOnClickListener(v -> {
             Intent intent = new Intent(UploadActivity.this, captureimage1.class);
             startActivity(intent);
         });
         perm();
    }

    void perm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }
}