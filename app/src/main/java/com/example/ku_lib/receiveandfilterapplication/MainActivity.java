package com.example.ku_lib.receiveandfilterapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ask to get permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }

        // for get image
        Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);

        if (uri != null) {
            try {
                ImageView imageView = findViewById(R.id.imageView);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                options.inMutable = true;
                options.inSampleSize = 2;

                String path = PathUtil.getPath(this, uri);

                Log.i("MyLog", "Path: " + path);

                Bitmap bitmap = BitmapFactory.decodeFile(path, options);

                if (bitmap == null)
                    Log.i("MyLog", "bitmap is null.");

                Filter filter = new Filter();
                filter.addSubFilter(new BrightnessSubfilter(70));
                filter.addSubFilter(new ContrastSubfilter(1.1f));
                bitmap = filter.processFilter(bitmap);

                imageView.setImageBitmap(bitmap);
                Log.i("MyLog", "Finish.");

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

    }
}
