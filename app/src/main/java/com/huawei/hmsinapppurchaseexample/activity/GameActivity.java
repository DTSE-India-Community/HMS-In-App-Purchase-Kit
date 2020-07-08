package com.huawei.hmsinapppurchaseexample.activity;
/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hmsinapppurchaseexample.R;
import com.huawei.hmsinapppurchaseexample.util.ProductPurchase;
import com.thecode.aestheticdialogs.AestheticDialog;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public String TAG = " GameActivity";
    FButton btnGk, btnScience, btnHistory, btnSports, btnSubscribe, btnNoAds;
    TextView txtTitle, txtCoin, txtLife;
    LinearLayout llAddCoin, llheart;
    RelativeLayout adFrameLayout;
    BannerView bannerView;
    InterstitialAd interstitialAd;
    AdListener adListener;
    SharedPreferences preferences;
    boolean silver_sub, gold_sub, diamond_sub, removeads;
    int coinValues = 0;
    String topic = "";
    private ArrayList<String> productIds;
    private ProductPurchase productPurchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/TitilliumWeb-Bold.ttf");
        Typeface typefaceSh = Typeface.createFromAsset(getAssets(), "fonts/shablagooital.ttf");

        txtTitle = findViewById(R.id.txtHeader);
        txtCoin = findViewById(R.id.txtCoin);
        txtLife = findViewById(R.id.txtLife);
        llAddCoin = findViewById(R.id.llAddCoin);
        llheart = findViewById(R.id.llheart);

        txtTitle.setText("Quiz Mania");
        txtTitle.setTypeface(typefaceSh);
        txtCoin.setTypeface(typeface);
        txtLife.setTypeface(typefaceSh);

        btnSubscribe = findViewById(R.id.btnSubscribe);
        btnNoAds = findViewById(R.id.btnNoAds);
        btnGk = findViewById(R.id.gk);
        btnScience = findViewById(R.id.generalScience);
        btnHistory = findViewById(R.id.history);
        btnSports = findViewById(R.id.sports);

        llAddCoin.setOnClickListener(this);
        llheart.setOnClickListener(this);
        btnSubscribe.setOnClickListener(this);
        btnNoAds.setOnClickListener(this);
        btnGk.setOnClickListener(this);
        btnScience.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnSports.setOnClickListener(this);


        Drawable sports = GameActivity.this.getResources().getDrawable(R.drawable.sports);
        Drawable history = GameActivity.this.getResources().getDrawable(R.drawable.history);
        Drawable science = GameActivity.this.getResources().getDrawable(R.drawable.science);
        Drawable premium = GameActivity.this.getResources().getDrawable(R.drawable.premium);
        Drawable premiumDiamond = GameActivity.this.getResources().getDrawable(R.drawable.premium_diamond);

        btnSports.setCompoundDrawablesWithIntrinsicBounds(sports, null, premium, null);
        btnHistory.setCompoundDrawablesWithIntrinsicBounds(history, null, premium, null);
        btnScience.setCompoundDrawablesWithIntrinsicBounds(science, null, premiumDiamond, null);


        btnNoAds.setShadowEnabled(false);
        btnSubscribe.setShadowEnabled(false);

        btnSubscribe.setTypeface(typeface);
        btnNoAds.setTypeface(typeface);
        btnGk.setTypeface(typeface);
        btnScience.setTypeface(typeface);
        btnHistory.setTypeface(typeface);
        btnSports.setTypeface(typeface);

        btnNoAds.setButtonColor(getResources().getColor(R.color.fbutton_color_pomegranate));
        btnSubscribe.setButtonColor(getResources().getColor(R.color.fbutton_color_silver));
        btnGk.setButtonColor(getResources().getColor(R.color.fbutton_color_midnight_blue));
        btnScience.setButtonColor(getResources().getColor(R.color.fbutton_color_midnight_blue));
        btnHistory.setButtonColor(getResources().getColor(R.color.fbutton_color_midnight_blue));
        btnSports.setButtonColor(getResources().getColor(R.color.fbutton_color_midnight_blue));

        adFrameLayout = findViewById(R.id.adFrameLayout);
        bannerView = new BannerView(this);
        adEventListener();


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        silver_sub = preferences.getBoolean("silver_sub", false);
        gold_sub = preferences.getBoolean("gold_sub", false);
        diamond_sub = preferences.getBoolean("diamond_sub", false);
        removeads = preferences.getBoolean("removeads", false);
        int life = preferences.getInt("userLifeNow", 3);
        int coinValue = preferences.getInt("coinNow", 100);
        txtLife.setText("" + life);
        txtCoin.setText("" + coinValue);
        if (!removeads) {
            loadBannerAds();
        }

    }

    // Banner Ad it loaded here ...
    private void loadBannerAds() {
        bannerView.setAdId("testw6vs28auh3");
        BannerAdSize adSize = BannerAdSize.BANNER_SIZE_SMART;
        bannerView.setBannerAdSize(adSize);
        adFrameLayout.addView(bannerView);
        bannerView.loadAd(new AdParam.Builder().build());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gk:
                btnGk.setEnabled(false);
                topic = "gk";
                goToQuizPage();
                break;
            case R.id.sports:
                btnSports.setEnabled(false);
                topic = "sp";
                checkForPremiumAccount("sp");
                break;
            case R.id.history:
                btnHistory.setEnabled(false);
                topic = "his";
                checkForPremiumAccount("his");
                break;
            case R.id.generalScience:
                btnScience.setEnabled(false);
                topic = "gs";
                checkForDiamondAccount();
                break;
            case R.id.btnSubscribe:
                btnSubscribe.setEnabled(false);
                goToIapProdtListPage("");
                break;
            case R.id.btnNoAds:
                btnNoAds.setEnabled(false);
                goToIapProdtListPage("no_ads");
                break;
            case R.id.llAddCoin:
                goToIapProdtListPage("extra_life");
                break;
            case R.id.llheart:
                showAddLifeDialog();
                break;
        }

    }

    // Go to IAP Product List Page ...
    public  void goToIapProdtListPage(String typeVal){
        startActivity(new Intent(GameActivity.this, HmsInAppPurchaseList.class).putExtra("type", typeVal));
        finish();

    }

    // Check for Diamond Account Subscription...
    public void checkForDiamondAccount() {
        if (diamond_sub) {
           goToQuizPage();
        } else {
            AestheticDialog.showFlatDark(GameActivity.this, "Diamond Account", "Subscribe to DIAMOND", AestheticDialog.INFO);
            btnScience.setEnabled(true);
        }
    }

    // Check for Premium Account Subscription ...
    public void checkForPremiumAccount(String topicName) {
        if (silver_sub || gold_sub || diamond_sub) {
            goToQuizPage();
        } else {
            AestheticDialog.showFlatDark(GameActivity.this, "Premium Account", "Subscribe to either SILVER or GOLD", AestheticDialog.WARNING);
            if(topicName.equalsIgnoreCase("sp")){
                btnSports.setEnabled(true);
            }else{
                btnHistory.setEnabled(true);
            }
        }

    }

    // Go to Quiz Activity ..
    public void goToQuizPage() {
        if (!txtLife.getText().toString().equalsIgnoreCase("0")) {
            if (!removeads) {
                loadInterstitialAd("testb4znbuh3n2");
            } else {
                startActivity(new Intent(GameActivity.this, QuizActivity.class).putExtra("topicName", topic));
                finish();
            }
        } else {
            adCoinDialog();
        }
    }

    public void adEventListener() {
        adListener = new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                // Display an interstitial ad.
                showInterstitial();
            }

            @Override
            public void onAdFailed(int errorCode) {

                Log.d(TAG, "Ad load failed with error code: " + errorCode);
                startActivity(new Intent(GameActivity.this, QuizActivity.class).putExtra("topicName", topic));
                finish();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d(TAG, "onAdClosed");
                startActivity(new Intent(GameActivity.this, QuizActivity.class).putExtra("topicName", topic));
                finish();
            }

            @Override
            public void onAdClicked() {
                Log.d(TAG, "onAdClicked");
                super.onAdClicked();
            }

            @Override
            public void onAdOpened() {
                Log.d(TAG, "onAdOpened");
                super.onAdOpened();
            }
        };
    }

    // Add Coing Dialog ...
    private void adCoinDialog() {
        final Dialog dialogCorrect = new Dialog(GameActivity.this);
        dialogCorrect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogCorrect.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogCorrect.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogCorrect.setContentView(R.layout.dialog_coin);
        dialogCorrect.setCancelable(false);
        dialogCorrect.show();
        ImageView imgNext = dialogCorrect.findViewById(R.id.image_close);
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCorrect.dismiss();
            }
        });


    }

    // Load Interstitial Ad here ...
    private void loadInterstitialAd(String adId) {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdId(adId);
        interstitialAd.setAdListener(adListener);
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
    }

    // Show Interstitial Ads..
    private void showInterstitial() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            startActivity(new Intent(GameActivity.this, QuizActivity.class).putExtra("topicName", topic));
            finish();
        }
    }

    // Show Add life dialog here....
    private void showAddLifeDialog() {
        if (Integer.parseInt(txtCoin.getText().toString()) >= 300) {

            final Dialog dialogCorrect = new Dialog(GameActivity.this);
            dialogCorrect.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialogCorrect.getWindow() != null) {
                ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
                dialogCorrect.getWindow().setBackgroundDrawable(colorDrawable);
            }
            dialogCorrect.setContentView(R.layout.dialog_addlife);
            dialogCorrect.setCancelable(false);
            dialogCorrect.show();
            FButton btnYes = dialogCorrect.findViewById(R.id.btnYes);
            FButton btnCacel = dialogCorrect.findViewById(R.id.btnCancel);
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogCorrect.dismiss();
                    coinValues = preferences.getInt("coinNow", 100);
                    int life = preferences.getInt("userLifeNow", 3);
                    coinValues = coinValues - 300;
                    life = life + 3;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("coinNow", coinValues);
                    editor.putInt("userLifeNow", life);
                    editor.apply();
                    txtCoin.setText("" + coinValues);
                    txtLife.setText("" + life);

                }
            });

            btnCacel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogCorrect.dismiss();
                }
            });
        } else {
            adCoinDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
