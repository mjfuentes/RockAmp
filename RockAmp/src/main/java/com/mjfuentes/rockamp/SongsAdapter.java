package com.mjfuentes.rockamp;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matias_2 on 06/06/13.
 */
public class SongsAdapter extends BaseAdapter{
    private Context context;
    private MusicItem[] values;
    private int auxData;
    private List<MusicItem> items;

    public SongsAdapter(Context ctx, int aux)
    {
        context = ctx;
        auxData = aux;
        loadData();

    }
    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return values[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv =  new TextView(context);
        tv.setText(values[position].artist_name + " - " + values[position].song_name);
        tv.setTextSize(20);
        tv.setPadding(5,8,0,8);
        tv.setSingleLine(true);
        return tv;
    }

    private void loadData()
    {
            items = new ArrayList<MusicItem>();
            String[] STAR = { "*" };
            Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

            Cursor cursor = context.getContentResolver().query(allsongsuri, STAR, selection, null, null);

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

                        items.add(temp);


                    } while (cursor.moveToNext());

                }
                cursor.close();
            }
        values = items.toArray(new MusicItem[items.size()]);


        if (auxData != 0)
        {
            ArrayList<MusicItem> temp = new ArrayList<MusicItem>();
            for (MusicItem i:items)
            {
                if (i.album_id == auxData)
                {
                    temp.add(i);
                }
            }
            values = temp.toArray(new MusicItem[temp.size()]);
        }
    }
}
