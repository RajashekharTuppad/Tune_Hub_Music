package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;


@Controller
public class PaymentController {
    @Autowired
    UsersService service;

    @GetMapping("/payment")
    public String pay(Model model, HttpSession session) {
        // Check if the user is already a premium user
        String userEmail = (String) session.getAttribute("email");
        if (service.isUserPremium(userEmail)) {
            // User is already a premium user, redirect to admin home
            return "redirect:/adminHome";
        }

        return "payment";
    }

    @GetMapping("/payment-success")
    @Configuration
    @EnableTransactionManagement
    @Transactional
    public String paymentSuccess(HttpSession session, RedirectAttributes redirectAttributes) {
        String mail = (String) session.getAttribute("email");
        Users u = service.getUser(mail);
        u.setPremium(true); // Set premium to true
        service.updateUser(u);

        // Redirect to login page after payment success
        redirectAttributes.addFlashAttribute("paymentSuccess", true);
        return "redirect:/login";
    }




    @GetMapping("/payment-failure")
    public String paymentFailure() {
        return "customerHome";
    }

    @SuppressWarnings("finally")
    @PostMapping("/createOrder")
    @ResponseBody
    public String createOrder() {
        int amount = 5000;
        Order order = null;
        try {
            RazorpayClient razorpay = new RazorpayClient("rzp_test_B65PTXe3fUYAqN", "8OCeQzspjTavmSKNNGmfaRhn");
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            order = razorpay.orders.create(orderRequest);
        } catch (RazorpayException e) {
            e.printStackTrace();
        } finally {
            return order.toString();
        }
    }

    @PostMapping("/verify")
    @ResponseBody
    public boolean verifyPayment(@RequestParam String orderId, @RequestParam String paymentId, @RequestParam String signature) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient("rzp_test_B65PTXe3fUYAqN", "8OCeQzspjTavmSKNNGmfaRhn");
            String verificationData = orderId + "|" + paymentId;
            boolean isValidSignature = Utils.verifySignature(verificationData, signature, "8OCeQzspjTavmSKNNGmfaRhn");
            return isValidSignature;
        } catch (RazorpayException e) {
            e.printStackTrace();
            return false;
        }
    }
}
