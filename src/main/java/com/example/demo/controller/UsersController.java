package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;

@Controller
public class UsersController {
    @Autowired
    UsersService service;

    @PostMapping("/register")
    public String addUsers(@ModelAttribute Users user) {
        boolean userStatus = service.emailExists(user.getEmail());
        if (userStatus == false) {
            service.addUser(user);
            System.out.println("user added");
        } else {
            System.out.println("user already exists");
        }
        return "home";
    }

    @PostMapping("/validate")
    public String validate(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
        if (service.validateUser(email, password)) {
            String role = service.getRole(email);
            session.setAttribute("email", email);

            if (role.equals("admin")) {
                return "redirect:/adminHome";
            } else {
                // Check if the user is premium
                Users user = service.getUser(email);
                if (user.isPremium()) {
                    session.setAttribute("premiumUser", true);
                    return "redirect:/adminHome"; // Redirect to admin home if the user is premium
                }

                return "adminHome";
            }
        } else {
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }

    @GetMapping("/adminHome")
    public String adminHome(HttpSession session, Model model) {
        // Check if the user is a premium user
        Boolean isPremium = (Boolean) session.getAttribute("premiumUser");
        if (isPremium != null && isPremium) {
            // User is a premium user, redirect to admin home
            return "adminHome";
        }

        // The rest of your admin home logic...

        return "adminHome";
    }
    
    @PostMapping("/updateUser")
    public String updateUser(@RequestBody Users user) {
        UsersService.updateUser(user);
        return "User updated successfully!";
    }
}
