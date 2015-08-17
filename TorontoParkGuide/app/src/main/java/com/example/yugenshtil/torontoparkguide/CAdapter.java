package com.example.yugenshtil.torontoparkguide;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * Created by yugenshtil on 15/08/15.
 */
public class CAdapter extends CursorAdapter {
    public CAdapter(Context context, Cursor cursor) {
        super(context, cursor,0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.showlist, parent,false);
    }

    // The method binds the cursor with the List View
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.lv);
        String body = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
// TextView email = (TextView) view.findViewById(R.id.tvemail);
// String em = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        name.setText(body);
    }
}