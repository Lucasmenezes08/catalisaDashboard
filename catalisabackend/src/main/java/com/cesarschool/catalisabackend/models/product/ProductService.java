package com.cesarschool.catalisabackend.models.product;

import com.cesarschool.catalisabackend.models.utils.ListaString;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.cesarschool.catalisabackend.models.utils.StringUtils;
import com.cesarschool.catalisabackend.models.utils.ResultService;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Transactional
    public ResultService include(Product product){
        ResultService result = validate(product);
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
    @Transactional
    public ResultService updateById(long id, Product product){
        ResultService result = validate(product);
        if(result.isValid()){
            Product original = search(id);
            if(original != null){
                original.setName(product.getName());
                original.setDescription(product.getDescription());
                original.setType(product.getType());
                productRepository.save(original);
                result.setRealized(true);
            }
            else{
                result.setRealized(false);
                result.addError("Produto inexistente");
            }
        }
        return result;
    }
    @Transactional
    public ResultService delete(String name){
        ListaString error = new ListaString();
        if(StringUtils.estaVazia(name)) {
            error.adicionar("Nome não informado");
            return new ResultService(false,false,error);
        }
        Product produto = search(name);
        if(produto != null) {
            productRepository.delete(produto);
            return new ResultService(true,true,error);
        }
        else {
            error.adicionar("Nome inexistente");
            return new ResultService(true,false,error);
        }
    }
    @Transactional
    public ResultService delete(long id){
        ListaString error = new ListaString();
        if(id <= 0) {
            error.adicionar("Id não informado");
            return new ResultService(false,false,error);
        }

        if(search(id) != null) {
            productRepository.delete(search(id));
            return new ResultService(true,true,error);
        }
        else {
            error.adicionar("ID inexistente");
            return new ResultService(true,false,error);
        }
    }
    @Transactional
    public Product search(String name){
        return productRepository.findByNameIgnoreCase(name).orElse(null);
    }
    @Transactional
    public Product search(long id){
        return productRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public ResultService validate(Product product){
        ListaString error = new ListaString();
        if(product == null){
            error.adicionar("Produto é nulo");
            return new ResultService(false, false, error);
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
        return new ResultService(valid, realized, error);
    }
}