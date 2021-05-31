package com.SandY.stomanage.HeadWarehouseTeam;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.SandY.stomanage.Adapters.AdapterTextSubText;
import com.SandY.stomanage.GlobalConstants;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ClassObj;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Storekeepers extends AppCompatActivity {

    TextView _header;
    ImageButton _clear;
    EditText _search;
    ListView _itemslist;

    ArrayList<String> uids;
    ArrayList<UserObj> users;

    ArrayList<String> rids;
    ArrayList<ClassObj> regiments;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        setClicks();
//        searchAction();
    }

    private void attachFromXml(){
        _header = (TextView) findViewById(R.id.header);
        _search = (EditText) findViewById(R.id.searchText);
        _itemslist = (ListView) findViewById(R.id.itemslist);
        _clear = (ImageButton) findViewById(R.id.clear);
    }

    private void modifyActivity() {
        _header.setText(getResources().getString(R.string.storekeepers));
        _search.setHint(getResources().getString(R.string.user_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = DBRef.child("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserObj HeadTeam = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(UserObj.class);
                uids = new ArrayList<>();
                users = new ArrayList<>();
                DatabaseReference regimentRef = DBRef.child("Classes").child(HeadTeam.getCid());
                rids = new ArrayList<>();
                regiments = new ArrayList<>();
                //lists to print
                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> ageGroup = new ArrayList<>();
                regimentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot RegimentSnapshot) {
                        for(DataSnapshot regimentDs : RegimentSnapshot.getChildren()) {
                            rids.add(regimentDs.getKey());
                            regiments.add(regimentDs.getValue(ClassObj.class));
                        }
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            String uid = ds.getKey();
                            UserObj user = ds.getValue(UserObj.class);
                            if (user.getCid().equals(HeadTeam.getCid()) && user.getUserPerm().equals(GlobalConstants.Perm.מחסנאי.toString())){
                                uids.add(uid);
                                users.add(user);
                                names.add(user.getFirstName() + " " + user.getLastName());
                                if (user.getResponsibility() == null) ageGroup.add(getResources().getString(R.string.not_set));
                                else{
                                    int index = rids.indexOf(user.getResponsibility());
                                    ageGroup.add(regiments.get(index).get_name() + " - " + regiments.get(index).get_ageGroup());
                                }
                            }
                        }
                        AdapterTextSubText adapter = new AdapterTextSubText(Storekeepers.this, names, ageGroup);
                        _itemslist.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //TODO ser error
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TODO ser error
            }
        });
    }

    private void setClicks(){
        _itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = new Dialog(Storekeepers.this);
                dialog.setContentView(R.layout.popup_spiner_button);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                }
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                dialog.getWindow().getAttributes().windowAnimations = R.style.popUpAnimation;
                dialog.show();

                Spinner dialogSpinner = dialog.findViewById(R.id.Spinner);
                Button dialogUpdate = dialog.findViewById(R.id.Button);

                ArrayList<String> regiment = new ArrayList<>();
                regiment.add(getResources().getString(R.string.select_regiment));
                regiment.add(getResources().getString(R.string.not_set));
                for (int i = 0; i < regiments.size() ; i++){
                    regiment.add(regiments.get(i).get_name() + " - " + regiments.get(i).get_ageGroup());
                }

                ArrayAdapter<String> regimentAdapter = new ArrayAdapter<>(Storekeepers.this, android.R.layout.simple_spinner_item, regiment);
                dialogSpinner.setAdapter(regimentAdapter);
                dialogUpdate.setText(getResources().getString(R.string.update));

                dialogUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogSpinner.getSelectedItemPosition() == 0){
                            Toast.makeText(Storekeepers.this, getResources().getString(R.string.select_regiment_error), Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (dialogSpinner.getSelectedItemPosition() == 1){
                            users.get(position).setResponsibility(null);
                            users.get(position).updateToDB(uids.get(position));
                            dialog.dismiss();
                            printItemList(_search.getText().toString());
                            return;
                        }
                        users.get(position).setResponsibility(rids.get(dialogSpinner.getSelectedItemPosition() - 2));
                        users.get(position).updateToDB(uids.get(position));
                        dialog.dismiss();
                        printItemList(_search.getText().toString());
                    }
                });
            }
        });
    }
}
