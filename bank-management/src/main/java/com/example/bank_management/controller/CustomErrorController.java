//package com.example.bank_management.controller;
//
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//
//@Controller
//public class CustomErrorController implements ErrorController {
//
////    @RequestMapping("/error")
////    public String handleError(HttpServletRequest request) {
////        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
////
////        if (status != null) {
////            int statusCode = Integer.parseInt(status.toString());
////
////            if (statusCode == HttpStatus.FORBIDDEN.value()) {
////                return "error/403"; // Shows 403.html
////            }
////        }
////        return "error/default";
////    }
//
//    @GetMapping("/403")
//    public String accessDenied() {
//        return "403"; // Direct access to 403 page
//    }
//}
//
