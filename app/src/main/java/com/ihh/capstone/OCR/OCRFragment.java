package com.ihh.capstone.OCR;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ihh.capstone.ApiService;
import com.ihh.capstone.MainActivity;
import com.ihh.capstone.R;
import com.ihh.capstone.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OCRFragment extends Fragment {
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_IMAGE_PICK = 1;
    private Button selectImageBtn;
    private ImageView imageView;
    private TextView OCRText;
    private Uri mImageUrl;
    private String encodedImage;
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



            //uri를 비트맵으로 변환하고 이를 다시 base64로 인코딩, 이를 서버로 보냄
            sendImage(BitmapToString(UriToBitmap(imageUri)));
        }


    }
    //uri 값을 비트맵 값으로 변환하고 변환한 비트맵 값을 base64로 인코딩하기
    public Bitmap UriToBitmap(Uri imageuri) {
        Bitmap bm = null;
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                //에러가 있을 경우 requireActivity를 바꿔볼 것
                bm = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().getContentResolver(), imageuri));
            } else {
                bm = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageuri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }
    //비트맵을 Base64로 인코딩하여 리턴
    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] bytes = baos.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

    //서버에 이미지를 보내고 문자열을 리턴받는 메소드
    private void sendImage(String sendImageData) {



            ApiService apiService = RetrofitClient.getApiService();
            //서버와 호환이 안될경우 data class 활용용
            RequestOCRImage image = new RequestOCRImage(sendImageData);

            Call<ResponseOCRText> uploadCall = apiService.uploadImage(image);
            uploadCall.enqueue(new Callback<ResponseOCRText>() {
                @Override
                public void onResponse(Call<ResponseOCRText> call, Response<ResponseOCRText> response) {
                    //서버에서 무사히 문자열 값을 받은 경우 해당 문자열을 화면에 연동
                    if (response.isSuccessful()) {
                        Log.d("OCR", String.valueOf(response.code()));

//                    String textResponse = String.valueOf(response.body());
//                    // 정상적으로 값을 받아올시 이를 ui에 연동
//                    OCRText.setText(textResponse);
                    } else {
                        //서버와 연동은 성공했으나 문자열 값을 받아오지 못함
                        Log.d("OCRfFail1", String.valueOf(response.code()));
                    }
                }

                @Override
                public void onFailure(Call<ResponseOCRText> call, Throwable t) {
                    // 서버와 연결되지 못함
                    Log.d("OCRFail2", String.valueOf(t.getMessage()));
                }
            });
        }



    //이미지의 로컬 주소를 절대 경로로 전환
    public String getAbsolutePath(Uri path, Context context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(path, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file access
                // Your code to access the file goes here


            } else {
                // Permission denied, handle accordingly (e.g., show an error message)
            }
        }
    }
}
    
    
    


