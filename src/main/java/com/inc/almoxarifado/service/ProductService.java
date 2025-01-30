package com.inc.almoxarifado.service;

import com.inc.almoxarifado.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<Product> createProduct(Product product);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product updateProduct(Long id, Product productDetails);

    void deleteProduct(Long id);

    Page<Product> getFilteredProducts(String name, String category, Pageable pageable);
}
