package cz.hocuspocus.coffeeblog.models.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        logger.info("Email via contact form was sent.");

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            // Pokud dojde k chybě při odesílání e-mailu, můžete zde provést příslušné akce, např. logování
            e.printStackTrace();
        }
    }

    public void sendPasswordResetEmail(String userEmail, String token){
        String subject = "Bažant na pekáči - obnova hesla";
        String resetUrl = "http://localhost:8080/account/reset-password?token=" + token;
        String message = "K obnovení hesla k Vašemu účtu klikněte na následující odkaz: \n" + resetUrl;

        sendEmail(userEmail, subject, message);

    }

}
