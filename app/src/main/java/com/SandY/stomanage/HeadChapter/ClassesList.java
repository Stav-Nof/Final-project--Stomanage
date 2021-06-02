package com.SandY.stomanage.HeadChapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.Adapters.AdapterTextSubText;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.ClassObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ClassesList extends AppCompatActivity {

    TextView _header;
    EditText _search;
    ImageButton _clear, _share;
    ListView _itemslist;

    String chid;
    String chapterName;

    List<String> keys;
    List<String> names;
    List<String> ages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        Intent intent = getIntent();
        chapterName = intent.getStringExtra("chapterName");
        chid = intent.getStringExtra("chid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        setClicks();
        searchAction();
    }

    private void attachFromXml(){
        _header = findViewById(R.id.header);
        _clear = findViewById(R.id.clear);
        _search = findViewById(R.id.searchText);
        _share = findViewById(R.id.share);
        _itemslist = findViewById(R.id.itemslist);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.Class) + " - " + chapterName);
        _search.setHint(getResources().getString(R.string.class_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search){
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = DBRef.child("Classes").child(chid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                keys = new ArrayList<>();
                names = new ArrayList<>();
                ages = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    ClassObj Class = ds.getValue(ClassObj.class);
                    String key = ds.getKey();
                    if (Class.get_name().contains(search) || Class.get_ageGroup().contains(search)){
                        keys.add(key);
                        names.add(Class.get_name());
                        ages.add(Class.get_ageGroup());
                    }
                }
                AdapterTextSubText adapter = new AdapterTextSubText(ClassesList.this, names, ages);
                _itemslist.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //TODO set error
            }
        });
    }

    private void setClicks() {
        _clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _search.setText("");
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
}