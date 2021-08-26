package com.SandY.stomanage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tejpratapsingh.pdfcreator.activity.PDFCreatorActivity;
import com.tejpratapsingh.pdfcreator.utils.FileManager;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFBody;
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFHorizontalView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFImageView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFLineSeparatorView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

public class PDF extends PDFCreatorActivity {

    String header;
    ArrayList<String> data1, data2,data3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        header = intent.getStringExtra("header");
        data1 = intent.getStringArrayListExtra("data1");
        data2 = intent.getStringArrayListExtra("data2");
        data3 = intent.getStringArrayListExtra("data3");


        createPDF(header, new PDFUtil.PDFUtilListener() {
            @Override
            public void pdfGenerationSuccess(File savedPDFFile) {

                File copied = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + header + ".pdf");
                try {
                    com.google.common.io.Files.copy(savedPDFFile, copied);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(PDF.this, "PDF Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void pdfGenerationFailure(Exception exception) {
                Toast.makeText(PDF.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected PDFHeaderView getHeaderView() {
        PDFHeaderView headerView = new PDFHeaderView(getApplicationContext());

        PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());

        PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H1);
        SpannableString word = new SpannableString(header);
        word.setSpan(new ForegroundColorSpan(Color.BLACK), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pdfTextView.setText(word);
        pdfTextView.setLayout(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1));
        pdfTextView.getView().setGravity(Gravity.CENTER_VERTICAL);
        pdfTextView.getView().setTypeface(pdfTextView.getView().getTypeface(), Typeface.BOLD);

        horizontalView.addView(pdfTextView);

        PDFImageView imageView = new PDFImageView(getApplicationContext());
        LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(
                60,
                60, 0);
        imageView.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.logo);
        imageLayoutParam.setMargins(0, 0, 10, 0);
        imageView.setLayout(imageLayoutParam);

        horizontalView.addView(imageView);

        headerView.addView(horizontalView);

        PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
        headerView.addView(lineSeparatorView1);

        return headerView;
    }

    @Override
    protected PDFBody getBodyViews() {
        PDFBody pdfBody = new PDFBody();

        int[] widthPercent = {33, 33, 36}; // Sum should be equal to 100%
        String[] textInTable = {"1", "2", "3"};

        PDFTableView.PDFTableRowView tableHeader = new PDFTableView.PDFTableRowView(getApplicationContext());
        PDFTextView TextHeader1 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        TextHeader1.setText(data1.get(0));
        tableHeader.addToRow(TextHeader1);

        PDFTextView TextHeader2 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        TextHeader2.setText(data2.get(0));
        tableHeader.addToRow(TextHeader2);

        PDFTextView TextHeader3 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        TextHeader3.setText(data3.get(0));
        tableHeader.addToRow(TextHeader3);

        PDFTableView.PDFTableRowView tableRowView1 = new PDFTableView.PDFTableRowView(getApplicationContext());

        PDFTextView pdfTextView1FirstRow = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        pdfTextView1FirstRow.setText(data1.get(1));
        tableRowView1.addToRow(pdfTextView1FirstRow);

        PDFTextView pdfTextView2FirstRo = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        pdfTextView2FirstRo.setText(data2.get(1));
        tableRowView1.addToRow(pdfTextView2FirstRo);

        PDFTextView pdfTextView3FirstRo = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
        pdfTextView3FirstRo.setText(data3.get(1));
        tableRowView1.addToRow(pdfTextView3FirstRo);

        PDFTableView tableView = new PDFTableView(getApplicationContext(), tableHeader, tableRowView1);

        for (int i = 2; i < data1.size(); i++) {
            // Create 10 rows
            PDFTableView.PDFTableRowView tableRowView = new PDFTableView.PDFTableRowView(getApplicationContext());
            PDFTextView pdfTextView1 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView1.setText(data1.get(i));
            tableRowView.addToRow(pdfTextView1);

            PDFTextView pdfTextView2 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView2.setText(data2.get(i));
            tableRowView.addToRow(pdfTextView2);

            PDFTextView pdfTextView3 = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
            pdfTextView3.setText(data3.get(i));
            tableRowView.addToRow(pdfTextView3);

            tableView.addRow(tableRowView);
        }
        pdfBody.addView(tableView);

        return pdfBody;
    }

    @Override
    protected void onNextClicked(File savedPDFFile) {
        finish();
    }
}
