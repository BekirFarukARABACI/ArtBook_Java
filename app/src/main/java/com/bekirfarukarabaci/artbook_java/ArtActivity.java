package com.bekirfarukarabaci.artbook_java;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bekirfarukarabaci.artbook_java.databinding.ActivityArtBinding;
import com.bekirfarukarabaci.artbook_java.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

public class ArtActivity extends AppCompatActivity {

    private ActivityArtBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLauncher();
        
    }

    public void save(View view){

    }

        public void selectImage(View view){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                //Android 33 ve üstü ise -> READ_MEDIA_IMAGES
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_MEDIA_IMAGES)){
                        Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Request permission
                                permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                            }
                        }).show();
                    }
                    else {
                        //Request permission
                        permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                    }
                }
                else {
                    //gallery
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }
            }
            else {
                //Android 32 ve altı -> READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                        Snackbar.make(view,"Permission needed for gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give permission", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Request permission
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                            }
                        }).show();
                    }
                    else {
                        //Request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }
                else {
                    //gallery
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGallery);
                }

            }



        }



        private void registerLauncher(){

            activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode()==RESULT_OK){
                        Intent intentFromResult = o.getData();
                        if (intentFromResult != null){
                            Uri imageData = intentFromResult.getData();
                            //binding.imageView.setImageURI(imageData);

                            //Bitmap e dönüştürüyoruz

                            try {
                                if (Build.VERSION.SDK_INT >= 28){
                                    //API 28+ çalışır
                                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver() , imageData);
                                    selectedImage = ImageDecoder.decodeBitmap(source);
                                    binding.imageView.setImageBitmap(selectedImage);
                                }
                                else {
                                    selectedImage = MediaStore.Images.Media.getBitmap(ArtActivity.this.getContentResolver(),imageData);
                                    binding.imageView.setImageBitmap(selectedImage);
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

            permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean o) {
                    if (o){
                        //permission granted
                        Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        activityResultLauncher.launch(intentToGallery);
                    }
                    else {
                        //permission denied
                        Toast.makeText(ArtActivity.this,"Permission needed!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
}