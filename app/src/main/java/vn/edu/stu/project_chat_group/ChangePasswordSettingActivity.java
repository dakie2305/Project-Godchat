package vn.edu.stu.project_chat_group;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ChangePasswordSettingActivity extends AppCompatActivity {
    MaterialButton btnBack, btnCommitPassChange, btnCancelPassChange;
    EditText etCurrentPass, etNewPass, etRetypeNewPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_setting);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCancelPassChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addControls() {
        btnBack = findViewById(R.id.btnBack);
        btnCommitPassChange =findViewById(R.id.btnCommitPassChange);
        btnCancelPassChange =findViewById(R.id.btnCancelPassChange);
        etCurrentPass = findViewById(R.id.etCurrentPass);
        etNewPass = findViewById(R.id.etNewPass);
        etRetypeNewPass = findViewById(R.id.etRetypeNewPass);


    }
}