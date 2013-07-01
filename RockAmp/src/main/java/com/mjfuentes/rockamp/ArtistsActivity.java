package com.mjfuentes.rockamp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Matias_2 on 06/06/13.
 */
public class ArtistsActivity extends Activity {
    ArtistsAdapter adapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);
        adapter = new ArtistsAdapter(this,"artists",0);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item aux = (Item) adapter.getItem(position);
                Intent i = new Intent(ArtistsActivity.this,AlbumsActivity.class);
                i.putExtra("art",aux.id);
                i.putExtra("artName",aux.name);
                i.putExtra("Artist",aux.id);
                startActivity(i);
            }
        });
    }
}