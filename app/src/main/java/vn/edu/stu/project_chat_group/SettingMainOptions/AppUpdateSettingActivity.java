package vn.edu.stu.project_chat_group.SettingMainOptions;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import vn.edu.stu.project_chat_group.R;

public class AppUpdateSettingActivity extends AppCompatActivity {

    MaterialButton btnBack;
    SwitchMaterial swAutoUpdate, swUpdateNoti;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_update_setting);

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
        swUpdateNoti.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(swUpdateNoti.isChecked()) {
                    playASound();
                }
            }
        });
    }

    private void playASound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.ting_sound);
        mediaPlayer.start();
    }

    private void addControls() {
        btnBack = findViewById(R.id.btnBack);
        swAutoUpdate = findViewById(R.id.swAutoUpdate);
        swUpdateNoti = findViewById(R.id.swUpdateNoti);
    }
}