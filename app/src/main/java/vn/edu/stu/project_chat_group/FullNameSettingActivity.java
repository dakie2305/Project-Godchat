package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

public class FullNameSettingActivity extends AppCompatActivity {
    MaterialButton btnCommit, btnCancel, btnBack;
    EditText etFirst, etLast;
    String firstname = "", famname ="", fullname="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_name_setting);
        addControls();
        addEvents();

    }

    private void addControls() {
        btnCommit = findViewById(R.id.btnCommitChange);
        btnCancel = findViewById(R.id.btnCancelChange);
        btnBack = findViewById(R.id.btnBack);
        etFirst = findViewById(R.id.etFirstNameInSetting);
        etLast = findViewById(R.id.etFamilyNameInSetting);
    }
    private void addEvents() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitChange();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPersonalInfoSetting();
            }
        });
    }

    private void openPersonalInfoSetting() {
        Intent intent = new Intent(FullNameSettingActivity.this, PersonalInformationSettingActivity.class);
        startActivity(intent);
    }

    private void commitChange() {
        getNameFromEditText();
        Intent intent = new Intent();
        intent.putExtra("fullname", fullname);
        setResult(2,intent);
        finish();

    }

    private void getNameFromEditText() {
        firstname = etFirst.getText().toString();
        famname = etLast.getText().toString();
        fullname = firstname + " " + famname;
    }
}