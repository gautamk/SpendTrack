package com.gautamk.spendtrack.app.async_tasks;

import android.content.Context;
import au.com.bytecode.opencsv.CSVWriter;
import com.gautamk.spendtrack.app.adapters.SpendToCsvAdapter;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by gautam on 26/5/14.
 */
public class ExportCSVAsyncTask extends NotificationAsyncTask<String, Integer, Boolean> {
    PostExecuteCallBack postExecuteCallBack = null;

    public ExportCSVAsyncTask(Context mContext) {
        super(1532, mContext);
    }

    public ExportCSVAsyncTask(Context mContext, PostExecuteCallBack postExecuteCallBack) {
        super(1532, mContext);
        this.postExecuteCallBack = postExecuteCallBack;
    }

    @Override
    protected void onPreExecute() {
        mBuilder.setSmallIcon(android.R.drawable.ic_menu_upload)
                .setProgress(0, 0, true)
                .setContentTitle("Export CSV");
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean success) {
        mBuilder.setProgress(0, 0, false).setContentText(success ? "successfully completed" : "failed");
        super.onPostExecute(success);
        if (postExecuteCallBack != null) postExecuteCallBack.postExecute(success);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int max = values[0],
                progress = values[1];
        mBuilder.setProgress(max, progress, false)
                .setContentText("Exporting Spend " + progress + " of " + max);
        updateNotification();
    }

    protected CSVWriter[] buildCSVWriter(String[] paths) {
        CSVWriter[] writers = new CSVWriter[paths.length];
        int index = 0;
        for (String path : paths) {
            BufferedWriter bufferedWriter;
            try {
                bufferedWriter = new BufferedWriter(new FileWriter(path));
                bufferedWriter.write("Date,Amount,Note,Tag\n");
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            writers[index++] = SpendToCsvAdapter.buildCSVWriter(bufferedWriter);
        }
        return writers;
    }

    @Override
    protected Boolean doInBackground(String... paths) {
        CSVWriter[] csvWriters = buildCSVWriter(paths);
        if (csvWriters.length < 1) {
            return false;
        } else {
            List<SpendManager.Spend> spends = SpendManager.list();
            int index = 0;
            for (CSVWriter writer : csvWriters) {
                for (SpendManager.Spend spend : spends) {
                    publishProgress(spends.size(), ++index);
                    SpendToCsvAdapter.writeSpendToCSV(spend, writer);
                }
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    }

    public static interface PostExecuteCallBack {
        public void postExecute(Boolean success);
    }
}
