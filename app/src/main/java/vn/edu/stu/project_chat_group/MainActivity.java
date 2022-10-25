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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;

import com.makeramen.roundedimageview.RoundedImageView;

import vn.edu.stu.project_chat_group.SettingMainOptions.SettingActivity;
import vn.edu.stu.project_chat_group.utilities.Constant;
import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class MainActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager;

    AppCompatImageView acpMenu;
    RoundedImageView rivImageProfileMain;
    TextView tvDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        addControls();

        preferencesManager = new PreferencesManager(MainActivity.this);
        loadUsersDetails();
        addEvents();
    }


    private void addControls() {
        rivImageProfileMain = findViewById(R.id.roundedImageProfileMain);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        acpMenu = findViewById(R.id.acpMenu);
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

    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.setOnMenuItemClickListener(this::onOptionsItemSelected);//sẽ đồng thời có công dụng như hàm bên dưới, vì hàm dưới đã code sẵn chức năng
        popup.show();
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
            case R.id.mnuExit:
                finish();
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
}