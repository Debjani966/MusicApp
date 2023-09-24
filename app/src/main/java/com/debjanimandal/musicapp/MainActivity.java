package com.debjanimandal.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private final static String Media_Path=Environment.getExternalStorageDirectory().getPath()+"/";
    private ArrayList<String> songList=new ArrayList<>();
    private MusicAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.e("Media Path",Media_Path);
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1 );
        }
        else {
            getAllAudioFiles();
        }
    }
    public void getAllAudioFiles()
    {
        if(Media_Path!= null)
        {
            File mainFile=new File(Media_Path);
            File[] filelist=mainFile.listFiles();
            for(File file: filelist) {
               // Log.e("Media Path", file.toString());
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else {
                    String path = file.getAbsolutePath();
                    if (path.endsWith(".mp3")) {
                        Log.e("Media Path", file.toString());
                        songList.add(path);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }

        adapter=new MusicAdapter(songList,MainActivity.this);
        recyclerView.setAdapter(adapter);
    }
    public void scanDirectory(File directory)
    {
        if(directory!=null) {
            File[] filelist = directory.listFiles();
            if(filelist != null) {
                for (File file : filelist) {
                   // Log.e("Media Path", file.toString());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        String path = file.getAbsolutePath();
                        if (path.endsWith(".mp3")) {
                            Log.e("Media Path", file.toString());
                            songList.add(path);
                        }
                    }
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            getAllAudioFiles();
        }
    }
}