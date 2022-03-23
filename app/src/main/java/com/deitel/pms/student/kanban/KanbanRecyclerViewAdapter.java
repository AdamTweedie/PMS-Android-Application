package com.deitel.pms.student.kanban;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;

import java.util.ArrayList;
import java.util.Map;

public class KanbanRecyclerViewAdapter extends RecyclerView
        .Adapter<com.deitel.pms.student.kanban.KanbanRecyclerViewAdapter.ViewHolder> {

    private ArrayList<ArrayList<String>> taskData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public KanbanRecyclerViewAdapter(Context context, ArrayList<ArrayList<String>> data) {
        this.taskData = data;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public KanbanRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.kanban_task_view, parent, false);
        return new KanbanRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KanbanRecyclerViewAdapter.ViewHolder holder, int position) {
        ArrayList<String> task = taskData.get(position);
        holder.editTaskContent.setText(task.get(0));
    }

    @Override
    public int getItemCount() {
        return taskData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EditText editTaskContent;

        ViewHolder(View itemView) {
            super(itemView);
            editTaskContent = itemView.findViewById(R.id.task_edit_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(TaskList itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
