package com.SandY.stomanage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.SandY.stomanage.ImageSaver;
import com.SandY.stomanage.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

public class AdapterTextImage extends ArrayAdapter {
    private List<String> itemNames;
    private Activity context;
    private String path;
    private String fileType;
    private Drawable noImag;

    public AdapterTextImage(Activity context, List<String> itemNames, String path, String fileType, Drawable noImag) {
        super(context, R.layout.layout_image_text_sub_text, itemNames);
        this.context = context;
        this.itemNames = itemNames;
        this.path = path;
        this.fileType = fileType;
        this.noImag = noImag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView == null)
            row = inflater.inflate(R.layout.layout_image_text_sub_text, null, true);
        TextView textView1 = (TextView) row.findViewById(R.id.text1);
        ImageView image = (ImageView) row.findViewById(R.id.image);

        textView1.setText(itemNames.get(position));

        File directory = context.getDir(path, Context.MODE_PRIVATE);
        File pic = new File(directory,itemNames.get(position) + fileType);

        if (!pic.exists()) downloadImage(position, image);
        else {
            Bitmap bitmap = new ImageSaver(context).
                    setFileName(itemNames.get(position) + fileType).
                    setDirectoryName(path).
                    load();

            image.setImageBitmap(bitmap);
        }
        return row;

    }

    private void downloadImage(int position, ImageView image){
        StorageReference Storage = FirebaseStorage.getInstance().getReference();
        Storage = Storage.child(path).child(itemNames.get(position) + fileType);
        Storage.getBytes(1204*1204).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                new ImageSaver(context).
                        setFileName(itemNames.get(position) + fileType).
                        setDirectoryName(path).
                        save(bitmap);
                image.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image.setImageDrawable(noImag);
            }
        });
    }


}