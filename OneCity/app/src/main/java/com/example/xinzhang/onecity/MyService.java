package com.example.xinzhang.onecity;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Time;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MyService extends Service {

    SharedPreferences sharepre;
    SharedPreferences.Editor editor;
    Notification notify;
    NotificationManager notifymanager;
    Handler handle;
    PendingIntent pendingintent;
    Time t;
    @Override
    public void onCreate(){
        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();
        notifymanager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        t=new Time();
        t.setToNow();
        handle=new Handler(){
            @Override
            public void handleMessage(Message msg){
                notify=new Notification.Builder(MyService.this).setSmallIcon(R.drawable.icon)
                        .setTicker("微工同城有新消息")
                        .setContentTitle("您的一个任务被响应")
                        .setContentText("您的任务在"+t.hour+"时"+t.minute+"分得到响应"+"\n"
                                        +"点击查看")
                        .build();
                notify.flags=Notification.FLAG_AUTO_CANCEL;
                notifymanager.notify(1,notify);
            }
        };
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId){
        new Thread(){
            public void run(){
                try{
                    while(true) {

                        Thread.sleep(30000);

                        URL url = new URL("http://" + sharepre.getString("ip", "10.133.9.207") + ":8080/OneCity/Serve?rq=6&a=" + URLEncoder.encode(sharepre.getString("account", "123"), "UTF-8"));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setReadTimeout(8000);
                        InputStream in = conn.getInputStream();
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String s = buffer.readLine();
                        if(!s.equals(null)) {
                            Message msg = new Message();
                            handle.sendMessage(msg);
                        }
                        Thread.sleep(30000);
                    }
                }catch(Exception e){}
            }
        }.start();
        return super.onStartCommand(intent,flag,startId);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}

