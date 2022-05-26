package com.bhat.av;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt.PromptInfo;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.RadioGroup;

import java.util.concurrent.Executor;

public class AppListFilterActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list_filter);

        initialize();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.choiceAllApps:{
                                                    Intent intent= new Intent(AppListFilterActivity.this,MainActivity.class);
                                                    intent.putExtra("filter","0");
                                                    startActivity(intent);
                                                    finish();
                                                    break;
                                            }

                    case R.id.choiceLowRisk:{
                                                     Intent intent= new Intent(AppListFilterActivity.this,MainActivity.class);
                                                     intent.putExtra("filter","1");
                                                     startActivity(intent);
                                                     finish();
                                                     break;
                                            }

                    case R.id.choiceHighRisk:{
                                                    Intent intent= new Intent(AppListFilterActivity.this,MainActivity.class);
                                                    intent.putExtra("filter","2");
                                                    startActivity(intent);
                                                    finish();
                                                    break;
                                             }

                    case R.id.choiceExtremeRisk:{
                                                    Intent intent= new Intent(AppListFilterActivity.this,MainActivity.class);
                                                    intent.putExtra("filter","3");
                                                    startActivity(intent);
                                                    finish();
                                                    break;
                                                }

                }
            }
        });
    }

    private void initialize() {

        radioGroup = findViewById(R.id.filterRadioChoiceGroup);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}