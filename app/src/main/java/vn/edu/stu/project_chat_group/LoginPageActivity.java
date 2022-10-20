package vn.edu.stu.project_chat_group;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPageActivity extends AppCompatActivity {

    private Button btnAnonLogin, btnLogin, btnGoogleLogin;
    private TextView textViewRegister;
    private SwitchMaterial switchMaterial;
    private EditText etUsername, etPassword;
    private ProgressDialog loadingBar;

    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        addControls();
        addEvents();
        checkIfDarkModeOrNot();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void addControls() {
        textViewRegister = findViewById(R.id.tvRegister);
        btnAnonLogin = (Button) findViewById(R.id.btnAnonymousLogin); //nút đăng nhập ẩn danh
        btnLogin= findViewById(R.id.btnLogin); //nút đăng nhập bình thường
        switchMaterial = findViewById(R.id.switchDarkMode);//Nút switch dark mode
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        loadingBar = new ProgressDialog(LoginPageActivity.this);
    }

    private void addEvents() {
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("648782678538-qmdqfq0gtqsk0kl0dlfsc0565tomg47c.apps.googleusercontent.com") //copy từ client id trong google services
                .requestEmail()
                .build();
        // Bắt đầu đăng nhập
        googleSignInClient= GoogleSignIn.getClient(LoginPageActivity.this
                ,googleSignInOptions);
        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        // Check condition
        if(firebaseUser!=null)
        {
            // When user already sign in
            // redirect to profile activity
            startActivity(new Intent(LoginPageActivity.this,ProfileIDActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

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

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= googleSignInClient.getSignInIntent();
                // Start activity for result
                startActivityForResult(intent,100);
            }
        });

    }
    private void checkIfDarkModeOrNot() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                switchMaterial.setChecked(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                switchMaterial.setChecked(false);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            // When request code is equal to 100
            // Initialize task
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            // check condition
            if(signInAccountTask.isSuccessful())
            {
                // When google sign in successful
                // Initialize string
                String s="Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount=signInAccountTask
                            .getResult(ApiException.class);
                    // Check condition
                    if(googleSignInAccount!=null)
                    {
                        // When sign in account is not equal to null
                        // Initialize auth credential
                        AuthCredential authCredential= GoogleAuthProvider
                                .getCredential(googleSignInAccount.getIdToken()
                                        ,null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Check condition
                                        if(task.isSuccessful())
                                        {
                                            // When task is successful
                                            // Redirect to profile activity
                                            startActivity(new Intent(LoginPageActivity.this
                                                    ,ProfileIDActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            // Display Toast
                                            displayToast("Firebase authentication successful");
                                        }
                                        else
                                        {
                                            // When task is unsuccessful
                                            // Display Toast
                                            displayToast("Authentication Failed :"+task.getException()
                                                    .getMessage());
                                        }
                                    }
                                });

                    }
                }
                catch (ApiException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
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