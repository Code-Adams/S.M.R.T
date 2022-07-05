package com.bhat.av;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class SplashScreenActivity extends AppCompatActivity {

   private BiometricManager biometricManager; //From androidx.biometric
   private androidx.biometric.BiometricPrompt biometricPrompt;
   private   Executor executor;
   private androidx.biometric.BiometricPrompt.PromptInfo promptInfo;

   public LinearLayout splashScreenMainLayout;

   private Boolean ShowSplashScreen= false, ShowBiometricPrompt= false;
   private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //To cover whole screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //To hide action bar
        getSupportActionBar().hide();

        //initialize all variables
        initialize();
       // Check Biometric available
        hasBiometric();

    }

    private void showSplashScreen(Boolean splashScreen) {

        if(splashScreen){

            splashScreenMainLayout.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this,AppListFilterActivity.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_TIME_OUT);


        }

    }

    private void hasBiometric() {
        biometricManager= BiometricManager.from(SplashScreenActivity.this);

        switch(biometricManager.canAuthenticate()){

            case  BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:{
                                                                    Toast.makeText(SplashScreenActivity.this, "Error: Biometric Sensor not found", Toast.LENGTH_SHORT).show();

                                                                    break;
                                                               }
            case  BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:{
                                                                    Toast.makeText(SplashScreenActivity.this, "Error: Biometric Sensor not working", Toast.LENGTH_SHORT).show();
                                                                    break;
                                                                }
            case  BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:{
                                                                    Toast.makeText(SplashScreenActivity.this, "Error: Biometric security not set up", Toast.LENGTH_SHORT).show();
                                                                    break;
                                                                }
            case BiometricManager.BIOMETRIC_SUCCESS:{
                                                        showBiometricPrompt(true);
                                                        break;
                                                    }

        }
    }

    private void showBiometricPrompt(Boolean biometricPromptShow) {

        if(biometricPromptShow) {
            biometricPrompt.authenticate(promptInfo);
        }
    }

    private void initialize() {

        splashScreenMainLayout= findViewById(R.id.splashScreenMain);
        executor= ContextCompat.getMainExecutor(SplashScreenActivity.this);
        biometricPrompt= new BiometricPrompt(SplashScreenActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                Toast.makeText(SplashScreenActivity.this, "Sorry can't authenticate you right now.", Toast.LENGTH_LONG).show();
                finish();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Toast.makeText(SplashScreenActivity.this, "Success", Toast.LENGTH_SHORT).show();
                showSplashScreen(true);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                Toast.makeText(SplashScreenActivity.this, "Auth Failed: Try again later", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        //Setting up title, description on auth dialog
        promptInfo= new BiometricPrompt.PromptInfo.Builder()
                        .setDeviceCredentialAllowed(true)
                        .setTitle("Biometric Auth")
                        .setSubtitle("Auth using fingerprint or face")
                        .setDescription("Your fingerprint is required to unlock this app")
                        .build();

    }
}