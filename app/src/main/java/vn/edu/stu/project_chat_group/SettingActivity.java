package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class SettingActivity extends AppCompatActivity {

    private MaterialButton btnDarkMode, btnLanguageChange, btnNoti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        addControls();
        addEvents();
    }

    private void addControls() {
        btnDarkMode = findViewById(R.id.btnDarkModeToggle); //setting chỉnh dark mode
        btnLanguageChange = findViewById(R.id.btnLanguage); //setting chỉnh ngôn ngữ
        btnNoti = findViewById(R.id.btnNotification); //setting chỉnh thông báo
    }
    private void addEvents() {
        btnDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDarkModeSetting();
            }
        });

        btnLanguageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLanguageChangeSetting();
            }
        });
        btnNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNotificationSetting();
            }
        });
    }

    private void openNotificationSetting() {
        Intent intent = new Intent(SettingActivity.this, NotificationActivity.class);
        startActivity(intent);
    }

    private void openLanguageChangeSetting() {
        Intent intent = new Intent(SettingActivity.this, LanguageChangeActivity.class);
        startActivity(intent);
    }

    private void openDarkModeSetting() {
        Intent intent = new Intent(SettingActivity.this, DarkModeSettingActivity.class);
        startActivity(intent);
    }


}