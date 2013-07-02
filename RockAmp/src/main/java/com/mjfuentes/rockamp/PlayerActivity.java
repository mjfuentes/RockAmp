package com.mjfuentes.rockamp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

/**
 * Created by Matias_2 on 01/07/13.
 */
public class PlayerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
       updateData();
       super.onResume();

    }

    private void updateData()
    {
        TextView artistName = (TextView) findViewById(R.id.artistName);
        TextView songName = (TextView) findViewById(R.id.songName);
        ImageView albumImage = (ImageView) findViewById(R.id.albumCover);
        PlayingInfo info = MusicController.getCurrentData();
        if (info!=null)
        {
            artistName.setText(info.artist);
            songName.setText(info.song);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), info.image);
                if (bitmap != null)
                {
                    Bitmap img =  Bitmap.createScaledBitmap(bitmap,80,80,true);
                    bitmap.recycle();
                    albumImage.setImageBitmap(img);
                }
                else albumImage.setImageResource(R.drawable.generic_album_micro);
            } catch (IOException e) {
                e.printStackTrace();
                albumImage.setImageResource(R.drawable.generic_album_micro);
            }
        }
    }

    public void loadButtons()
    {
        Button previous = (Button)findViewById(R.id.previous_song);
        Button play = (Button)findViewById(R.id.play_pause);
        Button next = (Button)findViewById(R.id.next_song);
        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicController.isPlaying())
                {
                    MusicController.pause();
                    updateData();
                }
                else {
                    MusicController.play();
                    updateData();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicController.next(); updateData();
            }

        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicController.previous(); updateData();
            }
        });

        RelativeLayout bar = (RelativeLayout)findViewById(R.id.bar);
        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlayerActivity.this,PlayActivity.class);
                startActivity(i);
            }
        });
    }
}

class ProgressTask extends AsyncTask
{

    @Override
    protected Object doInBackground(Object[] params) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }
}