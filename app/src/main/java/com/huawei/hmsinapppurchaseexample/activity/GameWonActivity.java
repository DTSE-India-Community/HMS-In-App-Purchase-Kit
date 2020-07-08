package com.huawei.hmsinapppurchaseexample.activity;
/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.huawei.hmsinapppurchaseexample.R;

import info.hoang8f.widget.FButton;

public class GameWonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_won);
        FButton btnPlayAgain = findViewById(R.id.platygaianbutton);
        btnPlayAgain.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));
    }

    public void PlayAgain(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GameWonActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        int life = preferences.getInt("userLifePrev", 3);
        int coinValue = preferences.getInt("coinNow", 100);
        editor.putInt("userLifeNow", life);
        editor.putInt("coinNow",coinValue);
        editor.apply();
        Intent intent = new Intent(GameWonActivity.this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}
