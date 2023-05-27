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
import android.widget.TextView;

import com.ihh.capstone.ApiService;
import com.ihh.capstone.R;
import com.ihh.capstone.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OCRFragment extends Fragment {

    private static final int REQUEST_IMAGE_PICK = 1;
    private Button selectImageBtn;
    private ImageView imageView;
    private TextView OCRText;

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
        OCRText = view.findViewById(R.id.tv_OCR);

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

//            sendImage(imageUri);
        }


    }

    //서버에 이미지를 보내고 문자열을 리턴받는 메소드
    private void sendImage(Uri imageUri) {
        File imageFile = new File(imageUri.getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        //imagePart : 서버로 보낼 이미지 데이터
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);


        ApiService apiService = RetrofitClient.getApiService();

        Call<OCRText> uploadCall = apiService.uploadImage(imagePart);
        uploadCall.enqueue(new Callback<OCRText>() {
            @Override
            public void onResponse(Call<OCRText> call, Response<OCRText> response) {
                //서버에서 무사히 문자열 값을 받은 경우 해당 문자열을 화면에 연동
                if (response.isSuccessful()) {
                    String textResponse = String.valueOf(response.body());
                    // 정상적으로 값을 받아올시 이를 ui에 연동
                    OCRText.setText(textResponse);
                } else {
                    //서버와 연동은 성공했으나 문자열 값을 받아오지 못함
                }
            }

            @Override
            public void onFailure(Call<OCRText> call, Throwable t) {
                // 서버와 연결되지 못함
            }
        });
    }
}
    
    
    


