package com.example.steven.cameraapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;


public class CameraAppMainActivity extends ActionBarActivity {

    private static final int REQUEST_IMAGE = 100;

    Button captureButton;
    ImageView imageView;
    File destination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_app_main);

        captureButton = (Button)findViewById(R.id.capture);
        captureButton.setOnClickListener(listener);
        imageView = (ImageView)findViewById(R.id.image);

        destination = new File(Environment
                .getExternalStorageDirectory(), "image.jpg");
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE
                && resultCode == Activity.RESULT_OK) {
            try {
                FileInputStream in =
                        new FileInputStream(destination);
                BitmapFactory.Options options =
                        new BitmapFactory.Options();
                options.inSampleSize = 10; //Downsample by 10x
                Bitmap userImage = BitmapFactory
                        .decodeStream(in, null, options);
                imageView.setImageBitmap(userImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private View.OnClickListener listener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent =
                                new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //Add extra to save full-image somewhere
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(destination));
                        startActivityForResult(intent, REQUEST_IMAGE);
                    } catch (ActivityNotFoundException e) {
                        //Handle if no application exists
                    }
                }
            };
}
