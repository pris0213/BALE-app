package com.example.app.bale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ExaminadorMainActivity extends AppCompatActivity
implements PesquisasFinalizadasFragment.OnListFragmentInteractionListener, PesquisasEmAndamentoFragment.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mSlidingTabLayout;

    // Valor que foi enviado da página inicial da aplicação e será enviado para a página de cadastro
    // public final static String EXTRA_PK_USUARIO = "com.example.app.bale.PK_USUARIO";
    public static String pk_usuario = null;
    public static String pk_pesquisa = null;
    // Extra criado para acoplar à PK_PESQUISA que será enviada para a lista de avaliações
    public final static String EXTRA_PK_PESQUISA = "com.example.app.bale.PK_PESQUISA";
    public final static String EXTRA_PESQUISA = "com.example.app.bale.PESQUISA";
    // The request code
    public final int BATERIA_REQUEST = 1;
    public final int REGISTER_PARTICIPANT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examinador_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Pega a PK_USUARIO enviada pela tela anterior
        Intent intent = getIntent();
        pk_usuario = intent.getStringExtra(LoginActivity.EXTRA_PK_USUARIO);
        adicionaUsuarioAsPreferencias(pk_usuario);

        FloatingActionButton mAddButton = (FloatingActionButton) findViewById(R.id.fab_examinador);
        mAddButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroParticipanteActivity.class);
                intent.putExtra(LoginActivity.EXTRA_PK_USUARIO, pk_usuario);
                startActivityForResult(intent, REGISTER_PARTICIPANT_REQUEST);
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Cria a barra de abas para as diferentes páginas
        mSlidingTabLayout = (TabLayout) findViewById(R.id.tab_examinador);
        mSlidingTabLayout.setTabGravity(mSlidingTabLayout.GRAVITY_FILL);
        mSlidingTabLayout.setupWithViewPager(mViewPager);
        mSlidingTabLayout.getTabAt(0).setText(R.string.tab_pesquisas_em_andamento);
        mSlidingTabLayout.getTabAt(1).setText(R.string.tab_pesquisas_finalizadas);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_examinator_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REGISTER_PARTICIPANT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                pk_usuario = data.getStringExtra(getResources().getString(R.string.extra_pk_usuario));
            }
        }
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

    public void onListFragmentInteraction(Pesquisa item) {
        Intent intent = new Intent(getApplicationContext(), BateriaActivity.class);
        intent.putExtra(EXTRA_PK_PESQUISA, Integer.toString(item.getPk_pesquisa()));
        intent.putExtra(EXTRA_PESQUISA, item);
        // requestCode - int: If >= 0, this code will be returned in onActivityResult() when the activity exits
        startActivityForResult(intent, BATERIA_REQUEST);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return (position == 0 ? new PesquisasEmAndamentoFragment() : new PesquisasFinalizadasFragment());
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }

    public void adicionaUsuarioAsPreferencias(String pk_usuario) {
        SharedPreferences mUserSettings = getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mUserSettings.edit();
        editor.putString("pk_usuario", pk_usuario);
        editor.apply();
    }
}

