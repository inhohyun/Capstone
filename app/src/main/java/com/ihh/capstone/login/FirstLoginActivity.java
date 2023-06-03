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
import com.ihh.capstone.MainActivity;
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
                RequestFirstLogin login = new RequestFirstLogin(textId, textPw);

                ApiService apiService = RetrofitClient.getApiService();

                // Make the network request
                Call<ResponseLogin> call = apiService.requestLogin(login);

                call.enqueue(new Callback<ResponseLogin>() {
                    @Override
                    public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {

                        if (response.isSuccessful()) {

                            ResponseLogin responseLogin = response.body();
                            ;
                            //로그인 성공
                            Log.d("login", String.valueOf(response.code()));
                            //사용자 정보 viewModel에 저장, 서버에서 login에 setter로 저장해준 값을 getter를 통해 가져와서 viewModel에 저장
                            if (responseLogin != null) {
                                if (responseLogin.getId() != null) {
                                    Log.d("correct", responseLogin.getId());
                                    viewModel.setUserInfo(responseLogin.getId(), responseLogin.getName(), responseLogin.getRank(), responseLogin.getPhone(), responseLogin.getSecret());

                                    Toast.makeText(FirstLoginActivity.this, "1차 로그인 성공", Toast.LENGTH_SHORT).show();

                                    //그냥 intent할때 데이터를 넘겨주고 넘거받은 쪽에서 viewModel에 저장하는 걸로
                                    //2차 로그인 추가할때 한 번 더 거쳐가는 것으로 수정
                                    Intent intent = new Intent(FirstLoginActivity.this, MainActivity.class);
                                    intent.putExtra("id", responseLogin.getId());
                                    intent.putExtra("name", responseLogin.getName());
                                    intent.putExtra("rank", responseLogin.getRank());
                                    intent.putExtra("phone", responseLogin.getPhone());
                                    intent.putExtra("otpKey", responseLogin.getSecret());
                                    startActivity(intent);
                                } else {
                                    Log.d("idValue", "null");
                                }
                            }

                        } else {
                            Log.d("id, pw 불일치로 인한 에러", String.valueOf(response.code()));
                            Toast.makeText(FirstLoginActivity.this, "ID 또는 PW가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onFailure(Call<ResponseLogin> call, Throwable t) {
                        Log.d("웹 통신 실패", String.valueOf(t.getMessage()));
                        Toast.makeText(FirstLoginActivity.this, "데이터를 불러오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

}