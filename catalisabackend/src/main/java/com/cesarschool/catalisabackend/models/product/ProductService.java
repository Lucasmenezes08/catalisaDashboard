package com.cesarschool.catalisabackend.models.product;

import org.springframework.stereotype.Service;
import com.cesarschool.catalisabackend.models.utils.StringUtils;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public Product include(Product product){
        if(){

        }
        if(){

        }
        return productRepository.save(product);
    }
}
