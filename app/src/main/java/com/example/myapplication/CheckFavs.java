package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;

import java.util.LinkedList;
import java.util.List;

public class CheckFavs extends AppCompatActivity implements SearchView.OnQueryTextListener{

    DatabaseHelper sqlHelper;
    List<Integer> selectedItemsPositions = new LinkedList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_favs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        ListView lvItems = (ListView) findViewById(R.id.fav_list);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        // use your custom layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_adapter, R.id.delete, values);
        lvItems.setAdapter(adapter);
        */
        SearchView editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        sqlHelper = new DatabaseHelper(getApplicationContext());

        Cursor data = sqlHelper.getData();

        populateListView(data);

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ListView lvItems = findViewById(R.id.fav_list);
                sqlHelper = new DatabaseHelper(getApplicationContext());
                CursorAdapter adapter = (CursorAdapter) lvItems.getAdapter();
                //System.out.println(lvItems.getCount());
                //SparseBooleanArray checkedItemPositions = lvItems.getCheckedItemPositions();
                for (int i = 0; i < lvItems.getCount(); i++) {


                    //CursorAdapter cursor = (CursorAdapter) lvItems.getItemAtPosition(i);
                    //ToggleButton isDelete = lvItems.getChildAt(i).findViewById(R.id.delete);
                    //View curr = cursor.getView(i, null,lvItems);
                    View curr = adapter.getView(i, null, lvItems);
                    //TextView txt = lvItems.getChildAt(i).findViewById(R.id.poem_id);
                    ToggleButton isDelete = curr.findViewById(R.id.delete);
                    TextView txt = curr.findViewById(R.id.poem_id);
                    String id = txt.getText().toString();
                    if (isDelete.isChecked()) {
                        System.out.println("we're in");
                        sqlHelper.deletePoem(id);
                    }
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);


                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }
    /*
    private void displayListView() {


        DatabaseHelper sqlHelper = new DatabaseHelper(getApplicationContext());
        Cursor data = sqlHelper.getData();

        // The desired columns to be bound
        String[] columns = new String[]{
                "weblink",
                "title",
                "author"


        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.poemTitle,
                R.id.poemAuthor,
                R.id.poem_id
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.list_adapter,
                data,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.fav_list);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }
    */
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        sqlHelper = new DatabaseHelper(getApplicationContext());

        Cursor data = sqlHelper.search(newText);
        populateListView(data);

        return false;
    }

    private void populateListView(Cursor data) {

        ListView lvItems = (ListView) findViewById(R.id.fav_list);


        //need _id column in sql database fo Cursor Adapter to work
        CursorAdapter adapter = new CursorAdapter(getApplicationContext(), data) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
                View view = LayoutInflater.from(context).inflate(R.layout.list_adapter, viewGroup, false);
                ToggleButton delete = view.findViewById(R.id.delete);


                delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int pos = (int) buttonView.getTag();
                        if (isChecked) {
                            System.out.println("deleted!!!!!");
                            if (!selectedItemsPositions.contains(pos)) {
                                selectedItemsPositions.add(pos);
                                System.out.println("added to list" + " " + pos);
                            }
                            buttonView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.star_big_off));


                        } else {
                            System.out.println("undeleted!!!");
                            if (selectedItemsPositions.contains(pos)) {
                                selectedItemsPositions.remove((Object) pos);
                            }
                            buttonView.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.star_big_on));
                        }
                    }

                });
                return view;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView title = (TextView) view.findViewById(R.id.poemTitle);
                TextView author = (TextView) view.findViewById(R.id.poemAuthor);
                TextView poem_id = view.findViewById(R.id.poem_id);
                poem_id.setVisibility(View.GONE);
                ToggleButton delete = view.findViewById(R.id.delete);

                final int pos = cursor.getPosition();




                delete.setTag(pos);
                selectedItemsPositions.add(999);
                System.out.println(selectedItemsPositions.contains(999));



                String id = cursor.getString(cursor.getColumnIndexOrThrow("weblink"));
                String str_title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String str_author = cursor.getString(cursor.getColumnIndexOrThrow("author"));

                if (selectedItemsPositions.contains(pos)) {
                    delete.setChecked(true);
                    System.out.println(str_title + "in list" + " " + pos);

                } else {
                    delete.setChecked(false);
                    System.out.println(str_title + "not in list" + " " + pos);
                }
                poem_id.setText(id);
                title.setText(str_title);
                author.setText(str_author);

            }



        };


        lvItems.setAdapter(adapter);


        lvItems.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                Cursor clickedObj = (Cursor)parent.getItemAtPosition(position);
                System.out.println("SHUT IT DOWN!");

                Intent intent = new Intent(getApplicationContext(), ViewFavs.class);
                intent.putExtra("id", clickedObj.getString(clickedObj.getColumnIndex("weblink")));
                intent.putExtra("poem", clickedObj.getString(clickedObj.getColumnIndex("poem")));
                startActivity(intent);
                /*
                Toast.makeText(CheckFavs.this,
                        "Clicked item:\n" +
                                clickedObj.getString(clickedObj.getColumnIndex("title")) + ": " +
                                clickedObj.getString(clickedObj.getColumnIndex("poem")),
                        Toast.LENGTH_LONG).show();
                */
            }});




    }
}
