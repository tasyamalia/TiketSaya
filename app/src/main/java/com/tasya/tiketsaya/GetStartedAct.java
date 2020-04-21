package com.tasya.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetStartedAct extends AppCompatActivity {
    Animation ttb, btt;
    Button btn_sign_in;
    Button btn_new_account_create;
    ImageView emblemWhite;
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        btn_new_account_create = findViewById(R.id.btn_new_account_create);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        emblemWhite = findViewById(R.id.emblemWhite);
        textView2 = findViewById(R.id.textView2);
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosign = new Intent(GetStartedAct.this, SignInAct.class);
                startActivity(gotosign);
            }
        });
        btn_new_account_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregisterone = new Intent(GetStartedAct.this, RegisterOneAct.class);
                startActivity(gotoregisterone);
            }
        });

        //Load Anim
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        btt = AnimationUtils.loadAnimation(this,R.anim.btt);
        //Run Animation
        emblemWhite.startAnimation(ttb);
        textView2.startAnimation(ttb);
        btn_sign_in.startAnimation(btt);
        btn_new_account_create.startAnimation(btt);
    }
}
