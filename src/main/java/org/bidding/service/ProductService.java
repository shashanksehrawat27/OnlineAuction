package org.bidding.service;

import org.bidding.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    Product update(Long id, Product product);
    boolean delete(Long id);
}