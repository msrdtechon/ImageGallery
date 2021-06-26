package com.example.imagegallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.imagegallery.Adapter.InflaterAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DownloadImgActivity extends AppCompatActivity {

    String path;
    Integer currentItem;
    ArrayList<String> photos;
    ViewPager viewPager;
    InflaterAdapter pagerAdapter;
    Toolbar toolbar;
    TextView tvToolbarTitle;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_img);


        Toolbar toolbar = findViewById(R.id.toolbar_gallery);
        tvToolbarTitle = (TextView) findViewById(R.id.tv1ToolbarTitle);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        tvToolbarTitle.setText("Gallery");

        View view = toolbar.getChildAt(0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DownloadImgActivity.this, MainActivity.class));
                finish();
            }
        });

        path = getIntent().getStringExtra("path");
        photos = getIntent().getStringArrayListExtra("images");
        viewPager = findViewById(R.id.viewPager);

        for(int i=0;i< photos.size();i++) {
            if(photos.get(i).equals(path)){
                currentItem = i;
            }
        }

        pagerAdapter = new InflaterAdapter(this, photos);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentItem);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.download){
            Log.d("TAG", photos.get(viewPager.getCurrentItem()));
            downloadImage(photos.get(viewPager.getCurrentItem()));
            return true;
        }
        return true;
    }

    private void downloadImage(String imageURL){

        if (!verifyPermissions()) {
            return;
        }

        boolean success = true;
        final String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
        File f = new File(Environment.getExternalStorageDirectory(), "Images");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                Files.createDirectory(Paths.get(f.getAbsolutePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if (!f.exists()) {
                f.mkdir();
                f.mkdirs();
                success = true;
            }
        }

        if (success) {


            Glide.with(this)
                    .load(imageURL)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                            Toast.makeText(DownloadImgActivity.this, "Saving Image...", Toast.LENGTH_SHORT).show();
                            saveImage(bitmap, f, fileName);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);

                            Toast.makeText(DownloadImgActivity.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void saveImage(Bitmap image, File storageDir, String imageFileName) {



        File imageFile = new File(storageDir, imageFileName);

        try {

            OutputStream fOut = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), imageFile.getAbsolutePath(), imageFileName, null);
            Toast.makeText(DownloadImgActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(DownloadImgActivity.this, "Error while saving image!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public Boolean verifyPermissions() {

        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(DownloadImgActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {

            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(DownloadImgActivity.this, STORAGE_PERMISSIONS, 1);
            return false;
        }

        return true;

    }
}