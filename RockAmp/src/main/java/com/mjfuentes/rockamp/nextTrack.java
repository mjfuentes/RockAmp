package com.mjfuentes.rockamp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Matias_2 on 28/06/13.
 */
public class nextTrack extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("nextTrack"))
        {
            Bundle extras = intent.getExtras();
            Boolean next  = extras.getBoolean("next");
        }
    }
}
