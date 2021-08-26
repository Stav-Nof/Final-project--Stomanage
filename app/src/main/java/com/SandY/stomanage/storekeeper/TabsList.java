package com.SandY.stomanage.storekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class TabsList extends AppCompatActivity {

    EditText _search;
    ListView _itemslist;
    TextView _header;
    ImageButton _clear;

    String cid;

    ArrayList<String> uids;
    ArrayList<String> names;
    ArrayList<String> prinresUids;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");

        attachFromXml();
        modifyActivity();
        printItemList(_search.getText().toString());
        setClicks();
        searchAction();
    }

    private void attachFromXml() {
        _search = (EditText) findViewById(R.id.searchText);
        _itemslist = (ListView) findViewById(R.id.itemslist);
        _header = (TextView) findViewById(R.id.header);
        _clear = (ImageButton) findViewById(R.id.clear);
    }

    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.open_tabs));
        _search.setHint(getResources().getString(R.string.chapters_name));
        _clear.setVisibility(View.INVISIBLE);
    }

    private void printItemList(String search) {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Open tabs").child(cid);
        DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                uids = new ArrayList<>();
                names = new ArrayList<>();
                prinresUids = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    uids.add(ds.getKey());
                }
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NotNull DataSnapshot snapshot) {
                        for (int i = 0; i < uids.size(); i++){
                            UserObj user = snapshot.child(uids.get(i)).getValue(UserObj.class);
                            String name = user.getFirstName() + " " + user.getLastName();
                            if (name.contains(search))
                            names.add(name);
                            prinresUids.add(uids.get(i));
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter(TabsList.this, R.layout.layout_item_list, names);
                        _itemslist.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setClicks(){
        _clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _search.setText("");
            }
        });

        _itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TabsList.this, UserTabs.class);
                intent.putExtra("cid", cid);
                intent.putExtra("uid", prinresUids.get(position));
                startActivity(intent);
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
