package pl.clientapp.bartek.repository;

public enum ClientDocumentType {

    IDENTITY_CARD("Identity card"),
    PASSPORT("Passport"),
    DRIVER_LICENCE("Driver licence");

    private String guiKey;

    public String getGuiKey() {
        return guiKey;
    }

    ClientDocumentType(String guiKey) {
        this.guiKey = guiKey;
    }

    public static ClientDocumentType valueFromGuiKey(String guiKey) {

        for (ClientDocumentType documentType : ClientDocumentType.values()) {
            if (guiKey.equals(documentType.guiKey)) {
                return documentType;
            }
        }
        return null;
    }
}
