package vn.edu.stu.project_chat_group.SettingMainOptions;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import vn.edu.stu.project_chat_group.R;

public class StoriesSettingActivity extends AppCompatActivity {
    MaterialButton btnBack;
    SwitchMaterial swArchiveStories;
    RadioGroup radioGroupStories;
    RadioButton radPublic, radFriendsOnly, radOnlyMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_setting);

        addControls();
        addEvents();
    }


    private void addControls() {
        btnBack = findViewById(R.id.btnBack);
        swArchiveStories = findViewById(R.id.swArchiveStories);
        radioGroupStories = findViewById(R.id.radioGroupStories);
        radPublic = findViewById(R.id.radPublic);
        radFriendsOnly = findViewById(R.id.radFriendsOnly);
        radOnlyMe = findViewById(R.id.radOnlyMe);
    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}