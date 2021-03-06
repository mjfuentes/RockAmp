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


public class MainActivity extends PlayerActivity {
    GridAdapter adapter;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        loadButtons();
        adapter  = new GridAdapter(this);
        GridView gv = (GridView) findViewById(R.id.gridView);
        gv.setAdapter(adapter);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nue = null;
                if (position == 1)
                {
                    nue = new Intent(MainActivity.this, ArtistsActivity.class);
                    startActivity(nue);
                }
                if (position == 2 )
                {
                    nue = new Intent(MainActivity.this,AlbumsActivity.class);
                    startActivity(nue);
                }

                if (position == 3)
                {
                    nue = new Intent(MainActivity.this,SongsActivity.class);
                    startActivity(nue);
                }
                if (position== 0)
                {
                    nue = new Intent(MainActivity.this,PlayActivity.class);
                    startActivity(nue);
                }
            }
        });
    }

}

