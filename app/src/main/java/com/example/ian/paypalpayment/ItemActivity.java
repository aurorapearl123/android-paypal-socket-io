package com.example.ian.paypalpayment;

import android.os.CountDownTimer;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.paypalpayment.Model.Item;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemActivity extends AppCompatActivity {

    @BindView(R.id.id_item_image_view) ImageView _item_image;
    @BindView(R.id.id_item_name_view) TextView _item_name;
    @BindView(R.id.id_item_description_view) TextView _item_description;
    @BindView(R.id.id_item_model_view) TextView _item_model;
    @BindView(R.id.id_item_price_view) TextView _item_price;
    @BindView(R.id.id_item_group_size_view) TextView _item_group_size;
    @BindView(R.id.id_text_view_countdown) TextView _item_view_countdown;
    @BindView(R.id.id_bet_button) ImageButton _id_bet_button;
    @BindView(R.id.id_bet_value) TextView _id_bet_value;

    private CountDownTimer mCountDownTimer;
    //private static final long START_TIME_IN_MILLIS = 600000;
    private static final long START_TIME_IN_MILLIS = 300000;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private boolean mTimerRunning;
    private int bet_value = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ButterKnife.bind(this);

        //final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(mToolbar);

       // getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);


        setItemDetail();

        startTimer();
        _id_bet_value.setText(bet_value+"");

        _id_bet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
                //--bet_value;
                _id_bet_value.setText(--bet_value+"");
            }
        });




    }

    public void setItemDetail()
    {
        Item item = getIntent().getParcelableExtra("item_data");

        Log.wtf("items", item.toString()+"");

        Glide.with(getApplicationContext())
                .load(item.getImage_path())
                //.apply(RequestOptions.circleCropTransform())
                .into(_item_image);

        _item_name.setText(item.getName());
        _item_description.setText(item.getDescription());
        _item_model.setText(item.getModel());
        _item_price.setText(item.getPrice());
        _item_group_size.setText(item.getGroup_size());
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                //mButtonStartPause.setText("Start");
                //mButtonStartPause.setVisibility(View.INVISIBLE);
                //mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();

        mTimerRunning = true;
        //mButtonStartPause.setText("pause");
        //mButtonReset.setVisibility(View.INVISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        _item_view_countdown.setText(timeLeftFormatted);
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
    }
}
