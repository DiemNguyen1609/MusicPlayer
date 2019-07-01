package com.example.mymusic;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {


 /*   public static ImageButton btnPlay,btnStop;
    public static TextView textViewSongTime;
    private Intent playerService;
    public static SeekBar seekBar;
    public TextView name,info,lyric;
    public Button btnLyric;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_2);

        String savedNameExtra = getIntent().getStringExtra("name");
        String savedInfoExtra = getIntent().getStringExtra("info");

        //Integer saveimgID = getIntent().getIntExtra("icon",0);


        initView(savedNameExtra,savedInfoExtra);
        btnLyric.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(lyric.getText().toString().equals(""))
                {
                    String savedLyricExtra = getIntent().getStringExtra("lyric");
                    lyric.setText(savedLyricExtra);
                    // Thiết lập scrollbars để xem toàn bộ lời baì hát
                    lyric.setMovementMethod(new ScrollingMovementMethod());

                    btnLyric.setText("Hide Lyric");
                }
                else
                {
                    btnLyric.setText("Show Lyric");
                    lyric.setText("");
                }


            }
        });
        playerService = new Intent(DetailActivity.this,PlayerInService.class);
        //playerService.putExtra("icon",saveimgID);
        //playerService.putExtra("songName",savedNameExtra);
        startService(playerService);
    }
    private void initView(String savedNameExtra,String savedInfoExtra)
    {
        btnPlay=(ImageButton) findViewById(R.id.btnPlay);
        btnStop=(ImageButton)findViewById(R.id.btnStop);
        //btnStop.setBackgroundResource(R.drawable.stop);
        seekBar=(SeekBar) findViewById(R.id.seekBar);
        textViewSongTime=(TextView)findViewById(R.id.textViewSongTime);

        btnLyric=(Button) findViewById(R.id.btnLyric);
        // các biến lần lượt nhận dữ liệu từ main view


        name = (TextView) findViewById(R.id.textView1);
        info = (TextView) findViewById(R.id.textView2);
        lyric = (TextView) findViewById(R.id.textView3);


        //Đưa dữ liệu nhận từ main view hiển thị lên detail view
        name.setText(savedNameExtra);
        info.setText(savedInfoExtra);






    }
    @Override
    protected void onDestroy()
    {
        if(!PlayerInService.mp.isPlaying())
        {
            PlayerInService.mp.stop();
            stopService(playerService);
        }else {
            btnPlay.setBackgroundResource(R.drawable.pause);
        }
        super.onDestroy();
    }
    @Override
    protected void onResume()
    {
        try{
            if(!PlayerInService.mp.isPlaying())
            {
                btnPlay.setBackgroundResource(R.drawable.play);
            }else
            {
                btnPlay.setBackgroundResource(R.drawable.pause);
            }
        }catch (Exception e)
        {
            Log.e("Exception",""+e.getMessage()+e.getStackTrace()+e.getCause());
        }
        super.onResume();
    }*/

    private SeekBar volumeBar;

    public ImageButton btnPlay;
    public ImageButton btnForward;
    public ImageButton btnBackward;
    public ImageButton btnNext;
    public ImageButton btnPrevious;
    public ImageButton btnPlaylist;
    public ImageButton btnRepeat;
    public ImageButton btnShuffle;
    public SeekBar songProgressBar;
    public TextView songTitleLabel;
    public TextView songCurrentDurationLabel;
    public TextView songTotalDurationLabel;
    public ImageButton btnVolume;
    public ImageButton btnVolumeMax;
    AudioManager audioManager;
    private boolean isFirst = false;
    // Media Player
    private MediaPlayer mp;
    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = 0;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    public static final int RUNTIME_PERMISSION_CODE = 7;
    ContentResolver contentResolver;
    Cursor cursor;
    Uri uri;
    Context context;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private Intent playerService;
    //ArrayList<HashMap<String, String>> songsListData ;

    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_2);
        // All player buttons
        btnVolume = (ImageButton) findViewById(R.id.btnVolume);
        btnVolumeMax = (ImageButton) findViewById(R.id.btnVolumeMax);
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

        // Mediaplayer
        mp = new MediaPlayer();
        //songManager = new SongManager();
        utils = new Utilities();
        context = getApplicationContext();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important

        // Getting all songs list
        //songsList = songManager.getPlayList(Environment.getExternalStorageDirectory().getAbsolutePath());
        AndroidRuntimePermission();
        GetAllMediaMp3Files();
        /**
         * Button Click event for Play list click event
         * Launches list activity which displays list of songs
         * */
        btnVolume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (volumeBar.getVisibility() == View.INVISIBLE) {
                    volumeBar.setVisibility(View.VISIBLE);
                    btnVolumeMax.setVisibility(View.VISIBLE);

                } else {
                    volumeBar.setVisibility(View.INVISIBLE);
                    btnVolumeMax.setVisibility(View.INVISIBLE);
                }
            }
        });
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //float volumeNum = progress / 100f;
                        //mp.setVolume(volumeNum, volumeNum);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                        Toast.makeText(getApplicationContext(), "Volume: " + progress, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );
        btnVolumeMax.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeBar.getProgress() + 1, 0);
                volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                Toast.makeText(getApplicationContext(), "Volume: " + volumeBar.getProgress(), Toast.LENGTH_SHORT).show();

            }
        });
        btnPlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                startActivityForResult(i, 100);
            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mp.getDuration()) {
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if (currentPosition - seekBackwardTime >= 0) {
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    mp.seekTo(0);
                }

            }
        });

        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if (currentSongIndex < (songsList.size() - 1)) {
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    playSong(0);
                    currentSongIndex = 0;
                }

            }
        });

        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (currentSongIndex > 0) {
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                } else {
                    // play last song
                    playSong(songsList.size() - 1);
                    currentSongIndex = songsList.size() - 1;
                }

            }
        });

        /**
         * Button Click event for Repeat button
         * Enables repeat flag to true
         * */
        btnRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                } else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isShuffle = false;
                    btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                }
            }
        });

        /**
         * Button Click event for Shuffle button
         * Enables shuffle flag to true
         * */
        btnShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (isShuffle) {
                    isShuffle = false;
                    Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
                    btnShuffle.setImageResource(R.drawable.btn_shuffle);
                } else {
                    // make repeat to true
                    isShuffle = true;
                    Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
                    // make shuffle to false
                    isRepeat = false;
                    btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
            }
        });

        /**
         * Button Click event for play/pause button
         *
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (isFirst) {
                    if (mp.isPlaying() && isFirst) {
                        mp.pause();
                        btnPlay.setImageResource(R.drawable.play961);
                    } else if (mp.isPlaying() == false && isFirst) {
                        mp.start();
                        btnPlay.setImageResource(R.drawable.pause96);
                    }
                } else {
                    playSong(0);
                    isFirst = true;
            }

            }
        });


    }

    public void GetAllMediaMp3Files() {

        contentResolver = context.getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                null
        );

        if (cursor == null) {

            Toast.makeText(MainActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(MainActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG);

        } else {

            int Title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            //Getting Song ID From Cursor.
            //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {

                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);

                String fullPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String SongTitle = cursor.getString(Title);
                HashMap<String, String> ListElementsArrayList = new HashMap<String, String>();
                // Adding Media File Names to ListElementsArrayList.
                ListElementsArrayList.put("songTitle", SongTitle);
                ListElementsArrayList.put("songPath", fullPath);
                songsList.add(ListElementsArrayList);

            } while (cursor.moveToNext());
        }
    }

    // Creating Runtime permission function.
    public void AndroidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder(MainActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE

                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel", null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                } else {

                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            } else {
///
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
                } else {

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
        }
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * Function to play a song
     *
     * @param songIndex - index of song
     */
    public void playSong(int songIndex) {
        // Play song
        try {
            mp.reset();
            mp.setDataSource(songsList.get(songIndex).get("songPath"));
            mp.prepare();
            mp.start();
            // Displaying Song title
            String songTitle = songsList.get(songIndex).get("songTitle");
            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.pause96);

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {

        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + Utilities.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + Utilities.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (Utilities.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /**
     *
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * When user stops moving the progress hanlder
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = Utilities.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    /**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        // check for repeat is ON or OFF
        if (isRepeat) {
            // repeat is on play same song again
            playSong(currentSongIndex);
        } else if (isShuffle) {
            // shuffle is on - play a random song
            Random rand = new Random();
            currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
            playSong(currentSongIndex);
        } else {
            // no repeat or shuffle ON - play next song
            if (currentSongIndex < (songsList.size() - 1)) {
                playSong(currentSongIndex + 1);
                currentSongIndex = currentSongIndex + 1;
            } else {
                // play first song
                playSong(0);
                currentSongIndex = 0;
            }
        }
    }

    /**
     * Receiving song index from playlist view
     * and play the song
     */
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            currentSongIndex = data.getExtras().getInt("songIndex");
            // play selected song
            playSong(currentSongIndex);
            isFirst = true;
        }

    }


}
























