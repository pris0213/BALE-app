package com.example.app.bale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.bale.data.DatabaseContract;

import org.apache.commons.lang.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CadastroExaminadorActivity extends AppCompatActivity {

    // Para o Log
    private static final String TAG = "Cadastro Examinador";

    @InjectView(R.id.input_examinador_explanation) TextView mExplanationText;
    @InjectView(R.id.input_examinador_full_name) EditText mNameText;
    @InjectView(R.id.input_examinador_email) EditText mEmailText;
    @InjectView(R.id.checkBox_examinador_adm) CheckBox mAdmBox;
    @InjectView(R.id.btn_examinador_register) Button mRegisterButton;

    private AlertDialog dialog;
    // Valor enviado pela tela anterior
    private static String pk_usuario;
    // Sessão para envio de e-mail
    private Session session;
    private ProgressDialog progressDialog;
    private boolean emailRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_examinador);
        ButterKnife.inject(this);

        // Pega a PK_USUARIO enviada pela tela anterior
        Intent intent = getIntent();
        pk_usuario = intent.getStringExtra(LoginActivity.EXTRA_PK_USUARIO);
        session = null;
        emailRegistrado = false;

        /*mRegisterButton = (Button) findViewById(R.id.btn_examinador_register);*/
        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    hideKeyboard(CadastroExaminadorActivity.this);
                    if (!validate()) {
                        Toast.makeText(CadastroExaminadorActivity.this, R.string.participante_toast_register, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // Verifica e-mail
                        new VerifyEmailTask().execute(mEmailText.getText().toString());
                        if (!emailRegistrado) {
                            progressDialog = ProgressDialog.show(CadastroExaminadorActivity.this, null,
                                    getResources().getText(R.string.examinador_progress_message), true, false);
                            cadastrar();
                        }
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    progressDialog = null;
                    e.printStackTrace();
                }
            }
        });
    }

    public void cadastrar() throws Exception {
        Log.d(TAG, "Cadastrando Examinador");
        mRegisterButton.setEnabled(false);

        String nome = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        boolean isAdm = mAdmBox.isChecked();
        String senhaTemporaria = geraSenha();

        ContentValues register = new ContentValues();
        register.put("nome", nome);
        register.put("email", email);
        register.put("e_administrador", isAdm);
        register.put("senha", senhaTemporaria);
        // Insere examinador
        getContentResolver().insert(DatabaseContract.UsuariosEntry.CONTENT_URI, register);
        // Envia e-mail para a nova pessoa examinadora
        sendEmail(mEmailText, senhaTemporaria);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            mNameText.setError(getResources().getText(R.string.error_name_too_short));
            valid = false;
        } else {
            mNameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError(getResources().getText(R.string.error_invalid_email));
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        return valid;
    }

    public String geraSenha() {
        // Senha = varchar(15)
        return RandomStringUtils.random(15, true, true);
    }

    public void sendEmail(EditText destinatario, String senhaTemporaria) throws Exception {
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
        new SendEmailTask().execute(login, destinatarioString, senhaTemporaria);
    }

    public class VerifyEmailTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            final String email = params[0];

            try {
                // Verifica se o e-mail está registrado no banco de dados da aplicação
                Cursor validEmailCursor = getContentResolver().query(
                        DatabaseContract.UsuariosEntry.CONTENT_URI,
                        new String[]{DatabaseContract.UsuariosEntry._ID},
                        DatabaseContract.UsuariosEntry.COLUMN_EMAIL + " = ?",
                        new String[]{email},
                        null);
                if (validEmailCursor.moveToFirst()) {
                    return true;
                }
                validEmailCursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            if (result) {
                Toast.makeText(getBaseContext(), getResources().getText(R.string.examinador_email_cadastrado),
                        Toast.LENGTH_LONG).show();
                emailRegistrado = true;
            }
        }
    }

    public class SendEmailTask extends AsyncTask<String, Void, Boolean> {
        String destinatarioString = null;
        String senhaTemporaria = null;

        @Override
        protected Boolean doInBackground(String... params) {
            final String login = params[0];
            destinatarioString = params[1];
            senhaTemporaria = params[2];

            try {
                // Envia e-mail
                Message message = new MimeMessage(session);
                //Remetente
                message.setFrom(new InternetAddress(login));
                //Destinatário(s)
                /*Address[] toUser = InternetAddress
                        .parse(destinatario);*/

                message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatarioString));
                //Assunto
                message.setSubject(getResources().getString(R.string.cadastro_examinador_email_subject));
                message.setText(getString(R.string.cadastro_examinador_email_greetings) +
                    "\n\n" +
                    getString(R.string.cadastro_examinador_email_body) +
                    "\n" +
                    getString(R.string.cadastro_examinador_email_body_password) + " " + senhaTemporaria + "." +
                    "\n" +
                    getString(R.string.cadastro_examinador_email_body_code) +
                    "\n" +
                    getString(R.string.cadastro_examinador_email_body_after_code) +
                    "\n\n" +
                    getString(R.string.cadastro_examinador_email_closing));
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
            /*if (!result && emailRegistrado)
                Toast.makeText(getBaseContext(), getResources().getText(R.string.recuperar_senha_send_mail_error),
                        Toast.LENGTH_LONG).show();
            else*/
            if (result) {
                // Voltar
                Intent intent = new Intent(getApplicationContext(), AdministradorMainActivity.class);
                intent.putExtra(LoginActivity.EXTRA_PK_USUARIO, pk_usuario);
                startActivity(intent);
                finish();
            }

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
