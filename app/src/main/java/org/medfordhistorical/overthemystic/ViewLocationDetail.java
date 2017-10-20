package org.medfordhistorical.overthemystic;

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
    private Button btn;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private String url = "http://174.138.43.181/directus/storage/uploads/00000000004.mp3";
    private TextView description;
    private String text = "Ballou Hall, named for Tufts' first president Hosea Ballou, was the first building to be erected for the new Tufts College. The cornerstone was laid in 1853 and construction was completed the following year. Designed by Boston architect Gridley James Fox Bryant (1816-1899), Ballou Hall was initially referred to as simply College Building or College Hall. It housed classrooms, the library, laboratories, a museum, administrative and faculty offices, student living quarters, and a chapel, now the Coolidge Room.\nImage and text courtesy of the Tufts Digital Collections and Archives.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_detail);

        ImageView imageView = (ImageView) findViewById(R.id.locationImage);

        Glide.with(imageView.getContext())
                .load("http://174.138.43.181/directus/storage/uploads/00000000003.jpg")
                .into(imageView);

        description = (TextView) findViewById(R.id.locationDescription);
        description.setText(text);

        btn = (Button) findViewById(R.id.play);
        progressDialog = new ProgressDialog(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        progressDialog.setMessage("Buffering...");
        progressDialog.show();

        try {
            mediaPlayer.setDataSource(url);
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
                    btn.setText("Pause Streaming");
                    mediaPlayer.pause();
                } else {
                    btn.setText("Start Streaming");
                    mediaPlayer.start();
                }
            }
        });
    }
}