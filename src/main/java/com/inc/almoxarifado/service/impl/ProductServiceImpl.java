package com.inc.almoxarifado.service.impl;

import com.inc.almoxarifado.exceptions.ResourceNotFoundException;
import com.inc.almoxarifado.model.Product;
import com.inc.almoxarifado.repository.ProductRepository;
import com.inc.almoxarifado.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        validateProduct(product);  // Validação dos campos obrigatórios
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
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));

        validateUpdate(productDetails);

        existingProduct.setName(productDetails.getName());
        existingProduct.setQuantity(productDetails.getQuantity());
        existingProduct.setCategory(productDetails.getCategory());
        existingProduct.setCode(productDetails.getCode());
        existingProduct.setSupplier(productDetails.getSupplier());

        return productRepository.save(existingProduct);
    }

    private void validateUpdate(Product productDetails) {
        if (productDetails.getName() == null || productDetails.getName().isBlank()) {
            throw new IllegalArgumentException("O campo 'name' é obrigatório.");
        }
        if (!productDetails.getCode().matches("^PROD\\d+$")) {
            throw new IllegalArgumentException("O código do produto deve começar com 'PROD' seguido de números.");
        }
        if (productDetails.getQuantity() == null || productDetails.getQuantity() < 0) {
            throw new IllegalArgumentException("A quantidade inicial deve ser maior ou igual a zero.");
        }
        if (productDetails.getSupplier() == null || productDetails.getSupplier().isBlank()) {
            throw new IllegalArgumentException("O campo 'supplier' é obrigatório.");
        }
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o ID: " + id));
        productRepository.delete(product);
    }

    @Override
    public Page<Product> getFilteredProducts(String name, String category, Pageable pageable) {
        return productRepository.findByFilters(
                (name == null || name.isBlank()) ? null : name,
                (category == null || category.isBlank()) ? null : category,
                pageable
        );
    }

    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            throw new IllegalArgumentException("O campo 'name' é obrigatório.");
        }
        if (product.getCode() == null || product.getCode().isBlank()) {
            throw new IllegalArgumentException("O campo 'code' é obrigatório.");
        }
        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new IllegalArgumentException("O campo 'category' é obrigatório.");
        }
        if (product.getSupplier() == null || product.getSupplier().isBlank()) {
            throw new IllegalArgumentException("O campo 'supplier' é obrigatório.");
        }
    }
}
