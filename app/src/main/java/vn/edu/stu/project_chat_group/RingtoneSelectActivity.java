package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RingtoneSelectActivity extends AppCompatActivity {
    MaterialButton btnBack;
    RadioGroup radioGroup;
    RadioButton radioButton;
    String name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone_select);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RingtoneSelectActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        radioGroup = findViewById(R.id.radGroupRingtone);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selectedId);
                name = radioButton.getText().toString();
                Intent intent = new Intent(RingtoneSelectActivity.this, NotificationActivity.class);
                intent.putExtra("Ringtone",name);
                startActivity(intent);
            }
        });

    }
}