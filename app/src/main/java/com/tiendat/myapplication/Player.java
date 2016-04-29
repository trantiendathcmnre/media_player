package com.tiendat.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    int position;
    Uri u;
    Thread updateSeekBar;

    SeekBar sb;
    ImageButton btPlay, btFF, btFB, btNxt, btPv;
    ImageView imglaucher;
    RelativeLayout mh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mh = (RelativeLayout)findViewById(R.id.manHinh);
        mh.setBackgroundResource(R.drawable.nen1);

        imglaucher = (ImageView) findViewById(R.id.imageViewlaucher);
        final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setDuration(60000);
        rotateAnimation.setFillAfter(true);
        imglaucher.startAnimation(rotateAnimation);
        rotateAnimation.setRepeatCount ( 100 );
        rotateAnimation.setRepeatMode ( 100 );



        btPlay = (ImageButton) findViewById(R.id.btPlay);
        btFF = (ImageButton) findViewById(R.id.btFF);
        btFB = (ImageButton) findViewById(R.id.btFB);
        btNxt = (ImageButton) findViewById(R.id.btNxt);
        btPv = (ImageButton) findViewById(R.id.btPv);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        sb = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread(){
            @Override
            public void run(){
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                while(currentPosition < totalDuration){
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //super.run();
            }
        };
        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        mySongs = (ArrayList) b.getParcelableArrayList("songlist");
        position = b.getInt("pos", 0);

        u = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        sb.setMax(mp.getDuration());

        updateSeekBar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. The action bar vill
        //automatically handle clicks on the Home/Up button,so long
        //as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //no inspection Simplifiable If Statement
        if (id == R.id.action_settings) {
            return true;
        }

        return  super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btPlay:
                if(mp.isPlaying()){
                    mp.pause();
                } else mp.start();
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position+1)%mySongs.size();
                u = Uri.parse( mySongs.get(position).toString() ) ;
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position = (position-1<0)?mySongs.size()-1: position-1;
                /*if(position-1 <0){
                    position = mySongs.size()-1;
                }
                else{
                    position = position-1;
                }*/
                u = Uri.parse( mySongs.get(position).toString() ) ;
                mp = MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;


        }
    }
}
