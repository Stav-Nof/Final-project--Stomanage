package com.SandY.stomanage.Administrator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.TroopObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Regiment extends AppCompatActivity {

    TextView _header;
    EditText _search;
    ImageButton _clear, _share, _new;
    ListView _itemslist;


    String tid;
    String troopName;
    TroopObj troop;

    List<String> items;
    List<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_add_search);

        Intent intent = getIntent();
        troopName = intent.getStringExtra("troopName");

        attachFromXml();
        modifyActivity();
        getTroopFromDB();
        setClicks();
        searchAction();
    }

    private void attachFromXml(){
        _header = findViewById(R.id.header);
        _clear = findViewById(R.id.clear);
        _search = findViewById(R.id.searchText);
        _share = findViewById(R.id.share);
        _new = findViewById(R.id.createNew);
        _itemslist = findViewById(R.id.itemslist);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.regiment) + " - " + troopName);
        _search.setHint(getResources().getString(R.string.regiment_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void getTroopFromDB(){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = DBRef.child("Troops");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    TroopObj troopTemp = ds.getValue(TroopObj.class);
                    String TidTemp = ds.getKey();
                    if (troopTemp.get_name().equals(troopName)){
                        tid = TidTemp;
                        troop = troopTemp;
                        printItemList(_search.getText().toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    private void printItemList(String search){
        if (troop == null){
            Toast.makeText(Regiment.this, getResources().getString(R.string.troopobj_not_found), Toast.LENGTH_SHORT).show();
            return;
        }
        if (troop.get_regiments() == null){
            Toast.makeText(Regiment.this, getResources().getString(R.string.no_regiment_in_troop), Toast.LENGTH_SHORT).show();
            return;
        }
        items = new ArrayList<>();
        keys = new ArrayList<>();
        for(String name : troop.get_regiments().values()){
            if (name.contains(search)) {
                items.add(name);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(Regiment.this, R.layout.layout_item_list, items);
        _itemslist.setAdapter(adapter);
    }

    private void setClicks() {
        _new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRegiment();
            }
        });

        _clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _search.setText("");
            }
        });

        _itemslist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(Regiment.this)
                        .setTitle(getResources().getString(R.string.delete) + " " + items.get(position) )
                        .setMessage(getResources().getString(R.string.delete_regiment_message))
                        .setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                troop.get_regiments().values().remove(items.get(position));
                                troop.updateToDB(tid);
                                dialog.dismiss();
                                printItemList(_search.getText().toString());
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                return false;
            }
        });
    }

    private void searchAction(){
        _search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                printItemList(_search.getText().toString());
                if (_search.getText().toString().equals("")) _clear.setVisibility(View.INVISIBLE);
                else _clear.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void newRegiment() {
        Dialog dialog = new Dialog(Regiment.this);
        dialog.setContentView(R.layout.popup_two_text_one_button);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.popUpAnimation;
        dialog.show();

        EditText ageGroup = (EditText) dialog.findViewById(R.id.EditText1);
        ageGroup.setHint(getResources().getString(R.string.enter_age_group));
        EditText name = (EditText) dialog.findViewById(R.id.EditText2);
        name.setHint(getResources().getString(R.string.regiment_name));
        Button add = (Button) dialog.findViewById(R.id.Button);
        add.setText(getResources().getString(R.string.add));

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ageGroup.getText().toString().isEmpty()){
                    ageGroup.setError(getResources().getString(R.string.enter_age_group));
                    ageGroup.requestFocus();
                    return;
                }
                if (name.getText().toString().isEmpty()){
                    name.setError(getResources().getString(R.string.regiment_name));
                    name.requestFocus();
                    return;
                }
                troop.regimentsInitialization();
                String ageGroupS = ageGroup.getText().toString();
                String nameS = name.getText().toString();
                if (troop.get_regiments().keySet() != null && troop.get_regiments().keySet().contains(ageGroupS)){
                    ageGroup.setError(getResources().getString(R.string.age_group_already_exists));
                    ageGroup.requestFocus();
                    return;
                }
                if (troop.get_regiments().values().contains(nameS)){
                    name.setError(getResources().getString(R.string.name_already_exists));
                    name.requestFocus();
                    return;
                }
                troop.get_regiments().put(ageGroupS, nameS);
                troop.updateToDB(tid);
                dialog.dismiss();
                printItemList(_search.getText().toString());
            }
        });
    }
}
