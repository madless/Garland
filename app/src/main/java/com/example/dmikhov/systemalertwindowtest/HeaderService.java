package com.example.dmikhov.systemalertwindowtest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by dmikhov on 13.10.2016.
 */
public class HeaderService extends Service {

    private static String NOTIFICATION_CLICK_ACTION = "CLICK";

    private WindowManager windowManager;
    private ImageView headerView;

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        Log.d("madlog", "service has created!");

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        headerView = new ImageView(this);
        headerView.setImageResource(R.drawable.cute_garland);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.CENTER;
        params.x = 0;
        params.y = 100;

        windowManager.addView(headerView, params);

        headerView.setClickable(false);
        headerView.setFocusable(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getAction() != null && intent.getAction().equals(NOTIFICATION_CLICK_ACTION)) {
            stopForeground(true);
            stopSelf();
        } else {
            Intent clickIntent = new Intent(this, HeaderService.class).setAction(NOTIFICATION_CLICK_ACTION);
            PendingIntent pendingClickIntent = PendingIntent.getService(getApplication(), 100, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.new_year, "", pendingClickIntent);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("Garland")
                    .setSmallIcon(R.drawable.new_year)
                    .setContentText("Happy new year =)")
                    .addAction(R.drawable.ic_close_black, "Close", pendingClickIntent);
            Notification notification = builder.build();
            startForeground(101, notification);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (headerView != null) windowManager.removeView(headerView);
    }

}
