package com.jenn.cmsShoppinCart.controllers;

import com.jenn.cmsShoppinCart.models.CategoryRepository;
import com.jenn.cmsShoppinCart.models.data.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

    private final CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model){
        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();
        model.addAttribute("categories",categories);
        return "admin/categories/index";
    }

    //included in all methods
//    @ModelAttribute("category")
//    public Category getCategory(){
//        return new Category();
//    }

    @GetMapping("/add")
    public String add(Category category){
        return "admin/categories/add";
    }

    @PostMapping("/add")
    public String forAddSubmit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors",bindingResult); 생략가능
            return "admin/categories/add";
        }
       //duplicate check
        Category categoryExists = categoryRepo.findByName(category.getName());
        if(categoryExists!=null){
            log.info("log exists access");
            redirectAttributes.addFlashAttribute("message","Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            //이거 왜 넣었더라 TODO
            redirectAttributes.addFlashAttribute("category",category);
        }else{
            //성공로직
            redirectAttributes.addFlashAttribute("message","Category added");
            redirectAttributes.addFlashAttribute("alertClass","alert-success");
            String slug = category.getName().toLowerCase().replace(" ","-");
            category.setSlug(slug);
            category.setSorting(100);
            categoryRepo.save(category);
        }
        return "redirect:/admin/categories/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model){
        model.addAttribute("category",categoryRepo.findById(id).get());
        model.addAttribute("edit",true);
        return "admin/categories/add";
    }

    @PostMapping("/edit")
    public String forEditSubmit(@Valid Category category, BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes){
        Category categoryCurrent = categoryRepo.findById(category.getId()).get();
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors",bindingResult); 생략가능
            model.addAttribute("edit",true);
            //카테고리 이름이 지워져도 페이지 제목에 원래 수정하려했던 페이지 이름 표시
            model.addAttribute("categoryName",categoryCurrent.getName());
            return "admin/categories/add";
        }
        // duplicate check , 아이디는 다른데 같은 이름이 있으면 오류보내기
        Category duplicateChk = categoryRepo.findByNameAndIdNot(category.getName(), category.getId());
        if(duplicateChk!=null){
            redirectAttributes.addFlashAttribute("message","Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
        }else{
            //성공로직
            redirectAttributes.addFlashAttribute("message","Category edited");
            redirectAttributes.addFlashAttribute("alertClass","alert-success");
            String slug = category.getName().toLowerCase().replace(" ","-");
            category.setSlug(slug);
            categoryRepo.save(category);
        }

        return "redirect:/admin/categories/edit/"+category.getId();
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes){
        categoryRepo.deleteById(id);
        redirectAttributes.addFlashAttribute("message","Category deleted");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");
        return "redirect:/admin/categories";
    }

    @ResponseBody
    @PostMapping("/reorder")
    public void reorder(@RequestParam("id[]") int[] id){
        int count = 1;
        Category category;
        for (int categoryId : id) {
            category = categoryRepo.findById(categoryId).get();
            category.setSorting(count);
            categoryRepo.save(category);
            count++;
        }
        //return "ok";
    }

}
