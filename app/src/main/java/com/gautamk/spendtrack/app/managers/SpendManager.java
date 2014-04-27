package com.gautamk.spendtrack.app.managers;

import android.content.Context;
import com.orm.SugarRecord;

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
        return Spend.listAll(Spend.class);
    }

    public static void delete(Spend spend) {
        spend.delete();
    }

    public static void update(Spend spend) {
        spend.save();
    }

    public static final class Spend extends SugarRecord<Spend> {
        public float amount;
        public String note, tag;
        public Date date;

        public Spend(Context context) {
            super(context);
            this.date = new Date();
            this.amount = 0;
            this.note = this.tag = "";
        }

        public Spend(Context context, float amount, String note, String tag, Date date) {
            super(context);
            this.amount = amount;
            this.note = note;
            this.tag = tag;
            this.date = date;
        }
    }
}
