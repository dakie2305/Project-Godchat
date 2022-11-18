package vn.edu.stu.project_chat_group;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import vn.edu.stu.project_chat_group.adapter.ChatAdapter;
import vn.edu.stu.project_chat_group.models.ChatMessage;
import vn.edu.stu.project_chat_group.models.User;
import vn.edu.stu.project_chat_group.utilities.Constant;
import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class ChatActivity extends AppCompatActivity {
    EditText etInputMessage;
    AppCompatImageView btnSendMessage;
    TextView tvTitleChat;

    MaterialButton btnBack, btnInfo;

    RecyclerView chatRecycleView;
    private ProgressBar progessBarChat;

    private User receiveUser;

    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferencesManager preferencesManager;
    private FirebaseFirestore database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        addControls();
        loadReceiverDetails();
        addEvents();
        customInit();

        listenMessage();

    }

    private void customInit() {
        chatAdapter = new ChatAdapter(chatMessages, getBitmapFromEncodedStr(receiveUser.image),preferencesManager.getString(Constant.KEY_USER_ID));
        chatRecycleView.setAdapter(chatAdapter);
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

    }

    private void addControls() {
        btnSendMessage = findViewById(R.id.btnSendMessage);
        etInputMessage = findViewById(R.id.etInputMessage);
        tvTitleChat = findViewById(R.id.tvTitleChat);
        btnBack = findViewById(R.id.btnBack);
        preferencesManager = new PreferencesManager(ChatActivity.this);
        chatMessages = new ArrayList<>();
        database=FirebaseFirestore.getInstance();
        progessBarChat = findViewById(R.id.progessBarChat);

        chatRecycleView = findViewById(R.id.chatRecycleView);
    }

    private void loadReceiverDetails(){
        receiveUser = (User) getIntent().getSerializableExtra(Constant.KEY_USER);
        tvTitleChat.setText(receiveUser.name);
    }
    private Bitmap getBitmapFromEncodedStr(String string){
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);  //decode string image để tạo lại ảnh đại diện người dùng và hiện lên
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        return bitmap;
    }
    private void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constant.KEY_SENDER_ID, preferencesManager.getString(Constant.KEY_USER_ID));
        message.put(Constant.KEY_RECEIVER_ID, receiveUser.id);
        message.put(Constant.KEY_MESSAGE, etInputMessage.getText().toString());
        message.put(Constant.TIMESTAMP, new Date());
        database.collection(Constant.KEY_COLLECTION_CHAT).add(message);
        etInputMessage.setText("");
    }
    private String formatDateTime(Date date){
        return new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(date);
    }

    private void listenMessage(){
        database.collection(Constant.KEY_COLLECTION_CHAT).
                whereEqualTo(Constant.KEY_SENDER_ID, preferencesManager.getString(Constant.KEY_USER_ID))
                .whereEqualTo(Constant.KEY_RECEIVER_ID, receiveUser.id)
                .addSnapshotListener(eventListener); //tạo custom event listener
        database.collection(Constant.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constant.KEY_SENDER_ID, receiveUser.id)
                .whereEqualTo(Constant.KEY_RECEIVER_ID, preferencesManager.getString(Constant.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error)->{
        if(error!=null){
            return;
        }
        if(value!=null){
            int count = chatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType()== DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderID = documentChange.getDocument().getString(Constant.KEY_SENDER_ID);
                    chatMessage.receiverID = documentChange.getDocument().getString(Constant.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constant.KEY_MESSAGE);
                    chatMessage.dateTime = formatDateTime(documentChange.getDocument().getDate(Constant.TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constant.TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2)->obj1.dateObject.compareTo(obj2.dateObject));
            if (count==0){
                chatAdapter.notifyDataSetChanged();
            }else{
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                chatRecycleView.smoothScrollToPosition(chatMessages.size()-1);
            }
            chatRecycleView.setVisibility(View.VISIBLE);
        }
        progessBarChat.setVisibility(View.GONE);
    };
}