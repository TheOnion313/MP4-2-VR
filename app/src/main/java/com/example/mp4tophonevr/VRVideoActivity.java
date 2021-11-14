package com.example.mp4tophonevr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;

public class VRVideoActivity extends Activity {

    private VideoView videoViewLeft, videoViewRight;
    private boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrvideo);

        Intent intent = new Intent()
                .setType("video/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri content;
        if (requestCode == 123 && resultCode == RESULT_OK) {
            content = data.getData(); //The uri with the location of the file
        } else {
            return;
        }

        Uri countdown = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.three_countdown);
        Uri countdownFrame1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.three_countdown_first_frame);


        videoViewLeft = (VideoView) findViewById(R.id.videoViewLeft);
        videoViewLeft.setVideoURI(countdownFrame1);

        videoViewRight = (VideoView) findViewById(R.id.videoViewRight);
        videoViewRight.setVideoURI(countdownFrame1);


        videoViewRight.start();
        videoViewLeft.start();

        videoViewRight.setOnCompletionListener(mp -> {
            videoViewLeft.pause();

            videoViewRight.setVideoURI(countdown);
            videoViewLeft.setVideoURI(countdown);

            videoViewRight.setOnCompletionListener(mp1 -> {
                videoViewRight.setVideoURI(content);
                videoViewLeft.setVideoURI(content);

                videoViewRight.setOnCompletionListener(mp2 -> {
                    finishAffinity();
                });

                videoViewRight.start();
                videoViewLeft.start();
            });

        });

    }

    public void pauseResume(View v) {
        if (!started) {
            videoViewRight.start();
            videoViewLeft.start();
        } else {
            videoViewRight.pause();
            videoViewLeft.pause();
        }

        started = !started;
    }

    public void pauseResume() {
        pauseResume(null);
    }


}