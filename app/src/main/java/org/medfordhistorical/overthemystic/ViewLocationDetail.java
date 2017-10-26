package org.medfordhistorical.overthemystic;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

public class ViewLocationDetail extends AppCompatActivity {
    private FloatingActionButton btn;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);

        Intent intent = getIntent();

        String name = intent.getStringExtra("siteName");
        String desc = intent.getStringExtra("siteDesc");
        String imageUrl = intent.getStringExtra("imageUrl");
        String audioUrl = intent.getStringExtra("audioUrl");

        setTitle(name);

        ImageView imageView = (ImageView) findViewById(R.id.locationImage);

        Glide.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);

        description = (TextView) findViewById(R.id.locationDescription);
        description.setText(desc);

        btn = (FloatingActionButton) findViewById(R.id.play);
        progressDialog = new ProgressDialog(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog.setMessage("Buffering...");
        progressDialog.show();

        try {
            mediaPlayer.setDataSource(audioUrl);
        } catch (IOException e) {
            Log.e("StartTour", "create failed");
        }

        try {
            mediaPlayer.prepare();
            progressDialog.cancel();
        } catch (IllegalStateException e) {
            Log.e("StartTour", "prepare failed");
        } catch (IOException e) {
            Log.e("StartTour", "prepare failed");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    btn.setImageResource(R.drawable.ic_action_play);
                    mediaPlayer.pause();
                } else {
                    btn.setImageResource(R.drawable.ic_action_pause);
                    mediaPlayer.start();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}