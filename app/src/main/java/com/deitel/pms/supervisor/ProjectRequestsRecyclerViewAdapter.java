package com.deitel.pms.supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;
import com.deitel.pms.student.Notifications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProjectRequestsRecyclerViewAdapter extends RecyclerView
        .Adapter<com.deitel.pms.supervisor.ProjectRequestsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ArrayList<String>> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public ProjectRequestsRecyclerViewAdapter(Context context, ArrayList<ArrayList<String>> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ProjectRequestsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.supervisor_project_request_row, parent, false);
        return new com.deitel.pms.supervisor.ProjectRequestsRecyclerViewAdapter.ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(com.deitel.pms.supervisor.ProjectRequestsRecyclerViewAdapter.ViewHolder holder, int position) {
        ArrayList<String> project = mData.get(position);
        holder.studentId.setText(project.get(0));
        holder.projectTitle.setText(project.get(1));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView studentId;
        TextView projectTitle;

        ViewHolder(View itemView) {
            super(itemView);
            studentId = itemView.findViewById(R.id.projectRequestStudentId);
            projectTitle = itemView.findViewById(R.id.projectRequestTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public ArrayList<String> getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ProjectRequests itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}