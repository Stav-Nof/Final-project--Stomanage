package com.SandY.stomanage.Administrator;

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
import com.SandY.stomanage.dataObject.TroopObj;
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

    private static final int PASSEORD_LENGTH = 6;

    EditText _firstName, _lastName, _email, _password, _confirmPassword;
    Spinner _leadership, _troop, _role;
    ImageButton _createNew;

    ArrayList<String> tids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

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
        _troop = findViewById(R.id.troop);
        _role = findViewById(R.id.role);
        _createNew = findViewById(R.id.createNew);
    }

    private void modifyActivity(){
        GlobalConstants.leadership[]  leaderships = GlobalConstants.leadership.values();
        ArrayList<String> leadership = new ArrayList();
        leadership.add(getResources().getString(R.string.select_leadership));
        for (int i = 0; i < leaderships.length; i++){
            leadership.add(leaderships[i].toString());
        }
        ArrayAdapter<String> leadershipAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, leadership);
        _leadership.setAdapter(leadershipAdapter);

        tids = new ArrayList();
        ArrayList<String> troops = new ArrayList();
        troops.add(getResources().getString(R.string.select_troop));
        ArrayAdapter<String> troopAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, troops);
        _troop.setAdapter(troopAdapter);

        _leadership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                troops.clear();
                troops.add(getResources().getString(R.string.select_troop));

                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref = DBRef.child("Troops");
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tids.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            TroopObj troop = ds.getValue(TroopObj.class);
                            if (troop.get_leadership().equals(leadership.get(position))){
                                troops.add(troop.get_name());
                                tids.add(ds.getKey());
                            }
                        }
                        _troop.setAdapter(troopAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                ref.addListenerForSingleValueEvent(valueEventListener);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        GlobalConstants.Perm[] Perms = GlobalConstants.Perm.values();
        ArrayList<String> roles = new ArrayList();
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
                if (_password.getText().toString().length() < PASSEORD_LENGTH){
                    _password.setError(getResources().getString(R.string.password_length_error));
                    _password.requestFocus();
                    return;
                }
                if (_confirmPassword.getText().toString().isEmpty()){
                    _confirmPassword.setError(getResources().getString(R.string.confirm_Password_error));
                    _confirmPassword.requestFocus();
                    return;
                }
                if (_leadership.getSelectedItemPosition() == 0){
                    Toast.makeText(NewUser.this, getResources().getString(R.string.select_leadership_error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (_troop.getSelectedItemPosition() == 0){
                    Toast.makeText(NewUser.this, getResources().getString(R.string.select_troop_error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (_role.getSelectedItemPosition() == 0){
                    Toast.makeText(NewUser.this, getResources().getString(R.string.select_role_error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (!_password.getText().toString().equals(_confirmPassword.getText().toString())){
                    _confirmPassword.setError(getResources().getString(R.string.password_match_error));
                    _confirmPassword.requestFocus();
                    return;
                }
                UserObj user = new UserObj(_firstName.getText().toString(),
                                            _lastName.getText().toString(),
                                            _email.getText().toString(),
                                            _troop.getSelectedItem().toString(),
                                            _leadership.getSelectedItem().toString(),
                                            _role.getSelectedItem().toString(),
                                            null);
                FirebaseAuth Auth = FirebaseAuth.getInstance();;
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
    }
}
