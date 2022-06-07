package ar.edu.itba.paw.webapp.helpers;

public enum Alert {
    NONE("none"),
    SUCCESS("success"),
    FAILURE("failure");

    private final String name;

    Alert(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Alert getAlert(String name){
        for(Alert alert : values()){
            if(alert.getName().equals(name))
                return alert;
        }
        return Alert.NONE;
    }
}
