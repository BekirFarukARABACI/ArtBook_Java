package com.bekirfarukarabaci.artbook_java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bekirfarukarabaci.artbook_java.databinding.ActivityArtBinding;
import com.bekirfarukarabaci.artbook_java.databinding.ActivityMainBinding;

public class ArtActivity extends AppCompatActivity {

    private ActivityArtBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        
    }

    public void save(View view){

    }

    public void selectImage(View view){

    }
}