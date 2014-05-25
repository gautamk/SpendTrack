package com.gautamk.spendtrack.app.adapters;

import android.content.Context;
import au.com.bytecode.opencsv.CSVReader;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gautam on 1/5/14.
 */
public class CsvToSpendAdapter {

    private static String resiliantGetFromArray(String[] array, int index, String defaultValue) {
        try {
            return array[index];
        } catch (ArrayIndexOutOfBoundsException e) {
            return defaultValue;
        }
    }

    private static String resiliantGetFromArray(String[] array, int index) {
        return resiliantGetFromArray(array, index, "");
    }

    public static List<SpendManager.Spend> adapt(Context context, String path, char separator, int skipLines) throws IOException, ParseException {
        CSVReader reader = new CSVReader(new FileReader(path), separator, '\'', skipLines);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String[] nextLine;
        ArrayList<SpendManager.Spend> spends = new ArrayList<>();
        while ((nextLine = reader.readNext()) != null) {
            String dateString = resiliantGetFromArray(nextLine, 0);
            float amount = Float.parseFloat(resiliantGetFromArray(nextLine, 1, "0"));
            String note = resiliantGetFromArray(nextLine, 2);
            String tag = resiliantGetFromArray(nextLine, 3);
            SpendManager.Spend spend = new SpendManager.Spend(context);
            spend.setAmount(amount);
            spend.setNote(note);
            spend.setTag(tag);
            spend.setDate(simpleDateFormat.parse(dateString));
            spends.add(spend);
        }
        return spends;
    }


}
