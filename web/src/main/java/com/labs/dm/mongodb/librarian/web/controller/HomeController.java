/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */

package com.labs.dm.mongodb.librarian.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author daniel
 */
@Controller
public class HomeController {
    
    @RequestMapping({"home.do"})
    public ModelAndView homePage(Model model) {
        //model.addAttribute("msg", "Hello!");
        System.out.println(model.getClass());
        
        return new ModelAndView("index");
    }
    
    @RequestMapping({"jquery.do"})
    public ModelAndView jquery(Model model) {
        return new ModelAndView("jquery");
    }    
}
