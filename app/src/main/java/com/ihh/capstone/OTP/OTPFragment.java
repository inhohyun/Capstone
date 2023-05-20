package com.ihh.capstone.OTP;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ihh.capstone.R;
import com.ihh.capstone.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class OTPFragment extends Fragment {
    private ViewModel viewModel;
    private TextView userId;
    private TextView userName;
    private TextView userRank;
    private TextView userPhoneNumber;

    private TextView userOTPCode;
    public OTPFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

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
        //일단 함수 호출 주석처리해둠(오류 방지)
//        setUserinfo();
        return view;

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
        //viewModel에 저장해둔 otpKey로 함수 호출
        viewModel.getUserOtpKey().observe(getViewLifecycleOwner(), this::convertOTPCode);
    }

    //서버에 otpKey를 보내고 otpCode를 리턴받는 함수
    private void convertOTPCode(String otpKey){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("otpCode를 보내줄 서버의 url")
                .build();

        OTPService optService = retrofit.create(OTPService.class);
        Call<String> sendTextCall = optService.sendOTPKey(otpKey);
        sendTextCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String responseText = response.body();
                    //ui에 otpCode 반영
                    userOTPCode.setText(responseText);

                } else {
                   //otpCode 반환 실패
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // 서버 연동 실패
            }
        });

    }
}