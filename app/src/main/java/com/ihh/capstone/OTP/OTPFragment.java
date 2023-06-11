package com.ihh.capstone.OTP;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


public class OTPFragment extends Fragment implements TimerService.TimerCallback {
    private ViewModel viewModel;
    private TextView userId;
    private TextView userName;
    private TextView userRank;
    private TextView userPhoneNumber;
    private TextView userOTPCode;
    private Button logoutBtn;
    private boolean isServiceBound = false;
    private ServiceConnection serviceConnection;
    private BroadcastReceiver broadcastReceiver;
    private TextView countdownTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //viewModel의 데이터가 초기화 될경우 getActivity를 통해 수정 -> 여러 액티비티에서 공유하기에 requireActivity가 맞긴 함
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        countdownTextView = view.findViewById(R.id.countdownTextView);

        userId = view.findViewById(R.id.tv_userId);
        userName = view.findViewById(R.id.tv_userName);
        userRank = view.findViewById(R.id.tv_userRank);
        userPhoneNumber = view.findViewById(R.id.tv_userPhoneNumber);
        userOTPCode = view.findViewById(R.id.tv_OTPCode);

        logoutBtn = view.findViewById(R.id.btn_logout);

        Log.d("OTPFragment", "start");

        //사용자 정보 표시하기
        viewModel.getUserId().observe(getViewLifecycleOwner(), value -> userId.setText("ID: " + value));
        viewModel.getUserName().observe(getViewLifecycleOwner(), value -> userName.setText("성함: " + value));
        viewModel.getUserRank().observe(getViewLifecycleOwner(), value -> userRank.setText("직급: " + value));
        viewModel.getUserPhoneNumber().observe(getViewLifecycleOwner(), value -> userPhoneNumber.setText("핸드폰 번호: " + value));
        logoutClick();

        viewModel.getUserOtpKey().observe(getViewLifecycleOwner(), key -> {
            Log.d("otpKey", key);
            convertOTPCode(key);

        });

        return view;
    }

    @Override
    public void onTimerCallback() {

    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        TimerService.TimerBinder binder = (TimerService.TimerBinder) iBinder;
        isServiceBound = true;
        binder.getService().setTimerCallback(this);
        updateCountdownUI(binder.getService().getCountdownSeconds());
    }

    @Override
    public void onStart() {
        super.onStart();
        bindTimerService();
        registerBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        unbindTimerService();
        unregisterBroadcastReceiver();
    }

    private void bindTimerService() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                TimerService.TimerBinder binder = (TimerService.TimerBinder) iBinder;
                isServiceBound = true;
                updateCountdownUI(binder.getService().getCountdownSeconds());

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isServiceBound = false;
            }
        };
        Intent intent = new Intent(getActivity(), TimerService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindTimerService() {
        if (isServiceBound) {
            getActivity().unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void registerBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int countdown = intent.getIntExtra("countdown", 0);
                updateCountdownUI(countdown);
            }
        };

        IntentFilter intentFilter = new IntentFilter(TimerService.ACTION_TIMER_UPDATE);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    private void updateCountdownUI(int countdown) {
        countdownTextView.setText(String.valueOf(countdown));
        if (countdown == 1) {
            viewModel.getUserOtpKey().observe(getViewLifecycleOwner(), key -> {
                Log.d("otpKey", key);
                convertOTPCode(key);

            });
        }
    }

    //서버에 otpKey를 보내고 otpCode를 리턴받는 함수
    private void convertOTPCode(String otpKey) {
        RequestOTPCode sendOtp = new RequestOTPCode(otpKey);

        ApiService apiService = RetrofitClient.getApiService();

        Call<ResponseOTPCode> call = apiService.sendOTPKey(sendOtp);

        call.enqueue(new Callback<ResponseOTPCode>() {
            @Override
            public void onResponse(Call<ResponseOTPCode> call, Response<ResponseOTPCode> response) {
                if (response.isSuccessful()) {
                    ResponseOTPCode otpCode = response.body();
                    //ui에 otpCode 반영
                    userOTPCode.setText(otpCode.getOTPCode());
                    Log.d("OTPKey", otpCode.getOTPCode());

                } else {
                    //otpCode 반환 실패
                    Log.d("onResponse But fail", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseOTPCode> call, Throwable t) {
                //서버 연동 실패
                Log.d("onFailure fail", t.getMessage());
            }
        });
    }

    //로그아웃 버튼 클릭
    private void logoutClick() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //viewModel 연동시 앱이 종료되는 현상?
                viewModel.initData();
                Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
            }
        });
    }
}