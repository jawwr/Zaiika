package com.project.zaiika.services;

import com.project.zaiika.models.Product;
import com.project.zaiika.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository repository;

    @Autowired
    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getAllProductFromMenu() {
        return null;
    }

    @Override
    public void addProductToMenu(Product product) {

    }

    @Override
    public void changeProduct(Product newProduct) {

    }

    @Override
    public void deleteProductById(long id) {

    }
}
