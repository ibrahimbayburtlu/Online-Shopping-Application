package com.ibrahimbayburtlu.productservice.controller;

import com.ibrahimbayburtlu.productservice.dto.ProductRequest;
import com.ibrahimbayburtlu.productservice.dto.ProductResponse;
import com.ibrahimbayburtlu.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // POST : http://localhost:8080/api/product
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);

        return productRequest.getDescription() + " added.";
    }

    // GET : http://localhost:8080/api/product
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }
}
