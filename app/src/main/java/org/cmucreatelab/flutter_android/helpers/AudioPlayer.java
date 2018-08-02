package org.cmucreatelab.flutter_android.helpers;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.cmucreatelab.flutter_android.helpers.static_classes.Constants;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Steve on 5/20/2016.
 *
 * AudioPlayer
 *
 * A helper class to simplify playing audio clips.
 *
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener {
    private Context appContext;
    private GlobalHandler globalHandler;
    public MediaPlayer mediaPlayer;
    private ConcurrentLinkedQueue<Integer> fileIds;

    private void playNext() throws IOException {
        if (globalHandler.isVoicePromptsActivated()) {
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


    private AudioPlayer(Context context) {
        fileIds = new ConcurrentLinkedQueue<>();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        appContext = context;
        globalHandler = GlobalHandler.getInstance(context);
    }


    public static AudioPlayer getInstance(Context context) {
            return new AudioPlayer(context);
    }


    public void addAudio(Integer fileId) {
        fileIds.add(fileId);
    }


    public void playAudio() {
        stop();
        if (!fileIds.isEmpty() && !mediaPlayer.isPlaying()) {
            try {
                playNext();
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in playAudio - AudioPlayer.");
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
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, "file I/O error in onCompletion - AudioPlayer.");
            }
        }
    }
}