package com.ihh.capstone.OTP;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ihh.capstone.ApiService;
import com.ihh.capstone.R;
import com.ihh.capstone.RetrofitClient;
import com.ihh.capstone.StartActivity;
import com.ihh.capstone.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OTPFragment extends Fragment {
    private ViewModel viewModel;
    private TextView userId;
    private TextView userName;
    private TextView userRank;
    private TextView userPhoneNumber;

    private TextView userOTPCode;

    private Handler handler;
    private Timer timer;
    private int secondsPassed = 30;
    private TextView timerTextView;

    private Button logoutBtn;

    public OTPFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        userId = view.findViewById(R.id.tv_userId);
        userName = view.findViewById(R.id.tv_userName);
        userRank = view.findViewById(R.id.tv_userRank);
        userPhoneNumber = view.findViewById(R.id.tv_userPhoneNumber);
        userOTPCode = view.findViewById(R.id.tv_OTPCode);
        timerTextView = view.findViewById(R.id.timerTextView);
        logoutBtn = view.findViewById(R.id.btn_logout);
        //타이머를 조작할 handler 초기화
        handler = new Handler();
        //fragment에서 viewModel 초기화시 onCreateView or onViewCreateed에서 초기화해야 함
        viewModel = new ViewModelProvider((ViewModelStoreOwner) getViewLifecycleOwner()).get(ViewModel.class);

        startTimer();
        setUserinfo();
        logoutClick();

        return view;

    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (secondsPassed > 0) {
                    secondsPassed--;
                    updateTimerUI();
                } else {
                    //30초에 한 번씩 otpCode 갱신 및 시간 초기화
                    viewModel.getUserOtpKey().observe(getViewLifecycleOwner(), key -> {
                        convertOTPCode(key);
                    });
                    restartTimer();
                }
            }
        }, 0, 1000); // Delay of 0 milliseconds and repeat every 1 second
    }

    private void restartTimer() {
        timer.cancel();
        secondsPassed = 30;
        startTimer();
    }

    private void updateTimerUI() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                timerTextView.setText(String.valueOf(secondsPassed));
            }
        });
    }


    //서버에 otpKey를 보내고 otpCode를 리턴받는 함수
    private void convertOTPCode(String otpKey) {

        ApiService apiService = RetrofitClient.getApiService();
        Call<OTP> call = apiService.sendOTPKey(otpKey);

        call.enqueue(new Callback<OTP>() {
            @Override
            public void onResponse(Call<OTP> call, Response<OTP> response) {
                if (response.isSuccessful()) {
                    String responseText = String.valueOf(response.body());
                    //ui에 otpCode 반영
                    userOTPCode.setText(responseText);

                } else {
                    //otpCode 반환 실패
                }
            }

            @Override
            public void onFailure(Call<OTP> call, Throwable t) {
                //서버 연동 실패
            }
        });
    }


    private void logoutClick() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //viewModel 연동시 앱이 종료되는 현상?
//                 viewModel.initData();
                viewModel.getUserOtpKey().observe(getViewLifecycleOwner(), otpk -> {
                    Log.d("됐나?", otpk);
                });

                //               Intent intent = new Intent(getActivity(), StartActivity.class);
                //               startActivity(intent);
            }
        });
    }


    //viewModel에서 사용자 정보를 꺼내 textView에 표시
    private void setUserinfo() {
        //     안되면  OTPFragment.this -> getViewLifecycleOwner() 바꿔보기
        viewModel.getUserId().observe(getViewLifecycleOwner(), id -> {
            userId.setText("ID: " + id);
        });
        viewModel.getUserName().observe(getViewLifecycleOwner(), name -> {
            userName.setText("성함: " + name);
        });
        viewModel.getUserRank().observe(getViewLifecycleOwner(), rank -> {
            userRank.setText("직급: " + rank);
        });
        viewModel.getUserPhoneNumber().observe(getViewLifecycleOwner(), phoneNumber -> {
            userPhoneNumber.setText("핸드폰 번호: " + phoneNumber);
        });

    }
}