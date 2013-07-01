package com.mjfuentes.rockamp;

/**
 * Created by Matias_2 on 28/06/13.
 */
public class serviceController {
    private static MusicService instance;
    public static MusicService getMusicService()
    {
        if (instance != null)
        {
            return instance;
        }

        else
        {
            return null;
        }
    }

    public static void setMusicService(MusicService service)
    {
        instance = service;
    }
}
