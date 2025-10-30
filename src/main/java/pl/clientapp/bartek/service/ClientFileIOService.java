package pl.clientapp.bartek.service;

import pl.clientapp.bartek.gui.Table;
import pl.clientapp.bartek.repository.ClientDocumentType;
import pl.clientapp.bartek.repository.ClientModel;
import pl.clientapp.bartek.repository.ClientSex;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    public void exportClients(List<ClientModel> clients){

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formattedDateTime = now.format(dateTimeFormatter);
        String fileName = "ExportedClients_" + formattedDateTime + ".csv";
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String headersLine = joinHeadersWithComaSeparator();
            bufferedWriter.write(headersLine);
            bufferedWriter.newLine();
            for (int i = 0; i < clients.size(); i++) {
                ClientModel client = clients.get(i);
                String firstName = client.getFirstName();
                String lastName = client.getLastName();
                String pesel = client.getPesel();
                ClientSex sex = client.getSex();
                ClientDocumentType document = client.getDocumentType();
                String documentNumber = client.getDocumentNumber();
                String clientLine = firstName + "," + lastName + "," + pesel + "," + sex + "," + document + "," + documentNumber;
                bufferedWriter.write(clientLine);
                bufferedWriter.newLine();


            }

            bufferedWriter.close();
            fileWriter.close();



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String joinHeadersWithComaSeparator(){

        String joinedHeaders = "";
        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            String header = EXPECTED_HEADERS[i];
//            joinedHeaders = joinedHeaders + header + ","; poniżej wersja skrócona
            joinedHeaders += header + ",";
        }


        return joinedHeaders.substring(0, joinedHeaders.length() - 1);
    }
}
