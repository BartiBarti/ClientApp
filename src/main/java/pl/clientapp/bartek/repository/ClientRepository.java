package pl.clientapp.bartek.repository;

import com.mysql.cj.jdbc.Driver;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository {

    private Connection connection;

    public ClientRepository() {

        try {
//            DriverManager.registerDriver(new Driver());
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/clientsdb?serverTimezone=UTC", "root", "");
            connection = DriverManager.getConnection("jdbc:sqlite:client.db");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ClientModel> findAllClients() {

        List<ClientModel> clientsList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from clients;");
            while (resultSet.next()) {
                ClientModel client = getClientFromResultSet(resultSet);
                clientsList.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientsList;
    }

    public List<ClientModel> findClientsByString(String searchText){

        List<ClientModel> clientsList = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String query = "select * from clients c where c.FirstName like '%?%' " +
                                                    "or c.LastName like '%?%' " +
                                                    "or c.Pesel like '%?%' " +
                                                    "or c.DocumentNumber like '%?%';";
            query = query.replaceAll("\\?", searchText);
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                ClientModel client = getClientFromResultSet(resultSet);
                clientsList.add(client);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientsList;

    }
//    -----------------------------------------------------

//    public List<ClientModel> findClientsByString(String searchText) {
//
//        List<ClientModel> clientsList = new ArrayList<>();
//
//        String sql = "SELECT * FROM clients c " +
//                "WHERE LOWER(c.FirstName) LIKE ? " +
//                "OR LOWER(c.LastName) LIKE ? " +
//                "OR c.Pesel LIKE ? " +
//                "OR LOWER(c.DocumentNumber) LIKE ?";
//
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            String pattern = "%" + searchText.toLowerCase() + "%";
//            statement.setString(1, pattern);
//            statement.setString(2, pattern);
//            statement.setString(3, "%" + searchText + "%"); // pesel nie trzeba zamieniać na lower
//            statement.setString(4, pattern);
//
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                ClientModel client = getClientFromResultSet(resultSet);
//                clientsList.add(client);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        return clientsList;
//    }



    public ClientModel findClientById(int id) {

        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("select * from clients where id = ?;");
            preparedStatement.setInt(1, id); // bo id indeksowane od 1 wyjątkowo
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ClientModel client = getClientFromResultSet(resultSet);
                return client;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean clientExist(String pesel, String documentNumber){
//        select * from clients c where c.Pesel = 99120345678 or c.DocumentNumber = 1234;
        try {
            Statement statement = connection.createStatement();
            String query =  "select * from clients c where c.Pesel = '%s' or c.DocumentNumber = '%s';";
            String filledQuery = String.format(query, pesel, documentNumber);
            ResultSet resultSet = statement.executeQuery(filledQuery);
            return  resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean saveClient(ClientModel client) {

        try {

            Statement statement = connection.createStatement();
            String query =
                    "insert into clients (FirstName, LastName, Pesel, Sex, " +
                            "DocumentType, DocumentNumber) values ('%s', '%s', '%s', '%s', '%s', '%s');";
            String filledQuery = String.format(query, client.getFirstName(), client.getLastName(),
                    client.getPesel(), client.getSex(),
                    client.getDocumentType(), client.getDocumentNumber());
            statement.executeUpdate(filledQuery);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private ClientModel getClientFromResultSet(ResultSet resultSet) throws SQLException {

        ClientModel client = new ClientModel();
        client.setId(resultSet.getInt("ID"));
        client.setFirstName(resultSet.getString("FirstName"));
        client.setLastName(resultSet.getString("LastName"));
        client.setPesel(resultSet.getString("Pesel"));

        ClientSex clientSex = ClientSex.valueOf(resultSet.getString("Sex"));
        client.setSex(clientSex);

        ClientDocumentType clientDocumentType = ClientDocumentType.valueOf(resultSet.getString("DocumentType"));
        client.setDocumentType(clientDocumentType);

        client.setDocumentNumber(resultSet.getString("DocumentNumber"));


        return client;
    }

    public void deleteClient(int clientId) {

        try {
            Statement statement = connection.createStatement();
            String query = "delete from clients where ID = %d";
            String filledQuery = String.format(query, clientId);
            statement.executeUpdate(filledQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllClients(){
        try {
            Statement statement = connection.createStatement();
            String query = "delete from clients;";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateClient(ClientModel client) {

        try {

            Statement statement = connection.createStatement();
            String query = "update clients set ";
            if (StringUtils.isNotBlank(client.getFirstName())) {
                query = query + "FirstName = '" + client.getFirstName() + "', ";
            }
            if (StringUtils.isNotBlank(client.getLastName())) {
                query = query + "LastName = '" + client.getLastName() + "', ";
            }
            if (StringUtils.isNotBlank(client.getPesel())) {
                query = query + "Pesel = '" + client.getPesel() + "' , ";
            }
            if (client.getSex() != null) {
                query = query + "Sex = '" + client.getSex() + "' ,";
            }
            if (client.getDocumentType() != null) {
                query = query + "DocumentType = '" + client.getDocumentType() + "' ,";
            }
            if (StringUtils.isNotBlank(client.getDocumentNumber())) {
                query = query + "DocumentNumber = '" + client.getDocumentNumber() + "' ,";
            }



            query = query.substring(0, query.length() - 2);
            query = query + " where ID = " + client.getId();
            statement.executeUpdate(query);

        } catch (SQLException e) {
             e.printStackTrace();
            return false;
        }
        return true;
    }




}
