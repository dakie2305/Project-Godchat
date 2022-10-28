package vn.edu.stu.project_chat_group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class GettingStartedAlternative2Activity extends AppCompatActivity {
    private ImageView imageViewRightArrow;
    private PreferencesManager preferencesManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started_alternative2);
        preferencesManager = new PreferencesManager(getApplicationContext());
        imageViewRightArrow = findViewById(R.id.right_arrow);

        imageViewRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPage3();
            }
        });

    }

    public void openPage3(){
        Intent intent = new Intent(this, GettingStartedAlternative3Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}