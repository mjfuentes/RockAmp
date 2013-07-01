package com.mjfuentes.rockamp;

import android.net.Uri;

/**
 * Created by Matias_2 on 06/06/13.
 */
public class Item {
    public int id;
    public String name;
    public Uri image;
    public Item(int num, String nam)
    {
        id = num;
        name = nam;
    }
    public Item(int num, String nam,Uri img)
    {
        id = num;
        name = nam;
        image = img;
    }
}
