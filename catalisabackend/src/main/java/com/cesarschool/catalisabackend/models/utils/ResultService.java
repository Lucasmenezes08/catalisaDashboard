package com.cesarschool.catalisabackend.models.utils;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResultService {
    private boolean valid;
    private boolean realized;
    private ListaString error;
    public ResultService(boolean valid, boolean realized, ListaString error) {
        this.valid = valid;
        this.realized = realized;
        this.error = error;
    }
    public void addError(String error){
        this.error.adicionar(error);
    }
}
