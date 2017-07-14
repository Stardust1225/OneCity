package com.example.xinzhang.onecity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Login extends Activity {

    EditText account,password,ip;
    Button login;
    TextView register;
    Handler handle;
    SharedPreferences sharepre;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);



        account=(EditText)findViewById(R.id.login_account);
        password=(EditText)findViewById(R.id.login_password);

        ip=(EditText)findViewById(R.id.login_ip);

        ip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                editor.putString("ip",ip.getText().toString());
                editor.commit();
            }
        });

        login=(Button)findViewById(R.id.login_login);
        register=(TextView)findViewById(R.id.login_register);

        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();


        if(sharepre.getString("autologin","").equals("true")){
            new Thread(){
                @Override
                public void run(){
                    try {
                        URL url = new URL("http://"+sharepre.getString("ip","")+":8080/OneCity/Serve?rq=1&a="+ URLEncoder.encode(sharepre.getString("account",""),"UTF-8")+
                                "&p="+sharepre.getString("password",""));
                        System.out.println(url.toString());
                        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setReadTimeout(8000);
                        InputStream in=conn.getInputStream();
                        BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"UTF-8"));
                        String s=buffer.readLine();
                        Message msg=new Message();
                        msg.what=Integer.valueOf(s).intValue();
                        handle.sendMessage(msg);
                    }catch (Exception e){}
                }
            }.start();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.getText().toString()==null||password.getText().toString()==null)
                    Toast.makeText(Login.this,"未填写用户名或密码",Toast.LENGTH_LONG).show();
                else{
                    new Thread(){
                        @Override
                        public void run(){
                            try {
                                URL url = new URL("http://"+sharepre.getString("ip","10.133.9.207")+":8080/OneCity/Serve?rq=1&a="+ URLEncoder.encode(account.getText().toString(),"UTF-8")+
                                        "&p="+password.getText().toString());
                                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.setReadTimeout(8000);
                                InputStream in=conn.getInputStream();
                                BufferedReader buffer=new BufferedReader(new InputStreamReader(in,"UTF-8"));
                                String s=buffer.readLine();
                                Message msg=new Message();
                                msg.what=Integer.valueOf(s).intValue();
                                handle.sendMessage(msg);
                            }catch (Exception e){}
                        }
                    }.start();
                }
            }
        });

        handle=new Handler(){
            @Override
            public void handleMessage(Message msg){
                    if(msg.what==-1){
                        Toast.makeText(Login.this, "登陆错误", Toast.LENGTH_LONG).show();
                    }
                    else {
                        editor.putString("autologin","true");
                        editor.putString("account",account.getText().toString());
                        editor.putString("password",password.getText().toString());
                        editor.putString("head",String.valueOf(msg.what));
                        editor.commit();
                        startActivity(new Intent(Login.this,MainActivity.class));
                        finish();
                    }
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });
    }
}
