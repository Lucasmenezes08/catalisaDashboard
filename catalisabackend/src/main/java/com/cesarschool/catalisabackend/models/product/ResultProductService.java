package com.cesarschool.catalisabackend.models.product;
import lombok.Getter;
import lombok.Setter;
import com.cesarschool.catalisabackend.models.utils.ListaString;

@Getter @Setter
public class ResultProductService {
    private boolean valid;
    private boolean realized;
    private ListaString error;
    public ResultProductService(boolean valid, boolean realized, ListaString error) {
        this.valid = valid;
        this.realized = realized;
        this.error = error;
    }
    public void addError(String error){
        this.error.adicionar(error);
    }
}
