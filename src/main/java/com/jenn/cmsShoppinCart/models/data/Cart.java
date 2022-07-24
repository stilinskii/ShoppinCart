package com.jenn.cmsShoppinCart.models.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cart {
    private int id;
    private String name;
    private String price;
    private int quantity;
    private String image;

}
