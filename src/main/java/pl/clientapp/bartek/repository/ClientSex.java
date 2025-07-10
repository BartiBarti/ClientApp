package pl.clientapp.bartek.repository;

public enum ClientSex {

    MAN("Man"),
    WOMAN("Women");

    private String guiKey;

    ClientSex(String guiKey) {
        this.guiKey = guiKey;
    }

    public static ClientSex valueFromGuiKey(String guiKey){

        for (ClientSex sex : ClientSex.values()) {
            if (guiKey.equals(sex.guiKey)){
                return sex;
            }
        }
        return null;

    }

}


