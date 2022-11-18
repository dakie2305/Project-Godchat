package vn.edu.stu.project_chat_group;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.project_chat_group.adapter.UsersAdapter;
import vn.edu.stu.project_chat_group.listener.UserListener;
import vn.edu.stu.project_chat_group.models.User;
import vn.edu.stu.project_chat_group.utilities.Constant;
import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class UsersActivity extends AppCompatActivity implements UserListener { //cần implement thêm UserLister

//    private ActivityUsersBinding binding;

    PreferencesManager preferencesManager;

    MaterialButton btnBack;
    ProgressBar progessBarUsers;
    TextView tvErrorMessage;
    public RecyclerView recyclerviewUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_users);
        addControls();
        addEvents();
        preferencesManager = new PreferencesManager(getApplicationContext());
        getUsers(); //lấy các user về
    }

    private void addEvents() {
        //nút trở về
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void addControls() {
        btnBack = findViewById(R.id.btnBack);   //nút trở về
        progessBarUsers = findViewById(R.id.progessBarUsers);   //thanh progress
        tvErrorMessage= findViewById(R.id.tvErrorMessage);  //textview sẽ hiện lỗi
        recyclerviewUsers = findViewById(R.id.recyclerviewUsers);  //recycly view
    }

    //Hàm lấy tất cả các users từ FirebaseFirestore về
    private void getUsers(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constant.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId = preferencesManager.getString(Constant.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult()!=null){
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(currentUserId.equalsIgnoreCase(queryDocumentSnapshot.getId())){
                                continue;
                            }
                            //Tạo một User mới dựa trên thông tin lấy từ database trên FirebaseFirestore về
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constant.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constant.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constant.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constant.KEY_FCM_TOKEN);
                            user.username = queryDocumentSnapshot.getString(Constant.KEY_USERNAME);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        //Nếu user mà nhỏ hơn 0 tức là không có user nào, hoặc là bị lỗi, hiển thị ra cái text view
                        if(users.size()>0){
                            UsersAdapter usersAdapter = new UsersAdapter(users, this);
                            recyclerviewUsers.setAdapter(usersAdapter);
                            recyclerviewUsers.setVisibility(View.VISIBLE);
                        }else showErrorMessage();
                    }else showErrorMessage();
                });
    }

    private void showErrorMessage(){
        tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading) {                //Khi đã hoàn thiện và bấm vào thì nút sẽ tự ẩn đi, thanh progressBar tự hiện ra
        if (isLoading) {
            progessBarUsers.setVisibility(View.VISIBLE);
        } else {
            progessBarUsers.setVisibility(View.INVISIBLE);
        }
    }

    //hàm này là custom trong UserListener
    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(UsersActivity.this, ChatActivity.class);
        intent.putExtra(Constant.KEY_USER, user); //truyền user đã bấm trong list vào ChatActivity
        startActivity(intent);
        finish();
    }
}