package com.rhodel.servicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rhodel on 9/13/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    protected MyBroadCastReceiverListener mListener = null;

    public interface MyBroadCastReceiverListener {
        void onTick();
    }

    public void setListener(MyBroadCastReceiverListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mListener != null)
            mListener.onTick();
    }
}
