package pl.clientapp.bartek.service;

import pl.clientapp.bartek.gui.Table;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class ClientFileIOService {


    private static final String[] EXPECTED_HEADERS = {"FIRST_NAME", "LAST_NAME", "PESEL", "SEX", "DOCUMENT_TYPE", "DOCUMENT_NUMBER"};


    public Boolean checkHeader(String line, Table table) {
        String[] headersFromFile = line.split(",");
        if (headersFromFile.length != 6) {
            JOptionPane.showMessageDialog(table,
                    "Wrong headers number!",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            for (int i = 0; i < headersFromFile.length; i++) {
                if (!Objects.equals(headersFromFile[i], EXPECTED_HEADERS[i])) {
                    JOptionPane.showMessageDialog(table,
                            "Wrong Headers Names",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                    return false;

                }
            }
        }
        return true;
    }


    public void writeToSummaryFile(String summaryLine) {

        String summaryFileName = "SummaryFile.txt";
        try {
            FileWriter fileWriter = new FileWriter(summaryFileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(summaryLine);
            bufferedWriter.newLine();
            bufferedWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
