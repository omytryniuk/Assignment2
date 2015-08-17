package com.example.yugenshtil.torontoparkguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    DatabaseHelper myDb;
    SQLiteDatabase db;
    Cursor curs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;


    }

   // Button b = (Button) findViewById(R.id.sb);
   // final View view = this.findViewById(android.R.id.content);
   // b.setOnClickListener(new View.OnClickListener() {
   //     public void onClick(View v) {


    // The method checks which checkboxes are checked
    //REFERENCE: http://stackoverflow.com/questions/18336151/how-to-check-if-android-checkbox-is-checked-within-its-onclick-method-declared
    public void receiveSearch(View v) {
        List<String> result = new ArrayList<String>();

        CheckBox c1 = (CheckBox)findViewById(R.id.cb1);
        CheckBox c2 = (CheckBox)findViewById(R.id.cb2);
        // CheckBox c3 = (CheckBox)findViewById(R.id.cb3);
        CheckBox c4 = (CheckBox)findViewById(R.id.cb4);
        CheckBox cb = (CheckBox)findViewById(R.id.cb);
        CheckBox c5 = (CheckBox)findViewById(R.id.cb5);
        CheckBox c6 = (CheckBox)findViewById(R.id.cb6);
        CheckBox c7 = (CheckBox)findViewById(R.id.cb7);
        CheckBox c8 = (CheckBox)findViewById(R.id.cb8);
        CheckBox c9 = (CheckBox)findViewById(R.id.cb9);
        CheckBox c10 = (CheckBox)findViewById(R.id.cb10);
        CheckBox c11 = (CheckBox)findViewById(R.id.room);
        CheckBox c12 = (CheckBox)findViewById(R.id.cb12);
        CheckBox c14 = (CheckBox)findViewById(R.id.cb14);


        if(c1.isChecked())
            result.add("Sport");
        if(c2.isChecked())
            result.add("Playground");
        if(cb.isChecked())
            result.add("Firepit");
        if(c4.isChecked())
            result.add("Pool");
        if(c5.isChecked())
            result.add("Gym");
        if(c6.isChecked())
            result.add("Kitchen");
        if(c7.isChecked())
            result.add("Leash");
        if(c8.isChecked())
            result.add("Diamond");
        if(c9.isChecked())
            result.add("Fitness");
        if(c10.isChecked())
            result.add("Club House");
        if(c11.isChecked())
            result.add("Room");
        if(c14.isChecked())
            result.add("Skate");
        if(c12.isChecked())
            result.add("Splash");


    // creates query
        String q = "";
        if(result.size() > 1) {
            q = "facilities like \'%"+result.get(0)+"%\' and ";
            for(int i = 1; i <= result.size()-1;i++){
                q+="facilities like \'%" + result.get(i)+"%\'";
                if(i != result.size()-1)
                    q+=" and ";

            }

        }


        if(result.size() ==1)
            q = "facilities like \'%"+result.get(0)+"%\'";

        myDb = new DatabaseHelper(this);
        db = myDb.getWritableDatabase();


        if (q.isEmpty() || q ==null){
            curs = db.rawQuery("SELECT rowid _id, name FROM Parks_table order by Name", null);
        }
        else {
            //  String s = "SELECT rowid _id, name FROM Parks_table order by Name";
            String snew = "SELECT rowid _id, name FROM Parks_table where " + q + " order by Name";
            System.out.println("NULIK " + snew);
           curs = db.rawQuery(snew,null);
            // System.out.println("NULIK" + quer.length());
            if(curs.getCount() == 0){
                Toast.makeText(Search.this,
                        "There is no matches", Toast.LENGTH_SHORT).show();
            }


        }

        CAdapter adapt = new CAdapter(this, curs);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapt);


        System.out.println("QueryO is " + result.size());

        System.out.println("QueryO is " + q);

        ///  Intent intent = new Intent(view.getContext(), Result.class);
        //  intent.putExtra("res",q);
        // startActivity(intent);


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
}
