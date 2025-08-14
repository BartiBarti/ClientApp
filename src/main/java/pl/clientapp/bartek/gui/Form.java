package pl.clientapp.bartek.gui;

import pl.clientapp.bartek.repository.ClientDocumentType;
import pl.clientapp.bartek.repository.ClientModel;
import pl.clientapp.bartek.repository.ClientSex;
import pl.clientapp.bartek.service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Map;

public class Form extends JFrame {
    private JPanel jMainPanel;
    private JLabel firstNameLabel;
    private JTextField firstNameTextField;
    private JLabel lastNamelabel;
    private JTextField lastNameTextField;
    private JLabel peselLabel;
    private JTextField peselTextField;
    private JLabel sexLabel;
    private JRadioButton womenRadioButton;
    private JRadioButton manRadioButton;
    private JLabel documentTypeLabel;
    private JComboBox documentTypeComboBox;
    private JLabel documentNumberLabel;
    private JTextField documentNumberTextField;
    private JButton saveButton;

    private ClientService clientService = new ClientService();

    public Form(ClientModel clientToUpdate, Table table) {

        setTitle("Client form");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(table);
        jMainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(jMainPanel);
        ButtonGroup sexGroup = new ButtonGroup();
        sexGroup.add(womenRadioButton);
        sexGroup.add(manRadioButton);

        if (clientToUpdate != null) {
            firstNameTextField.setText(clientToUpdate.getFirstName());
            lastNameTextField.setText(clientToUpdate.getLastName());
            peselTextField.setText(clientToUpdate.getPesel());
            if (ClientSex.MAN.equals(clientToUpdate.getSex())) {
                manRadioButton.setSelected(true);
            } else {
                womenRadioButton.setSelected(true);
            }
            documentTypeComboBox.setSelectedItem(clientToUpdate.getDocumentType().getGuiKey());
            documentNumberTextField.setText(clientToUpdate.getDocumentNumber());
        }

        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameTextField.getText();
                String lastName = lastNameTextField.getText();
                String pesel = peselTextField.getText();
                String sex = "";


                for (Enumeration<AbstractButton> buttons = sexGroup.getElements(); buttons.hasMoreElements(); ) {
                    AbstractButton button = buttons.nextElement();
                    if (button.isSelected()) {
                        sex = button.getText();

                    }
                }

                String documentType = (String) documentTypeComboBox.getSelectedItem();
                String documentNumber = documentNumberTextField.getText();

                Map<String, String> validateMessages = clientService.validateClient(firstName, lastName, pesel, sex,
                        documentType, documentNumber);
                if (!validateMessages.isEmpty()) {
                    String joinedMassages = joinMassages(validateMessages);
                    JOptionPane.showMessageDialog(Form.this, joinedMassages,
                            "Validation ERROR", JOptionPane.WARNING_MESSAGE);

                } else if (clientService.clientExists(pesel, documentNumber) && clientToUpdate == null){
                    JOptionPane.showMessageDialog(Form.this, "Client already exists!", "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    if (clientToUpdate == null){
                        boolean saveSuccess = clientService.createClient(firstName, lastName, pesel, sex,
                                documentType, documentNumber);
                        if (saveSuccess) {
                            JOptionPane.showMessageDialog(Form.this, "Client successfully saved!",
                                    "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(Form.this, " Client UNsussessfully saved!",
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        ClientModel updatedClient = new ClientModel();
                        updatedClient.setFirstName(firstName);
                        updatedClient.setLastName(lastName);
                        updatedClient.setPesel(pesel);
                        updatedClient.setSex(ClientSex.valueFromGuiKey(sex));
                        updatedClient.setDocumentType(ClientDocumentType.valueFromGuiKey(documentType));
                        updatedClient.setDocumentNumber(documentNumber);
                        updatedClient.setId(clientToUpdate.getId());


                        boolean updateSuccess = clientService.updateClient(updatedClient);
                        if (updateSuccess) {
                            JOptionPane.showMessageDialog(Form.this, "Client sccessfully updated",
                                    "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(Form.this, " Client UNsussessfully updated!",
                                    "ERROR", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                }
            }
        });
    }



    private String joinMassages(Map<String, String> validateMessages) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : validateMessages.entrySet()) {

            stringBuilder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }


        return stringBuilder.toString();

    }
}
