package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public Optional<Product> getProductById(long id){
        return productRepository.findById(id);
    }

    public void deleteProduct(long id){
        productRepository.deleteById(id);
    }

    public List<Product> getAllProductsByCategoryId(long id){
        return productRepository.findAllByCategory_Id(id);
    }
}
