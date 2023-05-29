package com.ihh.capstone.MultiData;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.ihh.capstone.R;

import java.util.ArrayList;
import java.util.Locale;


public class MultiDataFragment extends Fragment {

    private String voiceText; // 음성인식 결과 저장 변수
    private RadioGroup radioGroup;

    public MultiDataFragment() {
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
        return inflater.inflate(R.layout.fragment_multi_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout voiceLayout = view.findViewById(R.id.voice);
        LinearLayout imageLayout = view.findViewById(R.id.image);
        LinearLayout textLayout = view.findViewById(R.id.text);

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setVisibility(View.GONE);
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
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imagelayout 클릭 이벤트
                openGallery();
            }
        });

        textLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // textLayout 클릭 이벤트
                showTextInputDialog();
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 라디오 버튼 선택 이벤트 처리
                switch (checkedId) {
                    case R.id.radioVoice:
                        // 음성 선택됨
                        break;
                    case R.id.radioImage:
                        // 이미지 선택됨
                        break;
                    case R.id.radioText:
                        // 텍스트 선택됨
                        break;
                }
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
                showRadioButtons();
            }
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // 이미지에 대한 처리를 수행합니다.
            String imagePath = selectedImageUri.toString(); // 이미지 URI를 변수에 저장하는 경우

            showRadioButtons();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }
    private void showRadioButtons() {
        radioGroup.setVisibility(View.VISIBLE); // 라디오 버튼 보이기
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
                        showRadioButtons();
                    }
                })
                .setNegativeButton("취소", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}