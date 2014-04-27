package com.gautamk.spendtrack.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.ListView;
import com.gautamk.spendtrack.app.adapters.SpendListAdapter;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.util.List;


public class MainActivity extends Activity implements AddSpendFragement.OnSpentFragmentInteractionListener {
    private final Fragment defaultFragment = new ListSpendsFragment();

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ListSpendsFragment extends Fragment {
        ListView spendList;

        public ListSpendsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            this.spendList = (ListView) rootView.findViewById(R.id.spend_list);
            List<SpendManager.Spend> spends = SpendManager.list();
            SpendListAdapter spendListAdapter = new SpendListAdapter(getActivity(), R.id.spend_list, spends);
            spendList.setAdapter(spendListAdapter);
            return rootView;
        }


    }
}
