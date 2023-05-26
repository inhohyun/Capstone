package com.ihh.capstone.OTP;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
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

    private Handler handler = new Handler();
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

        //fragment에서 viewModel 초기화시 onCreateView or onViewCreateed에서 초기화해야 함
        viewModel = new ViewModelProvider(OTPFragment.this).get(ViewModel.class);

        startTimer();
        //일단 함수 호출 주석처리해둠(오류 방지)
//        setUserinfo();
        logoutClick();
        return view;

    }

    private void logoutClick() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewModel 연동시 앱이 종료되는 현상?
//                viewModel.initData();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
            }
        });
    }


    //viewModel에서 사용자 정보를 꺼내 textView에 표시
    private void setUserinfo() {
        viewModel.getUserId().observe(getViewLifecycleOwner(), id -> {
            userId.setText(id);
        });
        viewModel.getUserName().observe(getViewLifecycleOwner(), name -> {
            userName.setText(name);
        });
        viewModel.getUserRank().observe(getViewLifecycleOwner(), rank -> {
            userRank.setText(rank);
        });
        viewModel.getUserPhoneNumber().observe(getViewLifecycleOwner(), phoneNumber -> {
            userPhoneNumber.setText(phoneNumber);
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

            }
        });

    }

    //아래는 timer를 활용해 30초에 한 번씩 otpkey를 서버에 보내는 함수 호출하고 ui 갱신하는 함수
    private void callFunction() {
        if (secondsPassed == 0) {
            secondsPassed = 30;
        }
        //viewModel에 저장해둔 otpKey로 함수 호출
        viewModel.getUserOtpKey().observe(getViewLifecycleOwner(), this::convertOTPCode);
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 0, 30000);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if (secondsPassed > 0) {
                secondsPassed -= 30;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callFunction();
                    //ui 갱신
                    timerTextView.setText(String.format("%02d:%02d", secondsPassed / 60, secondsPassed % 60));
                }
            });
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

}