package vn.edu.stu.project_chat_group;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class LauncherActivity extends AppCompatActivity {

    private PreferencesManager preferencesManager;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);

        progressBar = findViewById(R.id.progressBar);
        preferencesManager = new PreferencesManager(getApplicationContext());
        
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LauncherActivity.this, LoginPageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                finish();
            }
        },1000);
    }
}