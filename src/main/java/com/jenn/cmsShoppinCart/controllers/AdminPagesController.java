package com.jenn.cmsShoppinCart.controllers;

import com.jenn.cmsShoppinCart.models.PageRepository;
import com.jenn.cmsShoppinCart.models.data.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/pages")
@RequiredArgsConstructor
public class AdminPagesController {

    private final PageRepository pageRepo;

    @GetMapping
    public String index(Model model){
        List<Page> pages = pageRepo.findAll();
        model.addAttribute("pages",pages);
        return "admin/pages/index";
    }
}
