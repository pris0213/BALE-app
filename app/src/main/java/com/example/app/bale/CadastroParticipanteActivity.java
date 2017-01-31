package com.example.app.bale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.app.bale.data.DatabaseContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CadastroParticipanteActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    @InjectView(R.id.input_participante_explanation) TextView mExplanationText;
    @InjectView(R.id.input_participante_full_name) EditText mNameText;
    @InjectView(R.id.input_participante_dob) TextView mDoBText;
    @InjectView(R.id.input_participante_dob2) TextView mDoBLink;
    @InjectView(R.id.input_participante_gender) TextView mGenderText;
    @InjectView(R.id.radio_participante_gender) RadioGroup mGenderRadio;
    @InjectView(R.id.input_participante_phone) EditText mPhoneText;
    @InjectView(R.id.input_participante_schooling) EditText mSchoolingText;
    @InjectView(R.id.input_participante_handedness) TextView mHandednessText;
    @InjectView(R.id.radio_participante_handedness) RadioGroup mHandednessRadio;
    @InjectView(R.id.checkBox_participante_retired) CheckBox mRetiredBox;
    @InjectView(R.id.input_participante_profession) EditText mProfessionText;
    @InjectView(R.id.idioms_label) TextView mIdiomsLabel;
    @InjectView(R.id.idioms_button) TextView mIdiomsButton;
    @InjectView(R.id.mother_tongue_label) TextView mMotherTongleLabel;
    @InjectView(R.id.mother_tongue_button) TextView mMotherTongueButton;
    @InjectView(R.id.input_participante_obs) EditText mObsText;
    @InjectView(R.id.btn_participante_register) Button mRegisterButton;

    private AlertDialog dialog;
    private static final Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat dateFormat;
    private static String dateFormatted;

    private static Date dobDate;

    // Dominância manual padrão é "D" (destra)
    private String mHandednessChosen = "D";
    // Gênero padrão (será "Outro/Não-binário", instanciando-se no método onCreate())
    private String mGenderChosen;
    // Valor enviado pela tela anterior
    private static String pk_usuario;
    // Valor a ser enviado para telas da BALE
    // public static String EXTRA_PK_PESQUISA = "com.example.app.bale.PK_PESQUISA";
    // private static String pk_pesquisa;
    // String para o idioma materno
    private CharSequence idiomaMaternoDB;
    // Vetor para inserção de idiomas no Banco de Dados
    private CharSequence[] idiomasDB;
    // The request code
    public final int REGISTER_PARTICIPANT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_participante);
        ButterKnife.inject(this);

        // Pega a PK_USUARIO enviada pela tela anterior
        Intent intent = getIntent();
        pk_usuario = intent.getStringExtra(LoginActivity.EXTRA_PK_USUARIO);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        mDoBLink = (TextView) findViewById(R.id.input_participante_dob2);
        calendar.add(Calendar.DATE, 0);
        dateFormatted = dateFormat.format(calendar.getTime());
        mDoBLink.setText(dateFormatted);
        mDoBLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
        // Gênero padrão
        mGenderChosen = getResources().getString(R.string.participante_nonbinary);

        // Para o diálogo da língua materna (igual ao idioma)
        idiomaMaternoDB = "";
        View openMotherTongueDialog = (View) findViewById(R.id.dialogMotherTongue);
        openMotherTongueDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(CadastroParticipanteActivity.this)
                        .title(R.string.participante_mother_tongue)
                        .items(R.array.string_array_idioms)
                        .itemsCallbackSingleChoice(30, new MaterialDialog.ListCallbackSingleChoice() {
                            // 30 = Índice do "Português" na lista de idiomas (pt/idioms.xml) - 1
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected check box to actually be selected.
                                 * See the limited multi choice dialog example in the sample project for details.
                                 **/
                                ((TextView) CadastroParticipanteActivity.this.findViewById(R.id.mother_tongue_button))
                                        .setText(text);
                                idiomaMaternoDB = text;
                                return true;
                            }
                        })
                        .positiveText(R.string.dialog_choose)
                        .show();
            }
        });

        // Para o diálogo dos idiomas
        View openIdiomsDialog = (View) findViewById(R.id.dialogIdioms);
        openIdiomsDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(CadastroParticipanteActivity.this)
                        .title(R.string.participante_idioms)
                        .items(R.array.string_array_idioms)
                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                if (text.length > 0) {
                                    ((TextView) CadastroParticipanteActivity.this.findViewById(R.id.idioms_button))
                                            .setText(toStringSeparadoPorVirgulas(text));
                                    idiomasDB = text;
                                }
                                else {
                                    idiomasDB = null;
                                }
                                return true;
                            }
                        })
                        .positiveText(R.string.dialog_choose)
                        .show();
            }
        });


        mRegisterButton = (Button) findViewById(R.id.btn_participante_register);
        mRegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
    }

    public String toStringSeparadoPorVirgulas(CharSequence[] text) {
        String stringSeparadaPorViruglas = "";
        if (text.length != 1) {
            for (int i = 0; i < text.length - 1; i++) {
                stringSeparadaPorViruglas = stringSeparadaPorViruglas + text[i] + ", ";
                // Exemplo: Espanhol, Italiano,
            }
        }
        stringSeparadaPorViruglas = stringSeparadaPorViruglas + text[text.length - 1];
        // Espanhol, Italiano, Francês
        return stringSeparadaPorViruglas;
    }

    public void escolherIdioma() {
        try {
            // Salva as PKs dos idiomas
            ArrayList<Integer> pk_idiomas_participante = new ArrayList<>();
            Map<String, String> idiomasDatabase = new HashMap<String, String>();

            // String[] selectionArgs = new String[1];

            // getContentResolver().delete(DatabaseContract.IdiomasEntry.CONTENT_URI, null, null);

            // Primeiramente, procurar na tabela quais idiomas já estão inseridos
            Cursor idiomasCursor = getContentResolver().query(
                    DatabaseContract.IdiomasEntry.CONTENT_URI,
                    new String[]{DatabaseContract.IdiomasEntry._ID, DatabaseContract.IdiomasEntry.COLUMN_NOME},
                    null,
                    null,
                    null
            );

            // Salva os idiomas já inseridos no banco de dados neste ArrayList
            while (idiomasCursor.moveToNext()) {
                idiomasDatabase.put(idiomasCursor.getString(0), idiomasCursor.getString(1));
            }

            ContentValues idiomas = new ContentValues();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cadastrar() {
        Log.d(TAG, "Cadastrando Participante");

        if (!validate()) {
            Toast.makeText(CadastroParticipanteActivity.this, R.string.participante_toast_register, Toast.LENGTH_SHORT).show();
            return;
        }

        mRegisterButton.setEnabled(false);

        /*final ProgressDialog progressDialog = new ProgressDialog(CadastroParticipanteActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.participante_progress_message));
        progressDialog.show();
        progressDialog.getWindow().setGravity(Gravity.CENTER);*/

        String name = mNameText.getText().toString();
        SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dob = databaseFormat.format(dobDate);
        String gender = mGenderChosen;
        String phone = mPhoneText.getText().toString();
        String schooling = mSchoolingText.getText().toString();
        String handedness = mHandednessChosen;
        String retired = null;
        boolean isRetired = mRetiredBox.isChecked();
        if (isRetired)
            retired = "true";
        else
            retired = "false";
        String profession = mProfessionText.getText().toString();
        String obs = mObsText.getText().toString();

        /*// Separar os idiomas num vetor
        //String[] idioms = mIdiomsText.getText().toString().split(",");
        String[] idioms = "Português,Inglês".split(",");
        // Remover possíveis espaços entre os idiomas
        for (String idiom : idioms) {
            if (idiom.charAt(0) == ' ') {
                idiom = idiom.substring(1);
            }
            if (idiom.charAt(idiom.length() - 1) == ' ') {
                idiom = idiom.substring(0, idiom.length() - 2);
            }
        }*/

        try {
            // Salva as PKs dos idiomas
            ArrayList<Integer> pk_idiomas_participante = new ArrayList<>();
            // Cria um HashMap (pk_idioma, nome_idioma)
            Map<String, String> idiomasDatabase = new HashMap<String, String>();

            // String[] selectionArgs = new String[1];

            // getContentResolver().delete(DatabaseContract.IdiomasEntry.CONTENT_URI, null, null);

            // Inserir os idiomas na tabela Idiomas
            // Primeiramente, procurar na tabela quais idiomas já estão inseridos
            Cursor idiomasCursor = getContentResolver().query(
                    DatabaseContract.IdiomasEntry.CONTENT_URI,
                    new String[]{DatabaseContract.IdiomasEntry._ID, DatabaseContract.IdiomasEntry.COLUMN_NOME},
                    null,
                    null,
                    null
            );

            // Salva os idiomas já inseridos no banco de dados neste ArrayList
            while(idiomasCursor.moveToNext()) {
                idiomasDatabase.put(idiomasCursor.getString(0), idiomasCursor.getString(1));
            }

            ContentValues idiomas = new ContentValues();
            for (CharSequence idiom : idiomasDB) {
                // Se este idioma já foi inserido no BD
                // TODO: esta fase será desnecessária em breve. Removê-la.
                if (idiomasDatabase.values().contains(idiom)) {
                    Set set = idiomasDatabase.entrySet();
                    Iterator iterator = set.iterator();
                    // Percorre o HashMap para conseguir a PK
                    while(iterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry)iterator.next();
                        if (mapEntry.getValue().equals(idiom))
                            // Insere a PK na lista de PK do participante
                            pk_idiomas_participante.add(Integer.parseInt((String) mapEntry.getKey()));
                    }
                }
                // Senão, insere este idioma no banco de dados e, depois, na lista de PK do participante
                // TODO: esta fase será desnecessária em breve. Removê-la.
                else {
                    idiomas.put("nome", idiom.toString());
                    // Insere
                    Uri idiomasUri = getContentResolver().insert(DatabaseContract.IdiomasEntry.CONTENT_URI, idiomas);
                    // Adiciona a língua à lista de línguas. Será útil para popular a tabela Participantes_Idiomas
                    pk_idiomas_participante.add((int) ContentUris.parseId(idiomasUri));
                }
                /*
                if (selectionArgs[0] == null)
                    selectionArgs[0] = idiom; */
            }

            ContentValues register = new ContentValues();
            register.put("nome", name);
            register.put("data_de_nascimento", dob);
            register.put("genero", gender);
            register.put("telefone", phone);
            register.put("escolaridade", schooling);
            register.put("dominancia_manual", handedness);
            register.put("tem_aposentadoria", retired);
            register.put("profissao", profession);
            register.put("idioma", idiomaMaternoDB.toString()); // Adiciona a língua materna
            register.put("examinador", pk_usuario);
            register.put("observacoes", obs);
            // Insere participante
            Uri participantesUri = getContentResolver().insert(DatabaseContract.ParticipantesEntry.CONTENT_URI, register);

            // Pega a PK do participante inserido (será útil para popular a tabela Participantes_Idiomas)
            int pk_participante = (int) ContentUris.parseId(participantesUri);

            // Insere na tabela Participantes_Idiomas
            for (Integer pk_idioma : pk_idiomas_participante) {
                ContentValues participantesIdiomas = new ContentValues();
                participantesIdiomas.put("id_participante", pk_participante);
                participantesIdiomas.put("id_idioma", pk_idioma);
                getContentResolver().insert(DatabaseContract.ParticipantesIdiomasEntry.CONTENT_URI, participantesIdiomas);
            }

            // Inserir a pessoa participante na tabela de Pesquisas
            ContentValues pesquisa = new ContentValues();
            pesquisa.put("_id", (byte[]) null);
            pesquisa.put("examinador", pk_usuario);
            pesquisa.put("participante", pk_participante);
            pesquisa.put("data_de_realizacao", databaseFormat.format(Calendar.getInstance().getTime()));
            pesquisa.put("esta_finalizada", false);
            getContentResolver().insert(DatabaseContract.PesquisasEntry.CONTENT_URI, pesquisa);
            // assert result != null;
            // pk_pesquisa = result.getLastPathSegment();

            // Voltar
            Intent intent = new Intent(getApplicationContext(), ExaminadorMainActivity.class);
            intent.putExtra(LoginActivity.EXTRA_PK_USUARIO, pk_usuario);
            // intent.putExtra(EXTRA_PK_PESQUISA, pk_pesquisa);
            startActivity(intent);
            finish();

        } catch(Exception e) {
            Toast.makeText(CadastroParticipanteActivity.this, R.string.participante_toast_register, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String dob = mDoBLink.getText().toString();

        if (name.isEmpty() || name.length() < 2) {
            mNameText.setError(getResources().getText(R.string.error_name_too_short));
            valid = false;
        } else {
            mNameText.setError(null);
        }

        return valid;
    }
    // Método para selecionar o gênero do participante
    public void onGenderRadioButtonClicked(View view) {
        // O botão está selecionado?
        boolean checked = ((RadioButton) view).isChecked();

        // Marcar qual botão foi clicado
        // mGenderChosen guarda a opção, valor que será inserido no banco de dados.
        switch (view.getId()) {
            case R.id.radio_female:
                if (checked)
                    mGenderChosen = getResources().getString(R.string.participante_female);
                break;

            case R.id.radio_male:
                if (checked)
                    mGenderChosen = getResources().getString(R.string.participante_male);
                break;

            case R.id.radio_nonbinary:
                if (checked)
                    mGenderChosen = getResources().getString(R.string.participante_nonbinary);
                break;
        }
    }

    // Método para selecionar a mão dominante do participante
    public void onHandednessRadioButtonClicked(View view) {
        // O botão está selecionado?
        boolean checked = ((RadioButton) view).isChecked();

        // Marcar qual botão foi clicado
        /* mHandednessChosen guarda a primeira letra da opção, valor que será inserido no banco de dados.
            radio_lefthanded = canhota = 'C'
            radio_righthanded = destra = 'D'
            radio_ambidextrous = ambidestra = 'A'
         */
        switch (view.getId()) {
            case R.id.radio_lefthanded:
                if (checked)
                    mHandednessChosen = "C";
                break;

            case R.id.radio_righthanded:
                if (checked)
                    mHandednessChosen = "D";
                break;

            case R.id.radio_ambidextrous:
                if (checked)
                    mHandednessChosen = "A";
                break;
        }
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        DatePicker datePicker;
        DatePickerDialog datePickerDialog;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

            // Insere datas limites (data atual > data de nascimento > 1/1/1916
            Calendar auxCalendar = Calendar.getInstance();
            datePickerDialog.getDatePicker().setMaxDate(auxCalendar.getTimeInMillis());

            auxCalendar.set(Calendar.YEAR, 1916);
            auxCalendar.set(Calendar.DAY_OF_YEAR, 1);
            long minDoB = auxCalendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMinDate(minDoB);

            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            calendar.set(year, month, day);
            dobDate = calendar.getTime();
            ((TextView) getActivity().findViewById(R.id.input_participante_dob2)).setText(dateFormat.format(dobDate));
        }
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent(CadastroParticipanteActivity.this, ExaminadorMainActivity.class);
        resultIntent.putExtra(getResources().getString(R.string.extra_pk_usuario), pk_usuario);
        setResult(RESULT_OK, resultIntent);
        super.onBackPressed();
    }

    // Dialogo para idiomas
   public interface multiChoiceListDialogListener {
        public void onOK(ArrayList<Integer> arrayList);
        public void onCancel();
    }
}
