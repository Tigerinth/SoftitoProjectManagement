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

import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserRepositoryService userRepositoryService;
    @Autowired
    UserRepository userRepository;
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

        if (sessionid == null || sessionid <= 0) {
            System.out.println("giris yapilmamis");
            return "redirect:/";
        }

        User managerperson = userRepository.getById(sessionid);
        managerMail = managerperson.getEmail();
        for(User user : userRepositoryService.getAll()){
            if(user.getEmail().matches(managerMail)){
                System.out.println("boyle bir mail yani kullanici var");

                //proje kimlik olusturma
                BCryptPasswordEncoder idEncoder = new BCryptPasswordEncoder();
                String encodedid = idEncoder.encode(user.getId().toString());

                Project yeniproje = new Project(projectName,projectDesc,managerMail,encodedid,true);
                List<Project> projects = user.getProjects();
                projects.add(yeniproje);
                yeniproje.setUser(managerperson);
                user.setProjects(projects);
                projectRepository.save(yeniproje);
                userRepository.save(user);
                System.out.println("projeyi olusturdu");
                return "redirect:/usermainpanel/user="+ managerperson.getUsername();
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
    @PostMapping("/invite")
    public String inviteproject(Model model,@RequestParam("inviteid")String inviteid,@RequestParam("usermail")String usermail){
        if (sessionid == null || sessionid <= 0) {
            System.out.println("giris yapilmamis");
            return "redirect:/";
        }
        User user = userRepository.getById(sessionid);
        System.out.println(sessionid);
        User inviteduser = userRepository.findByEmail(usermail);
        System.out.println("buldu davet edilen kullanici: " +inviteduser.getUsername());
        Project project = projectRepository.findByInviteid(inviteid);
        List<Project> inviteduserprojects = inviteduser.getProjects();
        inviteduserprojects.add(project);
        inviteduser.setProjects(inviteduserprojects);
        userRepository.save(inviteduser);
        return "redirect:/usermainpanel/user="+user.getUsername();
    }

    @GetMapping("/user-projects")
    public String showUserProjects(Model model) {
        if (sessionid == null || sessionid <= 0) {
            System.out.println("Giriş yapılmamış");
            return "redirect:/";
        }

        User user = userRepository.getById(sessionid);
        List<Project> userProjects = user.getProjects();

        model.addAttribute("projects", userProjects);

        return "userprojects";
    }

}
