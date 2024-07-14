package com.clienteApiRest.clienteApiRest.services;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.entities.Product;
import com.clienteApiRest.clienteApiRest.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product save(Product product){
        return productRepository.save(product);
    }

    public Optional<Product> readOne(Long id){
        return productRepository.findById(id);
    }

    public List<Product> findAll(){
        return (List<Product>) productRepository.findAll();
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public void subtractStock(Long id, Integer amount){
        productRepository.decrementStock(id, amount);
    }

    public void addStock(Long id, Integer amount){
        productRepository.incrementStock(id, amount);
    }
}
