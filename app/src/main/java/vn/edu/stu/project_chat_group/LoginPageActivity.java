package vn.edu.stu.project_chat_group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class LoginPageActivity extends AppCompatActivity {

    private Button btnAnonLogin, btnLogin;
    private TextView textViewRegister;
    private SwitchMaterial switchMaterial;
    private EditText etUsername, etPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        addControls();
        addEvents();

    }
    private void addControls() {
        textViewRegister = findViewById(R.id.tvRegister);
        btnAnonLogin = (Button) findViewById(R.id.btnAnonymousLogin); //nút đăng nhập ẩn danh
        btnLogin= findViewById(R.id.btnLogin); //nút đăng nhập bình thường
        switchMaterial = findViewById(R.id.switchDarkMode);//Nút switch dark mode
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        loadingBar = new ProgressDialog(LoginPageActivity.this);
    }

    private void addEvents() {
        //Đăng ký
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterActivity();
            }
        });

        //chuyển dark mode
        switchMaterial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
        //Đăng nhập ẩn danh
        btnAnonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPageActivity.this);
                builder.setTitle("Input Username");
                final EditText input = new EditText(LoginPageActivity.this);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                builder.setView(input);
                //tạo hai button ok và cancel để tiếp tục nhập username mới, không cần password
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = input.getText().toString();
                        if (TextUtils.isEmpty(username)) {
                            Toast.makeText(LoginPageActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
                        } // nếu không nhập username hay password mà ấn nút Login sẽ báo lỗi
                        else {
                            loadingBar.setTitle(R.string.creating_account);
                            loadingBar.setMessage(getResources().getString(R.string.please_wait)); //Chỗ này phải hơi rườm rà xíu nó mới chịu nhận R.string.please_wait
                            loadingBar.setCanceledOnTouchOutside(true); // bấm ngoài sẽ tắt loading bar
                            loadingBar.show();
                            openGettingStartedActivity();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginPageActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
                } // nếu không nhập username hay password mà ấn nút Login sẽ báo lỗi
                else{
                    loadingBar.setTitle(R.string.creating_account);
                    loadingBar.setMessage(getResources().getString(R.string.please_wait)); //Chỗ này phải hơi rườm rà xíu nó mới chịu nhận R.string.please_wait
                    loadingBar.setCanceledOnTouchOutside(true); // bấm ngoài sẽ tắt loading bar
                    loadingBar.show();
                    openGettingStartedActivity(); //mở giao diện Mở Đầu
                }
            }
        });

    }


    private void openRegisterActivity() {
        Intent intent = new Intent(LoginPageActivity.this, RegisterAccountActivity.class);
        startActivity(intent);
    }

    public void openGettingStartedActivity(){
        Intent intent = new Intent(LoginPageActivity.this,GettingStartedAlternativeActivity.class);
        startActivity(intent);
    }
}