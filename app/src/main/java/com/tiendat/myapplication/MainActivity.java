package com.tiendat.myapplication;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] items;
    RelativeLayout mh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mh = (RelativeLayout)findViewById(R.id.manHinh);
        mh.setBackgroundResource(R.drawable.nen2);
        lv = (ListView) findViewById(R.id.lvPlaylist);
        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[ mySongs.size()];
        for(int i = 0;i< mySongs.size();i++){
            //toast(mySongs.get(i).getName().toString());
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.song_layout,R.id.textView,items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songlist",mySongs) );
            }
        });

    }


    public ArrayList<File> findSongs(File root){
        ArrayList<File> a1 = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile :files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                a1.addAll(findSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3")||singleFile.getName().endsWith(".wav")){
                    a1.add(singleFile);
                }
            }
        }
        return  a1;
    }
    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }
}
