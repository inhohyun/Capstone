package com.ihh.capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ihh.capstone.MultiData.MultiDataFragment;
import com.ihh.capstone.OCR.OCRFragment;
import com.ihh.capstone.OTP.OTPFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        settingBottomListener();

        
    }



    //앱 시작할 때 뷰 연결
    private void init() {
        container = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    //바텀 네비게이션 세팅
    private void settingBottomListener() {
        //시작을 lostFragment로
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new OTPFragment()).commit();

        BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {
                            case R.id.navigation_OTP:
                                selectedFragment = new OTPFragment();
                                break;
                            case R.id.navigation_OCR:
                                selectedFragment = new OCRFragment();
                                break;
                            case R.id.navigation_MultiData:
                                selectedFragment = new MultiDataFragment();
                                break;
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();

                        return true;

                    }
                };
       bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }
}