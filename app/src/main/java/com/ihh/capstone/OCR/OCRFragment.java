package com.ihh.capstone.OCR;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ihh.capstone.R;

public class OCRFragment extends Fragment {

    private static final int REQUEST_IMAGE_PICK = 1;
    private Button selectImageBtn;
    private ImageView imageView;

    public OCRFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ocr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectImageBtn = view.findViewById(R.id.selectImageBtn);
        imageView = view.findViewById(R.id.imageView);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*"); //이미지 유형 지정
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == getActivity().RESULT_OK && data != null) {
            // 이미지를 선택한 후 처리할 작업을 여기에 추가하세요
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
            selectImageBtn.setVisibility(View.GONE);
            imageView.setBackgroundColor(getResources().getColor(android.R.color.white));
        }
    }
}