package com.example.xinzhang.onecity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Putforward extends Activity {

    //spinner
    List<String> spinnercontent;
    Spinner kindspinner;

    //putforward
    Button putforward;
    EditText title,content,money,location;


    //back
    ImageView back,userhead;

    SharedPreferences sharepre;
    SharedPreferences.Editor editor;

    Handler handle;

    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.putforward);

        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();

        name=(TextView)findViewById(R.id.putforward_name);
        name.setText(sharepre.getString("account",""));

        InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);

        //spinner
        kindspinner=(Spinner)findViewById(R.id.putforward_spinner);
        spinnercontent=new ArrayList<String>();
        spinnercontent.add("维修");
        spinnercontent.add("生活");
        spinnercontent.add("情感");
        spinnercontent.add("其他");
        kindspinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,spinnercontent));
        kindspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //putforward
        putforward=(Button)findViewById(R.id.putforward_putforward);
        title=(EditText)findViewById(R.id.putforward_title);
        content=(EditText)findViewById(R.id.putforward_content);
        money=(EditText)findViewById(R.id.putforward_money);
        location=(EditText)findViewById(R.id.putforward_location);

        putforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run(){
                        try {
                            URL url = new URL("http://"+sharepre.getString("ip","10.133.9.207")+":8080/OneCity/Serve?rq=2&a="+ URLEncoder.encode(sharepre.getString("account",""),"UTF-8")+
                                    "&p="+sharepre.getString("password","")+"&h="+sharepre.getString("head","1")+"&tit="+URLEncoder.encode(title.getText().toString(),"UTF-8")
                                    +"&con="+URLEncoder.encode(content.getText().toString(),"UTF-8")+"&pay="+URLEncoder.encode(money.getText().toString(),"UTF-8")
                                    +"&loc="+URLEncoder.encode(location.getText().toString(),"UTF-8"));
                            System.out.println(url.toString());
                            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setReadTimeout(8000);
                            InputStream in=conn.getInputStream();
                            BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"UTF-8"));
                            String s=buffer.readLine();
                            Message msg=new Message();
                            msg.what=Integer.parseInt(s);
                            System.out.println(msg.what);
                            handle.sendMessage(msg);
                        }catch (Exception e){}
                    }
                }.start();
            }
        });


        //back
        back=(ImageView)findViewById(R.id.putforward_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //userhead
        userhead=(ImageView)findViewById(R.id.putputforward_imageView);
        userhead.setImageResource(R.drawable.userhead1+Integer.parseInt(sharepre.getString("head","1")));

        //handle
        handle=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what!=-1){
                    Toast.makeText(Putforward.this,"提交成功",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Putforward.this,MainActivity.class));
                    finish();
                }
            }
        };
    }
}
