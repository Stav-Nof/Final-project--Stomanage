package com.SandY.stomanage.HeadChapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.GlobalConstants;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ClassObj;
import com.SandY.stomanage.dataObject.ItemObj;
import com.SandY.stomanage.dataObject.chapterObj;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class NewUser extends AppCompatActivity {

    private static final int PASSWORD_LENGTH = 6;

    EditText _firstName, _lastName, _email, _password, _confirmPassword;
    Spinner _leadership, _chapter, _role, _class;
    ImageButton _createNew;

    String cid;

    ArrayList<String> roles, clids, clnames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");

        attachFromXml();
        modifyActivity();
        setClicks();
    }

    private void attachFromXml() {
        _firstName = findViewById(R.id.firstName);
        _lastName = findViewById(R.id.lastName);
        _email = findViewById(R.id.email);
        _password = findViewById(R.id.password);
        _confirmPassword = findViewById(R.id.confirmPassword);
        _leadership = findViewById(R.id.leadership);
        _chapter = findViewById(R.id.chapter);
        _role = findViewById(R.id.role);
        _createNew = findViewById(R.id.createNew);
        _class = findViewById(R.id.Class);
    }

    private void modifyActivity(){
        _chapter.setVisibility(View.INVISIBLE);
        _leadership.setVisibility(View.INVISIBLE);

        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(cid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                clids = new ArrayList<>();
                clnames = new ArrayList<>();
                clids.add("0");
                clnames.add(getResources().getString(R.string.select_class));
                for (DataSnapshot ds : snapshot.getChildren()){
                    ClassObj classObj = ds.getValue(ClassObj.class);
                    clids.add(ds.getKey());
                    clnames.add(classObj.get_name() + " - " + classObj.get_ageGroup());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewUser.this, android.R.layout.simple_spinner_item, clnames);
                _class.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                //TODO set error
            }
        });



        GlobalConstants.Perm[] Perms = GlobalConstants.Perm.values();
        roles = new ArrayList();
        roles.add(getResources().getString(R.string.select_role));
        for (int i = 0; i < Perms.length; i++){
            roles.add(Perms[i].toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        _role.setAdapter(adapter);
    }

    private void  setClicks(){
        _createNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_firstName.getText().toString().isEmpty()){
                    _firstName.setError(getResources().getString(R.string.first_name_error));
                    _firstName.requestFocus();
                    return;
                }
                if (_lastName.getText().toString().isEmpty()){
                    _lastName.setError(getResources().getString(R.string.last_name_error));
                    _lastName.requestFocus();
                    return;
                }
                if (_email.getText().toString().isEmpty()){
                    _email.setError(getResources().getString(R.string.email_error));
                    _email.requestFocus();
                    return;
                }
                if (_password.getText().toString().isEmpty()){
                    _password.setError(getResources().getString(R.string.password_error));
                    _password.requestFocus();
                    return;
                }
                if (_password.getText().toString().length() < PASSWORD_LENGTH){
                    _password.setError(getResources().getString(R.string.password_length_error));
                    _password.requestFocus();
                    return;
                }
                if (_confirmPassword.getText().toString().isEmpty()){
                    _confirmPassword.setError(getResources().getString(R.string.confirm_Password_error));
                    _confirmPassword.requestFocus();
                    return;
                }
                if (_role.getSelectedItemPosition() == 0){
                    Toast.makeText(NewUser.this, getResources().getString(R.string.select_role_error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (cid.isEmpty()){
                    if (_leadership.getSelectedItemPosition() == 0){
                        Toast.makeText(NewUser.this, getResources().getString(R.string.select_leadership_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (_chapter.getSelectedItemPosition() == 0){
                        Toast.makeText(NewUser.this, getResources().getString(R.string.select_class_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (!_password.getText().toString().equals(_confirmPassword.getText().toString())){
                    _confirmPassword.setError(getResources().getString(R.string.password_match_error));
                    _confirmPassword.requestFocus();
                    return;
                }
                if (roles.get(_role.getSelectedItemPosition()).equals(GlobalConstants.Perm.מדריך.toString()) && _class.getSelectedItemPosition() == 0){
                    Toast.makeText(NewUser.this, getResources().getString(R.string.select_class_error), Toast.LENGTH_LONG).show();
                    return;
                }
                UserObj user = new UserObj(_firstName.getText().toString(),
                        _lastName.getText().toString(),
                        _email.getText().toString(),
                        cid,
                        _role.getSelectedItem().toString());
                user.setResponsibility(clids.get(_class.getSelectedItemPosition()));
                FirebaseAuth Auth = FirebaseAuth.getInstance();
                Auth.createUserWithEmailAndPassword(user.getEmail(), _password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user.WriteToDB(task.getResult().getUser().getUid());
                            finish();
                        }
                        else{
                            Toast.makeText(NewUser.this,"Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        _role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (roles.get(position).equals(GlobalConstants.Perm.מדריך.toString())) _class.setVisibility(View.VISIBLE);
                else _class.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
