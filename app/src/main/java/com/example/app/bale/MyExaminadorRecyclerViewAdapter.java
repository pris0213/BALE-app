package com.example.app.bale;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app.bale.ExaminadorFragment.OnListFragmentInteractionListener;
import com.example.app.bale.ExaminadorFragment.Examinador;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ExaminadorFragment.Examinador} and makes a call to the
 * specified {@link ExaminadorFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyExaminadorRecyclerViewAdapter extends RecyclerView.Adapter<MyExaminadorRecyclerViewAdapter.ViewHolder> {

    private final List<ExaminadorFragment.Examinador> mValues;
    private final ExaminadorFragment.OnListFragmentInteractionListener mListener;

    public MyExaminadorRecyclerViewAdapter(List<ExaminadorFragment.Examinador> items, ExaminadorFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_examinador, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getNomeExaminador());

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
        public final ImageView mImageView;
        public ExaminadorFragment.Examinador mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.frag_examinador_id);
            mContentView = (TextView) view.findViewById(R.id.frag_examinador_content);
            mImageView = (ImageView) view.findViewById((R.id.frag_examinador_image));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
