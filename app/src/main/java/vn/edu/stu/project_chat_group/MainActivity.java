package vn.edu.stu.project_chat_group;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import vn.edu.stu.project_chat_group.SettingMainOptions.SettingActivity;
import vn.edu.stu.project_chat_group.listener.ConversationListener;
import vn.edu.stu.project_chat_group.models.ChatMessage;
import vn.edu.stu.project_chat_group.adapter.RecentConversationAdapter;
import vn.edu.stu.project_chat_group.models.User;
import vn.edu.stu.project_chat_group.utilities.Constant;
import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class MainActivity extends AppCompatActivity implements ConversationListener {

    private PreferencesManager preferencesManager;

    AppCompatImageView acpMenu;
    RoundedImageView rivImageProfileMain;
    TextView tvDisplayName;
    MaterialButton btnPlus;

    RecyclerView recyclerviewUsers;
    RecyclerView conversationRecyclerView;

    ProgressBar progressBarRecentConvo;

    private List<ChatMessage> conversations;
    private RecentConversationAdapter conversationAdapter;
    private FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferencesManager = new PreferencesManager(getApplicationContext());
        addControls();
        addEvents();
        loadUsersDetails();
        getToken();
        listenConversation();
    }


    private void addControls() {
        rivImageProfileMain = findViewById(R.id.roundedImageProfileMain);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        acpMenu = findViewById(R.id.acpMenu);
        btnPlus = findViewById(R.id.btnPlus); //nút thêm bạn
        recyclerviewUsers = findViewById(R.id.recyclerviewUsers);
        conversationRecyclerView = findViewById(R.id.conversationRecyclerView);

        conversations = new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(conversations, this);
        conversationRecyclerView.setAdapter(conversationAdapter);
        database = FirebaseFirestore.getInstance();
        progressBarRecentConvo=findViewById(R.id.progressBarRecentConvo);

    }

    private void loadUsersDetails(){ //lấy thông tin người dùng để đặt lên bar đầu tiên
        tvDisplayName.setText(preferencesManager.getString(Constant.KEY_NAME));   //lấy tên người dùng ra để đặt lên text view
        byte[] bytes = Base64.decode(preferencesManager.getString(Constant.KEY_IMAGE),Base64.DEFAULT);  //decode string image để tạo lại ảnh đại diện người dùng và hiện lên
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        rivImageProfileMain.setImageBitmap(bitmap);
    }

    private void addEvents() {
        acpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(intent);
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(this::onOptionsItemSelected);//sẽ đồng thời có công dụng như hàm bên dưới, vì hàm dưới đã code sẵn chức năng
        popup.show();
    }

    private void updateToken(String token){   //hàm tự tạo token và cập nhật token để update lên firebase database
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constant.KEY_COLLECTION_USERS)
                .document(preferencesManager.getString(Constant.KEY_USER_ID));
        documentReference.update(Constant.KEY_FCM_TOKEN, token)
//                .addOnSuccessListener(unused -> showToase("Token updated successfully")) để kiểm tra cập nhập được token chưa
                .addOnFailureListener(e -> showToase("Token updated failed"));
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }



    //hàm để signout thành công
    private void signOut(){
        Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT);
        FirebaseFirestore database = FirebaseFirestore.getInstance();   //lấy dữ liệu từ firebase firestore về
        DocumentReference documentReference =
                database.collection(Constant.KEY_COLLECTION_USERS)
                        .document(preferencesManager.getString(Constant.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constant.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferencesManager.putBoolean(Constant.KEY_IS_SIGNED_IN, false);
                    preferencesManager.clear();
                    Intent intent2 = new Intent(getApplicationContext(), LoginPageActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                    finish();
                })
                .addOnFailureListener(e->showToase("Unable to signout"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);// nạp file giao diện menu vào main activity
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuSetting:
                openSettingActivity();
                break;
            case R.id.mnuAbout:
                Toast.makeText(this, "Feature updating...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.app_bar_search:
                Toast.makeText(this, "Feature updating...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mnuLogout: //signout khỏi ứng dụng
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSettingActivity() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void showToase(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void listenConversation(){
        database.collection(Constant.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constant.KEY_SENDER_ID, preferencesManager.getString(Constant.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constant.KEY_COLLECTION_CONVERSATION)
                .whereEqualTo(Constant.KEY_RECEIVER_ID, preferencesManager.getString(Constant.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderID = documentChange.getDocument().getString(Constant.KEY_SENDER_ID);
                    String receiverID = documentChange.getDocument().getString(Constant.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderID = senderID;
                    chatMessage.receiverID = receiverID;
                    if (preferencesManager.getString(Constant.KEY_USER_ID).equals(senderID)) {
                        chatMessage.conversationImage = documentChange.getDocument().getString(Constant.KEY_RECEIVER_IMAGE);
                        chatMessage.conversationName = documentChange.getDocument().getString(Constant.KEY_RECEIVER_NAME);
                        chatMessage.conversationID = documentChange.getDocument().getString(Constant.KEY_RECEIVER_ID);
                    } else {
                        chatMessage.conversationImage = documentChange.getDocument().getString(Constant.KEY_SENDER_IMAGE);
                        chatMessage.conversationName = documentChange.getDocument().getString(Constant.KEY_SENDER_NAME);
                        chatMessage.conversationID = documentChange.getDocument().getString(Constant.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constant.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constant.TIMESTAMP);
                    conversations.add(chatMessage);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    {
                        for (int i = 0; i < conversations.size(); i++) {
                            String senderID = documentChange.getDocument().getString(Constant.KEY_SENDER_ID);
                            String receiverID = documentChange.getDocument().getString(Constant.KEY_RECEIVER_ID);
                            if (conversations.get(i).senderID.equals(senderID) && conversations.get(i).receiverID.equals(receiverID)) {
                                conversations.get(i).message = documentChange.getDocument().getString(Constant.KEY_LAST_MESSAGE);
                                conversations.get(i).dateObject = documentChange.getDocument().getDate(Constant.TIMESTAMP);
                                break;
                            }
                        }
                    }
                }
            }
            Collections.sort(conversations,(obj1, obj2)->obj2.dateObject.compareTo(obj1.dateObject));
            conversationAdapter.notifyDataSetChanged();
            conversationRecyclerView.smoothScrollToPosition(0);
            conversationRecyclerView.setVisibility(View.VISIBLE);
            progressBarRecentConvo.setVisibility(View.GONE);

        }
    };

    @Override
    public void onConversationClick(User user) {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        intent.putExtra(Constant.KEY_USER,user);
        startActivity(intent);
    }
}