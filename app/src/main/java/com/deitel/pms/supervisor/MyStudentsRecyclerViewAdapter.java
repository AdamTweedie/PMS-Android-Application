package com.deitel.pms.supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;
import com.deitel.pms.admin.AdminStudentSupervisorView;

import java.util.ArrayList;
import java.util.List;

public class MyStudentsRecyclerViewAdapter extends RecyclerView.Adapter<MyStudentsRecyclerViewAdapter.ViewHolder> {

    List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public MyStudentsRecyclerViewAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.supervisor_my_student_row, parent, false);
        return new MyStudentsRecyclerViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(MyStudentsRecyclerViewAdapter.ViewHolder holder, int position) {
        String animal = getmData().get(position);
        holder.studentEmail.setText(animal);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return getmData().size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView studentEmail;

        ViewHolder(View itemView) {
            super(itemView);
            studentEmail = itemView.findViewById(R.id.tvMyStudentsEmail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }

    public void setmData(ArrayList<String> data) {
        this.mData = data;
    }

    public List<String> getmData() {
        return this.mData;
    }

    // allows clicks events to be caught
    public void setClickListener(SupervisorWorkspace itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setClickListener(SupervisorMessages itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void setClickListener(AdminStudentSupervisorView itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}