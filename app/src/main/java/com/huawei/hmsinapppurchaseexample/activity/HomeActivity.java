package com.huawei.hmsinapppurchaseexample.activity;
/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.huawei.hmsinapppurchaseexample.R;
import com.huawei.hmsinapppurchaseexample.util.ProductPurchase;

import info.hoang8f.widget.FButton;


public class HomeActivity extends AppCompatActivity {


    FButton btnPlayGame, btnQuit;
    TextView txtTitle;
    private ProductPurchase productPurchase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        initView();
        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                // UNCOMMENT THIS WHEN YOU NEED TO TEST IN-APP PURCHASE ...
//                editor.putBoolean("silver_sub", false);
//                editor.putBoolean("gold_sub", false);
//                editor.putBoolean("diamond_sub", false);
//                editor.putBoolean("removeads", false);
//                editor.putBoolean("gamelife", false);
                int life = preferences.getInt("userLifeNow", 3);
                int coinValue = preferences.getInt("coinNow", 100);
                editor.putInt("userLifeNow", life);
                editor.putInt("coinNow", coinValue);
                editor.apply();
                startActivity(new Intent(HomeActivity.this, GameActivity.class));
                finish();
            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/shablagooital.ttf");
        txtTitle = findViewById(R.id.gameTitle);
        txtTitle.setTypeface(typeface);
        btnQuit = findViewById(R.id.quit);
        btnPlayGame = findViewById(R.id.playGame);
        btnPlayGame.setTypeface(typeface);
        btnQuit.setTypeface(typeface);
        btnPlayGame.setButtonColor(getResources().getColor(R.color.fbutton_color_nephritis));
        btnQuit.setButtonColor(getResources().getColor(R.color.fbutton_color_pomegranate));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
