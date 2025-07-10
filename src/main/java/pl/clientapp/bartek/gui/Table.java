package pl.clientapp.bartek.gui;

import pl.clientapp.bartek.repository.ClientDocumentType;
import pl.clientapp.bartek.repository.ClientModel;
import pl.clientapp.bartek.repository.ClientSex;
import pl.clientapp.bartek.service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Table extends JFrame {
    private JLabel searchLabel;
    private JTextField searchTextField;
    private JScrollPane scrollPanel;
    private JTable clientsTable;
    private JButton deleteButton;
    private JButton editButton;
    private JButton addButton;
    private JPanel mainPanel;

    private ClientService clientService = new ClientService();

    public Table() {
        setTitle("Clients table");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);
        showMenuBar();
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadTable();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new Form(null).setVisible(true));
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAction();
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = clientsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "No clients selected yet!",
                            "WARNING", JOptionPane.WARNING_MESSAGE);
                } else {
                    int selectedClientID = (int) clientsTable.getValueAt(selectedRow, 0 );
                    String selectedFirstName = (String) clientsTable.getValueAt(selectedRow, 1);
                    String selectedLastName = (String) clientsTable.getValueAt(selectedRow, 2);
                    String selectedPesel = (String) clientsTable.getValueAt(selectedRow, 3);
                    ClientSex selectedSex = (ClientSex) clientsTable.getValueAt(selectedRow, 4);
                    ClientDocumentType  selectedDocumentType = (ClientDocumentType) clientsTable.getValueAt(selectedRow, 5);
                    String selectedDocumentNumber = (String) clientsTable.getValueAt(selectedRow, 6);
                    ClientModel clientModel = new ClientModel();
                    clientModel.setId(selectedClientID);
                    clientModel.setFirstName(selectedFirstName);
                    clientModel.setLastName(selectedLastName);
                    clientModel.setPesel(selectedPesel);
                    clientModel.setSex(selectedSex);
                    clientModel.setDocumentType(selectedDocumentType);
                    clientModel.setDocumentNumber(selectedDocumentNumber);
                    SwingUtilities.invokeLater(() -> new Form(clientModel).setVisible(true));
                }

            }
        });
    }

    private void deleteAction() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "No clients selected yet!",
                    "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want \n to delete this client?",
                    "CONFIRMATION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirmation == JOptionPane.YES_OPTION) {
                int selectedClientID = (int) clientsTable.getValueAt(selectedRow, 0);
                clientService.deleteClient(selectedClientID);
                JOptionPane.showMessageDialog(null, "Client Deleted",
                        "DELETED", JOptionPane.INFORMATION_MESSAGE);
                loadTable();
            }
        }
    }

    private void loadTable() {
        String[] headers = new String[]{"ID", "First name", "Last name", "Pesel", "Sex", "Document Type", "Document number"};
        DefaultTableModel tableModel = new DefaultTableModel(headers, 0);
        tableModel.setRowCount(0);

        List<ClientModel> clientModelList = clientService.getAllClients();
        for (ClientModel client : clientModelList) {
            tableModel.addRow(new Object[]{client.getId(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getPesel(),
                    client.getSex(),
                    client.getDocumentType(),
                    client.getDocumentNumber()});

        }

        clientsTable.setModel(tableModel);
    }

    private void showMenuBar() {

        JMenuBar jMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem importMenuItem = new JMenuItem("Import");
        importMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem exportMenuItem = new JMenuItem("Export");
        exportMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.setPreferredSize(new Dimension(150, 30));
        closeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        });

        fileMenu.add(importMenuItem);
        fileMenu.add(exportMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(closeMenuItem);
        jMenuBar.add(fileMenu);


        JMenu editMenu = new JMenu("Edit");
        JMenuItem addClientMenuItem = new JMenuItem("Add Client");
        addClientMenuItem.setPreferredSize(new Dimension(150, 30));
        addClientMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new Form(null).setVisible(true));
            }
        });
        JMenuItem editSelectedMenuItem = new JMenuItem("Edit Selected");
        editSelectedMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem deleteSelectedMenuItem = new JMenuItem("Delete Selected");
        deleteSelectedMenuItem.setPreferredSize(new Dimension(150, 30));
        deleteSelectedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAction();
            }
        });
        JMenuItem clearAllClients = new JMenuItem("Clear All Clients");
        clearAllClients.setPreferredSize(new Dimension(150, 30));
        editMenu.add(addClientMenuItem);
        editMenu.add(editSelectedMenuItem);
        editMenu.add(deleteSelectedMenuItem);
        editMenu.add(clearAllClients);
        jMenuBar.add(editMenu);


        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshTableMenuItem = new JMenuItem("Refresh Table");
        refreshTableMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem sortClientsMenuItem = new JMenuItem("Sort Clients");
        sortClientsMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem showStatisticsMenuItem = new JMenuItem("Show Statistics");
        showStatisticsMenuItem.setPreferredSize(new Dimension(150, 30));
        viewMenu.add(refreshTableMenuItem);
        viewMenu.add(sortClientsMenuItem);
        viewMenu.add(showStatisticsMenuItem);
        jMenuBar.add(viewMenu);


        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//                todo utworzyć okno "About"
//                JFrame aboutFrame = new JFrame("About");
//
//                JLabel aboutLabel = new JLabel("<html><center>Aplikacja bazodanowa klientów<br/>Version 1.0<br/>© 2025</center></html>", SwingConstants.CENTER);
//                aboutLabel.setFont(new Font("Arial", Font.PLAIN, 14));
//                aboutFrame.add(aboutLabel);
//
//                aboutFrame.setSize(300, 150);
//                aboutFrame.setLocationRelativeTo(null);
//                aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Zamknięcie tylko tego okna
//                aboutFrame.setResizable(false);
//                aboutFrame.setVisible(true);
//                System.out.println("About - otwarcie okna");

                
                JOptionPane.showMessageDialog(null,
                        "<html><center>Aplikacja bazodanowa klientów<br/>Version 1.0<br/>© 2025</center></html>", "About",
                        JOptionPane.INFORMATION_MESSAGE );
            }
        });
        aboutMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem shortcutsMenuItem = new JMenuItem("Shortcuts");
        shortcutsMenuItem.setPreferredSize(new Dimension(150, 30));
        helpMenu.add(aboutMenuItem);
        helpMenu.add(shortcutsMenuItem);
        jMenuBar.add(helpMenu);
        setJMenuBar(jMenuBar);


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Table().setVisible(true));
    }


}




