package com.example.mymusic;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.DIRECTORY_MUSIC;

public class DetailActivity extends Activity {
    Context context;

    public static final int RUNTIME_PERMISSION_CODE = 7;
    static final int VIEW_MODE_LISTVIEW = 0;
    static final int VIEW_MODE_GRIDVIEW = 1;
    ListView listView;
    GridView gridView;
    private ViewStub stubGrid;
    private ViewStub stubList;
    private int currentViewMode = 0;
    ListAdapter adapter;


    ContentResolver contentResolver;

    Cursor cursor;

    Uri uri;
    ArrayList<HashMap<String, String>> songsListData;

    // Songs list
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playlist);
        stubList = (ViewStub) findViewById(R.id.stub_list);
        stubGrid = (ViewStub) findViewById(R.id.stub_grid);
        //readFile(lyricArray);
        stubList.inflate();
        stubGrid.inflate();
        listView = (ListView) findViewById(R.id.listviewID);
        gridView = (GridView) findViewById(R.id.mygridview);

        context = getApplicationContext();
        songsListData = new ArrayList<HashMap<String, String>>();

//        adapter = new SimpleAdapter(this, songsListData,
//                R.layout.listview_row, new String[] { "songTitle"}, new int[] {
//                R.id.nameTextViewID});

//                adapter = new SimpleAdapter(context, songsListData,
//                R.layout.gridview_items, new String[] { "songTitle"}, new int[] {
//                R.id.txtTitle});
        // Requesting run time permission for Read External Storage.
        AndroidRuntimePermission();


        GetAllMediaMp3Files();
        SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
        currentViewMode = sharedPreferences.getInt("currentViewMode", VIEW_MODE_LISTVIEW);//Default is view listview
        //Register item lick
        listView.setOnItemClickListener(onItemClick);
        gridView.setOnItemClickListener(onItemClick);
        switchView();
        //gridView.setAdapter(adapter);
        //listView.setAdapter(adapter);

        if (songsList != null) {
            for (int i = 0; i < songsList.size(); i++) {
                // creating new HashMap
                HashMap<String, String> song = songsList.get(i);
                //nameTemp=songsList.get(i).get("file_name");
                //namesong[i]=songsList.get(i).get("file_name");
                // adding HashList to ArrayList
                songsListData.add(song);
            }
        }
//        MusicListAdapter whatever = new MusicListAdapter(this, nameArray, infoArray, imageArray);
//
//        listView = (ListView) findViewById(R.id.listviewID);
//        listView.setAdapter(whatever);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                //Intent intentImg = new Intent(MainActivity.this, PlayerInService.class);
//                //Gửi thông tin qua View thứ 2
//                // Bao gồm hình bài hát, tên, ca sĩ và lời bài hát được lấy từ file
//                String message = nameArray[position];
//                //Do thông tin ca sĩ chỉ có 1 người nên không cần dùng biến position
//                String infoID = infoArray;
//                String lyricID = lyricArray.get(position);
//                //Integer imageID=imageArray[position];
//
//                intent.putExtra("name", message);
//                intent.putExtra("info", infoID);
//                intent.putExtra("lyric", lyricID);
//                //intentImg.putExtra("icon", imageID);
//                startActivity(intent);
//            }
//        });
        //Adding menuItems to ListView
//        ListAdapter adapter = new SimpleAdapter(this, songsListData,
//                R.layout.listview_row, new String[] { "songTitle" }, new int[] {
//                R.id.nameTextViewID });

        //MusicListAdapter adapter=new MusicListAdapter(this,namesong);
        // selecting single ListView item
        //ListView lv = findViewById(R.id.list);
//        ListView lv = (ListView)findViewById(R.id.listviewID);
//        lv.setAdapter(adapter);
        // listening to single listitem click
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//                // getting listitem index
//                int songIndex = position;
//
//                // Starting new intent
//                Intent in = new Intent(getApplicationContext(),
//                        MainActivity.class);
//                // Sending songIndex to PlayerActivity
//                in.putExtra("songIndex", songIndex);
//                setResult(100, in);
//                // Closing PlayListView
//                finish();
//            }
//        });

    }

    private void switchView() {

        if (VIEW_MODE_LISTVIEW == currentViewMode) {
            //Display listview
            stubList.setVisibility(View.VISIBLE);
            //Hide gridview
            stubGrid.setVisibility(View.GONE);
        } else {
            //Hide listview
            stubList.setVisibility(View.GONE);
            //Display gridview
            stubGrid.setVisibility(View.VISIBLE);
        }
        setAdapters();
    }

    private void setAdapters() {
        if (VIEW_MODE_LISTVIEW == currentViewMode) {
            adapter = new SimpleAdapter(this, songsListData,
                    R.layout.listview_row, new String[]{"songTitle"}, new int[]{
                    R.id.nameTextViewID});

            listView.setAdapter(adapter);
        } else {
            adapter = new SimpleAdapter(this, songsListData,
                    R.layout.gridview_items, new String[]{"songTitle"}, new int[]{
                    R.id.txtTitle});

            gridView.setAdapter(adapter);

        }
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Do any thing when user click to item
            int songIndex = position;

            // Starting new intent
            Intent in = new Intent(getApplicationContext(),
                    MainActivity.class);
            // Sending songIndex to PlayerActivity
            in.putExtra("songIndex", songIndex);
            setResult(100, in);
            // Closing PlayListView
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_1:
                if (VIEW_MODE_LISTVIEW == currentViewMode) {
                    currentViewMode = VIEW_MODE_GRIDVIEW;
                } else {
                    currentViewMode = VIEW_MODE_LISTVIEW;
                }
                //Switch view
                switchView();
                //Save view mode in share reference
                SharedPreferences sharedPreferences = getSharedPreferences("ViewMode", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("currentViewMode", currentViewMode);
                editor.commit();

                break;
        }
        return true;
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

            Toast.makeText(DetailActivity.this, "Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(DetailActivity.this, "No Music Found on SD Card.", Toast.LENGTH_LONG);

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
                songsListData.add(ListElementsArrayList);
            } while (cursor.moveToNext());

        }
    }

    // Creating Runtime permission function.
    public void AndroidRuntimePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    android.app.AlertDialog.Builder alert_builder = new android.app.AlertDialog.Builder(DetailActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    DetailActivity.this,
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
                            DetailActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            } else {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case RUNTIME_PERMISSION_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
            }
        }
    }


}