package com.softito.projectmanagement.Controllers;

import com.softito.projectmanagement.Models.Project;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProjectController {

    //createproject
    @GetMapping("/createprojectpage")
    public String createprojectpage() {return "createproject";}
    //createproject


}
