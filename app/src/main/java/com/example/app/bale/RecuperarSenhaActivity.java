package com.example.app.bale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.bale.data.DatabaseContract;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RecuperarSenhaActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mCode;
    private EditText mPassword;
    private EditText mRepeatedPassword;
    private Button mSendEmailButton;
    private Button mChangePasswordButton;
    private TextView mDescriptionText;
    private TextView mEmailSentText;
    private ProgressDialog progressDialog;

    private Session session;
    // Código que será enviado por e-mail
    private String codigoGerado;
    // E-mail para ser enviado para a outra tela
    public final static String EXTRA_EMAIL = "com.example.app.bale.EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        mDescriptionText = (TextView) findViewById(R.id.recuperar_senha_info);
        mEmail = (EditText) findViewById(R.id.recuperar_senha_email);
        // Dar foco neste campo apenas quando ele for clicado.
        // Desta forma, o teclado só aparecerá quando o evento for acionado
        mEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        mSendEmailButton = (Button) findViewById(R.id.recuperar_senha_button);
        mEmailSentText = (TextView) findViewById(R.id.recuperar_senha_email_sent);

        mCode = (EditText) findViewById(R.id.recuperar_senha_code);
        // Dar foco neste campo apenas quando ele for clicado.
        // Desta forma, o teclado só aparecerá quando o evento for acionado
        mCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        // Sistema para checar automaticamente o texto
        mCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mCode.getText().toString().equals(codigoGerado)) {
                    mCode.setError(getString(R.string.recuperar_senha_code_error));
                    mCode.requestFocus();
                }
                else {
                    mCode.setError(null);
                }
            }
        });

        mPassword = (EditText) findViewById(R.id.recuperar_senha_new_password);
        // Altera a fonta da dica (hint) para a fonte padrão do aplicativo
        if (mPassword != null)
            mPassword.setTypeface(Typeface.DEFAULT);
        // Dar foco neste campo apenas quando ele for clicado.
        // Desta forma, o teclado só aparecerá quando o evento for acionado
        mPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        // Sistema para checar automaticamente o texto
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mPassword.getText().length() < 4) {
                    mPassword.setError(getString(R.string.error_invalid_password));
                    mPassword.requestFocus();
                }
                else {
                    mPassword.setError(null);
                }
            }
        });

        mRepeatedPassword = (EditText) findViewById(R.id.recuperar_senha_confirm_new_password);
        // Altera a fonta da dica (hint) para a fonte padrão do aplicativo
        if (mRepeatedPassword != null)
            mRepeatedPassword.setTypeface(Typeface.DEFAULT);
        // Dar foco neste campo apenas quando ele for clicado.
        // Desta forma, o teclado só aparecerá quando o evento for acionado
        mRepeatedPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });
        // Sistema para checar automaticamente o texto
        mRepeatedPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mRepeatedPassword.getText().toString().equals(mPassword.getText().toString())) {
                    mRepeatedPassword.setError(getString(R.string.error_confirm_password));
                    mRepeatedPassword.requestFocus();
                }
                else {
                    mRepeatedPassword.setError(null);
                }
            }
        });

        mChangePasswordButton = (Button) findViewById(R.id.recuperar_senha_login_button);
        // Fatores que ficarão visíveis a medida que o usuário adiciona conteúdos na tela
        mEmailSentText.setVisibility(View.GONE);
        mCode.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mRepeatedPassword.setVisibility(View.GONE);
        mChangePasswordButton.setVisibility(View.GONE);

        session = null;

        Intent intent = getIntent();
        String emailPreviamenteDigitado = intent.getStringExtra(LoginActivity.EXTRA_EMAIL);
        mEmail.setText(emailPreviamenteDigitado);

        assert mSendEmailButton != null;
        mSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ao clicar, tira o foco da caixa de e-mail
                mEmail.setFocusable(false);
                try {
                    hideKeyboard(RecuperarSenhaActivity.this);
                    progressDialog = ProgressDialog.show(RecuperarSenhaActivity.this, null,
                            getResources().getText(R.string.recuperar_senha_progress_message), true, false);
                    sendEmail(mEmail);
                } catch (Exception e) {
                    progressDialog.dismiss();
                    progressDialog = null;
                    e.printStackTrace();
                }
            }
        });

        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ChangePasswordTask().execute(mPassword.getText().toString(), mEmail.getText().toString());
            }
        });
    }

    public void sendEmail(EditText destinatario) throws Exception {
        final String login = getResources().getString(R.string.bale_email);
        final String password = getResources().getString(R.string.bale_password);
        Properties properties = new Properties();
        final String destinatarioString = destinatario.getText().toString();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(login, password);
                    }
                });

        // Ativar debug para sessão
        session.setDebug(true);

        // Chama a classe assíncrona
        new SendEmailTask().execute(login, destinatarioString);
    }

    public class SendEmailTask extends AsyncTask<String, Void, Boolean> {
        String destinatarioString = null;
        boolean emailRegistrado = false;

        @Override
        protected Boolean doInBackground(String... params) {
            final String login = params[0];
            destinatarioString = params[1];

            try {
                // Verifica se o e-mail está registrado no banco de dados da aplicação
                Cursor validEmailCursor = getContentResolver().query(
                        DatabaseContract.UsuariosEntry.CONTENT_URI,
                        new String[]{DatabaseContract.UsuariosEntry._ID},
                        DatabaseContract.UsuariosEntry.COLUMN_EMAIL + " = ?",
                        new String[]{destinatarioString},
                        null);
                if (!validEmailCursor.moveToFirst())
                    return false;
                else
                    emailRegistrado = true;
                validEmailCursor.close();

                // Envia e-mail
                Message message = new MimeMessage(session);
                //Remetente
                message.setFrom(new InternetAddress(login));
                //Destinatário(s)
                /*Address[] toUser = InternetAddress
                        .parse(destinatario);*/

                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatarioString));
                //Assunto
                message.setSubject(getResources().getString(R.string.recuperar_senha_email_subject));
                message.setText(getString(R.string.recuperar_senha_email_greetings) +
                        "\n\n" +
                        getString(R.string.recuperar_senha_email_body) + " " +geraCodigo() + "." +
                        "\n" +
                        getString(R.string.recuperar_senha_email_body_after_code) +
                        "\n\n" +
                        getString(R.string.recuperar_senha_email_closing));
                message.saveChanges();

                /* Método para enviar a mensagem criada */
                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            progressDialog.dismiss();
            progressDialog.cancel();
            progressDialog = null;
            if (result) {
                mEmailSentText.setVisibility(View.VISIBLE);
                mCode.setVisibility(View.VISIBLE);
                mPassword.setVisibility(View.VISIBLE);
                mRepeatedPassword.setVisibility(View.VISIBLE);
                mChangePasswordButton.setVisibility(View.VISIBLE);
                guardaCodigoNoBanco(codigoGerado, destinatarioString);
            }
            else
                if (!result && emailRegistrado)
                    Toast.makeText(getBaseContext(), getResources().getText(R.string.recuperar_senha_send_mail_error),
                        Toast.LENGTH_LONG).show();
                else
                    if (!result && !emailRegistrado)
                        Toast.makeText(getBaseContext(), getResources().getText(R.string.recuperar_senha_invalid_email_error),
                                Toast.LENGTH_LONG).show();

        }
    }

    public String geraCodigo() {
        // Código = varchar(6)
        codigoGerado = RandomStringUtils.random(6, true, true);
        return codigoGerado;
    }

    public void guardaCodigoNoBanco(String codigo, String destinatario) {

        ContentValues codigoValues = new ContentValues();
        codigoValues.put("codigo", codigo);

        try {
            getContentResolver().update(
                DatabaseContract.UsuariosEntry.CONTENT_URI,
                codigoValues,
                DatabaseContract.UsuariosEntry.COLUMN_EMAIL + " = ?",
                new String[]{destinatario});
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public class ChangePasswordTask extends AsyncTask<String, Void, Boolean> {

        String email = null;

        @Override
        protected Boolean doInBackground(String... params) {

            email = params[1];

            ContentValues passwordValues = new ContentValues();
            passwordValues.put("senha", params[0]);

            try {
                getContentResolver().update(
                        DatabaseContract.UsuariosEntry.CONTENT_URI,
                        passwordValues,
                        DatabaseContract.UsuariosEntry.COLUMN_EMAIL + " = ?",
                        new String[]{email});
            } catch(SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra(EXTRA_EMAIL, email);
                startActivity(intent);
                finish();
                // teste.setText(cursorteste.getString(1));

            }
            else
                Toast.makeText(getBaseContext(), getResources().getText(R.string.recuperar_senha_new_password_error),
                        Toast.LENGTH_LONG).show();
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
