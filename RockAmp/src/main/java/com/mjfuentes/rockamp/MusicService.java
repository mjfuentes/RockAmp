package com.mjfuentes.rockamp;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Matias_2 on 28/06/13.
 */

public class MusicService extends Service {

    private int songIndex;
    private boolean allSongs;
    private int songId;
    private MusicItem song;
    private MusicItem[] playList;
    private String[] playListNames;
    private int albumId;
    private int artistId;
    public MediaPlayer player;
    private final IBinder mBinder = new MusicBinder();
    private boolean playing;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        artistId = intent.getIntExtra("artist",0);
        albumId = intent.getIntExtra("album", 0);
        songId = intent.getIntExtra("song", 0);
        allSongs = intent.getBooleanExtra("all", false);
        loadPlaylist();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener( new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            if (songIndex < playList.length-1)
            {
                nextSong();
            }
            }
        });
        return 0;
    }

    public void StartPlaying(int artId,int albId,int soId, boolean all)
    {
        artistId = artId;
        albumId = albId;
        songId = soId;
        allSongs = all;
        loadPlaylist();
        playing = true;
        playSong();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }

    public void loadPlaylist()
    {
        ArrayList<String> songNames = new ArrayList<String>();
        ArrayList<MusicItem> items = new ArrayList<MusicItem>();
        String[] STAR = { "*" };
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = this.getContentResolver().query(allsongsuri, STAR, selection, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    MusicItem temp = new MusicItem();
                    temp.song_name = cursor
                            .getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
                    temp.song_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media._ID));

                    temp.fullpath = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));


                    temp.album_name = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    temp.album_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
                    temp.image_uri = ContentUris.withAppendedId(sArtworkUri, temp.album_id);

                    temp.artist_name = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    temp.artist_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));

                    if (!allSongs){
                        if ((temp.album_id == albumId) && (temp.artist_id == artistId)){

                            items.add(temp);
                            songNames.add(temp.artist_name + " - " + temp.song_name);
                        }
                    }
                    else
                    {

                        items.add(temp);
                        songNames.add(temp.artist_name + " - " + temp.song_name);
                    }
                        if (temp.song_id == songId)
                    {
                        song = temp;

                    }


                } while (cursor.moveToNext());

            }
            cursor.close();
            songIndex = items.indexOf(song);
            playList = items.toArray(new MusicItem[items.size()]);
            playListNames = songNames.toArray(new String[songNames.size()]);
        }
    }


    public void nextSong()
    {
        if (songIndex < playList.length-1){
        songIndex++;
        player.stop();
        playSong();
        }
    }

    public void pause()
    {
        if (playing)
        {
            player.pause();
            playing = false;
        }
    }

    public void previousSong()
    {
        if (songIndex>0){
        songIndex--;
        player.stop();
        playSong();
        }
    }

    public void playSong()
    {
        try {

            song = playList[songIndex];
            Uri newUri = Uri.parse(song.fullpath);
            if (player != null)
            {
            player.reset();
            }
            else player = new MediaPlayer();
            player.setDataSource(getApplicationContext(),newUri);
            player.prepare();
            if (playing)
            {
                player.start();
                playing = true;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public PlayingInfo getData()
    {
        PlayingInfo info = new PlayingInfo();
        info.artist = song.artist_name;
        info.album = song.album_name;
        info.song = song.song_name;
        info.index = songIndex;
        info.total = playList.length;
        info.image = song.image_uri;
        info.playList = playListNames;
        return info;
    }

    public class MusicBinder extends Binder
    {
        MusicService getService()
        {
            return MusicService.this;
        }
    }

    public void addSong(MusicItem item)
    {
        MusicItem[] newArray = new MusicItem[playList.length + 1];
        System.arraycopy(playList,0,newArray,0,playList.length);
        newArray[newArray.length-1] = item;
        playList = newArray;
    }

    public boolean isPlaying()
    {
        return playing;
    }

    public void play()
    {
        if (playing == false)
        {
            player.start();
            playing= true;
        }
    }

}
