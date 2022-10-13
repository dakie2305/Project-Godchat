package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class LanguageChangeActivity extends AppCompatActivity {
    MaterialButton btnBack;
    RadioButton radViet, radEng;
    RadioGroup radioGroup;
    Locale locale;
    String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_change);

        addControls();
        addEvents();

    }

    private void addControls() {
        btnBack = findViewById(R.id.btnBack); //nút back trở về setting chính

        radViet = findViewById(R.id.radViet);
        radEng = findViewById(R.id.radEnglish);
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

        checkSystemLanguage();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radEnglish:
                        locale = new Locale("en");
                        setLocale();
                        //Toast.makeText(LanguageChangeActivity.this, R.string.english_lang, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radViet:
                        locale = new Locale("vi");
                        setLocale();
                        //Toast.makeText(LanguageChangeActivity.this, R.string.vietnamese_lang, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void checkSystemLanguage() {
        loc = getResources().getConfiguration().getLocales().get(0).toString();
        //Toast.makeText(LanguageChangeActivity.this, loc, Toast.LENGTH_SHORT).show(); //để test xem loc sẽ nhận được locale tag ra sao

        //vì đôi lúc locale trả về vừa là vi_VN, có khi là vi nên cần check hai trường hợp
        if(loc.equalsIgnoreCase("vi_VN") || getResources().getConfiguration().getLocales().get(0).toString().equalsIgnoreCase("vi")){
            radioGroup.clearCheck();
            radViet.setChecked(true);
        }
        else{
            radioGroup.clearCheck();
            radEng.setChecked(true);
        }
    }

    private void setLocale() {
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
        Intent i = new Intent(this, LanguageChangeActivity.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void openSettingActivity() {
        Intent intent = new Intent(LanguageChangeActivity.this, SettingActivity.class);
        startActivity(intent);
    }
}