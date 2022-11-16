package ar.edu.itba.paw.webapp.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SourceDto {

    private String pointer;
    //private String parameter;
    //private String header;

    public static SourceDto toSourceList(final Map<String, String> sourceMap){
        SourceDto dto = new SourceDto();
        dto.pointer = sourceMap.getOrDefault("pointer", "");
        return dto;
    }

    public String getPointer() {
        return pointer;
    }

    public void setPointer(String pointer) {
        this.pointer = pointer;
    }
}
