package com.jenn.cmsShoppinCart.controllers;

import com.jenn.cmsShoppinCart.models.PageRepository;
import com.jenn.cmsShoppinCart.models.data.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Slf4j
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

    @GetMapping("/add")
    public String addPageForm(@ModelAttribute Page page){

        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String forAddSubmit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("errors",bindingResult);
            return "admin/pages/add";
        }
        redirectAttributes.addFlashAttribute("message","Page added");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ","-"):page.getSlug().toLowerCase().replace(" ","-");

        if(pageRepo.findBySlug(slug)!=null){
            log.info("log exists access");
            redirectAttributes.addFlashAttribute("message","Slug exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("page",page);
            log.info("page={}",page.getTitle());

        }else{
            page.setSlug(slug);
            page.setSorting(100);
            pageRepo.save(page);
        }
        return "redirect:/admin/pages/add";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id,Model model){
        log.info("member sorting={}",pageRepo.findById(id).get().getSorting());
        model.addAttribute("page",pageRepo.findById(id).get());
        model.addAttribute("edit",true);
        return "admin/pages/add";
    }
}
