package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GettingStartedAlternativeActivity extends AppCompatActivity {
    private ImageView imageViewRightArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started_alternative);

        imageViewRightArrow = findViewById(R.id.right_arrow);
        imageViewRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGettingStartedActivityAlternative2();
            }
        });
    }

    public void openGettingStartedActivityAlternative2(){
        Intent intent = new Intent(this, GettingStartedAlternative2Activity.class);
        startActivity(intent);
        //thêm animation
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}