package com.jenn.cmsShoppinCart.controllers;

import com.jenn.cmsShoppinCart.models.CategoryRepository;
import com.jenn.cmsShoppinCart.models.data.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

    private final CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories",categories);
        return "admin/categories/index";
    }



}
