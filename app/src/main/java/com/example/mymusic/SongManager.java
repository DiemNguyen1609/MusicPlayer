package com.example.mymusic;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.DIRECTORY_MUSIC;

public class SongManager {
    // SDCard Path
    //final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath();
    final String MEDIA_PATH = Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC).getPath();
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    public static final int RUNTIME_PERMISSION_CODE = 7;
    Context context;

    String[] ListElements = new String[] { };

    ListView listView;

    List<String> ListElementsArrayList ;

    ArrayAdapter<String> adapter ;

    ContentResolver contentResolver;

    Cursor cursor;

    Uri uri;
    // Constructor
    public SongManager() {

    }
    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
//    public ArrayList<HashMap<String, String>> getPlayList() {
//        File home = new File(MEDIA_PATH);
//        if (home.listFiles(new FileExtensionFilter()).length >0) {
//            for (File file : home.listFiles(new FileExtensionFilter())) {
//                HashMap<String, String> song = new HashMap<String, String>();
//                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
//                song.put("songPath", file.getPath());
//
//                // Adding each song to SongList
//                songsList.add(song);
//            }
//        }
//
//        // return songs list array
//        return songsList;
//
//    }

//    ArrayList<HashMap<String,String>> getPlayList(String rootPath) {
//        ArrayList<HashMap<String,String>> fileList = new ArrayList<>();
//
//
//        try {
//            File rootFolder = new File(rootPath);
//            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
//            for (File file : files) {
//                if (file.isDirectory()) {
//                    if (getPlayList(file.getAbsolutePath()) != null) {
//                        fileList.addAll(getPlayList(file.getAbsolutePath()));
//                    } else {
//                        break;
//                    }
//                } else if (file.getName().endsWith(".mp3")|| file.getName().endsWith(".MP3")||file.getName().endsWith(".wav")) {
//                    HashMap<String, String> song = new HashMap<>();
//                    song.put("file_path", file.getAbsolutePath());
//                    song.put("file_name", file.getName());
//                    fileList.add(song);
//                }
//            }
//            //Log.d("A",fileList.get(0).values().toString());
//            return fileList;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    /**
//     * Class to filter files which are having .mp3 extension
//     */
//    class FileExtensionFilter implements FilenameFilter {
//        public boolean accept(File dir, String name) {
//            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
//        }
//    }





}



