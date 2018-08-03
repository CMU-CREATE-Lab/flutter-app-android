package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Steve on 5/20/2016.
 *
 * FlutterAudioPlayer
 *
 * A helper class to simplify playing audio clips. (Modified from MFM audio player a bit)
 */
public class FlutterAudioPlayer implements MediaPlayer.OnCompletionListener {
    private Context appContext;
    public MediaPlayer mediaPlayer;
    private ConcurrentLinkedQueue<Integer> fileIds;
    private static FlutterAudioPlayer classInstance;
    private static final String VOICE_PROMPTS_ACTIVATED_KEY = "voice_prompts_activated";


    private void playNext() throws IOException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);

        if (preferences.getBoolean(VOICE_PROMPTS_ACTIVATED_KEY, false)) {
            Uri uri = Uri.parse("android.resource://" + appContext.getPackageName() + "/" + fileIds.poll());
            mediaPlayer.reset();
            mediaPlayer.setDataSource(appContext, uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }


    private FlutterAudioPlayer(Context context) {
        fileIds = new ConcurrentLinkedQueue<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        appContext = context;
    }


    public static FlutterAudioPlayer getInstance(Context context) {
        if (classInstance == null) {
            classInstance = new FlutterAudioPlayer(context);
        }
        return classInstance;
    }


    public void addAudio(Integer fileId) {
        fileIds.add(fileId);
    }


    public void playAudio() {
        classInstance.internalStop();
        if (!fileIds.isEmpty() && !mediaPlayer.isPlaying()) {
            try {
                classInstance.playNext();
            }
            catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in playAudio - FlutterAudioPlayer.");
            }
        }
    }


    public void stop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            fileIds.clear();
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    private void internalStop() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }



    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (!fileIds.isEmpty()) {
            try {
                playNext();
            }
            catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in onCompletion - FlutterAudioPlayer.");
            }
        }
    }
}