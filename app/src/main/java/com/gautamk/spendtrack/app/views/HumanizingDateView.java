package com.gautamk.spendtrack.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * Created by gautam on 27/4/14.
 */
public class HumanizingDateView extends TextView {
    private final PrettyTime p = new PrettyTime();
    private Date date = null;

    public HumanizingDateView(Context context) {
        super(context);
    }

    public HumanizingDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HumanizingDateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void setDateAsText() {
        if (date == null) {
            this.setText("No date set");
        } else {
            this.setText(p.format(date));
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        this.setDateAsText();
    }

}
