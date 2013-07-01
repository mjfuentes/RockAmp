package com.mjfuentes.rockamp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Matias_2 on 06/06/13.
 */
public class PlayActivity extends Activity {
    private int songId;
    private int albumId;
    private int artistId;
    private Bitmap albumImage;
    private MusicItem song;
    private Handler handler;
    private int songIndex;
    private ProgressBar progressBar;
    private MusicItem[] playList;
    private MediaPlayer player;
    private boolean playing;
    private boolean allSongs;
    private MusicService mService;
    private boolean mBound = false;
    private PlayingInfo updatedInfo;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mService = binder.getService();
            serviceController.setMusicService(mService);
            mService.StartPlaying(artistId,albumId,songId,allSongs);
            updateData();
            mBound = true;
            playing = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowplaying);
        Intent i = getIntent();
        artistId = i.getIntExtra("artist",0);
        albumId = i.getIntExtra("album",0);
        songId = i.getIntExtra("song", 0);
        allSongs = i.getBooleanExtra("all",false);
        configureButtons();

        if ((serviceController.getMusicService()==null) && (artistId != 0))
        {
            Intent intent  = new Intent(this,MusicService.class);
            bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        }
        else if (serviceController.getMusicService()!=null)
        {
            if (artistId!=0)
            {
                mService = serviceController.getMusicService();
                mService.StartPlaying(artistId,albumId,songId,allSongs);
                updateData();
                mBound = true;
                playing = true;
            }
            else
            {
                mService = serviceController.getMusicService();
                updateData();
            }
        }
    }

    public void updateData()
    {
        updatedInfo =  mService.getData();
        ((TextView)findViewById(R.id.artist)).setText(updatedInfo.artist);
        /*((TextView)findViewById(R.id.album)).setText(song.album_name);*/
        ((TextView)findViewById(R.id.song)).setText(updatedInfo.song);
        ((TextView)findViewById(R.id.index)).setText((updatedInfo.index+1) + "/" + updatedInfo.total);
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(updatedInfo.image);
            albumImage = BitmapFactory.decodeStream(is);
            if (is != null) {
                is.close();
            }
            if (albumImage != null) ((ImageView)findViewById(R.id.imageView)).setImageBitmap(albumImage);
        }
        catch (IOException e) {
        //No Image for Album
    }
    }

/*    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_BACK:
                player.release();
                super.onBackPressed();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }*/

    public void configureButtons()
    {
        Button play = (Button) findViewById(R.id.button3);
        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing)
                {
                    mService.pause();
                    playing = false;
                }
                else { mService.start();
                    playing = true;}
            }
        });

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updatedInfo.index < updatedInfo.total-1)
                {
                    songIndex++;
                    mService.nextSong();
                    updateData();
                }
            }
        });

        Button last = (Button) findViewById(R.id.last);
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updatedInfo.index > 0)
                {
                    songIndex--;
                    mService.previousSong();
                    updateData();
                }
            }
        });
    }
}