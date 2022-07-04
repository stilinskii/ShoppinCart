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
        List<Page> pages = pageRepo.findAllByOrderBySortingAsc();
        model.addAttribute("pages",pages);
        return "admin/pages/index";
    }

    //add.html에 th:object를 썼기때문에 모델 어트리뷰트가 있어야함.
    //th:object를 쓰면 id name value 생략가능
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

    @PostMapping("/edit")
    public String forEditSubmit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model){

        Page pageCurrent = pageRepo.getOne(page.getId());

        if(bindingResult.hasErrors()){
            model.addAttribute("errors",bindingResult);
            model.addAttribute("pageTitle",pageCurrent.getTitle());
            model.addAttribute("edit",true);
            return "admin/pages/add";
        }
        redirectAttributes.addFlashAttribute("message","Page edited");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");

        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ","-"):page.getSlug().toLowerCase().replace(" ","-");

        //아이디가 같지않은 page중에 입력된 slug와같은 slug를 가지고 있는지 체크
        Page slugExists = pageRepo.findBySlugAndIdNot(slug,page.getId());
        if(slugExists!=null){
            log.info("log exists access");
            redirectAttributes.addFlashAttribute("message","Slug exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("page",page);
            log.info("page={}",page.getTitle());

        }else{
            page.setSlug(slug);
            pageRepo.save(page);
        }
        return "redirect:/admin/pages/edit/"+page.getId();
    }

    @GetMapping("/delete/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes){
        pageRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message","Page deleted");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");

        return "redirect:/admin/pages";
    }

    @ResponseBody
    @PostMapping("/reorder")
    public String reorder(@RequestParam("id[]") int[] id){
        int count = 1;
        Page page;

        for (int pageId : id) {
            page = pageRepo.getOne(pageId);
            page.setSorting(count);
            pageRepo.save(page);
            count++;
        }
        
        return "ok";
    }
}
