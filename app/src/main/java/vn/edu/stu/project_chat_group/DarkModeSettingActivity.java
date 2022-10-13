package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class DarkModeSettingActivity extends AppCompatActivity {
    MaterialButton btnBack;
    RadioButton radOn, radOff, radSystem;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_mode_setting);


        addControls();
        addEvents();
    }
    private void addControls() {
        btnBack = findViewById(R.id.btnBack); //nút back trở về setting chính

        radOn = findViewById(R.id.radOn);//các radio button
        radOff = findViewById(R.id.radOff);
        radSystem = findViewById(R.id.radBasedOnSetting);
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

        boolean checkDarkMode = checkIfDarkModeOrNot();//Kiểm tra xem theme hiện tại đang là gì
        if (checkDarkMode==true)
            radOn.setChecked(true);
        else
            radOff.setChecked(false);

        //Check lựa chọn dark mode hay không, hay theo hệ thống
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radOn:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        Toast.makeText(DarkModeSettingActivity.this, R.string.on, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radOff:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        Toast.makeText(DarkModeSettingActivity.this, R.string.off, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radBasedOnSetting:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        Toast.makeText(DarkModeSettingActivity.this, R.string.Use_system_settings, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        break;
                }
            }
        });

    }
//chưa hoạt động được
    private boolean checkIfDarkModeOrNot() {
        int defaultNightMode = AppCompatDelegate.getDefaultNightMode();
        if (defaultNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            return true;
        }
        if (defaultNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            return false;
        }
        int modeType = getResources().getConfiguration().uiMode;
        switch (modeType){
            case Configuration.UI_MODE_NIGHT_NO:
                return false;
            case Configuration.UI_MODE_NIGHT_YES:
                return true;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                return false;
        }
        return false;
    }

    private void openSettingActivity() {
        Intent intent = new Intent(DarkModeSettingActivity.this, SettingActivity.class);
        startActivity(intent);
    }


}