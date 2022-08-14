package com.jenn.cmsShoppinCart;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("home");
//    }

    //이걸 안해주면 사진을 올렸을때 서버 껐다키지않으면 사진을 못불러온다.
    //사진이 추가되었지만 static은 정적인 애들이 있는곳이라서 바로바로 불러올 수 없음.
    //프로젝트 밖에 위치하면 안해도 되지만 안에 위치했을땐 이렇게해주면 됨.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/media/**")
                .addResourceLocations("file:/C:/Users/hayeong/Desktop/Spring/Spring projects/cmsShoppinCart/src/main/resources/static/media/");
    }
}
