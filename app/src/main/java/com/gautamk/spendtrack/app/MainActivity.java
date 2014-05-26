package com.gautamk.spendtrack.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.gautamk.spendtrack.app.async_tasks.ImportFromCSVAsyncTask;
import com.gautamk.spendtrack.app.managers.SpendManager;


public class MainActivity extends Activity implements AddSpendFragement.OnSpentFragmentInteractionListener {
    private final Fragment defaultFragment = new ListSpendsFragment(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, defaultFragment)
                    .commit();
        }
    }

    protected void replaceFragment(int container, Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected void showAddSpendFragment() {
        replaceFragment(R.id.container, new AddSpendFragement());
    }

    protected void showUpdateSpendFragment(SpendManager.Spend spend) {
        replaceFragment(R.id.container, new AddSpendFragement(spend));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.add_spend:
                this.showAddSpendFragment();
                return true;
            case R.id.action_settings:
                this.replaceFragment(R.id.container, new SettingsFragment());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void closeSpend() {
        replaceFragment(R.id.container, defaultFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        switch (requestCode) {
            case SettingsFragment.PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String path = data.getData().getPath();
                    Toast.makeText(this, path, Toast.LENGTH_LONG).show();
                    ImportFromCSVAsyncTask importFromCSVAsyncTask = new ImportFromCSVAsyncTask(this);
                    importFromCSVAsyncTask.execute(path);
                }
                break;
        }
    }
}
