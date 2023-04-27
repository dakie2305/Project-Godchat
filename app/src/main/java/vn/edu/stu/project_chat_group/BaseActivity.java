package vn.edu.stu.project_chat_group;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import vn.edu.stu.project_chat_group.utilities.Constant;
import vn.edu.stu.project_chat_group.utilities.PreferencesManager;

public class BaseActivity extends AppCompatActivity {

    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {

        PreferencesManager preferencesManager = new PreferencesManager(getApplicationContext());
        FirebaseFirestore database= FirebaseFirestore.getInstance();


        documentReference = database.collection(Constant.KEY_COLLECTION_USERS)
                .document(preferencesManager.getString((Constant.KEY_USER_ID)));

    }

    //if the app is pause or inactivity
    //set online status to 0
    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(Constant.KEY_AVAILABILITY,0);
    }

    //revert, if app is in use
    //set online status to 1
    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(Constant.KEY_AVAILABILITY,1);
    }
}