package com.mjfuentes.rockamp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Matias_2 on 06/06/13.
 */
public class AlbumsActivity extends PlayerActivity {
    AlbumsAdapter adapter;
    String artist;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        loadButtons();
        Intent intent = getIntent();
        int aux = intent.getIntExtra("art", 0);
        if (aux!=0)
        {
            artist = intent.getStringExtra("artName");
            setTitle(artist);
        }
        adapter = new AlbumsAdapter(this,"albums",aux);
        GridView lv = (GridView) findViewById(R.id.albumsGridView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item aux = (Item) adapter.getItem(position);
                Intent i = new Intent(AlbumsActivity.this,SongsActivity.class);
                i.putExtra("alb", aux.id);
                i.putExtra("albName",aux.name);
                i.putExtra("artName",artist);
                startActivity(i);
            }
        });
    }


}