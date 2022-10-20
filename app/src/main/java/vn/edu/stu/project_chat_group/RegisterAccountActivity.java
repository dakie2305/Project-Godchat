package vn.edu.stu.project_chat_group;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterAccountActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText etUsername, etPassword, etEmailAddress, etEmailCheck;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        mAuth = FirebaseAuth.getInstance();

        addControls();
        addEvents();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            openGettingStartedActivity();
        }
    }

    private void addEvents() {

        //Nút đăng ký tài khoản
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmailAddress.getText().toString();
                String emailCheck = etEmailCheck.getText().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterAccountActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
                } // nếu không nhập username hay password mà ấn nút đăng ký sẽ báo lỗi
                else{
                    if(isEmailValid(email)!=true){  //email không hợp lệ sẽ báo lỗi
                        Toast.makeText(RegisterAccountActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    }else{
                        if(checkEmail(email, emailCheck)==false){  //email không trùng sẽ báo lỗi
                            Toast.makeText(RegisterAccountActivity.this, "Email not match.", Toast.LENGTH_SHORT).show();
                        }else{
                            loadingBar.setTitle(R.string.creating_account);
                            loadingBar.setMessage(getResources().getString(R.string.please_wait)); //Chỗ này phải hơi rườm rà xíu nó mới chịu nhận R.string.please_wait
                            loadingBar.setCanceledOnTouchOutside(true); // bấm ngoài sẽ tắt loading bar
                            loadingBar.show();
                            openGettingStartedActivity();
                        }
                    }

                }
            }
        });
    }

    private boolean checkEmail(String email, String emailcheck){ //hàm kiểm tra xem email có nhập đúng hai lần không
        if(emailcheck.equalsIgnoreCase(email) && email.equalsIgnoreCase(emailcheck))
            return true;
        else
            return false;
    }
    boolean isEmailValid(CharSequence email) {  //kiểm tra email có hợp lệ hay không
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void addControls() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etEmailCheck = findViewById(R.id.etEmailAddressCheck);
        btnRegister = findViewById(R.id.buttonRegister);
        loadingBar = new ProgressDialog(RegisterAccountActivity.this);

    }

    private void openGettingStartedActivity() {
        Intent intent = new Intent(RegisterAccountActivity.this, GettingStartedAlternativeActivity.class);
        startActivity(intent);
    }
}