package com.jenn.cmsShoppinCart.controllers;

import com.jenn.cmsShoppinCart.models.CategoryRepository;
import com.jenn.cmsShoppinCart.models.ProductRepository;
import com.jenn.cmsShoppinCart.models.data.Category;
import com.jenn.cmsShoppinCart.models.data.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductsController {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model, @RequestParam(value = "page", required = false) Integer p){

        int perPage = 3;
        int page = (p !=null) ? p : 0;
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Product> products = productRepo.findAll(pageable);
        List<Category> categories = categoryRepo.findAll();

        HashMap<Integer, String> cats = new HashMap<>();
        for (Category category : categories) {
            cats.put(category.getId(),category.getName());
        }

        model.addAttribute("products",products);
        model.addAttribute("cats",cats);

        long count = productRepo.count();
        double pageCount = Math.ceil((double) count/(double) perPage);

        model.addAttribute("pageCount",(int)pageCount); // end page
        model.addAttribute("perPage",perPage);
        model.addAttribute("count",count);
        model.addAttribute("page",page); // start page

        return "admin/products/index";
    }

    //Edit이랑 add랑 같은 controller쓰게하려 했는데 분리하는 게 낫다는 판단. 지금은?
//    @GetMapping("/form")
//    public String add(@ModelAttribute Product product, @RequestParam(required = false) Integer id, Model model){
//        //Model is for category
//        List<Category> categories = categoryRepo.findAll();
//
//        log.info("product={}",product);
//        //for edit
//        if(id!=null){
//            Product productToBeEdited = productRepo.findById(id).orElse(null);
//            //model.addAttribute("OriginalProductName",productToBeEdited.getName());
//            model.addAttribute("product",productToBeEdited);
//        }
//        model.addAttribute("categories",categories);
//
//        return "admin/products/add";
//    }

    @GetMapping("/add")
    public String add(Product product, Model model) {

        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        return "admin/products/add";
    }

    @PostMapping("/add")
    public String forAddSubmit(@Valid @ModelAttribute Product product,
                               BindingResult bindingResult,
                               MultipartFile file,
                               RedirectAttributes redirectAttributes,
                               Model model) throws IOException {
        //Model is for category
        if(bindingResult.hasErrors()){
            //bindingResult.reject("product");
            List<Category> categories = categoryRepo.findAll();
            model.addAttribute("categories",categories);
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
        Product slugExists = productRepo.findBySlug(slug);
        if(! fileOK){
            redirectAttributes.addFlashAttribute("message","Image must be a jpg or png");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("product",product);

        }else if(slugExists != null){
            redirectAttributes.addFlashAttribute("message","Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("product",product);
        }else{
            product.setSlug(slug);
            product.setImage(filename);
            productRepo.save(product);

            //save image to local
            Files.write(path, bytes);

        }

        return "redirect:/admin/products/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Product product = productRepo.getOne(id);
        List<Category> categories = categoryRepo.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "admin/products/edit";
    }


    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("product") Product product,
                               BindingResult bindingResult,
                               MultipartFile file,
                               RedirectAttributes redirectAttributes,
                               Model model) throws IOException {


        Product currentProduct = productRepo.getOne(product.getId());
        List<Category> categories = categoryRepo.findAll();

        //Model is for category
        if(bindingResult.hasErrors()){
            //bindingResult.reject("product");
            model.addAttribute("productName",currentProduct.getName());
            model.addAttribute("categories",categories);
            return "admin/products/edit";
        }


        //I think there's other better way of doing this. TODO
        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        //수정할 이미지를 넣었을 경우
        if(!file.isEmpty()){
            if(filename.endsWith("jpg") || filename.endsWith("png")){
                fileOK = true;
            }
        } else {
            fileOK=true;
        }


        redirectAttributes.addFlashAttribute("message","Product edited");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");

        String slug = product.getName().toLowerCase().replace(" ","-");
        //다른아이디로 이미 같은 이름의 slug가 있는지 확인
        Product slugExists = productRepo.findBySlugAndIdNot(slug,product.getId());
        if(! fileOK){
            redirectAttributes.addFlashAttribute("message","Image must be a jpg or png");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("product",product);

        }else if(slugExists != null){
            redirectAttributes.addFlashAttribute("message","Product exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass","alert-danger");
            redirectAttributes.addFlashAttribute("product",product);
        }else{

            //성공로직
            product.setSlug(slug);

            //delete the current image if new image is inputed
            if(!file.isEmpty()){
                Path pathOld = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
                Files.delete(pathOld);
                product.setImage(filename);
                //save image to local
                Files.write(path, bytes);
            }else{
                product.setImage(currentProduct.getImage());
            }

            productRepo.save(product);



        }

        return "redirect:/admin/products/edit/" + product.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {
        Product one = productRepo.getOne(id);
        Path path = Paths.get("src/main/resources/static/media/" + one.getImage());
        Files.delete(path);
        productRepo.deleteById(id);

        redirectAttributes.addFlashAttribute("message","Page deleted");
        redirectAttributes.addFlashAttribute("alertClass","alert-success");

        return "redirect:/admin/products";
    }
}
