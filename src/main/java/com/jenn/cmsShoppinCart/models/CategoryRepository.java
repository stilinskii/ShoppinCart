package com.jenn.cmsShoppinCart.models;

import com.jenn.cmsShoppinCart.models.data.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Integer> {

}
