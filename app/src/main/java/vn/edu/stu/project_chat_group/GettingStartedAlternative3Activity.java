package vn.edu.stu.project_chat_group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class GettingStartedAlternative3Activity extends AppCompatActivity {
    private PreferencesManager preferencesManager;
    Button btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started_alternative3);
        preferencesManager = new PreferencesManager(getApplicationContext());
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNextActivity();
            }
        });
    }

    private void addControls() {
        btnOk = findViewById(R.id.okBtn);
    }

    private void openNextActivity() {
        Intent intent = new Intent(GettingStartedAlternative3Activity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}