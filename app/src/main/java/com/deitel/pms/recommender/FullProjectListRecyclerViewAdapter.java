package com.deitel.pms.recommender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;
import com.deitel.pms.supervisor.ProjectRequests;

import java.util.ArrayList;

public class FullProjectListRecyclerViewAdapter extends RecyclerView.Adapter<com.deitel.pms.recommender.FullProjectListRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ArrayList<String>> mData;
    private LayoutInflater mInflater;
    private com.deitel.pms.recommender.FullProjectListRecyclerViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public FullProjectListRecyclerViewAdapter(Context context, ArrayList<ArrayList<String>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public com.deitel.pms.recommender.FullProjectListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.student_notification_row, parent, false);
        return new com.deitel.pms.recommender.FullProjectListRecyclerViewAdapter.ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(com.deitel.pms.recommender.FullProjectListRecyclerViewAdapter.ViewHolder holder, int position) {
        ArrayList<String> project = getmData().get(position);
        if (project.get(2).length() > 100) {
            String titleSubstring = project.get(2).substring(0, 100);
            holder.projectTitle.setText(titleSubstring);
        } else {
            String titleSubstring = project.get(2);
            holder.projectTitle.setText(titleSubstring);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return getmData().size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView projectTitle;

        ViewHolder(View itemView) {
            super(itemView);
            projectTitle = itemView.findViewById(R.id.tvNotificationName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ArrayList<String> getItem(int id) {
        return getmData().get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(FullProjectList itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setmData(ArrayList<ArrayList<String>> data) {
        this.mData = data;
    }

    public ArrayList<ArrayList<String>> getmData() {
        return this.mData;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
