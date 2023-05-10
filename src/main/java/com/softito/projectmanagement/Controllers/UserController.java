package com.softito.projectmanagement.Controllers;

import com.softito.projectmanagement.Models.User;
import com.softito.projectmanagement.Repostitories.UserRepository;
import com.softito.projectmanagement.Services.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class UserController {
    @Autowired
    UserRepositoryService userRepositoryService;

    @PostMapping("login")
    public String userLoginPost(Model model, RedirectAttributes redirAttrs,@RequestParam("email") String email, @RequestParam("password") String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for(User user : userRepositoryService.getAll()){
            if (user.getEmail().matches(email) && passwordEncoder.matches(password, user.getPassword())) {
                return "redirect:/homepage";
            }
        }
        redirAttrs.addFlashAttribute("error", "Böyle bir kullanıcı bulunamadı!");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String getRegister(){
        return "register";
    }
    @PostMapping("/register")
    public String userRegisterPost(Model model, RedirectAttributes redirAttrs, @RequestParam("lName") String lName, @RequestParam("fName") String fName, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        if (password.equals(confirmPassword)) {
            if (fName != null && lName != null && password != null && email != null) {
                try {
                    User user = new User(email, fName, lName, encodedPassword);
                    userRepositoryService.add(user);
                    return "redirect:/login";
                } catch (Exception ex) {
                    redirAttrs.addFlashAttribute("error", "Böyle bir kullanıcı zaten var!");
                    return "redirect:/register";
                }
            } else {
                redirAttrs.addFlashAttribute("error", "Bir Hata Oluştu!");
                return "redirect:/register";
            }
        } else {
            redirAttrs.addFlashAttribute("error", "Şifreler Uyuşmadı!");
            return "redirect:/register";
        }
    }
}
