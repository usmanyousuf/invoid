package com.example.mylibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class captureimage1 extends AppCompatActivity {
    private final int CAMERA_ACTION_CODE = 1;
    Button cameraB;
    Button uploadB;
    ImageView uploadImage;

    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captureimage1);
        getSupportActionBar().hide();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        cameraB = findViewById(R.id.cameraB);
        uploadB = findViewById(R.id.uploadB);
        uploadImage = findViewById(R.id.uploadImage);
        findViewById(R.id.galleryB).setOnClickListener(v -> callGallery());
        uploadB.setOnClickListener(v -> uploadImage());
        cameraB.setOnClickListener(v -> {
            dispatchTakePictureIntent();
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if(intent.resolveActivity(getPackageManager()) != null){
//                startActivityForResult(intent, CAMERA_ACTION_CODE);
//            }else{
//                Toast.makeText(this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    private void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1002);
    }

    Uri filePath = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002) {

            if (resultCode == RESULT_OK
                    && null != data) {
                filePath = data.getData();
//                ImageView imageView = findViewById(R.id.image);
                uploadImage.setImageURI(filePath);
            }

        }
        else if (requestCode == CAMERA_ACTION_CODE && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            Bitmap finalPhoto = (Bitmap) bundle.get("data");
            uploadImage.setImageBitmap(finalPhoto);
            BitmapDrawable drawable = (BitmapDrawable) uploadImage.getDrawable();
            Bitmap bm = drawable.getBitmap();
            filePath=getImageUri(captureimage1.this,bm);
//            filePath = data.getData();
//            uploadImage.setImageURI(filePath);
        }
    }
    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, CAMERA_ACTION_CODE);
        } catch (ActivityNotFoundException e) {
        }
    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading

            final ProgressDialog progressDialog
                    = new ProgressDialog(captureimage1.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            Random random = new Random();
            int rand = random.nextInt(1233);
            final StorageReference ref
                    = storageReference
                    .child(
                            "Notifications/"
                                    + "image" + "/" + "img" + rand + ".jpg");

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            })
                    .addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                        if (task.isSuccessful()) {
                            final Uri downloadUrl = task.getResult();
//                            url = downloadUrl.toString();
                            progressDialog.dismiss();
                            finish();
                            Toast.makeText(captureimage1.this, "Uploaded", Toast.LENGTH_LONG).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(captureimage1.this, "Error Please check your internet connection ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> e.printStackTrace());

        }
    }
}



