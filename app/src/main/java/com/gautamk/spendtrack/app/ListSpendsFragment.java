package com.gautamk.spendtrack.app;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.gautamk.spendtrack.app.adapters.SpendListAdapter;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListSpendsFragment extends ListFragment implements AdapterView.OnItemLongClickListener {

    private MainActivity mainActivity;
    final List<SpendManager.Spend> spends = new ArrayList<SpendManager.Spend>();
    SpendListAdapter spendListAdapter;

    public ListSpendsFragment(MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }

    private void updateSpends() {
        this.spends.clear();
        spends.addAll(SpendManager.list());
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spendListAdapter = new SpendListAdapter(getActivity(), R.layout.fragment_list_spends, spends);
        setListAdapter(spendListAdapter);
        updateSpends();
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        SpendManager.Spend spend = spends.get(position);
        mainActivity.showUpdateSpendFragment(spend);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        SpendManager.Spend spend = spends.get(position);
        SpendManager.delete(spend);
        spends.clear();
        spends.addAll(SpendManager.list());
        spendListAdapter.notifyDataSetChanged();
        return true;
    }
}
