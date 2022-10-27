package vn.edu.stu.project_chat_group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;

import vn.edu.stu.project_chat_group.SettingMainOptions.SettingActivity;
import vn.edu.stu.project_chat_group.utilities.Constant;
import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class MainActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager;
    private ProgressDialog loadingBarSignout;
    AppCompatImageView acpMenu;
    RoundedImageView rivImageProfileMain;
    TextView tvDisplayName;
    MaterialButton btnPlus;

    RecyclerView recyclerviewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        addControls();

        preferencesManager = new PreferencesManager(getApplicationContext());
        loadUsersDetails();
        addEvents();
        getToken();
    }


    private void addControls() {
        rivImageProfileMain = findViewById(R.id.roundedImageProfileMain);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        acpMenu = findViewById(R.id.acpMenu);
        btnPlus = findViewById(R.id.btnPlus); //nút thêm bạn
        recyclerviewUsers = findViewById(R.id.recyclerviewUsers);
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
        loadingBarSignout.setTitle(R.string.Signing_out);   //hiện thanh loading bar
        loadingBarSignout.setMessage(getResources().getString(R.string.please_wait)); //Chỗ này phải hơi rườm rà xíu nó mới chịu nhận R.string.please_wait
        loadingBarSignout.setCanceledOnTouchOutside(true); // bấm ngoài sẽ tắt loading bar
        loadingBarSignout.show();
        FirebaseFirestore database = FirebaseFirestore.getInstance();   //lấy dữ liệu từ firebase firestore về
        DocumentReference documentReference =
                database.collection(Constant.KEY_COLLECTION_USERS)
                        .document(preferencesManager.getString(Constant.KEY_USER_ID));
        HashMap<String, Object> update = new HashMap<>();
        update.put(Constant.KEY_FCM_TOKEN, FieldValue.delete());   //xoá fcm token trong firestore database đi
        documentReference.update(update)
                .addOnSuccessListener(unused -> {
                    preferencesManager.clear();
                    Intent intent = new Intent(MainActivity.this, LoginPageActivity.class);
                    startActivity(intent);
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
            case R.id.mnuLogout: //signout khỏi ứng dụng
                signOut();
                break;
            case R.id.mnuAbout:
                Toast.makeText(this, "Feature updating...", Toast.LENGTH_SHORT).show();
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
}