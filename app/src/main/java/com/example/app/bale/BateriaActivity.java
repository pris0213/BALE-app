package com.example.app.bale;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.app.bale.bale.CompreensaoFraseActivity;
import com.example.app.bale.bale.HabitosLeituraEscritaActivity;

public class BateriaActivity extends AppCompatActivity
        implements TipoPerguntaFragment.OnListFragmentInteractionListener{

    // Valor que foi enviado da página inicial da aplicação e será enviado para a página de cadastro
    public final static String EXTRA_PK_USUARIO = "com.example.app.bale.PK_USUARIO";
    public static String pk_usuario = null;
    public final static String EXTRA_PK_PESQUISA = "com.example.app.bale.PK_PESQUISA";
    public static String pk_pesquisa = null;
    public static Pesquisa pesquisa = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bateria);

        // Pega a PK_USUARIO enviada pela tela anterior
        Intent intent = getIntent();
        pk_usuario = intent.getStringExtra(getResources().getString(R.string.extra_pk_usuario));
        pk_pesquisa = intent.getStringExtra(EXTRA_PK_PESQUISA);
        pesquisa = (Pesquisa) intent.getSerializableExtra(ExaminadorMainActivity.EXTRA_PESQUISA);

        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
            TipoPerguntaFragment avaliacoes = new TipoPerguntaFragment();
            avaliacoes.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.bateria_layout, avaliacoes).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_examinator_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onListFragmentInteraction(String item) {
        // Como switch case apenas aceita constantes, foi feito um if else encadeado.
        Intent intent;
        // Hábitos de Leitura e Escrita
        if (item.equals(getResources().getString(R.string.bateria_tipopergunta_habitos))) {
            intent = new Intent(getApplicationContext(), HabitosLeituraEscritaActivity.class);
            intent.putExtra(EXTRA_PK_USUARIO, pk_usuario);
            intent.putExtra(EXTRA_PK_PESQUISA, pk_pesquisa);
            intent.putExtra(ExaminadorMainActivity.EXTRA_PESQUISA, pesquisa);
            startActivity(intent);
        }
        else {
            // Memória Episódica
            if (item.equals(getResources().getString(R.string.bateria_tipopergunta_memoria))) {
                // TODO: Alterar classe quando esta for criada
                intent = new Intent(getApplicationContext(), HabitosLeituraEscritaActivity.class);
                intent.putExtra(EXTRA_PK_USUARIO, pk_usuario);
                intent.putExtra(EXTRA_PK_PESQUISA, pk_pesquisa);
                intent.putExtra(ExaminadorMainActivity.EXTRA_PESQUISA, pesquisa);
                startActivity(intent);
            }
            else {
                // Compreensão de Frases
                if (item.equals(getResources().getString(R.string.bateria_tipopergunta_compfrases))) {
                    // TODO: Alterar classe quando esta for criada
                    intent = new Intent(getApplicationContext(), CompreensaoFraseActivity.class);
                    intent.putExtra(EXTRA_PK_USUARIO, pk_usuario);
                    intent.putExtra(EXTRA_PK_PESQUISA, pk_pesquisa);
                    intent.putExtra(ExaminadorMainActivity.EXTRA_PESQUISA, pesquisa);
                    startActivity(intent);
                }
                else {
                    // Compreensão Verbal
                    if (item.equals(getResources().getString(R.string.bateria_tipopergunta_compverbal))) {
                        // TODO: Alterar classe quando esta for criada
                        intent = new Intent(getApplicationContext(), HabitosLeituraEscritaActivity.class);
                        intent.putExtra(EXTRA_PK_USUARIO, pk_usuario);
                        intent.putExtra(EXTRA_PK_PESQUISA, pk_pesquisa);
                        intent.putExtra(ExaminadorMainActivity.EXTRA_PESQUISA, pesquisa);
                        startActivity(intent);
                    }
                    else {
                        // Discurso Livre
                        if (item.equals(getResources().getString(R.string.bateria_tipopergunta_disclivre))) {
                            // TODO: Alterar classe quando esta for criada
                            intent = new Intent(getApplicationContext(), HabitosLeituraEscritaActivity.class);
                            intent.putExtra(EXTRA_PK_USUARIO, pk_usuario);
                            intent.putExtra(EXTRA_PK_PESQUISA, pk_pesquisa);
                            intent.putExtra(ExaminadorMainActivity.EXTRA_PESQUISA, pesquisa);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }
}
