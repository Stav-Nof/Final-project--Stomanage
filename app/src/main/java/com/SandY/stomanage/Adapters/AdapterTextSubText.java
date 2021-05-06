package com.SandY.stomanage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.SandY.stomanage.ImageSaver;
import com.SandY.stomanage.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class AdapterTextSubText extends ArrayAdapter {
    private List<String> item;
    private List<String> Subitem;
    private Activity context;

    public AdapterTextSubText(Activity context, List<String> itemNames, List<String> SubitemNames) {
        super(context, R.layout.layout_image_text_sub_text, itemNames);
        this.context = context;
        this.item = itemNames;
        this.Subitem = SubitemNames;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null)
            row = inflater.inflate(R.layout.layout_two_text, null, true);
        TextView textView1 = (TextView) row.findViewById(R.id.text1);
        TextView textView2 = (TextView) row.findViewById(R.id.text2);

        textView1.setText(item.get(position));
        textView2.setText(Subitem.get(position));


        return row;

    }

}