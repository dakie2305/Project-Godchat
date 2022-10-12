package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GettingStartedAlternative3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started_alternative3);


    }

    private void openNextActivity() {
        //
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}