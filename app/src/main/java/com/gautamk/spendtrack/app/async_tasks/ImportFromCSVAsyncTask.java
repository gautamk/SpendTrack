package com.gautamk.spendtrack.app.async_tasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import com.gautamk.spendtrack.app.adapters.CsvToSpendAdapter;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautam on 25/5/14.
 */
public class ImportFromCSVAsyncTask extends AsyncTask<String, Integer, Boolean> {
    final Context mContext;
    final static int PROGRESS_ID = 231;
    NotificationManager mNotifyManager;
    Notification.Builder mBuilder;

    public ImportFromCSVAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mNotifyManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new Notification.Builder(mContext);
        mBuilder.setContentTitle("Importing CSV").setContentText("Import Progress");
        mBuilder.setSmallIcon(android.R.drawable.ic_input_add);
        mBuilder.setOngoing(true);
        mNotifyManager.notify(PROGRESS_ID, mBuilder.build());

    }

    protected List<SpendManager.Spend> parseCSVFiles(String[] csvFilePaths) throws IOException, ParseException {
        List<SpendManager.Spend> spends = new ArrayList<>();
        for (String csvPath : csvFilePaths) {
            spends.addAll(CsvToSpendAdapter.adapt(mContext, csvPath, ';', 1));
        }
        return spends;
    }


    @Override
    protected Boolean doInBackground(String... csvFilePaths) {
        List<SpendManager.Spend> spends = null;
        try {
            spends = parseCSVFiles(csvFilePaths);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        int index = 0;
        for (SpendManager.Spend spend : spends) {
            SpendManager.add(spend);
            publishProgress(spends.size(), ++index);
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int max = values[0],
                progress = values[1];
        mBuilder.setProgress(max, progress, false).setContentText("Importing spend " + progress + " of " + max);
        mBuilder.setProgress(max, progress  , false);
        mNotifyManager.notify(PROGRESS_ID, mBuilder.build());
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mBuilder.setContentText(aBoolean ? "Import complete" : "Import Failed");
        mBuilder.setProgress(0, 0, false);
        mBuilder.setOngoing(false);
        mNotifyManager.notify(PROGRESS_ID, mBuilder.build());
        super.onPostExecute(aBoolean);
    }
}
