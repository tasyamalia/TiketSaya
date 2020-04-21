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

public class SuccessBuyTicketAct extends AppCompatActivity {
    Animation app_splash,btt,ttb;
    ImageView successBuy;
    TextView app_title,app_subtitle;
    Button btn_viewTicket, btn_myDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);

        successBuy = findViewById(R.id.successBuy);
        app_title = findViewById(R.id.app_title);
        app_subtitle = findViewById(R.id.app_subtitle);
        btn_viewTicket = findViewById(R.id.btn_viewTicket);
        btn_myDashboard = findViewById(R.id.btn_myDashboard);
        //load anim
        app_splash = AnimationUtils.loadAnimation(this,R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        //Run anim
        btn_viewTicket.startAnimation(btt);
        btn_myDashboard.startAnimation(btt);
        successBuy.startAnimation(app_splash);
        app_title.startAnimation(ttb);
        app_subtitle.startAnimation(ttb);

        btn_viewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(SuccessBuyTicketAct.this, MyProfileAct.class);
                startActivity(gotoprofile);
            }
        });
        btn_myDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent (SuccessBuyTicketAct.this, HomeAct.class);
                startActivity(gotohome);
            }
        });
    }
}
