package com.example.xinzhang.onecity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class Register extends Activity {

    ImageView back, userhead;
    EditText account, password, email;

    FloatingActionButton fab;

    Button register;

    Handler handle;

    SharedPreferences sharepre;
    SharedPreferences.Editor editor;

    Animation animation1,animation2;

    int headnumber=0;
    PopupMenu popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        back=(ImageView)findViewById(R.id.register_back);
        userhead=(ImageView)findViewById(R.id.register_userhead);

        account=(EditText)findViewById(R.id.register_account);
        password=(EditText)findViewById(R.id.register_password);
        email=(EditText)findViewById(R.id.register_email);

        register=(Button)findViewById(R.id.register_register);

        fab=(FloatingActionButton)findViewById(R.id.register_fab);

        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();

        //register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account.getText().equals("")||password.getText().equals("")||email.getText().equals(""))
                    Toast.makeText(Register.this,"未填写完成",Toast.LENGTH_LONG).show();
                else {
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("http://"+sharepre.getString("ip","")+":8080/OneCity/Serve?rq=0&a=" + URLEncoder.encode(account.getText().toString(), "UTF-8") +
                                        "&p=" + password.getText() + "&h="+String.valueOf(headnumber));
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.setReadTimeout(8000);
                                InputStream in = conn.getInputStream();
                                BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                                String s = buffer.readLine();
                                System.out.println(s);
                                Message msg = new Message();
                                msg.what = Integer.valueOf(s).intValue();

                                handle.sendMessage(msg);
                            } catch (Exception e) {
                            }
                        }
                    }.start();
                }
            }
        });

        handle=new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what==1){
                    editor.putString("account",account.getText().toString());
                    editor.putString("password",password.getText().toString());
                    editor.putString("email",email.getText().toString());
                    editor.putString("head",String.valueOf(headnumber));
                    editor.commit();
                    Toast.makeText(Register.this,"注册成功，请登录",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Register.this,Login.class));
                    finish();
                }
                else
                    Toast.makeText(Register.this,"用户名已被注册",Toast.LENGTH_LONG).show();
            }
        };

        //return
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });

        //fab
        animation1= AnimationUtils.loadAnimation(Register.this,R.anim.rotation2);
        animation2= AnimationUtils.loadAnimation(Register.this,R.anim.rotation3);
        animation1.setFillAfter(true);
        animation2.setFillAfter(true);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.startAnimation(animation1);
                popup=new PopupMenu(Register.this,fab);
                popup.getMenuInflater().inflate(R.menu.popupmenu3,popup.getMenu());
                popup.show();

                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        fab.startAnimation(animation2);
                    }
                });
            }
        });

        //headimage
        userhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup=new PopupMenu(Register.this,userhead);
                popup.getMenuInflater().inflate(R.menu.popupmenu2,popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        headnumber=item.getItemId()-R.id.menu2_head1;
                        userhead.setImageDrawable(getResources().getDrawable(R.drawable.userhead1+headnumber));
                        editor.putString("head",String.valueOf(headnumber));
                        editor.commit();
                        return true;
                    }
                });
            }
        });

    }
}
