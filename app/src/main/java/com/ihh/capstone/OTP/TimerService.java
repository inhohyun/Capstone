package com.ihh.capstone.OTP;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Timer;

public class TimerService extends Service {

    private Timer timer;
    private int countdownSeconds = 30;
    private Handler handler;
    private Runnable runnable;

    private TimerCallback timerCallback;

    public void setTimerCallback(TimerCallback callback) {
        this.timerCallback = callback;
    }

    public static final String ACTION_TIMER_UPDATE = "com.example.timer.TIMER_UPDATE";

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        startCountdown();
    }
    public interface TimerCallback {
        void onTimerCallback();

        void onServiceConnected(ComponentName componentName, IBinder iBinder);
    }

    private void startCountdown() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                countdownSeconds--;

                if (countdownSeconds <= 0) {
                    countdownSeconds = 30;
                }

                updateCountdownBroadcast();
                if (timerCallback != null) {
                    timerCallback.onTimerCallback();
                }

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(runnable, 1000);
    }

    private void updateCountdownBroadcast() {
        Intent intent = new Intent(ACTION_TIMER_UPDATE);
        intent.putExtra("countdown", countdownSeconds);

        sendBroadcast(intent);
    }

    public int getCountdownSeconds() {
        return countdownSeconds;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new TimerBinder();
    }

    public class TimerBinder extends Binder {
        public TimerService getService() {
            return TimerService.this;
        }
    }
}