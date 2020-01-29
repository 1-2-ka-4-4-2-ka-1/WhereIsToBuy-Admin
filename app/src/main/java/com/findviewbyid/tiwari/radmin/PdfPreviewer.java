package com.findviewbyid.tiwari.radmin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PdfPreviewer extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_previewer);

        pdfView = findViewById(R.id.id_pdf_view);

        String path =this.getFilesDir().toString();
        //  String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        File file = new File(getIntent().getStringExtra("filename"));

        pdfView.fromFile((file))
                .load();

    }
}
