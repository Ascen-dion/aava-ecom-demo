package com.ecommerce.app.service;

import com.ecommerce.app.model.Product;
import com.ecommerce.app.repository.ProductDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    // Get product by ID
    public Product getProductById(Long id) {
        return productDao.findById(id);
    }

    // Create new product
    public Product createProduct(Product product) {
        return productDao.save(product);
    }

    // Update existing product
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productDao.findById(id);
 
        if (product == null) {
        throw new RuntimeException("Product not found with id: " + id);
    }

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setImageUrl(productDetails.getImageUrl());
        product.setCategory(productDetails.getCategory());
        
        return productDao.save(product);
    }
    
    // Delete product
    public void deleteProduct(Long id) {
        Product product = productDao.findById(id);

             if (product == null) {
        throw new RuntimeException("Product not found with id: " + id);
    }

         productDao.delete(product.getId());
    }
    
    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        return productDao.findByCategory(category);
    }
    
    // Search products by name
    public List<Product> searchProducts(String name) {
        return productDao.findByNameContainingIgnoreCase(name);
    }
}
