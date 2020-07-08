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
import android.widget.Button;
import android.widget.TextView;

import com.huawei.hmsinapppurchaseexample.R;

public class PlayAgainActivity extends AppCompatActivity {

    Button playAgain;
    TextView wrongAnsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_again);
        playAgain = (Button) findViewById(R.id.playAgainButton);
        wrongAnsText = (TextView) findViewById(R.id.wrongAns);

        //play again button onclick listener
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PlayAgainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                int life = preferences.getInt("userLifePrev", 3);
                editor.putInt("userLifeNow", life);
                editor.apply();
                Intent intent = new Intent(PlayAgainActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Setting typefaces for textview and button - this will give stylish fonts on textview and button
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/shablagooital.ttf");
        playAgain.setTypeface(typeface);
        wrongAnsText.setTypeface(typeface);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
