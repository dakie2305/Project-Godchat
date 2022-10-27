package vn.edu.stu.project_chat_group.SettingMainOptions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import vn.edu.stu.project_chat_group.GettingStartedAlternativeActivity;
import vn.edu.stu.project_chat_group.LoginPageActivity;
import vn.edu.stu.project_chat_group.R;
import vn.edu.stu.project_chat_group.utilities.Constant;
import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class RegisterAccountActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText etUsername, etPassword, etEmailAddress, etEmailCheck, etFirstName, etLastName;
    private ProgressDialog loadingBar;
    private ProgressBar progressBar;
    private TextView tvSignIn, tvAddImage;

    private FrameLayout frameLayoutImageProfile;
    private RoundedImageView roundedImageView;
    private String encodedImage, fullName, userName;

    private PreferencesManager preferencesManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);
        preferencesManager = new PreferencesManager(getApplicationContext());

        addControls();
        addEvents();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterAccountActivity.this, LoginPageActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    private void addEvents() {
        //nút trở về giao diện login
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterAccountActivity.this, LoginPageActivity.class);
                startActivity(intent);
            }
        });

        //Nút đăng ký tài khoản
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = etUsername.getText().toString();
                String firstname = etFirstName.getText().toString();
                String lastname = etLastName.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmailAddress.getText().toString();
                String emailCheck = etEmailCheck.getText().toString();

                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterAccountActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
                } // nếu không nhập username hay password mà ấn nút đăng ký sẽ báo lỗi
                else if (isEmailValid(email) != true) {
                    Toast.makeText(RegisterAccountActivity.this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                }else if (!checkEmail(email, emailCheck)){
                    Toast.makeText(RegisterAccountActivity.this, "Email not match.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname)){
                    Toast.makeText(RegisterAccountActivity.this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
                }
                else
                    register(); //đăng ký
                }
    });
        frameLayoutImageProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
    }
    });
}

    private boolean checkEmail(String email, String emailcheck) { //hàm kiểm tra xem email có nhập đúng hai lần không
        if (emailcheck.equalsIgnoreCase(email) && email.equalsIgnoreCase(emailcheck))
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
        tvSignIn = findViewById(R.id.tvSigIn);

        progressBar = findViewById(R.id.progessBarRegister);
        roundedImageView = findViewById(R.id.imageProfile);
        tvAddImage = findViewById(R.id.tvAddImage);
        frameLayoutImageProfile = findViewById(R.id.layoutImage);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);

    }

    private void register() {
        fullName = etFirstName.getText().toString() + " " + etLastName.getText().toString();   //lấy full tên bằng kết hợp first và last name
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();     //tạo một Hashmap user
        user.put(Constant.KEY_NAME, fullName);  //đưa full name vào trong KEY_NAME để đẩy lên collection user
        user.put(Constant.KEY_USERNAME, userName);  //đưa username vào trong KEY_USERNAME để đẩy lên collection user
        user.put(Constant.KEY_EMAIL, etEmailAddress.getText().toString());  //tương tự như trên
        user.put(Constant.KEY_PASSWORD, etPassword.getText().toString());
        user.put(Constant.KEY_IMAGE, encodedImage);
        database.collection(Constant.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false); //sau khi add user thành công thì ẩn thanh loading bar đi
                    preferencesManager.putBoolean(Constant.KEY_IS_SIGNED_IN, true);
                    preferencesManager.putString(Constant.KEY_USER_ID, documentReference.getId()); //lưu lại tất cả thông tin người vừa đăng ký
                    preferencesManager.putString(Constant.KEY_NAME, fullName);
                    preferencesManager.putString(Constant.KEY_USERNAME, userName);
                    preferencesManager.putString(Constant.KEY_IMAGE, encodedImage);
                    loadingBar.setTitle(R.string.creating_account);
                    loadingBar.setMessage(getResources().getString(R.string.please_wait)); //Chỗ này phải hơi rườm rà xíu nó mới chịu nhận R.string.please_wait
                    loadingBar.setCanceledOnTouchOutside(true); // bấm ngoài sẽ tắt loading bar
                    loadingBar.show();
                    Intent intent = new Intent(RegisterAccountActivity.this, GettingStartedAlternativeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    Toast.makeText(RegisterAccountActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loading(Boolean isLoading) {                //Khi đã hoàn thiện và bấm vào thì nút sẽ tự ẩn đi, thanh progressBar tự hiện ra
        if (isLoading) {
            btnRegister.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnRegister.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    //hàm sẽ mã hoá ảnh dựa trên Bitmap và trả về chuỗi string để có thể đưa lên firebase firestore database
    public String encodedImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT); //trả về chuỗi string đã được mã hoá dựa trên thuật toán Bitmap
    }


    //toàn function để có thể chọn ảnh từ thư viện ảnh trong máy
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri uriImage = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uriImage);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            roundedImageView.setImageBitmap(bitmap);
                            tvAddImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}