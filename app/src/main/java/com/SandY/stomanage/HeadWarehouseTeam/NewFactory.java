package com.SandY.stomanage.HeadWarehouseTeam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.SandY.stomanage.Adapters.AdapterTextSubText;
import com.SandY.stomanage.GlobalConstants;
import com.SandY.stomanage.R;
import com.SandY.stomanage.dataObject.DateObj;
import com.SandY.stomanage.dataObject.FactoryObj;
import com.SandY.stomanage.dataObject.OrderObj;
import com.SandY.stomanage.dataObject.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class NewFactory extends AppCompatActivity {

    EditText _name;
    DatePicker _date;
    TextView _header;
    ImageButton _new;

    String cid;

    ArrayList<String> classes, switches, keys;

    DateObj date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        Intent intent = getIntent();
        cid = intent.getStringExtra("cid");

        attachFromXml();
        modifyActivity();
        setClicks();

    }

    private void attachFromXml() {
        _new = findViewById(R.id.createNew);
        _name = findViewById(R.id.name);
        _date = findViewById(R.id.date);
        _header = findViewById(R.id.header);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void modifyActivity(){
        _header.setText(getResources().getString(R.string.new_factory));
        _name.setHint(getResources().getString(R.string.Factory_name));

        LocalDate date = LocalDate.now().plusDays(1);
        _date.updateDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setClicks(){
        _date.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                LocalDate now = LocalDate.now();
                LocalDate selected = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                if (selected.isAfter(now));
                else{
                    Toast.makeText(NewFactory.this, getResources().getString(R.string.invalid_date), Toast.LENGTH_LONG).show();
                    LocalDate dateToSet = now.plusDays(1);
                    _date.updateDate(dateToSet.getYear(), dateToSet.getMonthValue() - 1, dateToSet.getDayOfMonth());
                }
            }
        });

        _new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_name.getText().toString().trim().isEmpty()){
                    _name.setError(getResources().getString(R.string.name_error));
                    _name.requestFocus();
                    return;
                }
                date = new DateObj( _date.getDayOfMonth(), (_date.getMonth() + 1), _date.getYear());

                setContentView(R.layout.template_activity_listview_add_search);

                TextView header = findViewById(R.id.header);
                header.setText(getResources().getString(R.string.select_class));
                findViewById(R.id.linearLayout).setVisibility(View.GONE);
                ListView item = findViewById(R.id.itemslist);

                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Classes").child(cid);
                DBRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        classes = new ArrayList<>();
                        switches = new ArrayList<>();
                        keys = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            keys.add(ds.getKey());
                            classes.add(ds.child("_name").getValue(String.class) + " - " + ds.child("_ageGroup").getValue(String.class));
                            switches.add("not selected");
                        }

                        AdapterTextSubText adapter = new AdapterTextSubText(NewFactory.this, classes, switches);
                        item.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }

                });
                item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (switches.get(position).equals("not selected")) switches.set(position, "selected");
                        else switches.set(position, "not selected");
                        AdapterTextSubText adapter = new AdapterTextSubText(NewFactory.this, classes, switches);
                        item.setAdapter(adapter);
                    }
                });

                findViewById(R.id.createNew).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProgressDialog dialog = ProgressDialog.show(NewFactory.this, "", "Loading. Please wait...", true);
                        dialog.show();
                        for (String s : switches){
                            if (s.equals("selected")){
                                HashMap<String, String> _classes = new HashMap<>();
                                for(int i = 0; i < keys.size(); i++){
                                    _classes.put(keys.get(i), switches.get(i));
                                }

                                FactoryObj factory = new FactoryObj(_name.getText().toString().trim(), date, _classes);

                                HashMap<String, String> orders = new HashMap<>();

                                DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference().child("Users");
                                DBRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            UserObj user = ds.getValue(UserObj.class);
                                            if(user.getUserPerm().equals(GlobalConstants.Perm.מדריך.toString()) && user.getCid().equals(cid)){
                                                if(factory.get_classes().get(user.getResponsibility()).equals("selected")){
                                                    OrderObj order = new OrderObj(factory.get_name(), factory.get_date());
                                                    String orderKey = order.WriteToDB(user.getCid(), ds.getKey());
                                                    orders.put(ds.getKey(), orderKey);
                                                }
                                            }
                                        }
                                        factory.set_order(orders);
                                        factory.WriteToDB(cid);
                                        dialog.dismiss();
                                        finish();
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                    }
                                });
                                return;
                            }
                        }
                        dialog.dismiss();
                        Toast.makeText(NewFactory.this, getResources().getString(R.string.not_selected_class), Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
    }
}
