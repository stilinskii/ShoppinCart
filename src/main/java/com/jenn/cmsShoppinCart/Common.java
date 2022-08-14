package com.jenn.cmsShoppinCart;

import com.jenn.cmsShoppinCart.models.CategoryRepository;
import com.jenn.cmsShoppinCart.models.PageRepository;
import com.jenn.cmsShoppinCart.models.data.Cart;
import com.jenn.cmsShoppinCart.models.data.Category;
import com.jenn.cmsShoppinCart.models.data.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

//intercepter 적용. 다른 메서드전에 이거 호출
@Slf4j
@ControllerAdvice
public class Common {
    @Autowired
    private PageRepository pageRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @ModelAttribute
    public void shareData(Model model, HttpSession session, Principal principal){
        //principal - give a access to the loged-in user

        if(principal != null){
            model.addAttribute("user",principal.getName());
            log.info("principal={}",principal.getName());
        }


        List<Page> pages = pageRepo.findAll();

        List<Category> categories = categoryRepo.findAll();

        boolean cartActive = false;

        if(session.getAttribute("cart")!=null){
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
            int size = 0;
            double total=0;

            for(Cart value : cart.values()){
                size += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble(value.getPrice());
            }

            model.addAttribute("csize",size);
            model.addAttribute("ctotal",total);

            cartActive = true;
        }

        model.addAttribute("cpages",pages);
        model.addAttribute("ccategories",categories);
        model.addAttribute("cartActive",cartActive);
    }
}
