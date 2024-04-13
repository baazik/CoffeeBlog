package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.ContactFormDTO;
import cz.hocuspocus.coffeeblog.models.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {

    @Value("${spring.mail.recipient}")
    private String recipient;

    @Autowired
    private EmailService emailService;

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contactForm", new ContactFormDTO());
        return "pages/home/contact";
    }

    @PostMapping("/contact")
    public String submitContactForm(@ModelAttribute("contactForm") ContactFormDTO contactForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "pages/home/contact";
        }

        // Získání dat z formuláře
        String to = recipient; // Adresa příjemce (můžete změnit podle vašich potřeb)
        String subject = contactForm.getSubject();
        String text = "From: " + contactForm.getName() + " <" + contactForm.getEmail() + ">\n\n" + contactForm.getMessage();

        // Odeslání e-mailu
        emailService.sendEmail(to, subject, text);

        return "pages/home/contact";
    }

}
