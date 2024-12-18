package com.my.arkku.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.os.CancellationSignal;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LockScreenActivity extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the Executor
        executor = Executors.newSingleThreadExecutor();

        // Set up the BiometricPrompt
        biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // On success, navigate to MainActivity
                        showToast("Authentication Succeeded");
                        navigateToMain();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        // Handle failed authentication
                        showToast("Authentication failed. Try again.");
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);

                        // Handle authentication errors (e.g., user cancels)
                        showToast("Authentication error: " + errString);

                        if (errorCode == BiometricPrompt.ERROR_CANCELED) {
                            // Handle cancellation by the user or the system
                            showToast("Authentication cancelled");
                        } else {
                            // Handle other errors here
                            showToast("Authentication error: " + errString);
                        }

                        // Close LockScreenActivity and MainActivity completely
                        finishAffinity();  // This will finish LockScreenActivity and all other activities in the stack
                    }
                });

        // Set up the BiometricPrompt UI with allowed authenticators for API 30+
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setDescription("Use your fingerprint, face, or device PIN to unlock")
                // Use setAllowedAuthenticators for specifying the allowed authenticators
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build();

        // Launch BiometricPrompt
        biometricPrompt.authenticate(promptInfo);
    }

    // Navigate to the main activity after successful authentication
    private void navigateToMain() {
        finish(); // Close LockScreenActivity
    }

    // Display toast messages for user feedback
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(LockScreenActivity.this, message, Toast.LENGTH_SHORT).show());
    }
}
