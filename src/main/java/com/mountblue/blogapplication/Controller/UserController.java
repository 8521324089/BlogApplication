package com.mountblue.blogapplication.Controller;

import com.mountblue.blogapplication.Model.User;
import com.mountblue.blogapplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            return "redirect:/";
        }
        model.addAttribute("user",new User());
        return "register";
    }

    @PostMapping("/register")
    public String createUser(@ModelAttribute User user,Model model){
        User savedUser = userService.createUser(user);
        if(savedUser.getId()==null){
            model.addAttribute("user",user);
            return "register";
        }else{
            model.addAttribute("user",savedUser);
            return "redirect:/login";
        }
    }
}
