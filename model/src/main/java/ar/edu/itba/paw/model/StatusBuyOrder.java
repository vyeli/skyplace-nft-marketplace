package ar.edu.itba.paw.model;

import java.util.ArrayList;
import java.util.List;

public enum StatusBuyOrder {
    NEW,
    PENDING;

    public final String name = toString();

    public String getName() {
        return name;
    }

    public static List<String> getStatusNames(){
        List<String> statusNames = new ArrayList<>();
        for(StatusBuyOrder s : values()){
            statusNames.add(s.getName());
        }
        return statusNames;
    }

    public static boolean hasStatus(String status){
        for(StatusBuyOrder s : values()){
            if(s.getName().equals(status))
                return true;
        }
        return false;
    }
}
