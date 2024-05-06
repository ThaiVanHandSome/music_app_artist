package com.example.music_app_artist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.music_app_artist.R;
import com.example.music_app_artist.models.RegisterRequest;
import com.example.music_app_artist.models.RegisterResponse;
import com.example.music_app_artist.retrofit.RetrofitClient;
import com.example.music_app_artist.services.APIService;
import com.example.music_app_artist.utils.Validate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private TextInputLayout nickNameLayout, phoneNumberLayout, emailLayout, passwordLayout, passwordAgainLayout;
    private TextInputEditText nickNameTxt, phoneNumberTxt, emailTxt, passwordTxt, passwordAgainTxt;
    private MaterialButton btnSignUp;
    private RadioGroup radioGroup;
    private FrameLayout overlay;
    private ProgressBar progressBar;
    private Validate validate = new Validate();
    private APIService apiService;
    private int gender = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mapping();

        handleEvent();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = findViewById(i);
                if(checkedRadioButton != null) {
                    String tag = checkedRadioButton.getTag().toString();
                    gender = Integer.parseInt(tag);
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSuccess()) {
                    enableWaiting();
                    register();
                }
            }
        });
    }

    private void register() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNickName(nickNameTxt.getText().toString());
        registerRequest.setPhoneNumber(phoneNumberTxt.getText().toString());
        registerRequest.setEmail(emailTxt.getText().toString());
        registerRequest.setPassword(passwordTxt.getText().toString());
        registerRequest.setGender(1);

        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        apiService.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                RegisterResponse res = response.body();
                if(res == null) {
                    Toast.makeText(SignUpActivity.this, "Encounter Error!", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(SignUpActivity.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, OtpVerifyActivity.class);
                intent.putExtra("type", "confirm");
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.d("error", t.toString());
                Toast.makeText(SignUpActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableWaiting() {
        progressBar.setVisibility(View.VISIBLE);
        overlay.setBackgroundColor(Color.argb(89, 0, 0, 0));
        overlay.setVisibility(View.VISIBLE);
        overlay.setFocusable(true);
        overlay.setClickable(true);
    }

    private void disableWaiting() {
        progressBar.setVisibility(View.INVISIBLE);
        overlay.setVisibility(View.INVISIBLE);
        overlay.setFocusable(false);
        overlay.setClickable(false);
    }

    private boolean checkSuccess() {
        return nickNameLayout.getError() == null && phoneNumberLayout.getError() == null && passwordLayout.getError() == null && emailLayout.getError() == null && passwordAgainLayout.getError() == null;
    }

    private void setEvent(TextInputLayout textInputLayout, TextInputEditText textInput, String type) {
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inp = textInput.getText().toString();
                if(inp.length() == 0) {
                    textInputLayout.setError(getText(R.string.error_required_field));
                } else {
                    if(type.equals("email") && !validate.validateEmail(inp)) {
                        textInputLayout.setError(getText(R.string.error_invalid_email));
                    } else if(type.equals("phoneNumber") && !validate.validatePhoneNumber(inp)) {
                        textInputLayout.setError(getText(R.string.error_invalid_phone));
                    } else if(type.equals("password") && !validate.validatePassword(inp)) {
                        textInputLayout.setError(getText(R.string.error_invalid_password));
                    } else if(type.equals("passwordAgain") && !passwordTxt.getText().toString().equals(inp)) {
                        textInputLayout.setError(getText(R.string.error_password_not_match));
                    } else {
                        textInputLayout.setError(null);
                    }
                }
            }
        });

        textInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String inp = textInput.getText().toString();
                if(b) {
                    if(inp.length() != 0) {
                        if(type.equals("email") && !validate.validateEmail(inp)) {
                            textInputLayout.setError(getText(R.string.error_invalid_email));
                        } else if(type.equals("phoneNumber") && !validate.validatePhoneNumber(inp)) {
                            textInputLayout.setError(getText(R.string.error_invalid_phone));
                        } else if(type.equals("password") && !validate.validatePassword(inp)) {
                            textInputLayout.setError(getText(R.string.error_invalid_password));
                        } else if(type.equals("passwordAgain") && !passwordTxt.getText().toString().equals(inp)) {
                            textInputLayout.setError(getText(R.string.error_password_not_match));
                        }
                        return;
                    }
                    textInputLayout.setError(null);
                } else {
                    if(inp.length() == 0) {
                        textInputLayout.setError(getText(R.string.error_required_field));
                    }
                }
            }
        });
    }

    private void handleEvent() {
        setEvent(nickNameLayout, nickNameTxt, "");
        setEvent(phoneNumberLayout, phoneNumberTxt, "phoneNumber");
        setEvent(emailLayout, emailTxt, "email");
        setEvent(passwordLayout, passwordTxt, "password");
        setEvent(passwordAgainLayout, passwordAgainTxt, "passwordAgain");
    }

    private void mapping() {
        nickNameLayout = (TextInputLayout) findViewById(R.id.nickNameLayout);
        phoneNumberLayout = (TextInputLayout) findViewById(R.id.phoneNumberLayout);
        emailLayout = (TextInputLayout) findViewById(R.id.emailLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.passwordLayout);
        passwordAgainLayout = (TextInputLayout) findViewById(R.id.passwordAgainLayout);

        nickNameTxt = (TextInputEditText) findViewById(R.id.nickNameTxt);
        phoneNumberTxt = (TextInputEditText) findViewById(R.id.phoneNumberTxt);
        emailTxt = (TextInputEditText) findViewById(R.id.emailTxt);
        passwordTxt = (TextInputEditText) findViewById(R.id.passwordTxt);
        passwordAgainTxt = (TextInputEditText) findViewById(R.id.passwordAgainTxt);

        btnSignUp = (MaterialButton) findViewById(R.id.btnSignUp);
        overlay = (FrameLayout) findViewById(R.id.overlay);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
    }
}