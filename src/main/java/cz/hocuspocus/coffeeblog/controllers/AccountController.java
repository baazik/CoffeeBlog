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
import jakarta.transaction.Transactional;
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

    @GetMapping("forgotpassword")
    public String showForgotPasswordForm(@ModelAttribute ForgotPasswordDTO forgotPasswordDTO) {
        return "pages/account/forgotpassword";
    }

    @PostMapping("forgotpassword")
    public String processForgotPasswordForm(@RequestParam("email") String userEmail, ForgotPasswordDTO forgotPasswordDTO, BindingResult result, Model model) {
        UserEntity user = userService.findUserByEmail(userEmail);

        if (result.hasErrors()) {
            return showForgotPasswordForm(forgotPasswordDTO);
        }

        // Kontrola, zda pro daného uživatele již existuje platný token
        Optional<PasswordResetTokenEntity> existingToken = passwordResetTokenRepository.findByUser(user);
        if (existingToken.isPresent()) {
            System.out.println("Token existuje");
            System.out.println("Zobrazuji token" + existingToken.get().getToken());
            // Existuje platný token, můžete buď ten existující token použít nebo jej smazat a vytvořit nový
            userService.deleteCurrentToken(existingToken.get().getId());
            System.out.println("Token byl smazán");
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            emailService.sendPasswordResetEmail(userEmail, token);
            System.out.println("Token byl smazán, vytvořil se nový a email se odeslal.");
        } else {
            // Vytvoření nového tokenu
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            emailService.sendPasswordResetEmail(userEmail, token);
            System.out.println("Byl vytvořen nový token a email se odeslal.");
        }

       model.addAttribute("success", "Email na obnovu hesla byl úspěšně odeslán.");

        return "pages/account/forgotpassword";
    }

    @GetMapping("resetpassword")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (userService.validatePasswordResetToken(token)) {
            model.addAttribute("token", token);
            return "/pages/account/updatepassword";
        } else {
            return "redirect:/account/login?invalidToken";
        }
    }

    @PostMapping("resetpassword")
    public String processResetPasswordForm(@RequestParam("token") String token,
                                           @RequestParam("password") String password) {
        if (userService.validatePasswordResetToken(token)) {
            userService.resetPassowrd(token,password);
            return "redirect:/account/login?resetPasswordSuccess";
        } else {
            return "redirect:/account/login?invalidToken";
        }
    }

}
