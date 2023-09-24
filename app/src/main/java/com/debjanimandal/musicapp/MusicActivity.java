package com.debjanimandal.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {

    private ImageButton buttonplaypause,buttonnext,buttonprevious;
    private TextView textViewFileNameMusic,textViewprogress, textViewTotalTime;
    private SeekBar seekBarVolume, seekBarMusic;
    String title,filepath;
    int position;
    ArrayList<String> list;

    private MediaPlayer mediaPlayer;
    Runnable runnable;
    Handler handler;
    int totalTime;
    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        buttonplaypause=findViewById(R.id.buttonpauseplay);
        buttonnext=findViewById(R.id.buttonNext);
        buttonprevious=findViewById(R.id.buttonPrevious);
        textViewprogress=findViewById(R.id.textViewProgress);
        textViewTotalTime=findViewById(R.id.textViewTotalTime);
        textViewFileNameMusic=findViewById(R.id.textViewFileNameMusic);
        seekBarMusic=findViewById(R.id.musicSeekBar);
        seekBarVolume=findViewById(R.id.voulumeSeekBar);

        animation= AnimationUtils.loadAnimation(MusicActivity.this,R.anim.translate_animation);
        textViewFileNameMusic.setAnimation(animation);

        title=getIntent().getStringExtra("title14");
        filepath=getIntent().getStringExtra("filepath14");
        position=getIntent().getIntExtra("position14",0);
        list=getIntent().getStringArrayListExtra("list14");

        textViewFileNameMusic.setText(title);
        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filepath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        buttonplaypause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    buttonplaypause.setBackgroundResource(R.drawable.play);
                }
                else
                {
                    mediaPlayer.start();
                    buttonplaypause.setBackgroundResource(R.drawable.pause);
                }

            }
        });

        buttonnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.reset();
                if(position== list.size()-1)
                {
                    position=0;
                }
                else {
                    position++;
                }
                String newfilepath=list.get(position);
                try {
                    mediaPlayer.setDataSource(newfilepath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    buttonplaypause.setBackgroundResource(R.drawable.pause);
                    String newTitle=newfilepath.substring(newfilepath.lastIndexOf("/")+1);
                    textViewFileNameMusic.setText(newTitle);
                    textViewFileNameMusic.clearAnimation();
                    textViewFileNameMusic.setAnimation(animation);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonprevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.reset();
                if(position==0)
                {
                    position=list.size()-1;
                }
                else {
                    position--;
                }
                String newfilepath=list.get(position);
                try {
                    mediaPlayer.setDataSource(newfilepath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    buttonplaypause.setBackgroundResource(R.drawable.pause);
                    String newTitle=newfilepath.substring(newfilepath.lastIndexOf("/")+1);
                    textViewFileNameMusic.setText(newTitle);
                    textViewFileNameMusic.clearAnimation();
                    textViewFileNameMusic.setAnimation(animation);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b)
                {
                    seekBarVolume.setProgress(i);
                    float volumelevel1=i/100f;
                    mediaPlayer.setVolume(volumelevel1,volumelevel1);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b)
                {
                    mediaPlayer.seekTo(i);
                    seekBarMusic.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                totalTime=mediaPlayer.getDuration();
                seekBarMusic.setMax(totalTime);

                int currentPosition=mediaPlayer.getCurrentPosition();
                seekBarMusic.setProgress(currentPosition);
                handler.postDelayed(runnable,1000);

                String elapsedTime=createTimeLapse(currentPosition);
                String lastTime=createTimeLapse(totalTime);
                textViewprogress.setText(elapsedTime);
                textViewTotalTime.setText(lastTime);

                if(elapsedTime.equals(totalTime))
                {
                    mediaPlayer.reset();
                    if(position== list.size()-1)
                    {
                        position=0;
                    }
                    else {
                        position++;
                    }
                    String newfilepath=list.get(position);
                    try {
                        mediaPlayer.setDataSource(newfilepath);
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        buttonplaypause.setBackgroundResource(R.drawable.pause);
                        String newTitle=newfilepath.substring(newfilepath.lastIndexOf("/")+1);
                        textViewFileNameMusic.setText(newTitle);
                        textViewFileNameMusic.clearAnimation();
                        textViewFileNameMusic.setAnimation(animation);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        handler.post(runnable);
    }
    public String createTimeLapse(int currentPosition)
    {
        //1 min=60sec
        //1 sec=1000 millisec

        String timeLable;
        int minute,second;
        minute=currentPosition/1000/60;
        second=currentPosition/1000%60;
        if(second<10)
        {
            timeLable=minute+":0"+second;
        }
        else {
            timeLable=minute+":"+second;
        }
        return timeLable;
    }
}