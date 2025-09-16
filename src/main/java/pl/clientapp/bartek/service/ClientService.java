package pl.clientapp.bartek.service;


import org.apache.commons.lang3.StringUtils;
import pl.clientapp.bartek.repository.ClientDocumentType;
import pl.clientapp.bartek.repository.ClientModel;
import pl.clientapp.bartek.repository.ClientRepository;
import pl.clientapp.bartek.repository.ClientSex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientService {


    private ClientRepository clientRepository = new ClientRepository();

// 1	Jan	Kowalski	99120345678	MAN	IDENTITY_CARD	ABC123456

    public Map<String, String> validateClient(String firstName, String lastName, String pesel,
                                              String sex, String documentType, String documentNumber) {

        Map<String, String> validateMessages = new HashMap<>();


        if (StringUtils.isBlank(firstName)) {
            validateMessages.put("First Name", "Field Required!");
        }   else if (!firstName.matches("\\p{L}+")) {
            validateMessages.put("First Name", "Only letters");
        }

        if (StringUtils.isBlank(lastName)) {
            validateMessages.put("Last Name", "Field Required!");
        } else if (!lastName.matches("\\p{L}+(?:[\\s\\-]\\p{L}+)*")) {
            validateMessages.put("Last Name", "Only letters");
        }

        if (StringUtils.isBlank(pesel)) {
            validateMessages.put("Pesel", "Field Required");
        } else if (pesel.length() != 11) {
            validateMessages.put("Pesel", "Wrong lenght - necessary 11 numbers");
        } else if (!pesel.matches("\\d+")) {
            validateMessages.put("Pesel", "Wrong input - necessary only digits");
        } else if (!validatePeselControlNumber(pesel)) {
            validateMessages.put("Pesel", "Wrong control sum!");
        }

        if (StringUtils.isBlank(documentType)) {
            validateMessages.put("Document Type", "Field Required");
        }
        if (StringUtils.isBlank(documentNumber)) {
            validateMessages.put("Document number", "Field Required!");
        } else {
            documentNumber = documentNumber.toUpperCase();
            ClientDocumentType clientDocumentType = ClientDocumentType.valueFromGuiKey(documentType);

            if (ClientDocumentType.IDENTITY_CARD.equals(clientDocumentType)){

                if (documentNumber.matches("[A-Z]{3}\\d{6}")) {

                    if (!isDocumentNumberValid(documentNumber)){
                        validateMessages.put("Document number", "Wrong control sum!");
                    }
                } else {
                    validateMessages.put("Document number", "Wrong pattern");
                }


            }
        }

        if (StringUtils.isBlank(sex)){
            validateMessages.put("Sex", "One option required");
        } else {
            ClientSex clientSex = ClientSex.valueFromGuiKey(sex);
            ClientSex clientSexFromPesel = validateClientSex(pesel);
            if (!clientSex.equals(clientSexFromPesel)) {
                validateMessages.put("Sex", "Chosen option, does not match pesel");
            }
        }

        return validateMessages;

    }

    public boolean updateClient(ClientModel client){
        return clientRepository.updateClient(client);
    }

    public boolean clientExists(String pesel, String documentNumber){
        return clientRepository.clientExist(pesel, documentNumber);
    }

    public void deleteClient(int clientId) {
        clientRepository.deleteClient(clientId);
    }

    private boolean isDocumentNumberValid(String documentNumber) {
        int[] scale = {7, 3, 1, 9, 7, 3, 1, 7, 3};
        int sum = 0;

        for (int i = 0; i < 9; i++) {
            char sign = documentNumber.charAt(i);
            int value;

            if (i < 3) {
                value = sign - 'A' + 10; // litery na liczby 10–35
//                AAA
//                A (65) - A (65) = 0 + 10 = 35
//                Z (90) - A (65) = 25 +10 = 35
            } else {
                value = Character.getNumericValue(sign); // cyfry
            }

            sum += value * scale[i];
        }

        int controlDigit = sum % 10;

        return controlDigit == 0;
    }

    public List<ClientModel> getAllClients (){
        return clientRepository.findAllClients();
    }

    public List<ClientModel> getClientByString (String searchText){
        return clientRepository.findClientsByString(searchText);

    }

    private boolean validatePeselControlNumber(String pesel) {

        int sum = 1 * Integer.parseInt(pesel.substring(0,1)) +
                3 * Integer.parseInt(pesel.substring(1,2))+
                7 * Integer.parseInt(pesel.substring(2,3))+
                9 * Integer.parseInt(pesel.substring(3,4))+
                1 * Integer.parseInt(pesel.substring(4,5))+
                3 * Integer.parseInt(pesel.substring(5,6))+
                7 * Integer.parseInt(pesel.substring(6,7))+
                9 * Integer.parseInt(pesel.substring(7,8))+
                1 * Integer.parseInt(pesel.substring(8,9))+
                3 * Integer.parseInt(pesel.substring(9,10));
        sum %= 10; // to samo co sum = sum % 10
        sum = 10 - sum;
        sum %= 10;

        if (sum == Integer.parseInt(pesel.substring(10,11))) {
            return true;

        } else {
            return false;
        }

    }

    private ClientSex validateClientSex(String pesel) {

        int sexNumber = Integer.parseInt((pesel.substring(9,10)));
        if (sexNumber % 2 == 0) {
            return ClientSex.WOMAN;
        } else {
            return ClientSex.MAN;
        }
    }

    public String joinMassages(Map<String, String> validateMessages) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : validateMessages.entrySet()) {

            stringBuilder.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");
        }


        return stringBuilder.toString();

    }

    public boolean createClient(String firstName, String lastName, String pesel,
                                String sex, String documentType, String documentNumber) {

        ClientModel clientModel = new ClientModel();

        clientModel.setFirstName(firstName);
        clientModel.setLastName(lastName);
        clientModel.setPesel(pesel);
        clientModel.setSex(ClientSex.valueFromGuiKey(sex));
        clientModel.setDocumentType(ClientDocumentType.valueFromGuiKey(documentType));
        clientModel.setDocumentNumber(documentNumber);

        return clientRepository.saveClient(clientModel);

    }

// zmiany po warstwach - repozytorium, serwisowa, i ost GUI - tak zmieniamy updateClient
}
