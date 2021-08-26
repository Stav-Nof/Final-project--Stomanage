package com.SandY.stomanage.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.SandY.stomanage.R;
import java.util.List;

public class AdapterTextX3 extends ArrayAdapter {
    private List<String> column1;
    private List<String> column2;
    private List<String> column3;
    private Activity context;

    public AdapterTextX3(Activity context, List<String> column1,  List<String> column2,  List<String> column3) {
        super(context, R.layout.layout_three_text, column1);
        this.context = context;
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null)
            row = inflater.inflate(R.layout.layout_three_text, null, true);
        TextView textView1 = (TextView) row.findViewById(R.id.text1);
        TextView textView2 = (TextView) row.findViewById(R.id.text2);
        TextView textView3 = (TextView) row.findViewById(R.id.text3);

        textView1.setText(column1.get(position));
        textView2.setText(column2.get(position));
        textView3.setText(column3.get(position));

        return row;

    }

}