package pl.clientapp.bartek.gui;

import pl.clientapp.bartek.repository.ClientAgeRange;
import pl.clientapp.bartek.repository.ClientDocumentType;
import pl.clientapp.bartek.repository.ClientModel;
import pl.clientapp.bartek.repository.ClientSex;
import pl.clientapp.bartek.service.ClientFileIOService;
import pl.clientapp.bartek.service.ClientService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Table extends JFrame {
    private JLabel searchLabel;
    private JTextField searchTextField;
    private JScrollPane scrollPanel;
    private JTable clientsTable;
    private JButton deleteButton;
    private JButton editButton;
    private JButton addButton;
    private JPanel mainPanel;
    private JButton refreshButton;

    private static Table table;

    private ClientService clientService = new ClientService();

    private ClientFileIOService clientFileIOService = new ClientFileIOService();

    public Table() {
        setTitle("Clients table");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(Table.this);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);
        showMenuBar();
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadTable(null);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new Form(null, table).setVisible(true));
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
                editClientAction();

            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTable(null);
            }
//            Table form - przycisk/ Nazwa przycisku - do kodu, text - nazwa przycisku/ stworzenie - action listener - create listener
//            utworzył się ActionPerformed - my dodaliśmy metodę loadTable, żeby na się odświeżyła tabela. (ale metody dodajemy jakie chcemy, żeby coś przycisk robił
        });
        refreshButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                int keyPressed = e.getKeyCode();

                if (KeyEvent.VK_R == keyPressed) {
                    loadTable(null);
                    System.out.println("test");
                }
            }
        });


        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyPressed = e.getKeyCode();
                if (KeyEvent.VK_ENTER == keyPressed) {
                    String searchText = searchTextField.getText();
                    List<ClientModel> filteredClients = clientService.getClientByString(searchText);
                    loadTable(filteredClients);
                }

            }
        });

    }

    private void editClientAction() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(Table.this, "No clients selected yet!",
                    "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            int selectedClientID = (int) clientsTable.getValueAt(selectedRow, 0);
            String selectedFirstName = (String) clientsTable.getValueAt(selectedRow, 1);
            String selectedLastName = (String) clientsTable.getValueAt(selectedRow, 2);
            String selectedPesel = (String) clientsTable.getValueAt(selectedRow, 3);
            ClientSex selectedSex = (ClientSex) clientsTable.getValueAt(selectedRow, 4);
            ClientDocumentType selectedDocumentType = (ClientDocumentType) clientsTable.getValueAt(selectedRow, 5);
            String selectedDocumentNumber = (String) clientsTable.getValueAt(selectedRow, 6);
            ClientModel clientModel = new ClientModel();
            clientModel.setId(selectedClientID);
            clientModel.setFirstName(selectedFirstName);
            clientModel.setLastName(selectedLastName);
            clientModel.setPesel(selectedPesel);
            clientModel.setSex(selectedSex);
            clientModel.setDocumentType(selectedDocumentType);
            clientModel.setDocumentNumber(selectedDocumentNumber);
            SwingUtilities.invokeLater(() -> new Form(clientModel, table).setVisible(true));
        }
    }

    private void deleteAction() {
        int selectedRow = clientsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(Table.this, "No clients selected yet!",
                    "WARNING", JOptionPane.WARNING_MESSAGE);
        } else {
            int confirmation = JOptionPane.showConfirmDialog(Table.this, "Are you sure you want \n to delete this client?",
                    "CONFIRMATION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirmation == JOptionPane.YES_OPTION) {
                int selectedClientID = (int) clientsTable.getValueAt(selectedRow, 0);
                clientService.deleteClient(selectedClientID);
                JOptionPane.showMessageDialog(Table.this, "Client Deleted",
                        "DELETED", JOptionPane.INFORMATION_MESSAGE);
                loadTable(null);
            }
        }
    }

    private void deleteAllAction() {
        int confirmation = JOptionPane.showConfirmDialog(Table.this, "Are you sure, you want to DELETE all clients?",
                "CONFIRMATION", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmation == JOptionPane.YES_OPTION) {
            clientService.deletaAllClients();
            JOptionPane.showMessageDialog(Table.this, "All Clients Deleted!",
                    "DELETED", JOptionPane.INFORMATION_MESSAGE);
            loadTable(null);
        }
    }

    private void loadTable(List<ClientModel> clients) {
        String[] headers = new String[]{"ID", "First name", "Last name", "Pesel", "Sex", "Document Type", "Document number"};
        DefaultTableModel tableModel = new DefaultTableModel(headers, 0);
        tableModel.setRowCount(0);
        List<ClientModel> clientModelList;
        if (clients == null) {
            clientModelList = clientService.getAllClients();
        } else {

            clientModelList = clients;
        }


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
        importMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Choose CSV File!");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV.files (*.csv)", "csv");
                fileChooser.setFileFilter(filter);
                int fileChooserResult = fileChooser.showOpenDialog(Table.this);
                if (fileChooserResult == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String fileCSVName = selectedFile.getName();
                    int lastDotIndex = fileCSVName.lastIndexOf('.');
                    if (lastDotIndex > 0) {
                        String fileExtension = fileCSVName.substring(lastDotIndex + 1).toLowerCase();
                        if (!fileExtension.equals("csv")) {
                            JOptionPane.showMessageDialog
                                    (Table.this, "Wrong file Extention", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            int lineCounter = 1;

                            try {
                                BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                                String line;
                                Boolean isHeader = true;
                                Boolean isHeaderValid = true;

                                while ((line = reader.readLine()) != null) {
                                    try {


                                        System.out.println(line);
                                        if (isHeader) {
                                            isHeader = false;
                                            isHeaderValid = clientFileIOService.checkHeader(line, table);
                                        } else {
                                            if (!isHeaderValid) {
                                                break;
                                            } else {
                                                String[] clientDataFromFile = line.split(",");
                                                lineCounter++;
                                                Map<String, String> validateMessages = clientService.validateClient(
                                                        clientDataFromFile[0],
                                                        clientDataFromFile[1],
                                                        clientDataFromFile[2],
                                                        clientDataFromFile[3],
                                                        clientDataFromFile[4],
                                                        clientDataFromFile[5]
                                                );

                                                if (validateMessages.isEmpty()) {
                                                    boolean isClientExist = clientService.clientExists(clientDataFromFile[2], clientDataFromFile[5]);
                                                    if (isClientExist) {
                                                        clientFileIOService.writeToSummaryFile(lineCounter + " - Client already exists!");
                                                    } else {
                                                        boolean clientSaved = clientService.createClient(
                                                                clientDataFromFile[0],
                                                                clientDataFromFile[1],
                                                                clientDataFromFile[2],
                                                                clientDataFromFile[3],
                                                                clientDataFromFile[4],
                                                                clientDataFromFile[5]);
                                                        if (clientSaved) {
                                                            clientFileIOService.writeToSummaryFile(lineCounter + " - Client successfully imported!");
                                                        } else {
                                                            clientFileIOService.writeToSummaryFile(lineCounter + " - unexpected error occurred!");
                                                        }
                                                    }

                                                } else {
                                                    String joindeMessages = clientService.joinMassages(validateMessages);
                                                    clientFileIOService.writeToSummaryFile(lineCounter + " - " + joindeMessages);
                                                }

                                            }

                                        }
                                    } catch (Throwable t) {
                                        String exeptionMessage = t.getMessage();
                                        clientFileIOService.writeToSummaryFile(lineCounter + " - " + exeptionMessage);
                                    }

                                }
                            } catch (Throwable t) {
                                String exeptionMessage = t.getMessage();
                                clientFileIOService.writeToSummaryFile(lineCounter + " - " + exeptionMessage);
                            }

                        }
                    }
                    System.out.println(selectedFile.getAbsolutePath());

                }
            }
        });
        JMenuItem exportMenuItem = new JMenuItem("Export");
        exportMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem closeMenuItem = new JMenuItem("Close");
        closeMenuItem.setPreferredSize(new Dimension(150, 30));
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));
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
//        addClientMenuItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
        addClientMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        addClientMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new Form(null, table).setVisible(true));
            }
        });

        JMenuItem editSelectedMenuItem = new JMenuItem("Edit Selected");
        editSelectedMenuItem.setPreferredSize(new Dimension(150, 30));
        editSelectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));

        editSelectedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editClientAction();
            }
        });

        JMenuItem deleteSelectedMenuItem = new JMenuItem("Delete Selected");
        deleteSelectedMenuItem.setPreferredSize(new Dimension(150, 30));
        deleteSelectedMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));

        deleteSelectedMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAction();
            }
        });
        JMenuItem clearAllClientsMenuItem = new JMenuItem("Clear All Clients");
        clearAllClientsMenuItem.setPreferredSize(new Dimension(150, 30));

        clearAllClientsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAllAction();
            }
        });
        editMenu.add(addClientMenuItem);
        editMenu.add(editSelectedMenuItem);
        editMenu.add(deleteSelectedMenuItem);
        editMenu.add(clearAllClientsMenuItem);
        jMenuBar.add(editMenu);

        JMenu viewMenu = new JMenu("View");

        JMenuItem sortClientsMenuItem = new JMenuItem("Sort Clients");
        sortClientsMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem showStatisticsMenuItem = new JMenuItem("Show Statistics");
        showStatisticsMenuItem.setPreferredSize(new Dimension(150, 30));

        showStatisticsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                todo - wywołanie metody liczącej statystyki
               List<ClientModel> clients = clientService.getAllClients();
               Map<ClientSex, Double> clientSexPercentMap = clientService.calculateClientSexPercent(clients);
               Map<ClientAgeRange, Integer> clientAgeRangeMap = clientService.calculateClientsAgeRange(clients);
               Map<ClientDocumentType, Double> clientDocumentTypeMap = clientService.calculateClientsDocuments(clients);
//               todo na podstawie tych 3 Map utworzyć końcowe podsumowanie, które się wyświetli w nowym JFrame, czyli okienku
//                analogicznie do okna About
                JFrame sexStatisticsFrame = new JFrame( "Sex statistic");
                sexStatisticsFrame.setLayout(new FlowLayout());
                JLabel sexTitleLabel = new JLabel( calculateClientSexPercent, SwingConstants.CENTER);
                sexTitleLabel.setFont(new Font("Arial", Font.BOLD, 15));
                sexStatisticsFrame.add(sexTitleLabel);
            }
        });

        viewMenu.add(sortClientsMenuItem);
        viewMenu.add(showStatisticsMenuItem);
        jMenuBar.add(viewMenu);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame aboutFrame = new JFrame("About");
                aboutFrame.setLayout(new FlowLayout());

                JLabel aboutTitleLabel = new JLabel("<html>" +
                        "<center>Aplikacja bazodanowa klientów<br/>" +
                        "Mateusz & Bartek Software House<br/>" +
                        "</center></html>", SwingConstants.CENTER);
                aboutTitleLabel.setFont(new Font("Arial", Font.BOLD, 15));
                aboutFrame.add(aboutTitleLabel);

                JLabel aboutDescriptionLabel = new JLabel("<html>" +
                        "<center>Program do obsługi bazy danych klientów firmy<br/>" +
                        "Version 1.0<br/>" +
                        "© 07.2025</center></html>", SwingConstants.CENTER);
                aboutDescriptionLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
                aboutFrame.add(aboutDescriptionLabel);


                aboutFrame.setSize(350, 150);
                aboutFrame.setLocationRelativeTo(null);
                aboutFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Zamknięcie tylko tego okna
                aboutFrame.setResizable(false); // maksymalizacja okna wyłączona
                aboutFrame.setVisible(true);
                System.out.println("About - otwarcie okna");


//                JOptionPane.showMessageDialog(null,
//                        "<html><center>Aplikacja bazodanowa klientów<br/>Version 1.0<br/>© 2025</center></html>", "About",
//                        JOptionPane.INFORMATION_MESSAGE ); // opcja w JOptionPane - okna dialogowe
            }
        });
        aboutMenuItem.setPreferredSize(new Dimension(150, 30));
        JMenuItem shortcutsMenuItem = new JMenuItem("Shortcuts");
        shortcutsMenuItem.setPreferredSize(new Dimension(150, 30));
        shortcutsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));


        shortcutsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String shortcuts = """
                        <html><pre>
                        Ctrl + N → Add Client
                        Ctrl + E → Edit Selected
                        Ctrl + D → Delete Selected
                        Ctrl + R → Refresh Table
                        Ctrl + Q → Close
                        Ctrl + S → Show Shortcuts
                        </pre></html>
                        """;

                JOptionPane.showMessageDialog(
                        Table.this,
                        shortcuts,
                        "Keyboard Shortcuts",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        helpMenu.add(aboutMenuItem);
        helpMenu.add(shortcutsMenuItem);
        jMenuBar.add(helpMenu);
        setJMenuBar(jMenuBar);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            table = new Table();
            table.setVisible(true);
        });
    }


}




