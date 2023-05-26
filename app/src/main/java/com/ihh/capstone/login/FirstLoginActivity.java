package com.ihh.capstone.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ihh.capstone.ApiService;
import com.ihh.capstone.R;
import com.ihh.capstone.RetrofitClient;
import com.ihh.capstone.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstLoginActivity extends AppCompatActivity {
    EditText emailArea;
    EditText passwordArea;
    Button loginBtn;

    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_login);

        emailArea = findViewById(R.id.emailArea);
        passwordArea = findViewById(R.id.passwordArea);
        loginBtn = findViewById(R.id.loginBtn);

        //viewModel 객체 생성
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        setLoginBtn();
    }

    //로그인 버튼 클릭시 서버에 아이디 비번 전달
    private void setLoginBtn() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textId = emailArea.getText().toString();
                String textPw = passwordArea.getText().toString();

                ApiService apiService = RetrofitClient.getApiService();

                // Make the network request
                Call<Login> call = apiService.requestLogin(textId, textPw);

                call.enqueue(new Callback<Login>() {
                    @Override
                    public void onResponse(Call<Login> call, Response<Login> response) {
                        Toast.makeText(FirstLoginActivity.this, "웹 통신 성공", Toast.LENGTH_LONG).show();
                        Login login = response.body(); // 서버에서 보내줄 데이터 받기

                        //로그인 성공
                        if (login != null && login.getIsSuccess().equals("success")) {

                            Log.d("login", "success");
                            //사용자 정보 viewModel에 저장, 서버에서 login에 setter로 저장해준 값을 getter를 통해 가져와서 viewModel에 저장
                            //에러시 viewModel 코드 지우고 연결해보기
                            viewModel.setUserInfo(login.getUserId(), login.getUserName(), login.getUserRank(), login.getUserPhoneNumber(), login.getUserOtpKey());


                            Toast.makeText(FirstLoginActivity.this, "1차 로그인 성공", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(FirstLoginActivity.this, SecondLoginActivity.class);
                            startActivity(intent);

                        }
                        //로그인 실패
                        else {
                            Log.d("login", "fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<Login> call, Throwable t) {
                        Toast.makeText(FirstLoginActivity.this, "웹 통신 실패", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

}