package com.jenn.cmsShoppinCart.controllers;

import com.jenn.cmsShoppinCart.models.ProductRepository;
import com.jenn.cmsShoppinCart.models.data.Cart;
import com.jenn.cmsShoppinCart.models.data.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/cart")
//@SuppressWarnings("unchecked")
public class CartController {

    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id,
                      HttpSession session,
                      Model model,
                      @RequestParam(value = "cartPage",required = false) String cartPage){

        Product product = productRepo.getById(id);
        if(session.getAttribute("cart")==null){
            //장바구니가 없을때
            HashMap<Integer, Cart> cart = new HashMap<>();
            cart.put(id,new Cart(id,product.getName(), product.getPrice(), 1, product.getImage()));
            session.setAttribute("cart",cart);
        }else{
            //장바구니 있을때
            HashMap<Integer,Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
            if(cart.containsKey(id)){
                //장바구니에 똑같은 상품이 이미 존재할때
                //이미 있는걸 override
                int qty = cart.get(id).getQuantity();
                cart.put(id,new Cart(id,product.getName(), product.getPrice(), ++qty, product.getImage()));
            }else {
                //장바구니에 똑같은 상품이 없을때
                cart.put(id,new Cart(id,product.getName(), product.getPrice(), 1, product.getImage()));
                session.setAttribute("cart",cart);
            }
        }

        //size랑 total이 필요하다

        //cart view 를위해 데이터 보내기
        HashMap<Integer,Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
        int size = 0;
        double total=0;

        for(Cart value : cart.values()){
            size += value.getQuantity();
            total += value.getQuantity() * Double.parseDouble(value.getPrice());
        }

        model.addAttribute("size",size);
        model.addAttribute("total",total);

        //cart html에 a에 파라미터랑 이거 왜 한거임?
        if(cartPage != null){
            return "redirect:/cart/view";
        }


        return "cart_view";
    }

    @GetMapping("/subtract/{id}")
    public String subtract(@PathVariable int id, HttpSession session, Model model, HttpServletRequest httpServletRequest){

        Product product = productRepo.getOne(id);
        HashMap<Integer,Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
        int qty = cart.get(id).getQuantity();
        if(qty==1){
            cart.remove(id);
            if(cart.size()==0){
                session.removeAttribute("cart");
            }
        }else{
            cart.put(id,new Cart(id,product.getName(), product.getPrice(), --qty, product.getImage()));
        }

        model.addAttribute("cart",cart);

        String refererLink = httpServletRequest.getHeader("referer");
        log.info("referlInk={}",refererLink);//http://localhost:8081/cart/view 뭐지 이렇게되면 의미없지않나

        return "redirect:"+refererLink;

    }


    @RequestMapping("/view")
    public String view(HttpSession session, Model model){

        if(session.getAttribute("cart")==null){
            return "redirect:/";
        }
        HashMap<Integer,Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");
        model.addAttribute("cart",cart);
        model.addAttribute("notCartViewPage",true);

        return "cart";
    }


}
