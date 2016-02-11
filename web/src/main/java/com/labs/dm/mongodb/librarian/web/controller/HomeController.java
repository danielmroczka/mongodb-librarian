/*
 * Copyright daniel.mroczka@gmail.com. All rights reserved. 
 */

package com.labs.dm.mongodb.librarian.web.controller;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author daniel
 */
@Controller
public class HomeController {
    
    @RequestMapping({"home.do"})
    public ModelAndView homePage(Model model) {
        return new ModelAndView("index");
    }
    
    @RequestMapping("/detect-device.do")
    public @ResponseBody String detectDevice(Device device) {
        String deviceType = "unknown";
        if (device.isNormal()) {
            deviceType = "normal";
        } else if (device.isMobile()) {
            deviceType = "mobile";
        } else if (device.isTablet()) {
            deviceType = "tablet";
        }
        return "Hello " + deviceType + " browser!";
    }
   
}
