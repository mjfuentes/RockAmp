package com.mjfuentes.rockamp;

/**
 * Created by Matias_2 on 28/06/13.
 */
public class MusicController {
    private static MusicService instance;
    private static PlayActivity activity;
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

    public static int getPosition()
    {
        if (instance != null)
        {
            return instance.player.getCurrentPosition();
        }
        return 0;
    }

    public static void setActivity(PlayActivity act)
    {
        activity = act;
    }

    public static PlayActivity getActivity()
    {
        return activity;
    }

    public static int getDuration()
    {
        if (instance !=null)
        {
            return instance.player.getDuration();
        }
        return 0;
    }

    public static void getPosition(int pos)
    {
        if (instance != null)
        {
            instance.player.seekTo(pos);
        }
    }
}
