package com.SandY.stomanage.Administrator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.SandY.stomanage.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdministratorMainMenu extends AppCompatActivity {

    TextView _name;
    CardView _class, _users;
    ImageButton _logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_main_menu);

        attachFromXml();
        modifyActivity();
        setOnClickListeners();

    }

    private void attachFromXml() {
        _name = (TextView)findViewById(R.id.name);
        _class = (CardView)findViewById(R.id.classCard);
        _users = (CardView)findViewById(R.id.userCard);
        _logout = findViewById(R.id.logOut);
    }

    private void modifyActivity(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("firstName");
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _name.setText(getResources().getString(R.string.welcome) + " " + dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                _name.setText(getResources().getString(R.string.welcome));
            }
        });
    }

    private void setOnClickListeners() {
        _class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorMainMenu.this, AClass.class);
                startActivity(intent);
            }
        });

        _users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministratorMainMenu.this, Users.class);
                startActivity(intent);
            }
        });

        _logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }
}