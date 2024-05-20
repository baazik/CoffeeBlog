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

        if (user == null) {
            // Uživatel neexistuje, ale chceme mu zobrazit zprávu o úspěchu
            model.addAttribute("success", "Email na obnovu hesla byl úspěšně odeslán.");
            return "pages/account/forgotpassword";
        }

        if (result.hasErrors()) {
            return showForgotPasswordForm(forgotPasswordDTO);
        }

        if (user == null) {
            // Uživatel neexistuje, ale chceme mu zobrazit zprávu o úspěchu
            model.addAttribute("success", "Email na obnovu hesla byl úspěšně odeslán.");
            return "pages/account/forgotpassword";
        } else {
            // Kontrola, zda pro daného uživatele již existuje platný token
            Optional<PasswordResetTokenEntity> existingToken = passwordResetTokenRepository.findByUser(user);
            if (existingToken.isPresent()) {
                // Existuje platný token, můžete buď ten existující token použít nebo jej smazat a vytvořit nový
                userService.deleteCurrentToken(existingToken.get().getId());
                String token = UUID.randomUUID().toString();
                userService.createPasswordResetTokenForUser(user, token);
                emailService.sendPasswordResetEmail(userEmail, token);
            } else {
                // Vytvoření nového tokenu
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
        // Validace tokenu, např. ověření jeho platnosti a existence v databázi
        if (!userService.isValidPasswordResetToken(token)) {
            // Pokud je token neplatný, přesměrujte na stránku s chybovou zprávou nebo formulářem pro žádost o nový token
            return "redirect:/error"; // Upravte podle potřeby
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
            // Volání servisní metody pro resetování hesla
            userService.resetPassword(token, password);
            // Úspěšné resetování hesla, přesměrujte uživatele na stránku s potvrzením
            redirectAttributes.addFlashAttribute("success", "Your password has been successfully reset.");
            return "redirect:/account/login"; // Upravte podle potřeby
        } catch (InvalidTokenException e) {
            // Chyba při ověřování tokenu, zobrazte chybovou zprávu a přesměrujte zpět na formulář
            redirectAttributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/reset-password?token=" + token;
        }
    }

}
