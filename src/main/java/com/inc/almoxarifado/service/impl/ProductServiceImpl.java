package com.inc.almoxarifado.service.impl;

import com.inc.almoxarifado.exceptions.ResourceNotFoundException;
import com.inc.almoxarifado.model.Product;
import com.inc.almoxarifado.repository.ProductRepository;
import com.inc.almoxarifado.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ResponseEntity<Product> createProduct(Product product) {
        validateProduct(product);
        Product savedProduct = productRepository.save(product);
        URI location = URI.create("/almo-sys/products/" + savedProduct.getId());
        return ResponseEntity.created(location).body(savedProduct);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
    }

    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));

        validateProduct(productDetails);

        product.setName(productDetails.getName());
        product.setQuantity(productDetails.getQuantity());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());
        product.setCode(productDetails.getCode());
        product.setCategory(productDetails.getCategory());
        product.setSupplier(productDetails.getSupplier());

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
        productRepository.delete(product);
    }


    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (product.getCode() == null || product.getCode().isEmpty()) {
            throw new IllegalArgumentException("O código do produto é obrigatório.");
        }
        if (product.getSupplier() == null || product.getSupplier().isEmpty()) {
            throw new IllegalArgumentException("O fornecedor do produto é obrigatório.");
        }
        if (product.getQuantity() == null || product.getQuantity() < 0) {
            throw new IllegalArgumentException("A quantidade inicial do produto deve ser positiva.");
        }
    }
}
