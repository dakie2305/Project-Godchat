package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.material.button.MaterialButton;

public class LanguageChangeActivity extends AppCompatActivity {
    MaterialButton btnBack;
    RadioButton radViet, radEng;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_change);

        addControls();
        addEvents();
    }

    private void addControls() {
        btnBack = findViewById(R.id.btnBack); //nút back trở về setting chính

        radViet = findViewById(R.id.radViet);
        radEng = findViewById(R.id.radEnglish);
        radioGroup = findViewById(R.id.radGroup);
    }

    private void addEvents() {
        //Back trở về setting
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingActivity();
            }
        });
        
    }

    private void openSettingActivity() {
        Intent intent = new Intent(LanguageChangeActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}