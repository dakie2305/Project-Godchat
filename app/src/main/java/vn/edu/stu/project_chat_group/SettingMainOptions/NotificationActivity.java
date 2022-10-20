package vn.edu.stu.project_chat_group.SettingMainOptions;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import vn.edu.stu.project_chat_group.R;

public class NotificationActivity extends AppCompatActivity {
    MaterialButton btnRingtone, btnCustomiseNoti, btnBack;
    SwitchMaterial swOn, swNotiPreview, swNewFriendNoti, swVibration, swSound;
    MediaPlayer mediaPlayer;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        
        addControls();
        addEvents();
    }



    private void addControls() {
        //thêm tất cả các button, và các switch
        swOn = findViewById(R.id.swOn);
        btnCustomiseNoti = findViewById(R.id.btnNotification);
        btnRingtone = findViewById(R.id.btnRingtone);
        swNotiPreview = findViewById(R.id.swPreviewNoti);
        swNewFriendNoti = findViewById(R.id.swNewFriendNoti);
        swVibration = findViewById(R.id.swVibrate);
        swSound = findViewById(R.id.swSounds);
        btnBack = findViewById(R.id.btnBack);
        tv = findViewById(R.id.tvRingtone_Sub);


    }
    private void addEvents() {
        btnRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRingtoneActivity();
            }
        });

        swOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(swOn.isChecked())
                    playASound(); //phát ra tiếng ting khi bật
            }
        });
        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (swSound.isChecked())
                    playASound(); //phát ra tiếng ting
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNotification();
            }
        });
        changeRingToneName();
        swVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(swVibration.isChecked())
                    vibrate();
            }
        });
    }

    private void vibrate() {
        Vibrator vibe = (Vibrator) getSystemService(NotificationActivity.VIBRATOR_SERVICE);
        vibe.vibrate(100);
    }

    private void changeRingToneName() {
        Intent intent = getIntent();
        String name= intent.getStringExtra("Ringtone");
        tv.setText(name);

    }

    private void openNotification() {
        Intent intent = new Intent(NotificationActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void openRingtoneActivity() {
        Intent intent = new Intent(NotificationActivity.this, RingtoneSelectActivity.class);
        startActivity(intent);
    }

    private void playASound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.ting_sound);
        mediaPlayer.start();
    }

}