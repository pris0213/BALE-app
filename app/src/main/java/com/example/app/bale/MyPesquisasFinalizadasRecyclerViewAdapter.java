package com.example.app.bale;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app.bale.PesquisasFinalizadasFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;

/**
 * Specified {@link PesquisasFinalizadasFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPesquisasFinalizadasRecyclerViewAdapter
        extends RecyclerView.Adapter<MyPesquisasFinalizadasRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Pesquisa> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPesquisasFinalizadasRecyclerViewAdapter(ArrayList<Pesquisa> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pesquisasfinalizadas, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getNomeParticipante());
        //holder.mContentView.setText(mValues.get(position).nomeExaminador);
        holder.mContentView.setText(mValues.get(position).getDataRealizacao());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final CircleProgressView mCircleProgressView;
        public Pesquisa mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mCircleProgressView = (CircleProgressView) view.findViewById(R.id.circleView);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    /*public interface OnItemClickListener {
        void onItemClick(Pesquisa item);
    }*/


}
