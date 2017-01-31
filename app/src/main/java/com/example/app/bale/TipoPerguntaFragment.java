package com.example.app.bale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
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

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class TipoPerguntaFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ArrayList<String> etapasPerguntas;
    private RecyclerView recyclerView;
    private Cursor etapasPerguntasCursor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TipoPerguntaFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TipoPerguntaFragment newInstance(int columnCount) {
        TipoPerguntaFragment fragment = new TipoPerguntaFragment();
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

        etapasPerguntas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tipopergunta_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            new FetchTipoPerguntaTask().execute((Void) null);
            //recyclerView.setAdapter(new MyTipoPerguntaRecyclerViewAdapter(avaliacoes, mListener));
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

    public class FetchTipoPerguntaTask extends AsyncTask<Void, Void, Boolean> {

        FetchTipoPerguntaTask() {
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                insereDumis();
                etapasPerguntasCursor = getActivity().getContentResolver().query(
                        DatabaseContract.CONTENT_URI_DISTINCT_ETAPA_PERGUNTAS,
                        null,
                        null,
                        null,
                        null
                );
                if (etapasPerguntasCursor != null) {
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
                etapasPerguntas = populaLista(etapasPerguntasCursor);
                recyclerView.setAdapter(new MyTipoPerguntaRecyclerViewAdapter(etapasPerguntas, mListener));
            }
        }

        public ArrayList<String> populaLista(Cursor tipoAvaliacaoCursor) {
            etapasPerguntas = new ArrayList<>();
            DatabaseUtils.dumpCursor(tipoAvaliacaoCursor);
            while (tipoAvaliacaoCursor.moveToNext()) {
                etapasPerguntas.add(tipoAvaliacaoCursor.getString(0));
            }
            // Log.d("ArrayList >>>> ", etapasPerguntas.toString());
            return etapasPerguntas;
        }
        /* TODO: Inserir isso DIRETAMENTE NO BANCO (quando possível) */
        public void insereDumis() {
            /* Hábitos (Leitura e Escrita) */
            ContentValues perg11 = new ContentValues();
            perg11.put("_id", (byte[]) null);
            perg11.put("conteudo", getResources().getString(R.string.habitos_leit_revista));
            perg11.put("numeracao", 1);
            perg11.put("etapa", getResources().getString(R.string.bateria_tipopergunta_habitos));
            perg11.put("esta_ativa", true);
            ContentValues perg12 = new ContentValues();
            perg12.put("_id", (byte[]) null);
            perg12.put("conteudo", getResources().getString(R.string.habitos_leit_jornal));
            perg12.put("numeracao", 2);
            perg12.put("etapa", getResources().getString(R.string.bateria_tipopergunta_habitos));
            perg12.put("esta_ativa", true);
            ContentValues perg13 = new ContentValues();
            perg13.put("_id", (byte[]) null);
            perg13.put("conteudo", getResources().getString(R.string.habitos_leit_livro));
            perg13.put("numeracao", 3);
            perg13.put("etapa", getResources().getString(R.string.bateria_tipopergunta_habitos));
            perg13.put("esta_ativa", true);
            ContentValues perg14 = new ContentValues();
            perg14.put("_id", (byte[]) null);
            perg14.put("conteudo", getResources().getString(R.string.habitos_leit_rede));
            perg14.put("numeracao", 4);
            perg14.put("etapa", getResources().getString(R.string.bateria_tipopergunta_habitos));
            perg14.put("esta_ativa", true);
            ContentValues perg15 = new ContentValues();
            perg15.put("_id", (byte[]) null);
            perg15.put("conteudo", getResources().getString(R.string.habitos_escr_mensagem));
            perg15.put("numeracao", 5);
            perg15.put("etapa", getResources().getString(R.string.bateria_tipopergunta_habitos));
            perg15.put("esta_ativa", true);
            ContentValues perg16 = new ContentValues();
            perg16.put("_id", (byte[]) null);
            perg16.put("conteudo", getResources().getString(R.string.habitos_escr_literario));
            perg16.put("numeracao", 6);
            perg16.put("etapa", getResources().getString(R.string.bateria_tipopergunta_habitos));
            perg16.put("esta_ativa", true);
            ContentValues perg17 = new ContentValues();
            perg17.put("_id", (byte[]) null);
            perg17.put("conteudo", getResources().getString(R.string.habitos_escr_naoliterario));
            perg17.put("numeracao", 7);
            perg17.put("etapa", getResources().getString(R.string.bateria_tipopergunta_habitos));
            perg17.put("esta_ativa", true);
            ContentValues perg18 = new ContentValues();
            perg18.put("_id", (byte[]) null);
            perg18.put("conteudo", getResources().getString(R.string.habitos_escr_outro));
            perg18.put("numeracao", 8);
            perg18.put("etapa", getResources().getString(R.string.bateria_tipopergunta_habitos));
            perg18.put("esta_ativa", true);

            /* Compreensão de Frases */
            ContentValues perg2 = new ContentValues();
            perg2.put("_id", (byte[]) null);
            perg2.put("conteudo", "Escrita");
            perg2.put("numeracao", 9);
            perg2.put("etapa", "Compreensão de Frases");
            perg2.put("esta_ativa", true);

            /* Memória Episódica */
            ContentValues perg3 = new ContentValues();
            perg3.put("_id", (byte[]) null);
            perg3.put("conteudo", "Fruta,Lugar/Construção,Utensilho de Cozinha,Instrumento Musical,Morango,Igreja,Garfo,Violão");
            perg3.put("numeracao", 10);
            perg3.put("etapa", "Memória Episódica");
            perg3.put("esta_ativa", true);

            /* Compreensão Verbal */
            ContentValues perg4 = new ContentValues();
            perg4.put("_id", (byte[]) null);
            perg4.put("conteudo", "Lúcia mora no interior do Paraná. Numa manhã de segunda-feira, ela saiu de casa para mais uma\n" +
                    "entrevista de trabalho na capital do estado. Ela foi para a rodoviária de carona com seu amigo Pedro. Estava\n" +
                    "chovendo naquela manhã. De repente, o carro passou por um buraco e o pneu furou. Lúcia pensou que iria\n" +
                    "perder o ônibus. Então, ela pegou um táxi até a rodoviária e conseguiu chegar a tempo.");
            perg4.put("numeracao", 11);
            perg4.put("etapa", "Compreensão Verbal");
            perg4.put("esta_ativa", true);

            /* Discurso Livre */
            ContentValues perg5 = new ContentValues();
            perg5.put("_id", (byte[]) null);
            perg5.put("conteudo", "Gostaria que o(a) senhor(a) me contasse uma\n" +
                    "história engraçada que aconteceu com o(a) senhor(a), ou que o(a) senhor(a)\n" +
                    "presenciou, ou que lhe contaram. Não pode ser uma piada.");
            perg5.put("numeracao", 12);
            perg5.put("etapa", "Discurso Livre");
            perg5.put("esta_ativa", true);

            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg11);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg12);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg13);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg14);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg15);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg16);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg17);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg18);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg2);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg3);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg4);
            getActivity().getContentResolver().insert(DatabaseContract.PerguntasEntry.CONTENT_URI, perg5);
        }

        @Override
        protected void onCancelled() {
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String item);
    }
}
