package com.example.app.bale;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.bale.data.DatabaseContract;
import com.example.app.bale.dummy.DummyContent;
import com.example.app.bale.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExaminadorFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Cursor examinadorCursor;
    private ArrayList<Examinador> examinadores;
    private RecyclerView recyclerView;
    private AdministradorMainActivity admActivity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExaminadorFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ExaminadorFragment newInstance(int columnCount) {
        ExaminadorFragment fragment = new ExaminadorFragment();
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
        View view = inflater.inflate(R.layout.fragment_examinador_list, container, false);
        // String pk = admAtividade.getPk_usuario();
        /*if (admActivity.getMAddButton() != null) {
            admActivity.getMAddButton().setVisibility(View.VISIBLE);
        }*/
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            new FetchExaminadoresTask().execute((Void) null);

            // recyclerView.setAdapter(new MyExaminadorRecyclerViewAdapter(DummyContent.ITEMS, mListener));
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
        void onListFragmentInteraction(Examinador item);
    }

    public class FetchExaminadoresTask extends AsyncTask<Void, Void, Boolean> {

        // Vetor que guardará os indexes de cada pesquisa em relação ao cursor etapasCursor
        public ArrayList<Integer> indexExaminador;

        FetchExaminadoresTask() {
            indexExaminador = new ArrayList<>();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // insereDumis();
                examinadorCursor = getActivity().getContentResolver().query(
                        DatabaseContract.UsuariosEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                if (examinadorCursor != null) {
                    Log.d("Cursor:::: ", examinadorCursor.toString());
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
                examinadores = populaLista(examinadorCursor);
                recyclerView.setAdapter(new MyExaminadorRecyclerViewAdapter(examinadores, mListener));
            }
        }

        public ArrayList<Examinador> populaLista(Cursor examinadorCursor) {
            ArrayList<Examinador> populaExaminadores = new ArrayList<>();
            DatabaseUtils.dumpCursor(examinadorCursor);
            // Cria as pesquisas
            while (examinadorCursor.moveToNext()) {
                // Se a coluna "e_administrador" for 0 (false)
                if (examinadorCursor.getString(5).equals("false") || examinadorCursor.getString(5).equals("0")) {
                    int pk_examinador = Integer.parseInt(examinadorCursor.getString(0));
                    String nomeExaminador = examinadorCursor.getString(1);
                    // Popula vetor
                    populaExaminadores.add(new Examinador(pk_examinador, nomeExaminador));
                }
            }

            Log.d("Examinadores >>>> ", populaExaminadores.toString());
            return populaExaminadores;
        }

        @Override
        protected void onCancelled() {
        }
    }

    public class Examinador {

        private  final int pk_examinador;
        private final String nomeExaminador;

        public Examinador(int pk_examinador, String nomeExaminador) {
            this.pk_examinador = pk_examinador;
            this.nomeExaminador = nomeExaminador;
        }

        public int getPk_examinador() {
            return pk_examinador;
        }

        public String getNomeExaminador() {
            return nomeExaminador;
        }
    }

}
