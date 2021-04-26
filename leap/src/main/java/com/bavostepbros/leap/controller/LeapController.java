package com.bavostepbros.leap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LeapController {
    @GetMapping("/")
    public String index(){
        return  "redirect:/authorize";
    }


    @GetMapping("/authorize")
    public String AuthorizationPage(){
        return "authorize";
    }
}