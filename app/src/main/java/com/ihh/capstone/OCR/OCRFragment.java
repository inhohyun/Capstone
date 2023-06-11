package com.ihh.capstone.OCR;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.ihh.capstone.ApiService;
import com.ihh.capstone.MainActivity;
import com.ihh.capstone.R;
import com.ihh.capstone.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    private Uri mImageUrl;
    private String encodedImage;

    //상품명과 가격이 저장될 리스트
    private List<String> itemList;
    private List<Float> priceList;

    private BarChart barChart;
    public OCRFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemList = new ArrayList<>();
        priceList = new ArrayList<>();

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
        barChart = view.findViewById(R.id.barChart);


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

            //여기에서 두 문자열에 값 추가하고 그래프 그리기
            //아래는 예시로 넣어본 데이터
//            itemList.add("숙주볶음");
//            priceList.add(18000F);
//            itemList.add("새로");
//            priceList.add(25000F);
//            itemList.add("생합탕");
//            priceList.add(20000F);

            showBarGraph();
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

    //서버에 이미지를 보내고 상품명과 가격정보를 리턴받기
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
                        //data class에 저장
                        ResponseOCRText responseOCRText = response.body();
                        itemList = responseOCRText.getItemName();
                        priceList = responseOCRText.getItemPrice();

                        Log.d("OCR", String.valueOf(response.code()));

                    //두 문자열을 저장
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



    //이미지의 로컬 주소를 절대 경로로 전환(사용안할듯, 절대 경로 대신 base64값으로 전달)
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

    //두 리스트를 활용해 그래프 그리기
    private void showBarGraph() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < priceList.size(); i++) {
            entries.add(new BarEntry(i, priceList.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Price");
        dataSet.setColor(Color.BLUE);

        ArrayList<String> xLabels = new ArrayList<>(itemList);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new LabelFormatter(xLabels));

        // Set the maximum value for the y-axis
        float maxYValue = getMaxYValue(priceList);
        barChart.getAxisLeft().setAxisMaximum(maxYValue);

        // Set the minimum value for the y-axis
        barChart.getAxisLeft().setAxisMinimum(0f);

        // Customize the x-axis appearance
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setAxisLineColor(Color.BLACK);
        barChart.getXAxis().setTextColor(Color.BLACK);

        // Set the x-axis label rotation and position
        barChart.getXAxis().setLabelRotationAngle(45f);
        barChart.getXAxis().setLabelCount(xLabels.size());
        barChart.getXAxis().setXOffset(10f);
        barChart.getXAxis().setYOffset(10f);

        // Customize the y-axis appearance
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setAxisLineColor(Color.BLACK);
        barChart.getAxisLeft().setTextColor(Color.BLACK);

        barChart.getDescription().setText("");
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private float getMaxYValue(List<Float> values) {
        float max = 0;
        for (Float value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max + 1000;
    }

    private static class LabelFormatter extends com.github.mikephil.charting.formatter.ValueFormatter {
        private final List<String> labels;

        LabelFormatter(List<String> labels) {
            this.labels = labels;
        }

        @Override
        public String getFormattedValue(float value) {
            int index = (int) value;
            if (index >= 0 && index < labels.size()) {
                return labels.get(index);
            }
            return "";
        }
    }
}
    
    
    


