package com.ihh.capstone.OTP;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.ihh.capstone.ApiService;
import com.ihh.capstone.MainActivity;
import com.ihh.capstone.R;
import com.ihh.capstone.RetrofitClient;
import com.ihh.capstone.ViewModel;
import com.ihh.capstone.login.FirstLoginActivity;

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
    private String mOTPKey;

    public OTPFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //viewModel의 데이터가 초기화 될경우 getActivity를 통해 수정 -> 여러 액티비티에서 공유하기에 requireActivity가 맞긴 함
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_otp, container, false);
        userId = view.findViewById(R.id.tv_userId);
        userName = view.findViewById(R.id.tv_userName);
        userRank = view.findViewById(R.id.tv_userRank);
        userPhoneNumber = view.findViewById(R.id.tv_userPhoneNumber);
        userOTPCode = view.findViewById(R.id.tv_OTPCode);
        timerTextView = view.findViewById(R.id.timerTextView);
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

        //타이머를 조작할 handler 초기화
        handler = new Handler();
        startTimer();
        return view;
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
                    mOTPKey = otpCode.getOTPCode();
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
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.getUserOtpKey().observe(getViewLifecycleOwner(), key -> {
                                Log.d("reCallOTPCode", "reCallOTPCode");
                                convertOTPCode(key);
                                userOTPCode.setText(mOTPKey);

                            });

                        }
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


    private void logoutClick() {
        logoutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //viewModel 연동시 앱이 종료되는 현상?
//                 viewModel.initData();
                viewModel.getUserOtpKey().observe(getViewLifecycleOwner(), otpk -> {
                    Log.d("logout", otpk);
                });

                //               Intent intent = new Intent(getActivity(), StartActivity.class);
                //               startActivity(intent);
            }
        });
    }


    //viewModel에서 사용자 정보를 꺼내 textView에 표시

}