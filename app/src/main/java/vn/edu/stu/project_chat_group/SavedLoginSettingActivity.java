package vn.edu.stu.project_chat_group;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SavedLoginSettingActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radOn, radOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_login_setting);
        
        addControls();
        addEvents();
    }

    private void addEvents() {
        radOff.setChecked(false); //mặc định là false
    }

    private void addControls() {
        radioGroup = findViewById(R.id.radGroupSavedLogin);
        radOn = findViewById(R.id.radSavedLoginOn);
        radOff = findViewById(R.id.radSavedLoginOff);
    }
}