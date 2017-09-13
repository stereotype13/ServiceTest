package com.rhodel.servicetest;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ServiceTestActivity extends AppCompatActivity {

    private Integer mNumberOfTicks = 0;
    private TextView mTextView;
    private MyBroadcastReceiver mMyBroadcastReceiver  = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);

        mTextView = (TextView) findViewById(R.id.tvMain);

        if (savedInstanceState != null) {
            mNumberOfTicks = savedInstanceState.getInt("TICKS");
            mTextView.setText(mNumberOfTicks.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mMyBroadcastReceiver = new MyBroadcastReceiver();
        mMyBroadcastReceiver.setListener(new MyBroadcastReceiver.MyBroadCastReceiverListener() {
            @Override
            public void onTick() {
                ++mNumberOfTicks;
                mTextView.setText(mNumberOfTicks.toString());
            }
        });
        IntentFilter intentFilter = new IntentFilter("TICK");
        registerReceiver(mMyBroadcastReceiver, intentFilter);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mMyBroadcastReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("TICKS", mNumberOfTicks);
        super.onSaveInstanceState(outState);
    }


}
