package com.jenn.cmsShoppinCart.models;

import com.jenn.cmsShoppinCart.models.data.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {
    //CrudRepository < PagingAndSortingRepository < JpaRepository


}
