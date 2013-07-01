package com.mjfuentes.rockamp;

/**
 * Created by Matias_2 on 28/06/13.
 */
public class MusicController {
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

    public static String[] getPlaylist()
    {
        if (instance != null)
        {
            return instance.getData().playList;
        }
        else return null;
    }

    public static void addSong(MusicItem item)
    {
        if (instance!= null)
        {
            instance.addSong(item);
        }
    }

    public static PlayingInfo getCurrentData()
    {
        if (instance != null)
        {
            return instance.getData();
        }
        else return null;
    }

    public static boolean isPlaying()
    {
        if (instance != null)
        {
            return instance.isPlaying();
        }
        else return false;
    }

    public static void pause()
    {
        if (instance != null)
        {
            instance.pause();
        }
    }

    public static void play()
    {
        if (instance != null)
        {
            instance.play();
        }
    }

    public static void next()
    {
        if (instance != null)
        {
            instance.nextSong();
        }
    }

    public static void previous()
    {
        if (instance != null)
        {
            instance.previousSong();
        }
    }
}
