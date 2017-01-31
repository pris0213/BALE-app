package com.example.app.bale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.bale.R;
import com.example.app.bale.data.DatabaseContract;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    @InjectView(R.id.input_first_name) EditText firstNameText;
    @InjectView(R.id.input_email) EditText emailText;
    @InjectView(R.id.input_password) EditText passwordText;
    @InjectView(R.id.input_confirm_password) EditText confirmPasswordText;
    @InjectView(R.id.btn_signup) Button signUpButton;
    @InjectView(R.id.link_login) TextView loginLink;
    private ProgressDialog progressDialog;
    private final String LOG = "Log: ";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.inject(this);
        progressDialog = null;

        // Altera a fonta da dica (hint) para a fonte padrão do aplicativo
        if (passwordText != null)
            passwordText.setTypeface(Typeface.DEFAULT);
        if (confirmPasswordText != null)
            confirmPasswordText.setTypeface(Typeface.DEFAULT);

        // Mantém o e-mail caso já tenha sido digitado na tela de Login
        Intent intent = getIntent();
        String emailPreviamenteDigitado = intent.getStringExtra(LoginActivity.EXTRA_EMAIL);
        emailText.setText(emailPreviamenteDigitado);

        signUpButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Ao clicar em se registrar, a caixa de progresso deveria aparecer na tela...
                    progressDialog = ProgressDialog.show(SignUpActivity.this, null,
                            getResources().getText(R.string.progress_message), true, false);
                    signUp();
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog.cancel();
                    }
                }
            }
        });
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                }
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    @Override
    public void onPause() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    public void signUp() {
        Log.d(TAG, "Sign Up");

        if (!validate()) {
            onSignUpFailed();
            return;
        }

        signUpButton.setEnabled(false);

        // progressDialog = ProgressDialog.show(SignUpActivity.this, null, getResources().getText(R.string.progress_message), true, false);

        String firstName = firstNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        try {
            ContentValues cadastro = new ContentValues();
            cadastro.put("nome", firstName);
            cadastro.put("email", email);
            cadastro.put("senha", password);
            cadastro.put("e_administrador", false);

            // progressDialog = ProgressDialog.show(SignUpActivity.this, null, getResources().getText(R.string.progress_message), true, false);
            getContentResolver().insert(DatabaseContract.UsuariosEntry.CONTENT_URI, cadastro);
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignUpSuccess or onSignUpFailed
                            // depending on success
                            Log.v(LOG, "* Chamando onSignUpSuccess() *");
                            onSignUpSuccess();
                            // onSignUpFailed();
                        }
                    }, 0);
        } catch(SQLException e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            onSignUpFailed();
            e.printStackTrace();
        }
    }


    public void onSignUpSuccess() {
        Toast.makeText(getBaseContext(), getResources().getText(R.string.account_created_message),
                Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.cancel();
        }
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(getBaseContext(), getResources().getText(R.string.error_register_failed),
                Toast.LENGTH_LONG).show();
        signUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = firstNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 2) {
            firstNameText.setError(getResources().getText(R.string.error_name_too_short));
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getResources().getText(R.string.error_invalid_email));
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            passwordText.setError(getResources().getText(R.string.error_invalid_password));
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (!confirmPassword.equals(password)) {
            confirmPasswordText.setError(getResources().getText(R.string.error_confirm_password));
            valid = false;
        } else {
            confirmPasswordText.setError(null);
        }

        return valid;
    }
}