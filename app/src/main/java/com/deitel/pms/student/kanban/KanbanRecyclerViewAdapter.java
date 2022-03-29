package com.deitel.pms.student.kanban;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;

import java.util.ArrayList;
import java.util.Map;

public class KanbanRecyclerViewAdapter extends RecyclerView
        .Adapter<com.deitel.pms.student.kanban.KanbanRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> taskData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int tabPosition;
    private TaskList taskList;
    private Context context;

    public KanbanRecyclerViewAdapter(Context context, ArrayList<String> data, int position, TaskList taskList) {
        this.taskData = data;
        this.mInflater = LayoutInflater.from(context);
        this.tabPosition = position;
        this.taskList = taskList;
        this.context = context;
    }
    @NonNull
    @Override
    public KanbanRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.kanban_task_view, parent, false);
        return new KanbanRecyclerViewAdapter.ViewHolder(view, new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull KanbanRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());
        holder.editTaskContent.setText(taskData.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return taskData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EditText editTaskContent;
        ImageButton button_delete;
        public MyCustomEditTextListener myCustomEditTextListener;

        ViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            editTaskContent = itemView.findViewById(R.id.task_edit_text);
            button_delete = itemView.findViewById(R.id.btn_kanban_task_delete);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.editTaskContent.addTextChangedListener(myCustomEditTextListener);
            itemView.setOnClickListener(this);

            itemView.findViewById(R.id.btn_kanban_task_delete)
                    .setOnClickListener(view -> {
                        final int position = getAdapterPosition();
                        notifyItemRemoved(getAdapterPosition());
                        getTaskData().remove(position);
                    });

            itemView.findViewById(R.id.btn_move_task)
                    .setOnClickListener(view -> {
                        final int position = getAdapterPosition();
                        String task = getTaskData().get(position);
                        switch(getTabPosition()) {
                            case 0:
                                getTaskList().addToPrefs(1, task);
                                notifyItemRemoved(getAdapterPosition());
                                getTaskData().remove(position);
                                break;
                            case 1:
                                getTaskList().addToPrefs(2, task);
                                notifyItemRemoved(getAdapterPosition());
                                getTaskData().remove(position);
                                break;
                            case 2:
                                Toast.makeText(getContext(), "Invalid Input!", Toast.LENGTH_SHORT).show();
                                break;
                        }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            taskData.set(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    public String getItem(int id) {
        System.out.println("DATA - " + getTaskData().get(id));
        return getTaskData().get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(TaskList itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public ArrayList<String> getTaskData() {
        return this.taskData;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setTabPosition(int position) {
        this.tabPosition = position;
    }

    public int getTabPosition() {
        return this.tabPosition;
    }

    private TaskList getTaskList() {
        return this.taskList;
    }

    private Context getContext() {
        return this.context;
    }
}
