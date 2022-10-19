package vn.edu.stu.project_chat_group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class PasswordAndSecuritySettingActivity extends AppCompatActivity {
    MaterialButton btnPasswordChange, btnSaveLoginInfo, btnBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_and_security_setting);
        
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPasswordChangeSetting();
            }
        });
        btnSaveLoginInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSaveLoginSetting();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void openSaveLoginSetting() {
    }

    private void openPasswordChangeSetting() {
        Intent intent = new Intent(PasswordAndSecuritySettingActivity.this, ChangePasswordSettingActivity.class);
        startActivity(intent);
    }

    private void addControls() {
        btnPasswordChange= findViewById(R.id.btnPasswordChange);
        btnSaveLoginInfo = findViewById(R.id.btnSaveLoginInfo);
        btnBack = findViewById(R.id.btnBack);
    }
    
}