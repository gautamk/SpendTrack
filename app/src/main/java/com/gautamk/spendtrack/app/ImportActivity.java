package com.gautamk.spendtrack.app;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;
import android.widget.Toast;
import com.gautamk.spendtrack.app.adapters.CsvToSpendAdapter;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ImportActivity extends Activity {
    private static final int PICKFILE_RESULT_CODE = 220;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_csv);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_import_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String path = data.getData().getPath();
                    Toast.makeText(this, path, Toast.LENGTH_LONG).show();
                    ImportFromCSVAsyncTask importFromCSVAsyncTask = new ImportFromCSVAsyncTask(this);
                    importFromCSVAsyncTask.execute(path);
                    startActivity(new Intent(this, ImportActivity.class));
                }
                break;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_import, container, false);
            Button button = (Button) rootView.findViewById(R.id.import_csv);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                    fileintent.setType("text/csv");
                    ImportActivity.this.startActivityForResult(fileintent, PICKFILE_RESULT_CODE);
                }
            });
            return rootView;
        }
    }
}

class ImportFromCSVAsyncTask extends AsyncTask<String, Integer, Boolean> {
    final Context mContext;
    final static int PROGRESS_ID = 231;
    NotificationManager mNotifyManager;
    Notification.Builder mBuilder;

    ImportFromCSVAsyncTask(Context context) {
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
        mBuilder.setProgress(values[0], values[1], false);
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
