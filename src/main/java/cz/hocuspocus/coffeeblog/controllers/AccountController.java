package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.data.entities.PasswordResetTokenEntity;
import cz.hocuspocus.coffeeblog.data.entities.UserEntity;
import cz.hocuspocus.coffeeblog.data.repositories.PasswordResetTokenRepository;
import cz.hocuspocus.coffeeblog.models.dto.ForgotPasswordDTO;
import cz.hocuspocus.coffeeblog.models.dto.LoggedUserDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import cz.hocuspocus.coffeeblog.models.exceptions.*;
import cz.hocuspocus.coffeeblog.models.services.EmailService;
import cz.hocuspocus.coffeeblog.models.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("login")
    public String renderLogin(HttpServletRequest request){
        // Geeting referer URL
        String referer = request.getHeader("Referer");
        // Saving to session
        request.getSession().setAttribute("prevPage", referer);
        // Showing login form
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

    @GetMapping("forgotpassword")
    public String showForgotPasswordForm(@ModelAttribute ForgotPasswordDTO forgotPasswordDTO) {
        return "pages/account/forgotpassword";
    }

    @PostMapping("forgotpassword")
    public String processForgotPasswordForm(@RequestParam("email") String userEmail, ForgotPasswordDTO forgotPasswordDTO, BindingResult result, Model model) {
        UserEntity user = null;
        try {
            user = userService.findUserByEmail(userEmail);
        } catch (Exception e) {
            System.out.println("Exception occurred while finding user by email: " + e.getMessage());
        }

        // Přidáme logování pro kontrolu
        if (user == null) {
            System.out.println("User not found for email: " + userEmail);
        } else {
            System.out.println("User found: " + user.toString());
        }

        if (result.hasErrors()) {
            return showForgotPasswordForm(forgotPasswordDTO);
        }

        if (user == null) {
            // User doesn't exist, but success message will be shown
            model.addAttribute("success", "Email na obnovu hesla byl úspěšně odeslán.");
            return "pages/account/forgotpassword";
        } else {
            // Check, if there is a existing token for the user
            Optional<PasswordResetTokenEntity> existingToken = passwordResetTokenRepository.findByUser(user);
            if (existingToken.isPresent()) {
                // Token exists and will be deleted, new one will be created
                userService.deleteCurrentToken(existingToken.get().getId());
                String token = UUID.randomUUID().toString();
                userService.createPasswordResetTokenForUser(user, token);
                emailService.sendPasswordResetEmail(userEmail, token);
            } else {
                // Creating new token
                String token = UUID.randomUUID().toString();
                userService.createPasswordResetTokenForUser(user, token);
                emailService.sendPasswordResetEmail(userEmail, token);
            }

            model.addAttribute("success", "Email na obnovu hesla byl úspěšně odeslán.");
        }

        return "pages/account/forgotpassword";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Validation of the token
        if (!userService.isValidPasswordResetToken(token)) {
            // If the token is invalid, error page will be shown
            return "redirect:/account/token-expired";
        }

        model.addAttribute("token", token);
        return "/pages/account/resetpassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword, RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            // Hesla se neshodují, zobrazte chybovou zprávu a přesměrujte zpět na formulář
            redirectAttributes.addFlashAttribute("error", "Passwords do not match.");
            return "redirect:/reset-password?token=" + token;
        }

        try {
            userService.resetPassword(token, password);
            redirectAttributes.addFlashAttribute("success", "Your password has been successfully reset.");
            return "redirect:/account/login"; // Upravte podle potřeby
        } catch (InvalidTokenException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/reset-password?token=" + token;
        }
    }

    @GetMapping("/token-expired")
    public String tokenExpired() {
        return "pages/account/tokenexpired";
    }

}
