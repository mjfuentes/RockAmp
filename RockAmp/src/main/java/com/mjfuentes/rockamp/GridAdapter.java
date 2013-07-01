package com.mjfuentes.rockamp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Matias_2 on 06/06/13.
 */
public class GridAdapter extends BaseAdapter {
    private Context context;
    private GridItem[] values;
    public GridAdapter(Context ctx)
    {
        context = ctx;
        values = new GridItem[] {
                new GridItem("Now Playing",R.drawable.play),
                new GridItem("Artists",R.drawable.artists),
                new GridItem("Albums",R.drawable.albums),
                new GridItem("Tracks",R.drawable.tracks)
                };
    };

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
        return values[position].itemImageId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rv;
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(inflater);
        rv =  (RelativeLayout) layoutInflater.inflate(R.layout.gridviewitem, parent,false);
        ImageView iv = (ImageView) rv.findViewById(R.id.imageView);
        iv.setImageResource(values[position].itemImageId);
        TextView tv = (TextView) rv.findViewById(R.id.textView);
        tv.setText(values[position].itemName);
        return rv;
    }
}
