package org.example.erpspring.controller.login;

import jakarta.servlet.http.HttpSession;
import org.example.erpspring.service.login.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/")
    public String index() {
        return "login/index";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        try {
            String message = loginService.login(username, password);
            model.addAttribute("message", message);
            return "redirect:/suppliers";
        } catch (Exception e) {
            model.addAttribute("error", "Username or password invalid");
            return "login/index";
        }
    }
    @GetMapping("/logout")
    public String logout(){
        loginService.logout();
        return "login/index";
    }
}