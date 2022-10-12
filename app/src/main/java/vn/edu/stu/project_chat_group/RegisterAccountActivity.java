package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RegisterAccountActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText etUsername, etPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        addControls();
        addEvents();

    }

    private void addEvents() {

        //Nút đăng ký tài khoản
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterAccountActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
                } // nếu không nhập username hay password mà ấn nút đăng ký sẽ báo lỗi
                else{
                    loadingBar.setTitle(R.string.creating_account);
                    loadingBar.setMessage(getResources().getString(R.string.please_wait)); //Chỗ này phải hơi rườm rà xíu nó mới chịu nhận R.string.please_wait
                    loadingBar.setCanceledOnTouchOutside(true); // bấm ngoài sẽ tắt loading bar
                    loadingBar.show();
                    openGettingStartedActivity();
                }
            }
        });
    }

    private void addControls() {
        btnRegister = findViewById(R.id.buttonRegister);
        loadingBar = new ProgressDialog(RegisterAccountActivity.this);

    }

    private void openGettingStartedActivity() {
        Intent intent = new Intent(RegisterAccountActivity.this, GettingStartedAlternativeActivity.class);
        startActivity(intent);
    }
}