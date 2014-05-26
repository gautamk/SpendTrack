package com.gautamk.spendtrack.app.async_tasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by gautam on 26/5/14.
 */
public abstract class NotificationAsyncTask<ARGS, PROGRESS, RESULT> extends AsyncTask<ARGS, PROGRESS, RESULT> {
    protected final NotificationManager mNotifyManager;
    protected final Notification.Builder mBuilder;
    protected final Context mContext;
    protected final int UNIQUE_ID;

    public NotificationAsyncTask(int UNIQUE_ID, Context mContext) {
        this.mContext = mContext;
        this.mNotifyManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        this.mBuilder = new Notification.Builder(mContext);
        this.mBuilder.setSmallIcon(android.R.color.transparent);
        this.UNIQUE_ID = UNIQUE_ID;

    }

    protected void updateNotification() {
        mNotifyManager.notify(UNIQUE_ID, mBuilder.build());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        notificationLocked(true);
    }


    protected void notificationLocked(boolean lock) {
        mBuilder.setOngoing(lock);
        updateNotification();
    }

    @Override
    protected void onPostExecute(RESULT o) {
        super.onPostExecute(o);
        notificationLocked(false);
    }

    @Override
    protected void onCancelled(RESULT o) {
        super.onCancelled(o);
        notificationLocked(false);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        notificationLocked(false);
    }
}
