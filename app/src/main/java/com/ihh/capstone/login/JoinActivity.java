package com.ihh.capstone.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ihh.capstone.R;
import com.ihh.capstone.StartActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JoinActivity extends AppCompatActivity {
    EditText joinId, joinPw1, joinPw2, userName, userRank, phoneNumber;

    Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join);

        init();
        setJoinBtn();
    }

    private void init() {
        joinId = findViewById(R.id.joinId);
        joinPw1 = findViewById(R.id.joinPw1);
        joinPw2 = findViewById(R.id.joinPw2);
        userName = findViewById(R.id.joinName);
        userRank = findViewById(R.id.joinRank);
        phoneNumber = findViewById(R.id.joinPoneNumber);
        joinBtn = findViewById(R.id.joinBtn);
    }

    //회원가입 버튼 클릭시 정보를 서버에 넘기고 시작화면으로 이동
    private void setJoinBtn() {
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textId = joinId.getText().toString();
                String textPw1 = joinPw1.getText().toString();
                String textPw2 = joinPw2.getText().toString();
                String textName = userName.getText().toString();
                String textRank = userRank.getText().toString();
                String textPhoneNumber = phoneNumber.getText().toString();
                boolean isGoToJoin = true;
                if(!textPw1.equals(textPw2)){
                    Toast.makeText(JoinActivity.this, "두 비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    isGoToJoin = false;
                }

                if (isGoToJoin){
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("연결 할 서버의 url")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    JoinService joinService = retrofit.create(JoinService.class);

                    joinService.requestJoin(textId, textPw1,textName,textRank,textPhoneNumber).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Toast.makeText(JoinActivity.this, "회원가입 되었습니다. 로그인을 진행해주세요.", Toast.LENGTH_SHORT).show();
                            //액티비티를 종료해 startActivity로 이동, 문제있을시 intent 사용
                            finish();
//                            Intent intent = new Intent(JoinActivity.this, StartActivity.class);
//                            startActivity(intent);
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(JoinActivity.this, "서버에 데이터를 보내지 못했습니다.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }
        });

    }
}