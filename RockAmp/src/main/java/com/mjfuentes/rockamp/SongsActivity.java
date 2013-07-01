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
public class SongsActivity extends Activity {
    private SongsAdapter adapter;
    private boolean allSongs;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);
        Intent intent = getIntent();
        int aux = intent.getIntExtra("alb",0);
        if (aux!=0)
        {

            setTitle(intent.getStringExtra("albName"));
            allSongs = false;
        }
        else allSongs = true;
        adapter = new SongsAdapter(this,aux);
        ListView lv = (ListView) findViewById(R.id.songslistView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SongsActivity.this,PlayActivity.class);
                MusicItem item = (MusicItem)adapter.getItem(position);
                i.putExtra("artist",item.artist_id);
                i.putExtra("album",item.album_id);
                i.putExtra("song",item.song_id);
                i.putExtra("all",allSongs);
                startActivity(i);
            }
        });
    }
}