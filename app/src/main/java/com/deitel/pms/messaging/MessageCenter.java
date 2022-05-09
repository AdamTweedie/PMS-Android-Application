package com.deitel.pms.messaging;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.deitel.pms.FirestoreUtils;
import com.deitel.pms.R;
import com.deitel.pms.User;
import com.deitel.pms.student.Notifications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MessageCenter extends Fragment implements MessagesRecyclerViewAdapter.ItemClickListener {

    String recipient;
    String sender;
    Context context;
    MessagesRecyclerViewAdapter adapter;
    ArrayList<String> messageContent = new ArrayList<>();

    public String getRecipient() {
        return recipient;
    }

    public MessageCenter(String recipient, String sender, Context context) {
        this.recipient = recipient;
        this.sender = sender;
        this.context = context;
        this.adapter = new MessagesRecyclerViewAdapter(context, null);
    }

    FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
    Message message = new Message(null, null, null, null);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.in_app_messaging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = requireView().findViewById(R.id.rvSentAndReceivedMessages);

        FirestoreUtils utils = new FirestoreUtils();

        String senderId = this.sender;

        TextView messageRecipient = (TextView) view.findViewById(R.id.tvMessageRecipient);
        messageRecipient.setText(getRecipient());
        EditText typedMessage = (EditText) view.findViewById(R.id.etMessageBody);
        ImageButton sendMessage = (ImageButton) view.findViewById(R.id.btnSendMessage);

        recyclerView = requireView().findViewById(R.id.rvSentAndReceivedMessages);
        adapter.setmData(messageContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setClickListener(MessageCenter.this);
        recyclerView.setAdapter(adapter);

        // load messages
        if (senderId.contains("supervisor")) {
            loadMessages(utils.getSUPERVISOR_COLLECTION_PATH(), utils.getUSER_COLLECTION_PATH(),
                    senderId, getRecipient());
        } else {
            loadMessages(utils.getUSER_COLLECTION_PATH(), utils.getSUPERVISOR_COLLECTION_PATH(),
                    senderId, getRecipient());
        }

        // send message
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss a");
                LocalDateTime now = LocalDateTime.now();
                message.setContent(typedMessage.getText().toString());
                message.setCreatedAt(dtf.format(now));
                message.setSenderId(senderId);
                message.setRecipient(getRecipient());

                insertSingleItem("S: " + message.getContent());

                // TODO - fix this so that when a user is created they have an attribute which
                // TODO - specifies if they are a supervisor or not
                if (senderId.contains("supervisor")) {
                    uploadToFirebase(message, utils.getSUPERVISOR_COLLECTION_PATH());
                    typedMessage.setText("");
                } else {
                    uploadToFirebase(message, utils.getUSER_COLLECTION_PATH());
                    typedMessage.setText("");
                }
            }
        });
    }

    private void loadMessages(String senderCollectionPath,
                              String recipientCollectionPath,
                              String senderId,
                              String recipientId) {

        Map<String, Object> messages = new HashMap<>();

        // this obtains messages from the sender to the recipient
        // and then obtains messages from the recipient to the sender.
        dbInstance.collection(senderCollectionPath)
                .document(senderId)
                .collection(recipientId + " messages")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            messages.put(snapshot.getId(), "S: " + snapshot.get("content"));
                        }
                        dbInstance.collection(recipientCollectionPath)
                                .document(recipientId)
                                .collection(senderId + " messages")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                            messages.put(snapshot.getId(), "R: " + snapshot.get("content"));
                                        }
                                        sortMessages(messages);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("LOGGER", "Messages not found with exception " + e);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    // maybe make this a thread
    private void uploadToFirebase(Message message, String collectionPath) {

        Map<String, Object> messageData = new HashMap<>();
        messageData.put("content", message.getContent());
        // add to sender files
        dbInstance.collection(collectionPath)
                .document(message.getSenderId())
                .collection(message.getRecipient() + " messages")
                .document((String) message.getCreatedAt())
                .set(messageData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void insertSingleItem(String item) {
        int insertIndex = messageContent.size();
        messageContent.add(insertIndex, item);
        adapter.notifyItemInserted(insertIndex);
    }

    private void insertMultipleItems(ArrayList<String> data) {
        int insertIndex = adapter.getItemCount();
        messageContent.addAll(insertIndex, data);
        adapter.notifyItemRangeInserted(insertIndex, data.size());
    }

    private void sortMessages(Map<String, Object> messages) {

        try {
            ArrayList<String> datestring = new ArrayList<String>(messages.keySet());
            Collections.sort(datestring, new Comparator<String>() {
                final DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
                @Override
                public int compare(String o1, String o2) {
                    try {
                        return f.parse(o1).compareTo(f.parse(o2));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });

            ArrayList<String> sortedMsgs = new ArrayList<>();
            for (String date : datestring) {
                sortedMsgs.add((String) messages.get(date));
            }
            insertMultipleItems(sortedMsgs);

        } catch (Exception e) {
            Log.e("LOGGER", "Failed to sort messages with exception " + e);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
