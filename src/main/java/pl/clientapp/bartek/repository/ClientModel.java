package pl.clientapp.bartek.repository;

public class ClientModel {

    private  int id;

    private String firstName;

    private String lastName;

    private String pesel;

    private ClientSex sex;

    private ClientDocumentType documentType;

    private String documentNumber;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public ClientSex getSex() {
        return sex;
    }

    public void setSex(ClientSex sex) {
        this.sex = sex;
    }

    public ClientDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(ClientDocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Override
    public String toString() {
        return "ClientModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pesel='" + pesel + '\'' +
                ", sex=" + sex +
                ", documentType=" + documentType +
                ", documentNumber='" + documentNumber + '\'' +
                '}';
    }
}
