package com.example.psuplays;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Timer;

public class StudentLiveGamesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_student_live_games, container, false);

        final VideoView videoView = (VideoView) root.findViewById(R.id.vviewLiveGame);
        final MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);

        final Uri video = Uri.parse(AppConfig.PLAYBACK_URL);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(video);

        Student_Dashboard.timer = new Timer();
        return root;
    }

    @Override
    public void onPause() {
        Log.e("debug","on pause called");
        Student_Dashboard.timer.cancel();
        Student_Dashboard.timer.purge();
        super.onPause();
    }

    @Override
    public void onResume() {
        Student_Dashboard.timer = new Timer();
        ((Student_Dashboard)getActivity()).refreshScore();
        super.onResume();
    }
}