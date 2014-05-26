package com.gautamk.spendtrack.app.adapters;

import au.com.bytecode.opencsv.CSVWriter;
import com.gautamk.spendtrack.app.managers.SpendManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by gautam on 25/5/14.
 */
public class SpendToCsvAdapter {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void writeSpendToCSV(SpendManager.Spend spend, CSVWriter csvWriter) {
        csvWriter.writeNext(new String[]{
                simpleDateFormat.format(spend.getDate()),
                Float.toString(spend.getAmount()),
                spend.getNote(),
                spend.getTag()
        });
    }

    public static void adapt(String path) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        CSVWriter csvWriter = new CSVWriter(bufferedWriter,';','\'');
        try {
            bufferedWriter.write("Date,Amount,Note,Tag\n");
            List<SpendManager.Spend> spends = SpendManager.list();
            for (SpendManager.Spend spend : spends) {
                writeSpendToCSV(spend,csvWriter);
            }
        } finally {
            csvWriter.close();
            bufferedWriter.close();
        }
    }
}
