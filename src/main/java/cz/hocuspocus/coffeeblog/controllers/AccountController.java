package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.LoggedUserDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import cz.hocuspocus.coffeeblog.models.exceptions.DuplicateEmailException;
import cz.hocuspocus.coffeeblog.models.exceptions.InvalidPasswordException;
import cz.hocuspocus.coffeeblog.models.exceptions.PasswordsDoNotEqualException;
import cz.hocuspocus.coffeeblog.models.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    public String renderLogin(HttpServletRequest request){
        // Získání původní URL
        String referer = request.getHeader("Referer");
        // Uložení do session
        request.getSession().setAttribute("prevPage", referer);
        // Zobrazení přihlašovacího formuláře
        return  "/pages/account/login";
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

    @GetMapping("changepassword")
    public String renderChangePassword(@ModelAttribute LoggedUserDTO userDTO){
        return "/pages/account/changepassword";
    }

    @PostMapping("changepassword")
    public String changePassword(
            @Valid @ModelAttribute LoggedUserDTO userDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return renderChangePassword(userDTO);
        }

        try {
            System.out.println("Attempting to change password");
            userService.changePassword(userDTO);
            System.out.println("Password changed successfully");
        } catch (PasswordsDoNotEqualException e) {
            System.out.println("Passwords do not match");
            result.rejectValue("password", "error", "Passwords do not match.");
            result.rejectValue("confirmPassword", "error", "Passwords do not match.");
            return "/pages/account/changepassword";
        } catch (InvalidPasswordException e) {
            System.out.println("Invalid current password");
            result.rejectValue("currentPassword", "error", "Invalid current password.");
            return "/pages/account/changepassword";
        }

        redirectAttributes.addFlashAttribute("success", "The password was successfully changed.");
        return "redirect:/account/login?logout";
    }

}
