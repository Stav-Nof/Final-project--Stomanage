package com.SandY.stomanage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import com.google.firebase.messaging.FirebaseMessaging;


public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NotificationService.createNotificationChannels(Login.this);
        NotificationService.NotificationOrders(Login.this, "yarin 1", "or");
        NotificationService.NotificationFactories(Login.this, "yarin", "or 2");



//        FirebaseApp.initializeApp(Login.this);
//        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
//        firebaseAppCheck.installAppCheckProviderFactory(
//                SafetyNetAppCheckProviderFactory.getInstance());
//
//        email = findViewById(R.id.email);
//        password = findViewById(R.id.password);
//        login = findViewById(R.id.login);
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(email.getText().toString().isEmpty()){
//                    email.setError(getResources().getString(R.string.email_error));
//                    email.requestFocus();
//                    return;
//                }
//                if(password.getText().toString().isEmpty()){
//                    password.setError(getResources().getString(R.string.password_error));
//                    password.requestFocus();
//                    return;
//                }
//                //TODO set error to less then 6 char password
//                FirebaseAuth _auth = FirebaseAuth.getInstance();
//                _auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        password.setText("");
//                        if(task.isSuccessful()){
//                            String uid = task.getResult().getUser().getUid();
//                            DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
//                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//                                @Override
//                                public void onComplete(Task<String> task) {
//                                    if (task.isSuccessful()){
//                                        DBRef.child("token").setValue(task.getResult());
//                                    }
//                                }
//                            });
//
//                            DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    UserObj user = snapshot.getValue(UserObj.class);
//                                    if (user.getUserPerm().equals(GlobalConstants.Perm.מדריך.toString())){
//                                        Intent intent = new Intent(Login.this, GuiderMainMenu.class);
//                                        startActivity(intent);
//                                        return;
//                                    }
//                                    if (user.getUserPerm().equals(GlobalConstants.Perm.מחסנאי.toString())){
//                                        Intent intent = new Intent(Login.this, StorekeeperMainMenu.class);
//                                        startActivity(intent);
//                                        return;
//                                    }
//                                    if (user.getUserPerm().equals(GlobalConstants.Perm.מחסנאי_ראשי.toString())){
//                                        Intent intent = new Intent(Login.this, HeadWarehouseTeamMainMenu.class);
//                                        startActivity(intent);
//                                        return;
//                                    }
//                                    if (user.getUserPerm().equals(GlobalConstants.Perm.ראש_שבט.toString())){
//                                        Intent intent = new Intent(Login.this, HeadChapterMainMenu.class);
//                                        startActivity(intent);
//                                        return;
//                                    }
//                                    if (user.getUserPerm().equals(GlobalConstants.Perm.מנהל.toString())){
//                                        Intent intent = new Intent(Login.this, AdministratorMainMenu.class);
//                                        startActivity(intent);
//                                        return;
//                                    }
//                                }
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    Toast.makeText(Login.this, "Login eeror: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//                        else{
//                            //TODO set error to fail login
//                        }
//                    }
//                });
//            }
//        });
    }
}