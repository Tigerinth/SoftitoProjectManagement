package com.softito.projectmanagement.Controllers;

import com.softito.projectmanagement.Models.Project;
import com.softito.projectmanagement.Models.Risk;
import com.softito.projectmanagement.Models.Task;
import com.softito.projectmanagement.Models.User;
import com.softito.projectmanagement.Repostitories.ProjectRepository;
import com.softito.projectmanagement.Repostitories.RiskRepository;
import com.softito.projectmanagement.Repostitories.UserRepository;
import com.softito.projectmanagement.Services.ProjectRepositoryService;
import com.softito.projectmanagement.Services.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    UserRepositoryService userRepositoryService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectRepositoryService projectRepositoryService;
    @Autowired
    RiskRepository riskRepository;

    Long sessionid;
    Long selectedproject;


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
    @GetMapping("/usermainpanel/user={uname}")
    public String userMainPanel(Model model, @PathVariable("uname") String username) {
        User loggeduser = userRepository.getById(sessionid);
        boolean isCurrentUser = false;
        for (User user : userRepositoryService.getAll()) {
            if (user.getUsername().equals(username)) {
                if(user.getUsername().equals(loggeduser.getUsername())){
                    isCurrentUser = true;
                    break;
                }
            }
        }
        if (isCurrentUser) {
            List<Project> userProjects = loggeduser.getProjects();
            model.addAttribute("projects", userProjects);
            model.addAttribute("username", loggeduser.getUsername());
            model.addAttribute("email", loggeduser.getEmail());
            return "usermainpanel";
        } else {
            System.out.println("Oturumdaki kullanıcıyla uyumlu değil");
            return "redirect:/notThisUserError"; //buradanda kendi sayfasina gecer
        }
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

                //proje kimlik denemesi
                BCryptPasswordEncoder idEncoder = new BCryptPasswordEncoder();
                String encodedid = idEncoder.encode(user.getId().toString());

                Project yeniproje = new Project(projectName,projectDesc,managerMail,encodedid,true);
                List<Project> projects = user.getProjects();
                projects.add(yeniproje);
                //yeniproje.setUser(managerperson);
                //yeniproje.setManagerMail(managerMail);
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

    /*
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
        System.out.println("bahsi geçen proje: "+ project.getProjectName());
        List<Project> inviteduserprojects = inviteduser.getProjects();
        try {
            inviteduserprojects.add(project);
            inviteduser.setProjects(inviteduserprojects);
            userRepository.save(inviteduser);
            System.out.println("basarili");
        } catch (Exception e) {
            System.out.println("Proje eklenirken bir hata oluştu: " + e.getMessage());
            // Hata durumuna göre yapılacak işlemler
            // Örneğin, hata mesajını kullanıcıya göstermek için bir hata sayfasına yönlendirme yapabilirsiniz.
            return "redirect:/error";
        }
        return "redirect:/usermainpanel/user="+user.getUsername();
    }
    */



    @GetMapping("/user-projects")
    public String showUserProjects(Model model) {
        if (sessionid == null || sessionid <= 0) {
            System.out.println("Giriş yapılmamış");
            return "redirect:/";
        }

        User user = userRepository.getById(sessionid);
        List<Project> userProjects = user.getProjects();
        for (Project project : userProjects) {
            System.out.println("Proje Adı: " + project.getProjectName());
            System.out.println("Açıklama: " + project.getProjectDesc());
            System.out.println("-----");
        }

        model.addAttribute("projects", userProjects);

        return "userprojects";
    }


    @PostMapping("/createproject")
    public String createProject(Model model,
                                @RequestParam("ProjectName") String projectName,
                                @RequestParam("ProjectDesc") String projectDesc,
                                @RequestParam("managerMail") String managerMail) {

        if (sessionid == null || sessionid <= 0) {
            System.out.println("Giriş yapılmamış");
            return "redirect:/";
        }

        User manager = userRepository.findByEmail(managerMail);

        if (manager == null) {
            System.out.println("Hatalı kullanıcı");
            return "redirect:/";
        }

        // Proje kimlik oluşturma
        BCryptPasswordEncoder idEncoder = new BCryptPasswordEncoder();
        String encodedId = idEncoder.encode(UUID.randomUUID().toString());

        Project project = new Project(projectName, projectDesc, managerMail, encodedId, true);
        List<User> users = new ArrayList<>();
        users.add(manager);
        project.setUsers(users);
        projectRepository.save(project);

        System.out.println("Proje başarıyla oluşturuldu.");

        return "redirect:/usermainpanel/user=" + manager.getUsername();
    }

    @PostMapping("/adduser")
    public String addUserToProject(Model model,
                                   @RequestParam("projectId") Long projectId,
                                   @RequestParam("managerMail") String managerMail,
                                   @RequestParam("userMail") String userMail) {
        if (sessionid == null || sessionid <= 0) {
            System.out.println("giriş yapılmamış");
            return "redirect:/";
        }

        User manager = userRepository.findByEmail(managerMail);
        User userToAdd = userRepository.findByEmail(userMail);
        Project project = projectRepository.findById(projectId).orElse(null);

        if (manager == null || userToAdd == null || project == null) {
            System.out.println("Hatalı kullanıcı, proje veya proje kimliği");
            return "redirect:/";
        }

        if (!manager.getEmail().equals(project.getManagerMail())) {
            System.out.println("Kullanıcı proje yöneticisi değil");
            return "redirect:/";
        }

        List<Project> userProjects = userToAdd.getProjects();
        userProjects.add(project);
        userToAdd.setProjects(userProjects);

        List<User> projectUsers = project.getUsers();
        projectUsers.add(userToAdd);
        project.setUsers(projectUsers);

        projectRepository.save(project);
        userRepository.save(userToAdd);

        System.out.println("Kullanıcı projeye başarıyla eklendi.");
        return "redirect:/projectdetails/project=" + project.getId();
    }

    @GetMapping("/projectdetails/project={projectid}")
    public String projectpage(Model model, @PathVariable("projectid")Long id){
        User loggeduser = userRepository.getById(sessionid);
        Project project = projectRepository.getById(id);
        if (sessionid == null || sessionid <= 0) {
            System.out.println("giriş yapılmamış");
            return "redirect:/";
        }

        if (!project.getUsers().contains(loggeduser)) {
            System.out.println("Bu projeye yoksunuz");
            return "redirect:/usermainpanel/user=" + loggeduser.getUsername();
        }
        selectedproject = project.getId();
        System.out.println(selectedproject + " id li proje secildi");
        model.addAttribute("projectdescription", project.getProjectDesc());
        model.addAttribute("managermail", project.getManagerMail());
        model.addAttribute("loggedmail", loggeduser.getEmail());
        model.addAttribute("projectname", project.getProjectName());
        model.addAttribute("projectusers", project.getUsers());
        model.addAttribute("tasklist", project.getTasks());
        model.addAttribute("risklist",project.getRisks());

        if (project.getManagerMail().equals(loggeduser.getEmail())) {
            model.addAttribute("showDeleteButtons", true);
        } else {
            model.addAttribute("showDeleteButtons", false);
        }

        return "projectdetails";
    }

    @PostMapping("/addtask")
    public String addtask(Model model,
                          @RequestParam("taskName") String taskName,
                          @RequestParam("taskDescription") String taskDescription,
                          @RequestParam("taskDuration") int taskDuration) {

        Project project = projectRepository.getById(selectedproject);
        if (project == null) {
            System.out.println("Hatalı proje kimliği");
            return "redirect:/";
        }
        System.out.println("secilen proje: "+project.getProjectName());
        Task newTask = new Task(taskName, taskDescription, taskDuration,false);
        project.getTasks().add(newTask);
        projectRepository.save(project);
        System.out.println("Görev projeye başarıyla eklendi.");

        return "redirect:/projectdetails/project="+project.getId();
    }

    @PostMapping("/addrisk")
    public String addRiskToProject(@RequestParam("projectId") Long projectId,
                                   @RequestParam("managerMail") String managerMail,
                                   @RequestParam("riskDescription") String riskDescription) {
        // Giriş yapılmış kullanıcının oturum bilgilerini kontrol et
        if (sessionid == null || sessionid <= 0) {
            System.out.println("Giriş yapılmamış");
            return "redirect:/";
        }

        // Proje ve kullanıcıyı veritabanından al
        User loggedUser = userRepository.getById(sessionid);
        Project project = projectRepository.getById(projectId);

        // Kullanıcının proje yöneticisi olup olmadığını kontrol et
        if (!loggedUser.getEmail().equals(managerMail)) {
            System.out.println("Kullanıcı proje yöneticisi değil");
            return "redirect:/";
        }

        Risk risk = new Risk();
        risk.setDescription(riskDescription);

        List<Risk> risks = project.getRisks();
        risks.add(risk);
        project.setRisks(risks);
        projectRepository.save(project);

        System.out.println("Risk başarıyla eklendi.");
        return "redirect:/project/" + projectId;
    }
    @GetMapping("/deleterisk/{riskIndex}")
    public String deleteRiskFromProject(@PathVariable Long riskIndex) {
        if (sessionid == null || sessionid <= 0) {
            System.out.println("Giriş yapılmamış");
            return "redirect:/";
        }

        User loggedUser = userRepository.getById(sessionid);
        Project project = projectRepository.getById(selectedproject);
        Risk risk = riskRepository.getById(riskIndex);

        if (!loggedUser.getEmail().equals(project.getManagerMail())) {
            System.out.println("Kullanıcı proje yöneticisi değil");
            return "redirect:/";
        }

        List<Risk> risks = project.getRisks();
            risks.remove(risk);
            projectRepository.save(project);
            System.out.println("Risk başarıyla silindi.");

        return "redirect:/projectdetails/project=" + project.getId();
    }

    @GetMapping("/deleteteamuser/{userId}")
    public String deleteTeamUser(@PathVariable("userId") Long userId) {
        User loggedUser = userRepository.getById(sessionid);
        User userToDelete = userRepository.getById(userId);
        Project project = projectRepository.getById(selectedproject);

        if (loggedUser.getEmail().equals(project.getManagerMail())) {
            // Proje yöneticisi, takım üyesini silebilir
            project.getUsers().remove(userToDelete);
            projectRepository.save(project);
            System.out.println("Takım üyesi başarıyla silindi.");
        } else {
            System.out.println("Kullanıcı proje yöneticisi değil");
        }

        return "redirect:/projectdetails/project=" + selectedproject;
    }


}
