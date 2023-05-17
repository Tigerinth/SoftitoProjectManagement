package com.softito.projectmanagement.Controllers;

import com.softito.projectmanagement.Models.User;
import com.softito.projectmanagement.Repostitories.UserRepository;
import com.softito.projectmanagement.Services.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRepositoryService userRepositoryService;

    Long sessionid;
    @GetMapping("/")
    public String loginPanel() {
        return "index";
    }

    /*
    @GetMapping("/usermainpanel/user={uname}")
    public String userMainPanel(Model model, @PathVariable("uname") String username) {
        User newuser = new User();
        for (User user : userRepositoryService.getAll()) {
            if (user.getUsername().equals(username)) {
                sessionid = user.getId();
                newuser = userRepository.getById(user.getId());
            }
        }
        model.addAttribute("username", newuser.getUsername());
        model.addAttribute("email",newuser.getEmail());
        return "usermainpanel";
    }*/

    @GetMapping("/notThisUserError")
    public String notthisusererrorpage(Model model){
        model.addAttribute("sessionid",sessionid);
        return "notThisUserError";
    }
    @GetMapping("/register")
    public String getRegister(){
        return "register";
    }
    @GetMapping("/createproject")
    public String createprojectpage(){return "createproject";}
    @GetMapping("/invite2project")
    public String invitepage(){return  "invite";}
    @GetMapping("/add2project")
    public String inviteuserpage(){return  "adduser";}
    @GetMapping("/addtask2project")
    public String addtaskpage() {return "addtasktoproject";}
    @GetMapping("/addrisk")
    public String addRiskpage() {return "addrisk";}
}
