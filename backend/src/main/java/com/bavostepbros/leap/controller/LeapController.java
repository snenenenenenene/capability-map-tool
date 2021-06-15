package com.bavostepbros.leap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LeapController {
    
    /** 
     * @return String
     */
    @GetMapping("/")
    public String index(){
        return  "redirect:/authorize";
    }


    
    /** 
     * @return String
     */
    @GetMapping("/authorize")
    public String AuthorizationPage(){
        return "authorize";
    }
}