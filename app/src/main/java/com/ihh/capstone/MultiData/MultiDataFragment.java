package com.ihh.capstone.MultiData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ihh.capstone.ApiService;
import com.ihh.capstone.R;
import com.ihh.capstone.RetrofitClient;
import com.ihh.capstone.login.RequestFirstLogin;
import com.ihh.capstone.login.ResponseLogin;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MultiDataFragment extends Fragment {

    private String voiceText; // 음성인식 결과 저장 변수
    private RadioGroup radioGroup;
    private String returnType;
    private TextView convertData;
    private ImageView convertImage;
    private TextToSpeech tts;

    public MultiDataFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tts 초기화
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != android.speech.tts.TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multi_data, container, false);
        convertData = view.findViewById(R.id.tv_convertData);
        convertImage = view.findViewById(R.id.iv_convertData);
        //초기 시작시 이미지뷰는 안보이게
        convertImage.setVisibility(View.INVISIBLE);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout voiceLayout = view.findViewById(R.id.voice);
        LinearLayout imageLayout = view.findViewById(R.id.image);
        LinearLayout textLayout = view.findViewById(R.id.text);


        //음성 입력을 클릭한 경우
        voiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // voicelayout 클릭 이벤트
                // 음성인식 인텐트 생성
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                // 음성인식 시작 startActivityForResult 호출
                int requestCode = 1; //
                startActivityForResult(intent, requestCode);
            }
        });
        //이미지 입력을 클릭한 경우
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imagelayout 클릭 이벤트
                openGallery();
            }
        });
        //텍스트 입력을 클릭한 경우
        textLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // textLayout 클릭 이벤트
                showTextInputDialog();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> voiceResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (voiceResults != null && !voiceResults.isEmpty()) {
                String voiceText = voiceResults.get(0);
                // voiceText 변수에 음성인식 결과가 저장

                //저장한 음성의 문자열을 base64로 인코딩해 서버로 전달
                showRadioButtons(encodeStringToBase64(voiceText));
            }
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // 이미지에 대한 처리를 수행합니다.

            //이미지 uri를 base64로 인코딩한 값을 전달
            showRadioButtons(encodeImageUriToBase64(selectedImageUri));
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }


    private void showTextInputDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        builder.setTitle("텍스트 입력")
                .setView(R.layout.dialog_text_input)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 확인 버튼이 클릭되었을 때의 처리
                        Dialog dialogView = (Dialog) dialog;
                        EditText editText = dialogView.findViewById(R.id.editText);
                        String inputText = editText.getText().toString();
                        // inputText 변수에 사용자가 입력한 텍스트 저장

                        //입력받은 문자열을 base64로 인코딩해 서버로 전달
                        showRadioButtons(encodeStringToBase64(inputText));
                    }
                })
                .setNegativeButton("취소", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //출력값을 선택하는 버튼 활성화, 버튼 클릭시 바로 서버 호출
    //inputData 값은 base64로 인코딩된 값임
    private void showRadioButtons(String inputData) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());

        //라디오 버튼이 다이얼로그로 뜨도록 구현
        dialogBuilder.setView(R.layout.dialog_radio);
        dialogBuilder.setTitle("출력 형식을 선택해주세요");
        dialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //inputData, radio 클릭 결과를 활용해 서버 호출하기
                Dialog dialogView = (Dialog) dialog;
                radioGroup = dialogView.findViewById(R.id.radioGroup);
                Log.d("dialog", "확인");
                //inputData를 base64로 인코딩하는 작업 필요
                //전송 데이터 객체 생성
                RequestMultiData multiData = new RequestMultiData(inputData);
                ApiService apiService = RetrofitClient.getApiService();

                //출력 받을 데이터를 선택
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    //서버에 보낼 클래스에 해당 버튼의 결과 setter로 저장해 보내기
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        Log.d("radio", "test");
                        // 라디오 버튼 선택 이벤트 처리
                        switch (checkedId) {
                            case R.id.radioVoice:
                                // 음성 선택됨(완료)
                                Log.d("voice", "returnVoice");
                                Call<ResponseMultiData> call1 = apiService.requestMultiData(multiData);
                                call1.enqueue(new Callback<ResponseMultiData>() {
                                    @Override
                                    public void onResponse(Call<ResponseMultiData> call, Response<ResponseMultiData> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("convertVoice", String.valueOf(response.code()));

                                            String data = String.valueOf(response.body());
                                            //data를 디코딩하고 이를 음성으로 출력하기(작성예정)
                                            String base64String = data;
                                            String decodedString = decodeBase64ToString(base64String);
                                            //문자열을 음성으로 출력
                                            tts.speak(decodedString, TextToSpeech.QUEUE_FLUSH, null);
                                        } else {
                                            Log.d("convertFail1", String.valueOf(response.code()));
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseMultiData> call, Throwable t) {
                                        Log.d("convertFail2", String.valueOf(t.getMessage()));
                                    }
                                });

                                break;
                            case R.id.radioImage:
                                // 이미지 선택됨(완료)
                                returnType = "Image";

                                Call<ResponseMultiData> call2 = apiService.requestMultiData(multiData);
                                call2.enqueue(new Callback<ResponseMultiData>() {
                                    @Override
                                    public void onResponse(Call<ResponseMultiData> call, Response<ResponseMultiData> response) {
                                        if (response.isSuccessful()) {

                                            Log.d("convert", String.valueOf(response.code()));
                                            String data = String.valueOf(response.body());
                                            //data를 원래 이미지로 디코딩하고 이를 imageView로 보여줌
                                            convertImage.setImageBitmap(decodeBase64ToImage(data));
                                            //textView는 안보이게 설정
                                            convertData.setVisibility(View.INVISIBLE);

                                        } else {
                                            Log.d("convertFail1", String.valueOf(response.code()));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseMultiData> call, Throwable t) {
                                        Log.d("convertFail2", String.valueOf(t.getMessage()));
                                    }
                                });
                                break;
                            case R.id.radioText:
                                // 텍스트 선택됨(완료)
                                returnType = "text";
                                Log.d("text", returnType);
                                Call<ResponseMultiData> call3 = apiService.requestMultiData(multiData);
                                call3.enqueue(new Callback<ResponseMultiData>() {
                                    @Override
                                    public void onResponse(Call<ResponseMultiData> call, Response<ResponseMultiData> response) {
                                        if (response.isSuccessful()) {
                                            Log.d("convert", String.valueOf(response.code()));
                                            String base64StringData = String.valueOf(response.body());
                                            //base64를 바이트 데이터로 디코딩
                                            byte[] decodedBytes = Base64.decode(base64StringData, Base64.DEFAULT);
                                            //디코딩된 바이트 배열을 원래 문자열로 변환
                                            String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
                                            //이미지 뷰는 안보이게
                                            convertImage.setVisibility(View.INVISIBLE);
                                            convertData.setText(decodedString);

                                        } else {
                                            Log.d("convertFail1", String.valueOf(response.code()));
                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseMultiData> call, Throwable t) {
                                        Log.d("convertFail2", String.valueOf(t.getMessage()));
                                    }
                                });

                                break;
                        }
                    }
                });
            }


        });
        dialogBuilder.setNegativeButton("취소", null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    //이미지의 uri값을 base64로 인코딩
    public String encodeImageUriToBase64(Uri imageUri) {
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = getBytesFromInputStream(inputStream);
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    //문자열 데이터를 base64로 인코딩
    public String encodeStringToBase64(String value) {
        byte[] bytes = value.getBytes(Charset.forName("UTF-8"));
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    //base64를 비트맵 이미지로 디코딩
    public Bitmap decodeBase64ToImage(String base64Image) {
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    // Base64로 인코딩된 문자열을 원래 문자열로 디코딩하는 메서드
    public String decodeBase64ToString(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


}