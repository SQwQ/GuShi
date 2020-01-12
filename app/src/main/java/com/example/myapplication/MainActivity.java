package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class MainActivity extends AppCompatActivity {

    private Document htmlDocument;
    private String[] names = {"白居易","岑参","常建","陈陶","陈子昂","崔颢","崔曙","崔涂","戴叔伦","杜甫","杜牧","杜审言","杜荀鹤","高适","顾况","韩翃","韩偓","韩愈","贺知章","皇甫冉","贾岛","金昌绪","李白","李端","李频","李颀","李商隐","李益","刘长卿","刘方平","刘昚虚","刘禹锡","柳中庸","柳宗元","卢纶","骆宾王","马戴","孟浩然","孟郊","裴迪","綦毋潜","钱起","秦韬玉","丘为","权德舆","僧皎然","沈佺期","司空曙","宋之问","李隆基(唐玄宗)","王勃","王昌龄","王翰","王建","王湾","王维","王之涣","韦应物","韦庄","温庭筠","佚名","许浑","薛逢","元结","佚名","元稹","张祜","张籍","张继","张九龄","张泌","张乔","张旭","郑畋","朱庆馀","祖咏","柳永","晏几道","苏轼","周邦彦","贺铸","姜夔","吴文英","张先","晏殊","欧阳修","秦观","晁补之","陆游","范仲淹","范成大","辛弃疾","史达祖","刘克庄","刘辰翁","周密","张炎","王沂孙","李清照","张孝祥","范成大","姜夔","蒋捷","张炎","王沂孙","周密各","辛弃疾","吴文英","张先","晏殊","欧阳修","苏轼","黄庭坚","吴文英","林逋","柳永"};
    public TextView result;
    public ToggleButton star;
    DatabaseHelper sqlHelper;

    String current_id;
    String current_author;
    String current_title;
    String current_content;
    boolean hinted = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sqlHelper = new DatabaseHelper(getApplicationContext());
        result = (TextView) findViewById(R.id.poemoftheday);

        star = (ToggleButton) findViewById(R.id.star);
        star.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    System.out.println("checked!!!!!");
                    if (current_id != null) {
                        sqlHelper.addData(current_id, current_title, current_author, current_content);
                        star.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fav_on));
                    }


                } else {
                    System.out.println("unchecked!!!");
                    sqlHelper.deletePoem(current_id);
                    star.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fav_off));
                }
            }

        });

        /*
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        */

        FloatingActionButton genPoem = findViewById(R.id.generatePoem);
        genPoem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
                    jsoupAsyncTask.execute();

            }
        });
        genPoem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String info = "作者: " + current_author + "\n" + "诗名：" + current_title;
                Toast.makeText(getApplicationContext(),info,Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        FloatingActionButton getFav = findViewById(R.id.getFav);
        getFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CheckFavs.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);


                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, LinkedList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected  LinkedList<String> doInBackground(Void... params) {
            String result = "HeyHey" ;
            LinkedList<String> ids = new LinkedList<String>();
            try {

                String url = url_completer();
                System.out.println(url);
                htmlDocument = Jsoup.connect(url).get();
                Elements elems = htmlDocument.getElementsByClass("contson");
                while(elems.isEmpty()) {
                    System.out.println("It's fucking null");
                    url = url_completer();
                    System.out.println(url);
                    htmlDocument = Jsoup.connect(url).get();
                    elems = htmlDocument.getElementsByClass("contson");
                }
                for (Element elem : elems) {

                    ids.add(elem.id());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return ids;
        }

        @Override
        protected void onPostExecute(LinkedList<String> input) {

            final LinkedList<String> res = input;
            try {
                String str = res.get(0);
                Elements poem = htmlDocument.select("div[id=" + str + "]");
                current_id = str;
                current_title = poem.prev().prev().text();
                current_content = poem.text();
                result.setText(current_content);
                ToggleFav(str);
                scroll_hint();
            } catch (IndexOutOfBoundsException e) {
                Toast.makeText(MainActivity.this,
                        "There is no Internet or there are no poems available for" + current_author,
                        Toast.LENGTH_LONG).show();
            }



            result.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
                int count = 0;
                String str;
                Elements poem;
                public void onSwipeTop() {

                }
                public void onSwipeRight() {
                    if (count >= 0) {
                        count--;
                        str = res.get(count);
                        poem = htmlDocument.select("div[id=" + str + "]");
                        current_id = str;
                        current_title = poem.prev().prev().text();
                        current_content = poem.text();
                        result.setText(current_content);
                        ToggleFav(str);

                    }

                }
                public void onSwipeLeft() {
                    if (count < res.size()) {
                        count++;
                        str = res.get(count);
                        poem = htmlDocument.select("div[id=" + str + "]");
                        current_id = str;
                        current_title = poem.prev().prev().text();
                        current_content = poem.text();
                        result.setText(current_content);
                        ToggleFav(str);

                    }
                }
                public void onSwipeBottom() {
                    System.out.println("text clicked");
                    JsoupToastAsyncTask jsoupToastAsyncTask = new JsoupToastAsyncTask();
                    jsoupToastAsyncTask.execute();

                }


            });



        }

        String url_completer() {

            String url = "https://so.gushiwen.org/search.aspx?type=author&page=";
            current_author = names[(int)(Math.random()*(names.length-1))];

            return url + ((int)(Math.random()*10) + 1) +"&value=" + current_author;
        }

        void ToggleFav(String id) {
            if (sqlHelper.checkDB(id)) {
                star.setChecked(true);
                System.out.println("true star");
            } else {
                star.setChecked(false);
                star.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fav_off));
                System.out.println("false star");
            }
                //star.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.star_big_on));

                //
        }

    }

    private class JsoupToastAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            AnalysisGetter analysisGetter = new AnalysisGetter(current_title);
            return analysisGetter.getAnalysis('t');

        }

        @Override
        protected void onPostExecute(String input) {
            Toast.makeText(MainActivity.this,
                    "白话文:\n" +
                            input,
                    Toast.LENGTH_LONG).show();

        }
    }

    public void scroll_hint() {
        if (!hinted) {
            hinted = true;
            Toast.makeText(getApplicationContext(),"Swipe left and right for more. Swipe down for translation",Toast.LENGTH_SHORT).show();
        }
    }





    /*public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("ci.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            json = new String(buffer, "UTF-8");


            System.out.println("YOYOYOYOYOYO!");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }*/


}
