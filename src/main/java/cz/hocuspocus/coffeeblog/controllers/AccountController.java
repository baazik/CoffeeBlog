package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import cz.hocuspocus.coffeeblog.models.exceptions.DuplicateEmailException;
import cz.hocuspocus.coffeeblog.models.exceptions.PasswordsDoNotEqualException;
import cz.hocuspocus.coffeeblog.models.services.UserService;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

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

        try {
            userService.create(userDTO, false);
        } catch (DuplicateEmailException e){
            result.rejectValue("email","error","Email is already used.");
            return "/pages/account/register";
        } catch (PasswordsDoNotEqualException e){
            result.rejectValue("password","error", "Passwords are not matched.");
            result.rejectValue("confirmPassword","error", "Passwords are not matched.");
            return "/pages/account/register";
        }

        redirectAttributes.addFlashAttribute("success","User was successfully registred.");
        return "redirect:/account/login";
    }

}
