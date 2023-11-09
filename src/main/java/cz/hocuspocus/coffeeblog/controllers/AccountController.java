package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/account")
public class AccountController {

    @GetMapping("login")
    public String renderLogin(){
        return "/pages/account/login";
    }

    @GetMapping("register")
    public String renderRegister(@ModelAttribute UserDTO userDTO){
        return "/pages/account/register";
    }

    @PostMapping("register")
    public String register(
            @Valid @ModelAttribute UserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes
            ){

        if (result.hasErrors()){
            return renderRegister(userDTO);
        }

        // later on, there will be a call of a service method here

        redirectAttributes.addFlashAttribute("success","User was successfully registred.");
        return "redirect:/account/login";
    }

}
