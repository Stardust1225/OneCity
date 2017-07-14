package com.example.xinzhang.onecity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class MainActivity extends Activity {

    SharedPreferences sharepre;
    SharedPreferences.Editor editor;

    View v1,v2,v3;

    //mainview
    DrawerLayout drawer;
    RelativeLayout opendrawer,putforward;
    ViewPager pager;
    TextView recom,kind,friend;
    List<View> viewlist;
    FloatingActionButton fab;
    ImageView littlehead;

    //pager1
    ListView recomlist;
    List<Map<String, Object>> list1=new ArrayList<Map<String, Object>>();
    Handler handle1;
    //pager2

    //pager3
    ListView frilist;

    //startview
    LinearLayout theme,account,collection,history,nearby;
    TextView quit,name,email;
    ImageView userhead;

    //dialog
    TextView dialogtitle,dialogcontent,dialoglocation,dialogpay,dialogcustomer;
    ImageView dialoguserhead;
    FloatingActionButton dialogfab;
    Button accept,back;
    AlertDialog dialog;
    AlertDialog.Builder build;
    Handler handle2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();

        v1=getLayoutInflater().inflate(R.layout.viewpager1,null);
        v2=getLayoutInflater().inflate(R.layout.viewpager2,null);
        v3=getLayoutInflater().inflate(R.layout.viewpager3,null);

        //mainview
        drawer=(DrawerLayout)findViewById(R.id.main_drawerlayout);

        opendrawer=(RelativeLayout) findViewById(R.id.main_opendrawer);
        putforward=(RelativeLayout) findViewById(R.id.main_putforward);

        pager=(ViewPager)findViewById(R.id.main_viewpager);

        recom=(TextView)findViewById(R.id.main_recommend);
        kind=(TextView)findViewById(R.id.main_kind);
        friend=(TextView)findViewById(R.id.main_friend);
        recom.setTextColor(getResources().getColor(R.color.lightblue));

        fab=(FloatingActionButton)findViewById(R.id.main_fab);

        //littlehead
        littlehead=(ImageView)findViewById(R.id.main_littlehead);
        littlehead.setImageResource(R.drawable.userhead1+Integer.valueOf(sharepre.getString("head","0")).intValue());

        //toolbar
        opendrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.START);
            }
        });
        recom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
                recom.setTextColor(getResources().getColor(R.color.lightblue));
                kind.setTextColor(getResources().getColor(R.color.darkgray));
                friend.setTextColor(getResources().getColor(R.color.darkgray));
            }
        });
        kind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
                kind.setTextColor(getResources().getColor(R.color.lightblue));
                recom.setTextColor(getResources().getColor(R.color.darkgray));
                friend.setTextColor(getResources().getColor(R.color.darkgray));
            }
        });
        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
                friend.setTextColor(getResources().getColor(R.color.lightblue));
                kind.setTextColor(getResources().getColor(R.color.darkgray));
                recom.setTextColor(getResources().getColor(R.color.darkgray));
            }
        });

        //viewpager
        viewlist=new ArrayList<View>();
        viewlist.add(v1);
        viewlist.add(v2);
        viewlist.add(v3);
        PagerAdapter pageradapter=new PagerAdapter(){
            @Override
            public int getCount() {
                return viewlist.size();
            }
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
            @Override
            public void destroyItem(ViewGroup container, int position, Object  object){
                container.removeView(viewlist.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewlist.get(position));
                return viewlist.get(position);
            }
        };
        pager.setAdapter(pageradapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0: recom.setTextColor(getResources().getColor(R.color.lightblue));
                        kind.setTextColor(getResources().getColor(R.color.darkgray));
                        friend.setTextColor(getResources().getColor(R.color.darkgray));
                        break;
                    case 1: kind.setTextColor(getResources().getColor(R.color.lightblue));
                        recom.setTextColor(getResources().getColor(R.color.darkgray));
                        friend.setTextColor(getResources().getColor(R.color.darkgray));
                        break;
                    case 2: friend.setTextColor(getResources().getColor(R.color.lightblue));
                        kind.setTextColor(getResources().getColor(R.color.darkgray));
                        recom.setTextColor(getResources().getColor(R.color.darkgray));
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //putforward
        putforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Putforward.class));
            }
        });

        //fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation= AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotation1);
                animation.setFillAfter(true);
                fab.startAnimation(animation);
                list1=new ArrayList<Map<String, Object>>();
                new Thread(new UpdateRecom()).start();
            }
        });

        //page1
        recomlist=(ListView)v1.findViewById(R.id.viewpager1_list);
        recomlist.setAdapter(new RecomAdapter(this,list1));

        handle1=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                RecomAdapter adapter=new RecomAdapter(MainActivity.this,list1);
                recomlist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"已刷新",Toast.LENGTH_SHORT).show();
            }
        };

    }

    @Override
    public void onStart(){
        super.onStart();

        startService(new Intent(MainActivity.this,MyService.class));

        //page1
        recomlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                build=new AlertDialog.Builder(MainActivity.this);
                View dialogview=LayoutInflater.from(MainActivity.this).inflate(R.layout.mainalertdialogview,null);
                build.setView(dialogview);
                build.setCancelable(false);
                dialog=build.show();

                //dialog
                dialogcontent=(TextView)dialogview.findViewById(R.id.dialog_content);
                dialogtitle=(TextView)dialogview.findViewById(R.id.dialog_title);
                dialoglocation=(TextView)dialogview.findViewById(R.id.dialog_location);
                dialogpay=(TextView)dialogview.findViewById(R.id.dialog_repay);
                dialogcustomer=(TextView)dialogview.findViewById(R.id.dialog_customer);

                Map<String,Object> hashmap=new HashMap<String,Object>();
                hashmap=list1.get(position);

                dialogcustomer.setText("发布者： "+(String)hashmap.get("name"));
                dialogcontent.setText("具体内容： "+(String)hashmap.get("content"));
                dialogtitle.setText("主题： "+(String)hashmap.get("title"));
                dialoglocation.setText("位置： "+(String)hashmap.get("distance"));
                dialogpay.setText("报酬： "+(String)hashmap.get("money"));

                dialoguserhead=(ImageView)dialogview.findViewById(R.id.dialog_userhead);
                dialoguserhead.setImageResource(R.drawable.userhead1+position%7);



                dialogfab=(FloatingActionButton) dialogview.findViewById(R.id.dialog_collection);

                accept=(Button)dialogview.findViewById(R.id.dialog_accept);
                back=(Button)dialogview.findViewById(R.id.dialog_back);

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL("http://" + sharepre.getString("ip", "10.133.9.207") + ":8080/OneCity/Serve?rq=5&num="+list1.get(position).get("number"));
                                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                    conn.setRequestMethod("GET");
                                    conn.setReadTimeout(8000);
                                    InputStream in = conn.getInputStream();
                                    BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                                    String s = buffer.readLine();
                                    Message msg = new Message();
                                    if(s.equals("1")) {
                                        msg.what = position;
                                        StringBuffer buffer1=new StringBuffer(sharepre.getString("accept",""));
                                        buffer1.append("?"+String.valueOf(position));
                                        editor.putString("accept",buffer.toString());
                                        editor.commit();
                                    }
                                    else
                                        msg.what=-1;
                                    handle2.sendMessage(msg);
                                } catch (Exception e) {}
                            }
                        }.start();
                        dialog.dismiss();
                    }
                });

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialogfab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogfab.setImageResource(R.drawable.redcollection);
                        StringBuffer buffer=new StringBuffer(sharepre.getString("collection",""));
                        buffer.append("?"+String.valueOf(position));
                        editor.putString("collction",buffer.toString());
                        editor.commit();
                        Toast.makeText(MainActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        handle2=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what!=-1) {
                    Toast.makeText(MainActivity.this, "接受成功", Toast.LENGTH_SHORT).show();
                    list1.remove(msg.what);
                    handle1.sendMessage(new Message());
                }
                else
                    Toast.makeText(MainActivity.this,"已经被他人抢先了",Toast.LENGTH_SHORT).show();
            }
        };

        //page3
        frilist=(ListView)v3.findViewById(R.id.viewpager3_listview);
        List<Map<String, Object>>frilistcontent=getFriData();
        frilist.setAdapter(new FriAdapter(this,frilistcontent));

        //startview
        account=(LinearLayout)findViewById(R.id.startview_account);
        theme=(LinearLayout)findViewById(R.id.startview_theme);
        collection=(LinearLayout)findViewById(R.id.startview_collection);
        history=(LinearLayout)findViewById(R.id.startview_history);
        nearby=(LinearLayout)findViewById(R.id.startview_nearby);

        userhead=(ImageView)findViewById(R.id.startview_userhead);
        userhead.setImageResource(R.drawable.userhead1+Integer.parseInt(sharepre.getString("head","0")));

        quit=(TextView)findViewById(R.id.startview_quit);
        name=(TextView)findViewById(R.id.startview_name);
        email=(TextView)findViewById(R.id.startview_email);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.commit();
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MyAccount.class));
            }
        });

        name.setText(sharepre.getString("account","Unknown"));
        email.setText(sharepre.getString("email","Unknown"));

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,History.class));
            }
        });
    }

    public List<Map<String,Object>> getFriData(){
        List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
        Map<String,Object> map;
        for(int i=0;i<=8;i++) {
            map = new HashMap<String, Object>();
            map.put("time","None");
            map.put("name","None");
            map.put("customerhead",R.drawable.userhead1+i%7);
            list.add(map);
        }
        return list;
    }

    private class UpdateRecom implements Runnable{
            @Override
            public void run(){
                try {
                    URL url = new URL("http://"+sharepre.getString("ip","10.133.9.207")+":8080/OneCity/Serve?rq=4&a=" +
                                    URLEncoder.encode(sharepre.getString("account", ""), "UTF-8"));
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(8000);
                    InputStream in = conn.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    String s = null;
                    int i = 0, j = -1, k = 0,number=0;
                    String[] content = new String[6];
                    Map<String,Object> map=new HashMap<String, Object>();
                    while (!(s=buffer.readLine()).equals("")) {

                        map=new HashMap<String, Object>();
                        i = 0;
                        j = -1;
                        number++;
                        for (k = 0; k < 6; k++) {
                            i = j;
                            j = s.indexOf("&", i + 1);
                            content[k] = s.substring(i + 1, j);
                        }
                        map.put("head",R.drawable.userhead1+number%7);
                        map.put("title",content[1]);
                        map.put("name",content[0]);
                        map.put("distance",content[3]);
                        map.put("money",content[4]);
                        map.put("content",content[2]);
                        map.put("number",content[5]);
                        map.put("time","Unknow");
                        list1.add(map);
                    }
                    handle1.sendMessage(new Message());
                }catch (Exception e){}
            }
    }
}

