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


public class OTPFragment extends Fragment {
    private ViewModel viewModel;
    private TextView userId;
    private TextView userName;
    private TextView userRank;
    private TextView userPhoneNumber;
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
        setUserinfo();
        // Inflate the layout for this fragment
        return view;

    }
    //viewModel에서 사용자 정보를 꺼내 textView에 표시
    private void setUserinfo() {
        viewModel.getUserId().observe(getViewLifecycleOwner(), id ->{
            userId.setText(id);
        });
        viewModel.getUserName().observe(getViewLifecycleOwner(), name->{
            userName.setText(name);
        });
        viewModel.getUserRank().observe(getViewLifecycleOwner(), rank->{
            userRank.setText(rank);
        });
        viewModel.getUserPhoneNumber().observe(getViewLifecycleOwner(), phoneNumber->{
            userPhoneNumber.setText(phoneNumber);
        });
    }
}
//커밋 완료