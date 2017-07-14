package com.example.xinzhang.onecity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Change extends Activity {

    EditText email,phone,qq,wechat,card,alipay;

    SharedPreferences sharepre;
    SharedPreferences.Editor editor;

    FloatingActionButton fab;

    ImageView userhead,back;

    TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change);

        email = (EditText) findViewById(R.id.change_email);
        phone = (EditText) findViewById(R.id.change_phone);
        qq = (EditText) findViewById(R.id.change_qq);
        wechat = (EditText) findViewById(R.id.change_wechat);
        card = (EditText) findViewById(R.id.change_card);
        alipay = (EditText) findViewById(R.id.change_alipay);

        account = (TextView) findViewById(R.id.change_account);

        userhead = (ImageView) findViewById(R.id.change_userhead);
        back = (ImageView) findViewById(R.id.change_back);

        userhead.setImageResource(R.drawable.userhead1 + Integer.valueOf(sharepre.getString("head", "0")).intValue());
        account.setText(sharepre.getString("account", ""));

        sharepre = getSharedPreferences("setting", MODE_WORLD_WRITEABLE);
        editor = sharepre.edit();

        fab = (FloatingActionButton) findViewById(R.id.change_fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("email", email.getText().toString());
                editor.putString("phone", phone.getText().toString());
                editor.putString("qq", qq.getText().toString());
                editor.putString("wechat", wechat.getText().toString());
                editor.putString("card", card.getText().toString());
                editor.putString("alipay", alipay.getText().toString());
                editor.commit();
                startActivity(new Intent(Change.this, MyAccount.class));
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Change.this, MyAccount.class));
                finish();
            }
        });
    }

}
