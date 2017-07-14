package com.example.xinzhang.onecity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class History extends Activity {

    SharedPreferences sharepre;
    SharedPreferences.Editor editor;

    Button put, done;
    ListView putlist, donelist;
    List<Map<String, Object>> putlistcontent, donelistcontent;

    FloatingActionButton fab;

    Handler handle1;

    @Override
    protected void onCreate(Bundle save) {
        super.onCreate(save);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history);
        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();

        put = (Button) findViewById(R.id.history_puttask);
        done = (Button) findViewById(R.id.history_donetask);

        fab = (FloatingActionButton) findViewById(R.id.history_fab);

        putlist = (ListView) findViewById(R.id.history_puttasklist);
        donelist = (ListView) findViewById(R.id.history_donetasklist);

        donelist.setVisibility(View.GONE);

        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putlist.setVisibility(View.VISIBLE);
                donelist.setVisibility(View.GONE);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                donelist.setVisibility(View.VISIBLE);
                putlist.setVisibility(View.GONE);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(History.this, R.anim.rotation1);
                animation.setFillAfter(true);
                fab.startAnimation(animation);
                new Thread(new History.UpdateHistory()).start();
            }
        });

        putlistcontent = new ArrayList<Map<String, Object>>();
        donelistcontent = new ArrayList<Map<String, Object>>();

        handle1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                RecomAdapter adapter = new RecomAdapter(History.this, putlistcontent);
                putlist.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                adapter = new RecomAdapter(History.this, donelistcontent);
                donelist.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                Toast.makeText(History.this, "已刷新", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private class UpdateHistory implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL("http://" + sharepre.getString("ip", "10.133.9.207") + ":8080/OneCity/Serve?rq=7&a=" +
                        URLEncoder.encode(sharepre.getString("account", ""), "UTF-8"));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(8000);
                InputStream in = conn.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String s = null;
                int i = 0, j = -1, k = 0, number = 0;
                String[] content = new String[6];
                Map<String, Object> map = new HashMap<String, Object>();
                while (!(s = buffer.readLine()).equals("")) {
                    map = new HashMap<String, Object>();
                    i = 0;
                    j = -1;
                    number++;
                    for (k = 0; k < 6; k++) {
                        i = j;
                        j = s.indexOf("&", i + 1);
                        content[k] = s.substring(i + 1, j);
                    }
                    map.put("head", R.drawable.userhead1 + number % 7);
                    map.put("title", content[1]);
                    map.put("name", content[0]);
                    map.put("distance", content[3]);
                    map.put("money", content[4]);
                    map.put("content", content[2]);
                    map.put("number", content[5]);
                    map.put("time", "Unknow");
                    putlistcontent.add(map);
                }

                url = new URL("http://" + sharepre.getString("ip", "10.133.9.207") + ":8080/OneCity/Serve?rq=8&num=" + sharepre.getString("accept", ""));
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(8000);
                in = conn.getInputStream();
                buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                s = null;
                i = 0;
                j = -1;
                k = 0;
                number = 0;
                content = new String[6];
                map = new HashMap<String, Object>();
                while (!(s = buffer.readLine()).equals("")) {
                    map = new HashMap<String, Object>();
                    i = 0;
                    j = -1;
                    number++;
                    for (k = 0; k < 6; k++) {
                        i = j;
                        j = s.indexOf("&", i + 1);
                        content[k] = s.substring(i + 1, j);
                    }
                    map.put("head", R.drawable.userhead1 + number % 7);
                    map.put("title", content[1]);
                    map.put("name", content[0]);
                    map.put("distance", content[3]);
                    map.put("money", content[4]);
                    map.put("content", content[2]);
                    map.put("number", content[5]);
                    map.put("time", "Unknow");
                    donelistcontent.add(map);
                }
                handle1.sendMessage(new Message());
            } catch (Exception e) {
            }
        }
    }
}
