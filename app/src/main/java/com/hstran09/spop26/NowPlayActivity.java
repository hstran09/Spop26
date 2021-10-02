package com.hstran09.spop26;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hstran09.spop26.common.image.CircleTransform;
import com.hstran09.spop26.model.NowPlay;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

public class NowPlayActivity extends Activity {

    static final String SAMPLE_URL = "https://www.listennotes.com/e/p/6b6d65930c5a4f71b254465871fed370/";

    TextView podcastTitle;
    TextView publisher;
    TextView episodeTitle;
    TextView episodeDescription;
    ImageView thumbnail;

    ImageButton playOrPauseBtn;
    SeekBar seekBar;

    NowPlay nowPlay;
    MediaPlayer mediaPlayer;

    Runnable runnable;
    Handler handler;

    boolean alreadyPlaying;

    private void initViews() {
        setContentView(R.layout.activity_now_play);
        playOrPauseBtn = findViewById(R.id.player_play_or_pause);
        playOrPauseBtn.setBackgroundResource(R.drawable.ic_baseline_play_circle_filled_48);
        seekBar = findViewById(R.id.player_seekbar);

        podcastTitle = findViewById(R.id.player_podcast_title);
        publisher = findViewById(R.id.player_publisher);
        episodeTitle = findViewById(R.id.player_episode_title);
        episodeDescription = findViewById(R.id.player_episode_description);
        thumbnail = findViewById(R.id.player_thumbnail);
    }

    private void setNowPlay() {
        nowPlay = new NowPlay()
                .setId(getIntent().getStringExtra("id"))
                .setAudio(getIntent().getStringExtra("audio"))
                .setDescription(getIntent().getStringExtra("description"))
                .setThumbnail(getIntent().getStringExtra("thumbnail"))
                .setTitle(getIntent().getStringExtra("episodeTitle"))
                .setPodcast(new NowPlay.Podcast()
                        .setTitle(getIntent().getStringExtra("podcastTitle"))
                        .setPublisher(getIntent().getStringExtra("publisher")));

        podcastTitle.setText(nowPlay.getPodcast().getTitle());
        publisher.setText(nowPlay.getPodcast().getPublisher());
        episodeTitle.setText(nowPlay.getTitle());
        episodeDescription.setText(nowPlay.getDescription());
        Picasso.get()
                .load(nowPlay.getThumbnail())
                .centerCrop()
                .transform(new CircleTransform(10, 0))
                .fit()
                .into(thumbnail);
    }

    void playAudio(String audioUri) {
        Uri uri = Uri.parse(audioUri);
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(NowPlayActivity.this, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mp.getDuration());
                mediaPlayer.start();
                updateSeekBar();
            }
        });

        mediaPlayer.setOnBufferingUpdateListener((mp, percent) -> {
            double ratio = percent / 100D;
            int bufferingLevel = (int) (mp.getDuration() * ratio);
            seekBar.setSecondaryProgress(bufferingLevel);
        });

        alreadyPlaying = true;
    }

    void updateSeekBar() {
        int currentPosition = 0;
        if (Objects.nonNull(mediaPlayer))
            currentPosition = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(currentPosition);
        runnable = () -> updateSeekBar();
        handler.postDelayed(runnable, 1000);

    }

    void resumeOrPauseAudio() {
        Thread thread = new Thread();
        thread.start();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playOrPauseBtn.setBackgroundResource(R.drawable.ic_baseline_play_circle_filled_48);

        }
        else {
            mediaPlayer.start();
            playOrPauseBtn.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_48);

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initViews();
        setNowPlay();

        mediaPlayer = new MediaPlayer();
        alreadyPlaying = false;
        playOrPauseBtn.setOnClickListener(v -> {
            if (alreadyPlaying) {
                resumeOrPauseAudio();
            } else {
                playOrPauseBtn.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_48);
                playAudio(nowPlay.getAudio());
            }
        });

        handler = new Handler();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}