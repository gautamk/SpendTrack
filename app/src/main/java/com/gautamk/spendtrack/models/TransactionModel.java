package com.gautamk.spendtrack.models;

import com.orm.SugarRecord;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by gautam on 31/1/15.
 */
public class TransactionModel extends SugarRecord<TransactionModel> {
    double value;
    String note;
    String when;
    String created_at;
    String modified_at;

    public TransactionModel(double value, String note, Date when) {
        this.value = value;
        this.note = note;
        this.when = new DateTime(when).toString();
        this.created_at = new DateTime().toString();
    }

    @Override
    public void save() {
        modified_at = new DateTime().toString();
        super.save();
    }
}
