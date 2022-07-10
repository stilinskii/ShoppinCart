package com.jenn.cmsShoppinCart.models;

import com.jenn.cmsShoppinCart.models.data.Category;
import com.jenn.cmsShoppinCart.models.data.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findBySlug(String slug);
}
