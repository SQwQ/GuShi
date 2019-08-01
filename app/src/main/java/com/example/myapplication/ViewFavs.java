package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;

public class ViewFavs extends AppCompatActivity {
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView analysis = findViewById(R.id.analysis);
        analysis.setMovementMethod(new ScrollingMovementMethod());

        TextView poem = findViewById(R.id.poem);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String poem_body = intent.getStringExtra("poem");



        poem.setText(poem_body);
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();




        FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back_to_check_favs);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
                Intent intent = new Intent(getApplicationContext(), CheckFavs.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            AnalysisGetter analysisGetter = new AnalysisGetter(id);
            return analysisGetter.getAnalysis('a');

        }

        @Override
        protected void onPostExecute(String input) {
            TextView analysis = findViewById(R.id.analysis);
            analysis.setText(input);

        }
    }
}
