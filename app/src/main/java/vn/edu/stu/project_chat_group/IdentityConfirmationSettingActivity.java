package vn.edu.stu.project_chat_group;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class IdentityConfirmationSettingActivity extends AppCompatActivity {
    MaterialButton btnConfirmId, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_confirmation_setting);
        
        addControls();
        addEvents();
    }
    private void addControls() {
        btnConfirmId = findViewById(R.id.btnConfirmId);
        btnBack = findViewById(R.id.btnBack);
    }
    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnConfirmId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IdentityConfirmationSettingActivity.this, "OK", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


}