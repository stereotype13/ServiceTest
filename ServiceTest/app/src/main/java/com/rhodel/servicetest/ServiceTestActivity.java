package com.rhodel.servicetest;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ServiceTestActivity extends AppCompatActivity {

    private Integer mNumberOfTicks = 0;
    private TextView mTextView;
    private MyBroadcastReceiver mMyBroadcastReceiver  = null;

    private Button mBtnStartStop = null;
    private Button mBtnReset = null;

    private boolean mStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_test);

        mTextView = (TextView) findViewById(R.id.tvMain);
        mBtnStartStop = (Button) findViewById(R.id.btnStartStop);
        mBtnReset = (Button) findViewById(R.id.btnReset);

        if (savedInstanceState != null) {
            mNumberOfTicks = savedInstanceState.getInt("TICKS");
            mTextView.setText(mNumberOfTicks.toString());

            mStarted = savedInstanceState.getBoolean("STARTED");
        }

        if (mStarted) {
            mBtnStartStop.setText("STOP");
            mBtnReset.setEnabled(false);
        } else {
            mBtnStartStop.setText("START");
            mBtnReset.setEnabled(true);
        }

        mBtnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStarted) {
                    mStarted = false;
                    mBtnStartStop.setText("START");
                    mBtnReset.setEnabled(true);
                    Intent stopMyServiceIntent = new Intent(ServiceTestActivity.this, MyService.class);
                    getApplicationContext().stopService(stopMyServiceIntent);

                }
                else {
                    mStarted = true;
                    mBtnStartStop.setText("STOP");
                    mBtnReset.setEnabled(false);

                    IntentFilter intentFilter = new IntentFilter("TICK");
                    registerReceiver(mMyBroadcastReceiver, intentFilter);

                    Intent intent = new Intent(ServiceTestActivity.this, MyService.class);
                    getApplicationContext().startService(intent);
                }

            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNumberOfTicks = 0;
                mTextView.setText(new Integer(0).toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMyBroadcastReceiver == null)
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

        if (mStarted) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!mStarted)
            unregisterReceiver(mMyBroadcastReceiver);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("TICKS", mNumberOfTicks);
        outState.putBoolean("STARTED", mStarted);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {

        if (isFinishing() && !mStarted) {
            Intent stopMyServiceIntent = new Intent(ServiceTestActivity.this, MyService.class);
            stopService(stopMyServiceIntent);
        }

        super.onDestroy();
    }
}
