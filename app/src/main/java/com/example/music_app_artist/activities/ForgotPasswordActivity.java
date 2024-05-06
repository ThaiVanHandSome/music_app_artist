package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.music_app_artist.R;
import com.example.music_app_artist.models.ForgotPassword;
import com.example.music_app_artist.models.ResponseMessage;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.example.music_app_artist.utils.Validate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private TextInputEditText emailTxt;
    private MaterialButton btnReset;
    private FrameLayout overlay;
    private ProgressBar progressBar;
    private Validate validate = new Validate();
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mapping();

        emailTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inp = emailTxt.getText().toString();
                if(inp.length() == 0) {
                    emailLayout.setError("Vui lòng nhập trường này");
                } else {
                    if(!validate.validateEmail(inp)) {
                        emailLayout.setError("Vui lòng nhập email chính xác!");
                    } else {
                        emailLayout.setError(null);
                    }
                }
            }
        });
        emailTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String inp = emailTxt.getText().toString();
                if(b) {
                    if(inp.length() != 0 && !validate.validateEmail(inp)) {
                        emailLayout.setError("Vui lòng nhập email chính xác!");
                        return;
                    }
                    emailLayout.setError(null);
                } else {
                    if(inp.length() == 0) {
                        emailLayout.setError("Vui lòng nhập trường này");
                    }
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailLayout.getError() == null) {
                    overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
                    overlay.setVisibility(View.VISIBLE);
                    overlay.setFocusable(true);
                    overlay.setClickable(true);
                    progressBar.setVisibility(View.VISIBLE);
                    sendOtp();
                }
            }
        });
    }

    private void sendOtp() {
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setEmail(emailTxt.getText().toString());
        apiService.sendOtp(forgotPassword).enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                progressBar.setVisibility(View.INVISIBLE);
                overlay.setVisibility(View.INVISIBLE);
                overlay.setFocusable(false);
                overlay.setClickable(false);
                ResponseMessage res = response.body();
                if(res == null) {
                    Toast.makeText(ForgotPasswordActivity.this, "Encounter Wrong!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(res.isSuccess()) {
                    Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerifyActivity.class);
                    intent.putExtra("email", emailTxt.getText().toString());
                    intent.putExtra("type", "forgot");
                    startActivity(intent);
                    finish();
                }
                Toast.makeText(ForgotPasswordActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                overlay.setVisibility(View.INVISIBLE);
                overlay.setFocusable(false);
                overlay.setClickable(false);
                Toast.makeText(ForgotPasswordActivity.this, "Call API Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mapping() {
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        emailTxt = (TextInputEditText) findViewById(R.id.emailTxt);
        btnReset = (MaterialButton) findViewById(R.id.btnReset);
        overlay = (FrameLayout) findViewById(R.id.overlay);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}