package com.example.yugenshtil.torontoparkguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowAll extends AppCompatActivity {

        DatabaseHelper myDb;
        SQLiteDatabase db;

    public void tr(String s){

        String res="";
        if (s.isEmpty())
            res = "SELECT rowid _id, name FROM Parks_table order by Name";
        else {
            res = "SELECT rowid _id, name FROM Parks_table where name like \'%" + s + "%\' order by Name";

        }
        Log.d("Daa"," "+res);


        Cursor curs = db.rawQuery(res, null);
        final CAdapter adapt = new CAdapter(this, curs);
        ListView lv = (ListView) findViewById(R.id.showParks);
        SearchView sv = (SearchView) findViewById(R.id.searchView);
        // Binds cursor with adapter/ListView
        lv.setAdapter(adapt);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all);

        myDb = new DatabaseHelper(this);
        db = myDb.getWritableDatabase();
       final Cursor curs = db.rawQuery("SELECT rowid _id, name FROM Parks_table order by name", null);
        CAdapter adapt = new CAdapter(this, curs);
        ListView lv = (ListView) findViewById(R.id.showParks);
        SearchView sv = (SearchView) findViewById(R.id.searchView);
        // Binds cursor with adapter/ListView
        lv.setAdapter(adapt);




        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {

             tr(text);
               // adapt.getFilter().filter(text);
                return false;
            }
        });
        // Defines which park form the list was chosen and runs the query to retrieve all information about the park
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Toast toa = Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT);
                //String displayedText = getApplicationContext().getResources().getString(R.string.toa);
                String displayedText1 = ((TextView) ((LinearLayout) toa.getView()).getChildAt(0)).getText().toString();
                String s = "Hello";
                Intent intent = new Intent(view.getContext(), Details.class);
                // String name = (TextView)findViewById(R.id.getText().toString());
                //String[] ar = {"Hello","Oleg"};
                String query = "SELECT * from Parks_table where name = \"" + displayedText1 + "\"";
                Cursor curs = db.rawQuery(query, null);
                curs.moveToFirst();
                String name = curs.getString(curs.getColumnIndexOrThrow("NAME"));
                String address = curs.getString(curs.getColumnIndexOrThrow("LOCATION"));
                String PostalCode = curs.getString(curs.getColumnIndexOrThrow("POSTALCODE"));
                String Facilities = curs.getString(curs.getColumnIndexOrThrow("FACILITIES"));


                curs.close();

                List<String> test = new ArrayList<String>();
                test.add(name);
                test.add(address);
                test.add(PostalCode);
                test.add(Facilities);

                intent.putStringArrayListExtra("test", (ArrayList<String>) test);

                startActivityForResult(intent, 0);


                // https://www.youtube.com/watch?v=q6-4E1JGT_k 1:15 min

                //   intent.putExtra("try", s);
                //   String[] colourCodes;
                //  colourCodes = getResources().getStringArray(R.array.listValues);
                //   String selection = colourCodes[position];
                //    lc = selection;

            }
        });


    }


    // http://developer.android.com/training/search/setup.html

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.options_menu, menu);
            System.out.println("Elliot Click");
            return true;

    }


// Set search menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            View view;
            view = this.findViewById(android.R.id.content);

            Intent intent = new Intent(view.getContext(), Search.class);
            startActivityForResult(intent, 0);
            System.out.println("Elliot Click1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
