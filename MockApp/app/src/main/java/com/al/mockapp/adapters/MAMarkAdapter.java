package com.al.mockapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.al.mockapp.R;
import com.al.mockapp.models.MAMarkModel;

import java.util.ArrayList;

/**
 * Created by vineeth on 01/04/16
 */
public class MAMarkAdapter extends RecyclerView.Adapter<MAMarkAdapter.ViewHolder> {
    private ArrayList<MAMarkModel> mMarks;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MAMarkAdapter(ArrayList<MAMarkModel> marks) {
        mMarks = marks;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mark_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        viewHolder.mSubjectTextView.setText(mMarks.get(viewHolder.getAdapterPosition()).getSubjectName());
        viewHolder.mMarkTextView.setText(String.valueOf(mMarks.get(viewHolder.getAdapterPosition()).getMarks()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMarks.size();
    }

    // reference to the views for each data item
    // which will provide access to all the views for a data item in a view holder
    class ViewHolder extends RecyclerView.ViewHolder {
        protected final TextView mSubjectTextView;
        protected final TextView mMarkTextView;

        public ViewHolder(View view) {
            super(view);

            mSubjectTextView = (TextView) view.findViewById(R.id.subject_textview);
            mMarkTextView = (TextView) view.findViewById(R.id.mark_textview);
        }
    }

}