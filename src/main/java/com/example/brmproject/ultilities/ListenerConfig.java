//package com.example.brmproject.ultilities;
//
//import com.example.brmproject.service.BookDetailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class ListenerConfig {
//
//    private  BookDetailService bookDetailService;
//
//    @Autowired
//    public ListenerConfig(BookDetailService bookDetailService) {
//        this.bookDetailService = bookDetailService;
//    }
//
//    @Bean
//    public ServletListenerRegistrationBean<SessionDestroyListener> sessionListenerBean() {
//        return new ServletListenerRegistrationBean<>(new SessionDestroyListener(bookDetailService));
//    }
//
//
//}
