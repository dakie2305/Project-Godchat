package vn.edu.stu.project_chat_group.SettingMainOptions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import vn.edu.stu.project_chat_group.MainActivity;
import vn.edu.stu.project_chat_group.R;

public class SettingActivity extends AppCompatActivity {

    private MaterialButton btnDarkMode, btnLanguageChange, btnNoti, btnPersonalAccountMange, btnPasswordManage, btnAppUpdate;
    private MaterialButton btnStories;
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
        btnPersonalAccountMange = findViewById(R.id.btnPersonalAccountMange); //setting chỉnh thông tin tài khoản
        btnPasswordManage = findViewById(R.id.btnPasswordManage); //setting chỉnh sửa mật khẩu và bảo mậts
        btnAppUpdate = findViewById(R.id.btnUpdateApp); //setting auto update
        btnStories = findViewById(R.id.btnStories); //setting chỉnh stories

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
        btnPersonalAccountMange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPersonalAccountManageSetting();
            }
        });
        btnPasswordManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPasswordChangeActivity();
            }
        });
        btnAppUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutoUpdateAppSetting();
            }
        });
        btnStories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStoriesSetting();
            }
        });
    }

    private void openStoriesSetting() {
        Intent intent = new Intent(SettingActivity.this, StoriesSettingActivity.class);
        startActivity(intent);
    }

    private void openAutoUpdateAppSetting() {
        Intent intent = new Intent(SettingActivity.this, AppUpdateSettingActivity.class);
        startActivity(intent);
    }

    private void openPasswordChangeActivity() {
        Intent intent = new Intent(SettingActivity.this, PasswordAndSecuritySettingActivity.class);
        startActivity(intent);
    }

    private void openPersonalAccountManageSetting() {
        Intent intent = new Intent(SettingActivity.this, PersonalInformationSettingActivity.class);
        startActivity(intent);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}