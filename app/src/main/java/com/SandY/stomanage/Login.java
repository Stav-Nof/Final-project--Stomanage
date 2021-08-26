package com.SandY.stomanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.SandY.stomanage.Administrator.AdministratorMainMenu;
import com.SandY.stomanage.Guider.GuiderMainMenu;
import com.SandY.stomanage.HeadChapter.HeadChapterMainMenu;
import com.SandY.stomanage.HeadWarehouseTeam.HeadWarehouseTeamMainMenu;
import com.SandY.stomanage.dataObject.UserObj;
import com.SandY.stomanage.storekeeper.StorekeeperMainMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login, forgotPassword;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(Login.this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(SafetyNetAppCheckProviderFactory.getInstance());

//       NotificationService.createNotificationChannels(Login.this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        forgotPassword = findViewById(R.id.forgotPassword);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    email.setError(getResources().getString(R.string.email_error));
                    email.requestFocus();
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    password.setError(getResources().getString(R.string.password_error));
                    password.requestFocus();
                    return;
                }
                if (password.getText().toString().length() < GlobalConstants.passwordLength) {
                    password.setError(getResources().getString(R.string.password_length_error));
                    password.requestFocus();
                    return;
                }
                dialog = ProgressDialog.show(Login.this, "", "Loading. Please wait...", true);
                dialog.show();
                FirebaseAuth _auth = FirebaseAuth.getInstance();
                _auth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        password.setText("");
                        if (task.isSuccessful()) {
//                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//                                @Override
//                                public void onComplete(Task<String> task) {
//                                    if (task.isSuccessful()){
//                                        DBRef.child("token").setValue(task.getResult());
//                                    }
//                                }
//                            });
                            openNextView();
                        }
                        else {
                            Toast.makeText(Login.this, "Login eeror: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    email.setError(getResources().getString(R.string.email_error));
                    email.requestFocus();
                    return;
                }
                FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Login.this, getResources().getString(R.string.password_reset_link),Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(Login.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    private void openNextView(){
        String uid =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                email.setText("");
                password.setText("");
                UserObj user = snapshot.getValue(UserObj.class);
                Intent intent = null;
                if (user.getUserPerm().equals(GlobalConstants.Perm.מדריך.toString())) intent = new Intent(Login.this, GuiderMainMenu.class);
                else if (user.getUserPerm().equals(GlobalConstants.Perm.מחסנאי.toString())) intent = new Intent(Login.this, StorekeeperMainMenu.class);
                else if (user.getUserPerm().equals(GlobalConstants.Perm.מחסנאי_ראשי.toString())) intent = new Intent(Login.this, HeadWarehouseTeamMainMenu.class);
                else if (user.getUserPerm().equals(GlobalConstants.Perm.ראש_שבט.toString())) intent = new Intent(Login.this, HeadChapterMainMenu.class);
                else if (user.getUserPerm().equals(GlobalConstants.Perm.מנהל.toString())) intent = new Intent(Login.this, AdministratorMainMenu.class);
                else{
                    dialog.dismiss();
                    Toast.makeText(Login.this, getResources().getString(R.string.login_error),Toast.LENGTH_LONG).show();
                    return;
                }
                dialog.dismiss();
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(Login.this, "Login eeror: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}