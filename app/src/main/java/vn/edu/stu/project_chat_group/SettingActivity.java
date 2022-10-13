package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class SettingActivity extends AppCompatActivity {

    private MaterialButton btnDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        addControls();
        addEvents();
    }

    private void addControls() {
        btnDarkMode = findViewById(R.id.btnDarkModeToggle); //setting chỉnh dark mode
    }
    private void addEvents() {
        btnDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDarkModeSetting();
            }
        });
    }

    private void openDarkModeSetting() {
        Intent intent = new Intent(SettingActivity.this, DarkModeSettingActivity.class);
        startActivity(intent);
    }


}