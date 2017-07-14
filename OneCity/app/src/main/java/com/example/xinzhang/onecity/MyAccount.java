package com.example.xinzhang.onecity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MyAccount extends Activity {

    SharedPreferences sharepre;
    SharedPreferences.Editor editor;

    TextView email,phone,qq,wechat,card,alipay,put,done;

    //fab
    FloatingActionButton fab;

    //back
    ImageView back;

    ImageView head;
    TextView name;

    //getNumber
    Handler handle;
    String doneNumber,putNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myaccount);

        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();

        email=(TextView)findViewById(R.id.myaccount_email);
        phone=(TextView)findViewById(R.id.myaccount_phone);
        qq=(TextView)findViewById(R.id.myaccount_qq);
        wechat=(TextView)findViewById(R.id.myaccount_wechat);
        card=(TextView)findViewById(R.id.myaccount_card);
        alipay=(TextView)findViewById(R.id.myaccount_alipay);
        done=(TextView)findViewById(R.id.myaccount_done);
        put=(TextView)findViewById(R.id.myaccount_put);

        //fab
        fab=(FloatingActionButton)findViewById(R.id.myaccount_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation animation1=AnimationUtils.loadAnimation(MyAccount.this,R.anim.rotation2);
                final Animation animation2=AnimationUtils.loadAnimation(MyAccount.this,R.anim.rotation3);
                animation1.setFillAfter(true);
                animation2.setFillAfter(true);
                fab.startAnimation(animation1);
                PopupMenu popup=new PopupMenu(MyAccount.this,fab);
                popup.getMenuInflater().inflate(R.menu.popupmenu1,popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId()==R.id.menu1_flash){
                            fab.startAnimation(animation2);
                            new Thread(new GetNumberThread()).start();
                        }
                        return true;
                    }
                });
                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        fab.startAnimation(animation2);
                    }
                });

            }
        });

        //back
        back=(ImageView)findViewById(R.id.myaccount_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        name=(TextView)findViewById(R.id.myaccount_name);
        name.setText(sharepre.getString("account","Unknown"));

        head=(ImageView)findViewById(R.id.myaccount_userhead);
        head.setImageResource(R.drawable.userhead1+Integer.valueOf(sharepre.getString("head","0")).intValue());
    }

    @Override
    public void onStart(){
        super.onStart();

        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();

        email.setText(sharepre.getString("email",""));
        phone.setText(sharepre.getString("phone",""));
        qq.setText(sharepre.getString("qq",""));
        wechat.setText(sharepre.getString("wechat",""));
        card.setText(sharepre.getString("card",""));
        alipay.setText(sharepre.getString("alipay",""));

        new Thread(new GetNumberThread()).start();

        handle=new Handler(){
            @Override
            public void handleMessage(Message msg){
                System.out.println("here0");
                put.setText(putNumber);
                done.setText(doneNumber);
            }
        };
    }
    private class GetNumberThread implements Runnable{
        @Override
        public void run(){
            try {
                URL url = new URL("http://" + sharepre.getString("ip", "10.133.9.207") + ":8080/OneCity/Serve?rq=3&a=" +
                        URLEncoder.encode(sharepre.getString("account", ""), "UTF-8"));
                System.out.println(url.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(8000);
                InputStream in = conn.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                putNumber = buffer.readLine();
                doneNumber = buffer.readLine();
                handle.sendMessage(new Message());
            } catch (Exception e) {}
        }
    }




}



