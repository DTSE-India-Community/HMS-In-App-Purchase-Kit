package com.huawei.hmsinapppurchaseexample.activity;

/**
 * Name of the project QUIZ MANIA.
 * Created by Sanghati Mukherjee.
 * Huawei Technologies Co., Ltd.
 * sanghati.mukherjee@huawei.com
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.hmsinapppurchaseexample.R;

import java.util.Collections;
import java.util.List;

import com.huawei.hmsinapppurchaseexample.db.QuizManiaHelper;
import info.hoang8f.widget.FButton;
import com.huawei.hmsinapppurchaseexample.model.Questions;

public class QuizActivity extends AppCompatActivity {

    FButton btnA, btnB, btnC, btnD;
    TextView txtQuestion, txtTitle, txtLife, txtResult, txtCoin;
    QuizManiaHelper quizManiaHelper;
    Questions questionObj;
    List<Questions> list;
    int quesionId = 0;
    int coinValue = 0;
    CountDownTimer countDownTimer;
    Typeface tb, sb;
    SharedPreferences preferences;
    int life = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quiz);
        txtQuestion = findViewById(R.id.txtQuestion);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);
        txtTitle = findViewById(R.id.txtTitle);
        txtLife = findViewById(R.id.txtLife);
        txtResult = findViewById(R.id.txtResult);
        txtCoin = findViewById(R.id.txtCoin);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        life = preferences.getInt("userLifeNow", 3);
        coinValue = preferences.getInt("coinNow", 100);
        txtLife.setText("" + life);

        tb = Typeface.createFromAsset(getAssets(), "fonts/TitilliumWeb-Bold.ttf");
        sb = Typeface.createFromAsset(getAssets(), "fonts/shablagooital.ttf");
        txtTitle.setTypeface(sb);
        txtQuestion.setTypeface(tb);
        btnA.setTypeface(tb);
        btnB.setTypeface(tb);
        btnC.setTypeface(tb);
        btnD.setTypeface(tb);
        txtResult.setTypeface(sb);
        txtCoin.setTypeface(tb);

        quizManiaHelper = new QuizManiaHelper(this);
        quizManiaHelper.getWritableDatabase();

        // The logic for changing the question pattern by topic..
        String topicName = getIntent().getStringExtra("topicName");
        if (quizManiaHelper.getAllListOfQuestions(topicName).size() == 0) {
            quizManiaHelper.listOfAllQuestion();
        }
        list = quizManiaHelper.getAllListOfQuestions(topicName);

        // Shuffle the list here...
        Collections.shuffle(list);
        questionObj = list.get(quesionId);

        // Question And Option on Screen will be shown..
        onNextQuestionAndOption();


    }


    public void onNextQuestionAndOption() {

        btnA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));
        btnB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));
        btnC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));
        btnD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));

        //This method will setText for que and options
        txtQuestion.setText(questionObj.getQuestion());
        btnA.setText(questionObj.getOptionA());
        btnB.setText(questionObj.getOptionB());
        btnC.setText(questionObj.getOptionC());
        btnD.setText(questionObj.getOptionD());
        txtCoin.setText(String.valueOf(coinValue));

    }

    //Onclick listener for first button
    public void btnA(View view) {
        //compare the option with the ans if yes then make button color green
        if (questionObj.getOptionA().equals(questionObj.getAnswer())) {
            btnA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_nephritis));
            //Check if user has not exceeds the que limit
            if (quesionId < list.size() - 1) {
                disableAllButtons();
                correctAnsDialog();
            } else {
                gameWon();
            }
        } else {
            wrongAnsDialog();
        }
    }

    //Onclick listener for sec button
    public void btnB(View view) {
        if (questionObj.getOptionB().equals(questionObj.getAnswer())) {
            btnB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_nephritis));
            if (quesionId < list.size() - 1) {
                disableAllButtons();
                correctAnsDialog();
            } else {
                gameWon();
            }
        } else {
            wrongAnsDialog();
        }
    }

    //Onclick listener for third button
    public void btnC(View view) {
        if (questionObj.getOptionC().equals(questionObj.getAnswer())) {
            btnC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_nephritis));
            if (quesionId < list.size() - 1) {
                disableAllButtons();
                correctAnsDialog();
            } else {
                gameWon();
            }
        } else {
            wrongAnsDialog();
        }
    }

    //Onclick listener for fourth button
    public void btnD(View view) {
        if (questionObj.getOptionD().equals(questionObj.getAnswer())) {
            btnD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_nephritis));
            if (quesionId < list.size() - 1) {
                disableAllButtons();
                correctAnsDialog();
            } else {
                gameWon();
            }
        } else {
            wrongAnsDialog();
        }
    }



    //This method will navigate from current activity to GameWon
    public void gameWon() {
        Intent intent = new Intent(this, GameWonActivity.class);
        startActivity(intent);
        finish();
    }

    //This method is called when user loose its 3 life..
    //this method will navigate user to the activity PlayAgain..
    public void gameLostPlayAgain() {
        Intent intent = new Intent(this, PlayAgainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }

    public void correctAnsDialog() {
        final Dialog dialogCorrect = new Dialog(QuizActivity.this);
        dialogCorrect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogCorrect.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogCorrect.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogCorrect.setContentView(R.layout.dialog_correct);
        dialogCorrect.setCancelable(false);
        dialogCorrect.show();

        TextView correctText = (TextView) dialogCorrect.findViewById(R.id.text_title);
        ImageView imgNext = dialogCorrect.findViewById(R.id.image_close);

        correctText.setTypeface(sb);

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //This will dismiss the dialog
                dialogCorrect.dismiss();
                //it will increment the question number
                quesionId++;
                //get the que and 4 option and store in the questionObj
                questionObj = list.get(quesionId);

                // Adding the 20 coin here ...
                coinValue = coinValue + 20;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("coinNow", coinValue);
                editor.apply();

                //Now this method will set the new que and 4 options
                onNextQuestionAndOption();
                //reset the color of buttons back to white
                resetButtonColors();
                //Enable button - remember we had disable them when user ans was correct in there particular button methods
                enableAllButtons();
            }
        });
    }

    public void wrongAnsDialog() {
        final Dialog dialogCorrect = new Dialog(QuizActivity.this);
        dialogCorrect.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialogCorrect.getWindow() != null) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialogCorrect.getWindow().setBackgroundDrawable(colorDrawable);
        }
        dialogCorrect.setContentView(R.layout.dialog_wrong);
        dialogCorrect.setCancelable(false);
        dialogCorrect.show();

        TextView correctText = (TextView) dialogCorrect.findViewById(R.id.text_title);
        ImageView imgNext = dialogCorrect.findViewById(R.id.image_close);

        correctText.setTypeface(sb);

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCorrect.dismiss();
                life--;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("userLifePrev", life);
                editor.apply();
                txtLife.setText("" + life);
                if (life <= 0) {
                    gameLostPlayAgain();
                }
            }
        });
    }


    //This method will make button color white again since our one button color was turned green
    public void resetButtonColors() {
        btnA.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));
        btnB.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));
        btnC.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));
        btnD.setButtonColor(ContextCompat.getColor(getApplicationContext(), R.color.fbutton_color_midnight_blue));
    }

    //This method will disable all the option button
    public void disableAllButtons() {
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
    }

    //This method will all enable the option buttons
    public void enableAllButtons() {
        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);
    }




}
