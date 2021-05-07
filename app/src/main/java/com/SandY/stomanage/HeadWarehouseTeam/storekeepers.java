package com.SandY.stomanage.HeadWarehouseTeam;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.SandY.stomanage.Adapters.AdapterTextSubTextImage;
import com.SandY.stomanage.Administrator.Troops;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class storekeepers extends AppCompatActivity {

    TextView _header;
    ImageButton _clear;
    EditText _search;
    ListView _itemslist;

    ArrayList<String> Uids;
    ArrayList<UserObj> users;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_activity_listview_search);

        attachFromXml();
        modifyActivity();
//        printItemList(_search.getText().toString());
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

//    private void printItemList(String search){
//        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference ref = DBRef.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Uids = new ArrayList<>();
//                users = new ArrayList<>();
//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    String uid = ds.getKey();
//                    UserObj user = ds.getValue(UserObj.class);
//                    if (item.contains(search)){
//                        items.add(item);
//                        subItems.add(subItem);
//                    }
//                }
//                AdapterTextSubTextImage adapter = new AdapterTextSubTextImage(Troops.this, items, subItems, "Troops", ".png", getResources().getDrawable(R.drawable.image_not_available));
//                _itemslist.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        };
//        ref.addListenerForSingleValueEvent(valueEventListener);
//    }
}
