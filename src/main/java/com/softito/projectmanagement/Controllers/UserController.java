package com.softito.projectmanagement.Controllers;

import com.softito.projectmanagement.Models.Project;
import com.softito.projectmanagement.Models.User;
import com.softito.projectmanagement.Repostitories.ProjectRepository;
import com.softito.projectmanagement.Repostitories.UserRepository;
import com.softito.projectmanagement.Services.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class UserController {
    @Autowired
    UserRepositoryService userRepositoryService;
    @Autowired
    ProjectRepository projectRepository;

    Long sessionid;


    /*              LOGIN                */
    @PostMapping("/login")
    public String userLoginPost(Model model, RedirectAttributes redirAttrs,@RequestParam("username") String username, @RequestParam("password") String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for(User user : userRepositoryService.getAll()){
            if (user.getUsername().matches(username) && passwordEncoder.matches(password, user.getPassword())) {
                sessionid = user.getId();
                System.out.println("suan bulundugunuz kullanicinin id si = " + sessionid + " dir");
                model.addAttribute("session",sessionid);
                return "redirect:/usermainpanel/user="+user.getUsername();
            }
        }
        redirAttrs.addFlashAttribute("error", "Böyle bir kullanıcı bulunamadı!");
        return "redirect:/";
    }
    /*              LOGIN                */


    /*              REGISTER                */
    @PostMapping("/register")
    public String userRegisterPost(Model model, RedirectAttributes redirAttrs, @RequestParam("lName") String lName, @RequestParam("fName") String fName, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,@RequestParam("username") String userName) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        if (password.equals(confirmPassword)) {
            if (fName != null && lName != null && password != null && email != null) {
                try {
                    for(User user : userRepositoryService.getAll()){
                        if(user.getEmail().equals(email) || user.getUsername().equals(userName)){
                            redirAttrs.addFlashAttribute("error", "e-posta veya kullanıcı adı zaten var.");
                            return "redirect:/register";
                        }
                    }
                } catch (Exception ex) {
                    redirAttrs.addFlashAttribute("error", "hata");
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
        User newUser = new User(email, fName, lName, encodedPassword,userName);
        userRepositoryService.add(newUser);
        return "redirect:/";
    }

    /*              REGISTER                */

    @PostMapping("/createproj")
    public String createproject(Model model,
                                @RequestParam("ProjectName") String projectName,
                                String managerMail,
                                @RequestParam("ProjectDesc") String projectDesc){

        if (sessionid <= 0 || sessionid == null){
            System.out.println("giris yapilmamis");
            return "redirect:/register";
        }
        //managerMail = User  BURADA KALDIM
        for(User user : userRepositoryService.getAll()){
            if(user.getEmail().matches(managerMail) && user.getEmail().matches()){
                System.out.println("böyle bir mail var");
                Project yeniproje = new Project(projectName,projectDesc,managerMail,true);
                projectRepository.save(yeniproje);
                return "redirect:/usermainpanel";
            }
        }
        System.out.println("mail bulunamadı");
        return "redirect:/usermainpanel";
    }

    @GetMapping("/projectdetails/project={projectid}")
    public String projectpage(Model model, @PathVariable("projectid")Long id){
        Project project = projectRepository.getById(id);
        model.addAttribute("projectdescription",project.getProjectDesc());
        model.addAttribute("managermail",project.getManagerMail());
        model.addAttribute("projectname",project.getProjectName());
        return "projectdetails";
    }

}
