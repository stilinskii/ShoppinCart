package com.jenn.cmsShoppinCart.controllers;

import com.jenn.cmsShoppinCart.models.CategoryRepository;
import com.jenn.cmsShoppinCart.models.ProductRepository;
import com.jenn.cmsShoppinCart.models.data.Category;
import com.jenn.cmsShoppinCart.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoriesController {

    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/{slug}")
    public String category(@PathVariable String slug, Model model, @RequestParam(value = "page", required = false) Integer p){

        int perPage = 3;
        int page = (p !=null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);
        long count;

        if(slug.equals("all")){
            Page<Product> products = productRepo.findAll(pageable);
            count = productRepo.count();
            model.addAttribute("products",products);
        }else{
            Category category = categoryRepo.findBySlug(slug);
            if(category == null){
                return "redirect:/";
            }
            String categoryId = Integer.toString(category.getId());
            String categoryName = category.getName();
            Page<Product> products = productRepo.findAllByCategoryId(categoryId, pageable);

            count = productRepo.countByCategoryId(categoryId);
            model.addAttribute("products",products);
            model.addAttribute("categoryName",categoryName);
        }
            model.addAttribute("count",count);

        double pageCount = Math.ceil((double) count/(double) perPage);

        model.addAttribute("pageCount",(int)pageCount); // end page
        model.addAttribute("perPage",perPage);
        model.addAttribute("count",count);
        model.addAttribute("page",page); // start page

        return "products";
    }
}
