package com.cesarschool.catalisabackend.models.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @GetMapping(value="/produto")
    public String controlador(){
        return "dale boy";
    }
}
