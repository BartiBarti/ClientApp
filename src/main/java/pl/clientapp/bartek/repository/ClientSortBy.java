package pl.clientapp.bartek.repository;

public enum ClientSortBy {

    ID_ASC("ID", "ID", "Ascending", "asc"),
    FIRST_NAME_ASC("First name", "FirstName", "Ascending", "asc"),
    LAST_NAME_ASC("Last name", "LastName", "Ascending", "asc"),
    PESEL_ASC("PESEL", "Pesel", "Ascending", "asc"),
    DOCUMENT_NUMBER_ASC("Document number", "DocumentNumber", "Ascending", "asc"),
    ID_DESC("ID", "ID", "Descending", "desc"),
    FIRST_NAMe_DESC("First name", "FirstName", "Descending", "desc"),
    LAST_NAME_DESC("Last name", "LastName", "Descending", "desc"),
    PESEL_DESC("PESEL", "Pesel", "Descending", "desc"),
    DOCUMENT_NUMBER_DESC("Document number", "DocumentNumber", "Descending", "desc");

    private String guiKey;

    private String dbColumnName;

    private String guiKeyOrderType;

    private String dbOrderType;

    //    Konstruktor poniżej
    ClientSortBy(String guiKey, String dbColumnName, String guiKeyOrderType, String dbOrderType) {
        this.guiKey = guiKey;
        this.dbColumnName = dbColumnName;
        this.guiKeyOrderType = guiKeyOrderType;
        this.dbOrderType = dbOrderType;
    }

    public String getGuiKeyOrderType() {
        return guiKeyOrderType;
    }

    public String getDbOrderType() {
        return dbOrderType;
    }

    public String getDbColumnName() {
        return dbColumnName;
    }

    public String getGuiKey() {
        return guiKey;
    }

    public static ClientSortBy valueFromGuiKeyAndOrderType(String guiKey, String guiKeyOrderType) {
        for (ClientSortBy clientSortBy : ClientSortBy.values()) {
            if (guiKey.equals(clientSortBy.guiKey) && guiKeyOrderType.equals(clientSortBy.guiKeyOrderType)) {
                return clientSortBy;
            }
        }
        return null;
    }


}
