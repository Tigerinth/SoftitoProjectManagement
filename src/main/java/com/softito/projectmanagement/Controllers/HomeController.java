package com.softito.projectmanagement.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String loginPanel() {
        return "index";
    }
    @GetMapping("/usermainpanel")
    public String userMainPanel(){
        return "usermainpanel";
    }
}
