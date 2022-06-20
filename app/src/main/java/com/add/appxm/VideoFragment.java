package com.add.appxm;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;

public class VideoFragment extends Fragment {
    @Nullable
    VideoView view;
    AnimationDrawable drawables;
    String[] paths = new String[3];
    int index = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        index = 0;
        if (view == null)
            view = (VideoView) inflater.inflate(R.layout.activity_video, container, false);

        paths[0] = "android.resource://" + getActivity().getPackageName() + "/raw/xys1";
        paths[1] = "android.resource://" + getActivity().getPackageName() + "/raw/xys2";
        paths[2] = "android.resource://" + getActivity().getPackageName() + "/raw/xys3";
        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                index++;
                if (index < 3) {
                    view.setVideoPath(paths[index]);
                } else {
                    index = 0;
                }
            }
        });
        view.setVideoPath("android.resource://" + getActivity().getPackageName() + "/raw/xys1");

        return view;
    }

    @Override
    public void onDetach() {
        if (view != null) {
            view.stopPlayback();
        }
        super.onDetach();
    }
}
