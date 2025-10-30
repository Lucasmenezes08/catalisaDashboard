package com.cesarschool.catalisabackend.models.product;

import com.cesarschool.catalisabackend.models.utils.ListaString;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.cesarschool.catalisabackend.models.utils.StringUtils;
import com.cesarschool.catalisabackend.models.product.Product;
import com.cesarschool.catalisabackend.models.product.ProductRepository;
import com.cesarschool.catalisabackend.models.product.ResultProductService;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Transactional
    public ResultProductService include(Product product){
        ResultProductService result = validate(product);
        if(result.isValid()){
            if(search(product.getName()) == null){
                productRepository.save(product);
                result.setRealized(true);
                return result;
            }
            else{
                result.setRealized(false);
                result.addError("Produto já existe");
                return result;
            }
        }
        return result;
    }
    public ResultProductService update(Product product){
        ResultProductService result = validate(product);
        if(result.isValid()){
            if(search(product.getName()) != null){
                productRepository.save(product);
                result.setRealized(true);
                return result;
            }
            else{
                result.setRealized(false);
                result.addError("Produto inexistente");
                return result;
            }
        }
        return result;
    }
    public ResultProductService delete(String name){
        ListaString error = new ListaString();
        if(StringUtils.estaVazia(name)) {
            error.adicionar("Nome não informado");
            return new ResultProductService(false,false,error);
        }
        Product produto = search(name);
        if(produto != null) {
            productRepository.delete(produto);
            return new ResultProductService(true,true,error);
        }
        else {
            error.adicionar("Nome inexistente");
            return new ResultProductService(true,false,error);
        }
    }
    public ResultProductService delete(long id){
        ListaString error = new ListaString();
        if(id <= 0) {
            error.adicionar("Id não informado");
            return new ResultProductService(false,false,error);
        }

        if(search(id) != null) {
            productRepository.delete(search(id));
            return new ResultProductService(true,true,error);
        }
        else {
            error.adicionar("ID inexistente");
            return new ResultProductService(true,false,error);
        }
    }
    public Product search(String name){
        return productRepository.findByNameIgnoreCase(name).orElse(null);
    }
    public Product search(long id){
        return productRepository.findById(id).orElse(null);
    }
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public ResultProductService validate(Product product){
        ListaString error = new ListaString();
        if(product == null){
            error.adicionar("Produto é nulo");
            return new ResultProductService(false, false, error);
        }
        boolean valid = true;
        boolean realized = false;
        if(StringUtils.estaVazia(product.getName())){
            error.adicionar("Produto sem nome");
            valid = false;
        }
        if(product.getType() == null){
            error.adicionar("Produto sem tipo");
            valid = false;
        }
        return new ResultProductService(valid, realized, error);
    }
}