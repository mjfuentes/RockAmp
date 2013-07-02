package com.mjfuentes.rockamp;

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
public class ArtistsAdapter extends BaseAdapter{
    private Context context;
    private Item[] values;
    private int auxData;
    private List<MusicItem> items;

    public ArtistsAdapter(Context ctx, String option, int aux)
    {
        context = ctx;
        loadData(option);
        auxData = aux;
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
        tv.setText(values[position].name);
        tv.setTextSize(20);
        tv.setPadding(5,8,0,8);
        tv.setSingleLine(true);
        return tv;
    }

    private void loadData(String option)
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
                                        .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                        temp.song_id = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media._ID));

                        temp.fullpath = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.DATA));


                        temp.album_name = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        temp.album_id = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                        temp.artist_name = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        temp.artist_id = cursor.getInt(cursor
                                .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));

                        items.add(temp);


                    } while (cursor.moveToNext());

                }
                cursor.close();
            }

            int temp = 0;
            ArrayList<Item> tempList = new ArrayList<Item>();
            for (MusicItem i:items)
            {
                if (i.artist_id != temp)
                {
                    tempList.add(new Item(i.artist_id,i.artist_name));
                    temp = i.artist_id;
                }
            }
            values = tempList.toArray(new Item[tempList.size()]);

    }
}
