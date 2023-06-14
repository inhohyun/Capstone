package com.ihh.capstone.MultiData;

import
android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.vision.text.TextBlock;
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
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextRecognizer;

public class MultiDataFragment extends Fragment {

    private String voiceText; // 음성인식 결과 저장 변수
    private RadioGroup radioGroup;
    private TextView convertData;
    private ImageView convertImage;
    private TextToSpeech tts;
    private String inputData;

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
                inputData = voiceText;
                showRadioButtons(inputData);
            }
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // 이미지에 대한 처리를 수행합니다.
            Log.d("inputImage", "image");
            inputData = readTextFromImageUri(requireContext(), selectedImageUri);
            showRadioButtons(inputData);
            //이미지 uri를 base64로 인코딩한 값을 전달
//            showRadioButtons(encodeImageUriToBase64(selectedImageUri));
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }


    //텍스트로 입력
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
                        inputData = inputText;
                        //입력받은 문자열을 base64로 인코딩해 서버로 전달
                        showRadioButtons(inputData);
                    }
                })
                .setNegativeButton("취소", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //출력값을 선택하는 버튼 활성화, 버튼 클릭시 바로 서버 호출
    private void showRadioButtons(String inputData) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());

        //라디오 버튼이 다이얼로그로 뜨도록 구현
        dialogBuilder.setView(R.layout.dialog_radio);
        dialogBuilder.setTitle("출력 형식을 선택해주세요");
        dialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dialog dialogView = (Dialog) dialog;
                radioGroup = dialogView.findViewById(R.id.radioGroup);
                Log.d("dialog", "확인");
                //위에까지는 정상작동
                //출력 받을 데이터를 선택
                int selectedId = radioGroup.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radioVoice:
                        Log.d("radio", "voice");
                        tts.speak(inputData, TextToSpeech.QUEUE_FLUSH, null);
                        break;
                    case R.id.radioImage:
                        Log.d("radio", "Image");
                        //이미지로 출력 미구현
                        break;
                    case R.id.radioText:
                        Log.d("radio", "text");
                        // 텍스트 선택됨(완료)
                        convertData.setText(inputData);
                        break;
                }
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


    //아래는 멀티데이터에서 서버 미사용으로 사용안함
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

    //ocr 기능
    private String readTextFromImageUri(Context context, Uri imageUri) {
        try {
            // 이미지 URI를 비트맵으로 변환
            Bitmap imageBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));

            // TextRecognizer 초기화
            TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();

            if (!textRecognizer.isOperational()) {
                // OCR이 작동되지 않을 경우 예외 처리
                return "OCR이 작동되지 않습니다.";
            }

            // 이미지에서 텍스트 감지
            Frame frame = new Frame.Builder().setBitmap(imageBitmap).build();
            SparseArray<TextBlock> textBlocks = textRecognizer.detect(frame);

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            Log.e("ReadTextFromImage", "Error: " + e.getMessage());
            return "문자를 읽는 중 오류가 발생했습니다.";
        }

    }
}