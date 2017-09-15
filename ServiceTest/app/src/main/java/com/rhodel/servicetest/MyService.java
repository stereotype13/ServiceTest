package com.rhodel.servicetest;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by rhodel on 9/13/2017.
 */

public class MyService extends IntentService {

    private boolean mKeepLooping = true;


    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        long startTime = System.currentTimeMillis();

        while (mKeepLooping) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 1000) {
                startTime = System.currentTimeMillis();
                Intent updateTimeIntent = new Intent("TICK");

                sendBroadcast(updateTimeIntent);
            }
        }

    }

    @Override
    public void onDestroy() {
        mKeepLooping = false;
        super.onDestroy();
    }
}
