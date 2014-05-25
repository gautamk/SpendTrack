package com.gautamk.spendtrack.app.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import com.orm.SugarRecord;
import com.orm.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by gautam on 27/4/14.
 */
public class SpendManager {
    public static void add(Spend spend) { //TODO
        spend.save();
    }

    public static Spend get(long id) {// TODO
        return Spend.findById(Spend.class, id);
    }

    public static List<Spend> list() {
        List<Spend> spends = (List<Spend>) Select.from(Spend.class).orderBy("date desc").list();
        return spends;
    }

    public static void delete(Spend spend) {
        spend.delete();
    }

    public static void truncate() {
        Spend.executeQuery("DELETE FROM Spend");
        Spend.executeQuery("VACUUM");
    }

    public static void update(Spend spend) {
        spend.save();
    }

    public static final class Spend extends SugarRecord<Spend> {
        private float amount;
        private String note, tag;
        private long date;

        public Spend(Context context) {
            super(context);
            this.note = this.tag = "";
            this.amount = 0f;
            this.date = new Date().getTime();
        }

        public Spend(Context context, float amount, String note, String tag, Date date) {
            super(context);
            this.amount = amount;
            this.note = note;
            this.tag = tag;
            this.date = date.getTime();
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Date getDate() {
            return new Date(this.date);
        }

        public void setDate(Date date) {
            this.date = date.getTime();
        }
    }

    public static class ClearSpendsAsyncTask extends AsyncTask<Void, Integer, Boolean> {
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

        protected List<Spend> getAllSpends() {
            mBuilder.setContentTitle("Fetching Spends").setContentText("Fetching spends to delete");
            mBuilder.setProgress(0, 0, true);
            mNotifyManager.notify(PROGRESS_ID, mBuilder.build());
            return SpendManager.list();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<Spend> spends = getAllSpends();
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
}
