package com.jenn.cmsShoppinCart.controllers;

import com.jenn.cmsShoppinCart.models.CategoryRepository;
import com.jenn.cmsShoppinCart.models.ProductRepository;
import com.jenn.cmsShoppinCart.models.data.Category;
import com.jenn.cmsShoppinCart.models.data.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductsController {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model){
        List<Product> products = productRepo.findAll();
        model.addAttribute("products",products);
        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Product product, Model model){
        //Model is for category
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories",categories);
        return "admin/products/add";
    }

    @PostMapping("/add")
    public String forAddSubmit(@Valid @ModelAttribute Product product,
                               BindingResult bindingResult,
                               MultipartFile file,
                               RedirectAttributes redirectAttributes) throws IOException {
        //Model is for category
        if(bindingResult.hasErrors()){
            return "admin/products/add";
        }
        //I think there's another better way of doing this. TODO
        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if(filename.endsWith("jpg") || filename.endsWith("png")){
            fileOK = true;
        }

        redirectAttributes.addFlashAttribute("message","Product added");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");

        String slug = product.getName().toLowerCase().replace(" ","-");
        Category slugExists = productRepo.findBySlug(slug);
        if(! fileOK){
            redirectAttributes.addFlashAttribute("message","Image must be a jpg or png");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
        }else if(slugExists != null){
            redirectAttributes.addFlashAttribute("message","Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
           // redirectAttributes.addFlashAttribute("product",product);
        }else{
            product.setSlug(slug);
            product.setImage(filename);
            productRepo.save(product);

            //save image to local
            Files.write(path, bytes);

        }

        return "redirect:/admin/products/add";
    }
}
