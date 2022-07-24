package com.jenn.cmsShoppinCart.models;

import com.jenn.cmsShoppinCart.models.data.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Category findByName(String name);
    Category findByNameAndIdNot(String name,int id);

    List<Category> findAllByOrderBySortingAsc();

    Category findBySlug(String slug);
}
