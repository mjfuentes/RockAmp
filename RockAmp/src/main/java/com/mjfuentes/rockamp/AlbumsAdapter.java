package com.mjfuentes.rockamp;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matias_2 on 06/06/13.
 */
public class AlbumsAdapter extends BaseAdapter{
    private Context context;
    private Item[] values;
    private int auxData;
    private List<MusicItem> items;

    public AlbumsAdapter(Context ctx, String option, int aux)
    {
        context = ctx;
        auxData = aux;
        loadData(option);

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


        RelativeLayout rv;
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        rv =  (RelativeLayout) layoutInflater.inflate(R.layout.album_layout, parent,false);
        ImageView iv = (ImageView) rv.findViewById(R.id.imageView);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), values[position].image);
            if (bitmap != null)
            {
            Bitmap img =  Bitmap.createScaledBitmap(bitmap,160,160,true);
            bitmap.recycle();
            iv.setImageBitmap(img);
            }
            else iv.setImageResource(R.drawable.disc);

        }
        catch (Exception e)
        {
            //No image for album
            iv.setImageResource(R.drawable.disc);
        }
        TextView tv = (TextView) rv.findViewById(R.id.textView);
        tv.setText(values[position].name);
        return rv;
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
                if (auxData != 0)
                {
                    ArrayList<MusicItem> temp = new ArrayList<MusicItem>();
                    for (MusicItem i:items)
                    {
                        if (i.artist_id == auxData)
                        {
                            temp.add(i);
                        }
                    }
                    items = temp;
                }
            }
            int temp = 0;
            ArrayList<Item> tempList = new ArrayList<Item>();
            for (MusicItem i:items)
            {
                if (i.album_id != temp)
                {
                    tempList.add(new Item(i.album_id,i.album_name,i.image_uri));
                    temp = i.album_id;
                }
            }
            values = tempList.toArray(new Item[tempList.size()]);
        }



    }

