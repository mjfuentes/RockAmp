package com.mjfuentes.rockamp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
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
    private Runnable barProgress;
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
    private TextView totalDuration;
    private boolean endTask;
    private TextView currentProgress;
    ProgressUpdater updater;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mService = binder.getService();
            MusicController.setMusicService(mService);
            mService.StartPlaying(artistId,albumId,songId,allSongs);
            updateData();
            mBound = true;
            playing = true;
            final Button play = (Button) findViewById(R.id.button3);
            play.setText("Pause");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        MusicController.setActivity(this);
        endTask = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowplaying);
        Intent i = getIntent();
        artistId = i.getIntExtra("artist",0);
        albumId = i.getIntExtra("album",0);
        songId = i.getIntExtra("song", 0);
        allSongs = i.getBooleanExtra("all", false);
        totalDuration = (TextView) findViewById(R.id.total);
        currentProgress = (TextView)findViewById(R.id.position);
        configureButtons();

        if ((MusicController.getMusicService()==null) && (artistId != 0))
        {
            Intent intent  = new Intent(this,MusicService.class);
            bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        }
        else if (MusicController.getMusicService()!=null)
        {
            if (artistId!=0)
            {
                mService = MusicController.getMusicService();
                mService.StartPlaying(artistId,albumId,songId,allSongs);
                updateData();
                mBound = true;
            }
            else
            {
                mService = MusicController.getMusicService();
                updateData();
            }
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        updater = new ProgressUpdater();
    }


    public void updateData()
    {
        updatedInfo =  mService.getData();
        ((TextView)findViewById(R.id.artist)).setText(updatedInfo.artist);
        /*((TextView)findViewById(R.id.album)).setText(song.album_name);*/
        ((TextView)findViewById(R.id.song)).setText(updatedInfo.song);
        ((TextView)findViewById(R.id.index)).setText((updatedInfo.index+1) + "/" + updatedInfo.total);
        Button play = (Button)findViewById(R.id.button3);
        if (MusicController.isPlaying())
        {
            play.setText("Pause");
            playing = true;
        }
        else {
            play.setText("Play");
            playing =false;
        }
        InputStream is = null;
        ProgressUpdater updater = new ProgressUpdater();
        updater.execute();
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

    public boolean checkStatus()
    {
        if (endTask == false)
        {
            return false;
        }
        else if (endTask)
        {
            endTask = false;
            return true;
        }
        return false;
    }

    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
                builder.setItems(new CharSequence[]{"PlayList","Options"},new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
                            builder.setItems(updatedInfo.playList,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //this.playSong(which);
                                }
                            });
                            Dialog playlist = builder.create();
                            playlist.show();
                        }
                    }
                });
                Dialog options = builder.create();
                options.show();
                return true;
        }

        return super.onKeyDown(keycode, e);
    }

    public void configureButtons()
    {
        final Button play = (Button) findViewById(R.id.button3);
        play.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing)
                {
                    mService.pause();
                    playing = false;
                    play.setText("Play");
                }
                else { mService.play();
                    playing = true;
                play.setText("Pause");}
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
                    endTask = true;
                    updater.cancel(true);
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
                    endTask = true;
                    updater.cancel(true);
                    updateData();
                }
            }
        });
    }

    public void updateBar(int current, int total)
    {
        progressBar.setMax(total);
        progressBar.setProgress(current);
    }

    public void updateTimes(String actual, String total)
    {
        currentProgress.setText(actual);
        totalDuration.setText(total);
    }

}

class ProgressUpdater extends AsyncTask<PlayActivity,Integer,Object>
{
    boolean killtask = false;
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        MusicController.getActivity().updateBar(values[0],values[1]);
        int totalSecs = values[1] / 1000;
        int totalMins = totalSecs / 60;
        totalSecs =(totalSecs - (totalMins * 60));
        String totalTime;
        if (totalSecs >= 10)
        {
            totalTime =  (totalMins + ":" + totalSecs );
        }
        else totalTime =  (totalMins + ":0" + totalSecs);

        int partialSecs = values[0] / 1000;
        int partialMins = partialSecs / 60;
        partialSecs = (partialSecs - (partialMins * 60));
        String partialTime;
        if (partialSecs>=10){
            partialTime = (partialMins + ":" + partialSecs);
        }
        else partialTime = (partialMins + ":0" + partialSecs);

        MusicController.getActivity().updateTimes(partialTime,totalTime);
        killtask = MusicController.getActivity().checkStatus();
    }

    @Override
    protected Object doInBackground(PlayActivity... params) {
        int total = MusicController.getDuration();
        int actual = 0;
        publishProgress(actual,total);
        while ((actual < total) && !killtask)
        {
            try {
                Thread.sleep(100);
                actual = MusicController.getPosition();
                publishProgress(actual,total);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}