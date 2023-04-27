package com.ihh.capstone.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ihh.capstone.MainActivity;
import com.ihh.capstone.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText emailArea;
    EditText passwordArea;
    Button loginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        emailArea = findViewById(R.id.emailArea);
        passwordArea = findViewById(R.id.passwordArea);
        loginBtn = findViewById(R.id.loginBtn);

        setLoginBtn();
    }

    private void setLoginBtn() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textId = emailArea.getText().toString();
                String textPw = passwordArea.getText().toString();

                // Gson 통신을 할 Retrofit 객체 생성
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("연결 할 서버의 url")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LoginService loginService = retrofit.create(LoginService.class);

// Interface를 활용해 웹 통신하기
                loginService.requestLogin(textId, textPw).enqueue(new Callback<Login>() {
                    // 웹통신 성공
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        Toast.makeText(LoginActivity.this, "웹 통신 성공", Toast.LENGTH_LONG).show();
                        Login login = response.body(); // 서버에서 보내줄 데이터 받기

                        //로그인 성공
                        if (login != null && login.getIsSuccess().equals("success")) {

                            Log.d("login","success");

                        }
                        //로그인 실패
                        else {
                            Log.d("login","fail");
                        }
                    }

                    // 웹통신 실패
                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "웹 통신 실패", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

}