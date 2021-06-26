package com.ducanh.appchat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.ducanh.appchat.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    String link;
    PDFView pdfView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView=findViewById(R.id.web_view);
        pdfView=findViewById(R.id.pdf_viewer);

        Intent intent=new Intent();
        intent=getIntent();
        link=intent.getStringExtra("link");
        String type=intent.getStringExtra("type");

        if (type.equals("pdf")){
            webView.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
//            pdfView.fromUri(Uri.parse(link)).load();
//            System.out.println(link+"===============");

            new RetrivePdfStream().execute(link);

        }else{
            webView.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.loadUrl(link);
        }

    }
    class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {

                // adding url
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // if url connection response code is 200 means ok the execute
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }
            // if error return null
            catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        // Here load the pdf and dismiss the dialog box
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
//            dialog.dismiss();
        }
    }
}