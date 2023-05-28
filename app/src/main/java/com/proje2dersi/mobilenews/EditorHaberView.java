package com.proje2dersi.mobilenews;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.proje2dersi.mobilenews.databinding.ActivityEditorHaberViewBinding;
import com.proje2dersi.mobilenews.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class EditorHaberView extends AppCompatActivity {

    private ActivityEditorHaberViewBinding binding;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditorHaberViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String text = intent.getStringExtra("text");
        String date = intent.getStringExtra("date");
        String imageURL =intent.getStringExtra("image");
        binding.date.setText(date);
        binding.text.setText(text);
        Picasso.get().load(imageURL).into(binding.image);
        actionBar = getSupportActionBar();
        actionBar.setTitle(""+title);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(0, 0, 0)));
    }
}