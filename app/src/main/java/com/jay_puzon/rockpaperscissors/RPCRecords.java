package com.jay_puzon.rockpaperscissors;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.R.layout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RPCRecords extends ListActivity {
    RPCSQLiteDB db = new RPCSQLiteDB(this);
    ArrayList<String> hst;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the records
        hst = db.GetRecords();

        if (hst.size() > 0) {
            // show the records
            setListAdapter(new ArrayAdapter<>(this, layout.simple_list_item_1, hst));
        } else {
            Toast.makeText(this, "No Records Found!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}
