package com.deitel.pms.messaging;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.R;
import com.deitel.pms.student.Notifications;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> mData;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MessagesRecyclerViewAdapter(Context context, ArrayList<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    @NonNull
    @Override
    public MessagesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message = getmData().get(position);
        holder.myTextView.setText(message);

        ConstraintSet constraintSet = new ConstraintSet();
        if (message.contains("S:")) {
            String newMessage = message.replace("S:", "");
            holder.myTextView.setText(newMessage);
            constraintSet.clone(holder.parent);
            constraintSet.connect(holder.messageConstraint.getId(),
                    ConstraintSet.END,
                    holder.parent.getId(),
                    ConstraintSet.END,0);
            constraintSet.connect(holder.messageConstraint.getId(),
                    ConstraintSet.TOP,
                    holder.parent.getId(),
                    ConstraintSet.TOP,0);
            constraintSet.applyTo(holder.parent);
            holder.messageConstraint.setBackgroundTintList(ColorStateList
                    .valueOf(Color.parseColor("#9163CB")));
            holder.myTextView.setTextColor(Color.parseColor("#FFFFFF"));

        } else {
            String newMessage = message.replace("R:", "");
            holder.myTextView.setText(newMessage);
            constraintSet.clone(holder.parent);
            constraintSet.connect(holder.messageConstraint.getId(),ConstraintSet.START,holder.parent.getId(),ConstraintSet.START,0);
            constraintSet.connect(holder.messageConstraint.getId(),ConstraintSet.TOP,holder.parent.getId(),ConstraintSet.TOP,0);
            constraintSet.applyTo(holder.parent);
            holder.messageConstraint.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CFD1D3")));

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return getmData().size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ConstraintLayout parent;
        ConstraintLayout messageConstraint;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvMessageContent);
            parent = itemView.findViewById(R.id.parentId);
            messageConstraint = itemView.findViewById(R.id.cl);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public String getItem(int id) {
        return getmData().get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(MessageCenter itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public ArrayList<String> getmData() {
        return mData;
    }

    public void setmData(ArrayList<String> mData) {
        this.mData = mData;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
