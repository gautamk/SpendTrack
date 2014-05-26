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

}
