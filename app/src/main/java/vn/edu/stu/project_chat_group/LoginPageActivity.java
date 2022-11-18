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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import vn.edu.stu.project_chat_group.SettingMainOptions.RegisterAccountActivity;
import vn.edu.stu.project_chat_group.utilities.Constant;
import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class LoginPageActivity extends AppCompatActivity {

    private Button btnAnonLogin, btnLogin, btnGoogleLogin;
    private TextView textViewRegister;
    private SwitchMaterial switchMaterial;
    private EditText etUsername, etPassword;
    private ProgressDialog loadingBar;
    private ProgressBar progessBarLogin;

    private PreferencesManager preferencesManager;

    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(getApplicationContext());
        //Kiểm tra xem đã đăng nhập chưa, nếu đã đăng nhập thì chuyển thẳng vào MainActivity
        if(preferencesManager.getBoolean(Constant.KEY_IS_SIGNED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login_page);
        addControls();
        addEvents();
        checkIfDarkModeOrNot();

    }


    private void addDataToFirestore(){ //quan trọng, dùng để test thêm dữ liệu vào Firestore
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> data = new HashMap<>();
        data.put("first_name", "Chriga");  //test thêm first name: Chriga và last name
        data.put("last_name", "God");
        database.collection("users").add(data) //tạo một collection tên users để chứa first name và last name
                .addOnSuccessListener(documentReference -> {Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show();}) //nếu thành công thì xuất Toast
                .addOnFailureListener(exception -> {
                    Toast.makeText(this, "Data failed: " + exception.getMessage(), Toast.LENGTH_SHORT);
                });
    }

    private void addControls() {
        textViewRegister = findViewById(R.id.tvRegister); //Dòng chữ dùng để đăng ký
        btnAnonLogin = (Button) findViewById(R.id.btnAnonymousLogin); //nút đăng nhập ẩn danh
        btnLogin= findViewById(R.id.btnLogin); //nút đăng nhập bình thường
        switchMaterial = findViewById(R.id.switchDarkMode);//Nút switch dark mode
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin); //nút đăng nhập google
        etUsername = findViewById(R.id.etUsername); //Nhập Username
        etPassword = findViewById(R.id.etPassword); //Nhập password
        progessBarLogin = findViewById(R.id.progessBarLogin);

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
                //tạo hai button ok và cancel để tiếp tục nhập username mới, không cần password
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPageActivity.this);
                builder.setTitle("Input Username");
                final EditText input = new EditText(LoginPageActivity.this);
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = input.getText().toString();
                        if (TextUtils.isEmpty(username)) {
                            // nếu không nhập username hay password mà ấn nút Login sẽ báo lỗi
                            Toast.makeText(LoginPageActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            loadingBar.setTitle(R.string.creating_account);
                            loadingBar.setMessage(getResources().getString(R.string.please_wait)); //Chỗ này phải hơi rườm rà xíu nó mới chịu nhận R.string.please_wait
                            // bấm ngoài sẽ tắt loading bar
                            loadingBar.setCanceledOnTouchOutside(true);
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
                if (!isValidSignInDetai()) { //kiểm tra xem có nhập username với pass chưa
                    Toast.makeText(LoginPageActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
                } // nếu không nhập username hay password mà ấn nút Login sẽ báo lỗi
                else{
                    signIn();
                    //addDataToFirestore();
                    //openGettingStartedActivity(); //mở giao diện Mở Đầu
                }
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


    private void openRegisterActivity() {
        Intent intent = new Intent(LoginPageActivity.this, RegisterAccountActivity.class);
        startActivity(intent);
    }

    public void openGettingStartedActivity(){
        Intent intent = new Intent(LoginPageActivity.this,GettingStartedAlternativeActivity.class);
        startActivity(intent);
    }

    private Boolean isValidSignInDetai(){                   //kiểm tra đăng nhập điền đã đúng chưa
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginPageActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
            return false;
        } // nếu không nhập username hay password mà ấn nút Login sẽ báo lỗi
        return true;
    }

    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constant.KEY_COLLECTION_USERS)
                .whereEqualTo(Constant.KEY_USERNAME, etUsername.getText().toString())
                .whereEqualTo(Constant.KEY_PASSWORD, etPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult()!=null && task.getResult().getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferencesManager.putBoolean(Constant.KEY_IS_SIGNED_IN,true);
                        preferencesManager.putString(Constant.KEY_USER_ID,documentSnapshot.getId());
                        preferencesManager.putString(Constant.KEY_NAME, documentSnapshot.getString(Constant.KEY_NAME));
                        preferencesManager.putString(Constant.KEY_IMAGE, documentSnapshot.getString(Constant.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), GettingStartedAlternativeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else
                    {
                        loading(false);
                        Toast.makeText(this, R.string.Unable_to_sign_in, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loading(Boolean isLoading) {                //Khi đã hoàn thiện và bấm vào thì nút sẽ tự ẩn đi, thanh progressBar tự hiện ra
        if (isLoading) {
            btnLogin.setVisibility(View.INVISIBLE);
            progessBarLogin.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            progessBarLogin.setVisibility(View.INVISIBLE);
        }
    }
}