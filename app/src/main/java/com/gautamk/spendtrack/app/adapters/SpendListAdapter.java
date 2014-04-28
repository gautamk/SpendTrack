package com.gautamk.spendtrack.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.gautamk.spendtrack.app.R;
import com.gautamk.spendtrack.app.managers.SpendManager;
import com.gautamk.spendtrack.app.views.HumanizingDateView;

import java.util.List;

/**
 * Created by gautam on 27/4/14.
 */
public class SpendListAdapter extends ArrayAdapter<SpendManager.Spend> {
    final Context context;
    final int resource;
    final List<SpendManager.Spend> objects;
    private final LayoutInflater mInflater;


    public SpendListAdapter(Context context, int resource, List<SpendManager.Spend> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.mInflater = LayoutInflater.from(context);
    }

    static class ViewHolder {
        public final TextView
                amount,
                note,
                tag;
        HumanizingDateView date;

        public ViewHolder(View container) {
            tag = (TextView) container.findViewById(R.id.tag);
            date = (HumanizingDateView) container.findViewById(R.id.date);
            note = (TextView) container.findViewById(R.id.note);
            amount = (TextView) container.findViewById(R.id.amount);
        }
    }

    private void setViewAtPosition(int position, ViewHolder holder) {
        SpendManager.Spend spend = objects.get(position);
        holder.amount.setText("" + spend.getAmount());
        holder.tag.setText(spend.getTag());
        holder.date.setDate(spend.getDate());
        holder.note.setText(spend.getNote());
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_spend_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setViewAtPosition(position, holder);
        return convertView;
    }
}
