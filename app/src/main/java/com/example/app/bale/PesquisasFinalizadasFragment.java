package com.example.app.bale;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.bale.data.DatabaseContract;

import java.util.ArrayList;
import java.util.Date;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PesquisasFinalizadasFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Cursor pesquisasCursor;
    private ArrayList<Pesquisa> pesquisas;
    private RecyclerView recyclerView;
    private AdministradorMainActivity admActivity;

    private static final int PESQUISASFinalizadas_LOADER = 0;
    private static final String[] PESQUISAS_COLUMNS = {
            DatabaseContract.PesquisasEntry.TABLE_NAME + "." + DatabaseContract.PesquisasEntry._ID,
            DatabaseContract.PesquisasEntry.COLUMN_EXAMINADOR,
            DatabaseContract.UsuariosEntry.COLUMN_NOME,
            DatabaseContract.PesquisasEntry.COLUMN_PARTICIPANTE,
            DatabaseContract.ParticipantesEntry.COLUMN_NOME,
            DatabaseContract.PesquisasEntry.COLUMN_DATA_DE_REALIZACAO,
            DatabaseContract.PesquisasEntry.COLUMN_ESTA_FINALIZADA
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PesquisasFinalizadasFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PesquisasFinalizadasFragment newInstance(int columnCount) {
        PesquisasFinalizadasFragment fragment = new PesquisasFinalizadasFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pesquisasfinalizadas_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            new FetchPesquisasTask().execute((Void) null);

            //recyclerView.setAdapter(new MyPesquisasFinalizadasRecyclerViewAdapter(context, pesquisasCursor));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Pesquisa item);
    }

    // Nesta classe interna, acessamos o banco de dados para criar as Pesquisas
    public class FetchPesquisasTask extends AsyncTask<Void, Void, Boolean> {

        // Vetor que guardará os indexes de cada pesquisa em relação ao cursor etapasCursor
        public ArrayList<Integer> indexPesquisa;

        FetchPesquisasTask() {
            indexPesquisa = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Não está pegando a PK salva em SharedPreferences!!!!
                SharedPreferences mUserSettings = getActivity().getSharedPreferences("UserSettings", Context.MODE_PRIVATE);
                String pk_usuario_pref = mUserSettings.getString("pk_usuario", "1");

                insereDumis();
                pesquisasCursor = getActivity().getContentResolver().query(
                        DatabaseContract.CONTENT_URI_RELATIONSHIP_JOIN_PESQUISASFINALIZADAS_GERAL,
                        null,
                        null,
                        null,
                        null
                );

                if (pesquisasCursor != null) {
                    Log.d("Log: ", "pesquisaCursor != null");
                    Log.d("PesquisasCursor = ", pesquisasCursor.toString());
                    return true;
                }
            } catch (UnsupportedOperationException e) {
                return false;
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                pesquisas = populaLista(pesquisasCursor);
                recyclerView.setAdapter(new MyPesquisasFinalizadasRecyclerViewAdapter(pesquisas, mListener));
            }
        }

        public ArrayList<Pesquisa> populaLista(Cursor pesquisasCursor) {
            pesquisas = new ArrayList<>();
            DatabaseUtils.dumpCursor(pesquisasCursor);
            // Cria as pesquisas
            while(pesquisasCursor.moveToNext()) {
                int pk_pesquisa = Integer.parseInt(pesquisasCursor.getString(0));
                //String dataRealizacao = converteData(pesquisasCursor.getString(1));
                String dataRealizacao = pesquisasCursor.getString(1);
                String nomeParticipante = pesquisasCursor.getString(2);
                String nomeExaminador = pesquisasCursor.getString(3);
                Pesquisa pesquisa = new Pesquisa(pk_pesquisa, dataRealizacao, nomeParticipante, nomeExaminador);
                // Popula o vetor
                pesquisas.add(pesquisa);
            }

            Log.d("ArrayList >>>> ", pesquisas.toString());
            return pesquisas;
        }

        public String converteData(String dataOriginal) {
            /* Formato da dataOriginal: 2016-09-30 (Sujeito a mudanças)
             *
             * [0]    [1]   [2]
             * 2016   09    30
             */
            String dataFinal = "";

            // Salva num vetor cada item que compõe a data
            String[] dataVetor = dataOriginal.split("-");

            // Hora (00:00)
            // dataVetor[3] = dataVetor[3].substring(0, 5);

            // Dia (9)
            if (dataVetor[2].charAt(0) == '0')
                dataVetor[2] = dataVetor[2].substring(1);

            // Mês (outubro)
            if (dataVetor[1].charAt(0) == '0')
                dataVetor[1] = dataVetor[1].substring(1);
            dataVetor[1] = Translator.toMes(dataVetor[1], getActivity()).toLowerCase();

            dataFinal = dataVetor[2] + " " + dataVetor[1] + " " + dataVetor[0];
            // + " " + getResources().getString(R.string.at).toLowerCase() + " "  + dataVetor[3];

            return dataFinal;
        }

        @Override
        protected void onCancelled() {
        }

        protected void insereDumis() {
            /*ContentValues cadastro1 = new ContentValues();
            cadastro1.put("_id", (byte[]) null);
            cadastro1.put("nome", "Paula Silva");
            cadastro1.put("email", "email@exemplo.com");
            cadastro1.put("senha", "123456");
            cadastro1.put("e_administrador", false);

            ContentValues cadastro2=  new ContentValues();
            cadastro2.put("_id", (byte[]) null);
            cadastro2.put("nome", "Dominique Santos");
            cadastro2.put("data_de_nascimento", new Date(1930, 4, 1).toString());
            cadastro2.put("genero", "outro");
            cadastro2.put("telefone", "33333333");
            cadastro2.put("escolaridade", "4");
            cadastro2.put("dominancia_manual", "c");
            cadastro2.put("tem_aposentadoria", false);
            cadastro2.put("profissao", "professor");
            cadastro2.put("idioma", 1);
            cadastro2.put("examinador", 2);

            ContentValues cadastro3=  new ContentValues();
            cadastro3.put("_id", (byte[]) null);
            cadastro3.put("nome", "Maria Lurdes");
            cadastro3.put("data_de_nascimento", new Date(1930, 4, 1).toString());
            cadastro3.put("genero", "outro");
            cadastro3.put("telefone", "33333333");
            cadastro3.put("escolaridade", "4");
            cadastro3.put("dominancia_manual", "c");
            cadastro3.put("tem_aposentadoria", false);
            cadastro3.put("profissao", "professor");
            cadastro3.put("idioma", 1);
            cadastro3.put("examinador", 1);

            ContentValues pesq1 = new ContentValues();
            pesq1.put("_id", (byte[]) null);
            pesq1.put("examinador", 1);
            pesq1.put("participante", 1);
            pesq1.put("data_de_realizacao", new Date(2016, 9, 9).toString());
            pesq1.put("esta_finalizada", true);

            ContentValues pesq2 = new ContentValues();
            pesq2.put("_id", (byte[]) null);
            pesq2.put("examinador", 2);
            pesq2.put("participante", 2);
            pesq2.put("data_de_realizacao", new Date(2016, 9, 9).toString());
            pesq2.put("esta_finalizada", false);

            getActivity().getContentResolver().insert(DatabaseContract.UsuariosEntry.CONTENT_URI, cadastro1);
            getActivity().getContentResolver().insert(DatabaseContract.ParticipantesEntry.CONTENT_URI, cadastro2);
            getActivity().getContentResolver().insert(DatabaseContract.ParticipantesEntry.CONTENT_URI, cadastro3);
            getActivity().getContentResolver().insert(DatabaseContract.PesquisasEntry.CONTENT_URI, pesq1);
            getActivity().getContentResolver().insert(DatabaseContract.PesquisasEntry.CONTENT_URI, pesq2);*/

            ContentValues alterarPesquisa = new ContentValues();
            alterarPesquisa.put("esta_finalizada", false);

            getActivity().getContentResolver().update(
                    DatabaseContract.PesquisasEntry.CONTENT_URI,
                    alterarPesquisa,
                    "_id= 1",
                    null);
        }
    }
}
