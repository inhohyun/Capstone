package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Button btn_picture;
    private Bitmap imageBitmap;
    private ImageView imageView;
    private static final int REQUEST_IMAGE_CODE = 101;
    private final ActivityResultLauncher<Intent> imageCaptureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageview);

        btn_picture = findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takepicture();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageBitmap != null) {
            imageBitmap.recycle();
            imageBitmap = null;
        }
    }
    public void takepicture() {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//카메라 앱 호출
        if (imageIntent.resolveActivity(getPackageManager()) != null) {
            imageCaptureLauncher.launch(imageIntent);
        }
    }
}