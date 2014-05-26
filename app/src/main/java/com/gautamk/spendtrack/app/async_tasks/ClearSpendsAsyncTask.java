package com.gautamk.spendtrack.app.async_tasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.util.List;

/**
* Created by gautam on 25/5/14.
*/
public class ClearSpendsAsyncTask extends AsyncTask<Void, Integer, Boolean> {
    private static final int PROGRESS_ID = 2232;
    Context mContext;
    private NotificationManager mNotifyManager;
    private Notification.Builder mBuilder;

    public ClearSpendsAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mNotifyManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(mContext);
        mBuilder.setSmallIcon(android.R.drawable.ic_delete);
        mBuilder.setOngoing(true);
        mNotifyManager.notify(PROGRESS_ID, mBuilder.build());
    }

    protected List<SpendManager.Spend> getAllSpends() {
        mBuilder.setContentTitle("Fetching Spends").setContentText("Fetching spends to delete");
        mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(PROGRESS_ID, mBuilder.build());
        return SpendManager.list();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        List<SpendManager.Spend> spends = getAllSpends();
        mBuilder.setContentTitle("Deleting Spends").setContentText("Deleting Spends");
        int index = 0;
        for (SpendManager.Spend spend : spends) {
            SpendManager.delete(spend);
            publishProgress(spends.size(), ++index);
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int max = values[0],
                progress = values[1];
        mBuilder.setProgress(max, progress, false).setContentText("Deleting spend " + progress + " of " + max);
        mNotifyManager.notify(PROGRESS_ID, mBuilder.build());
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mBuilder.setContentText(aBoolean ? "Delete all complete" : "Delete allFailed");
        mBuilder.setProgress(0, 0, false);
        mBuilder.setOngoing(false);
        mNotifyManager.notify(PROGRESS_ID, mBuilder.build());
        super.onPostExecute(aBoolean);
    }
}
