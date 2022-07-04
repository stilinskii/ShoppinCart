package com.jenn.cmsShoppinCart.models;

import com.jenn.cmsShoppinCart.models.data.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    //CrudRepository < PagingAndSortingRepository < JpaRepository
    Page findBySlug(String slug);

//    @Query("SELECT p FROM Page p WHERE p.id != :id and p.slug = :slug")
//    Page findBySlug(int id,String slug);

    Page findBySlugAndIdNot(String slug,int id);

    List<Page> findAllByOrderBySortingAsc();
}
