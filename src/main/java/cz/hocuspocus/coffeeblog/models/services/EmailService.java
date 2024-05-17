package cz.hocuspocus.coffeeblog.models.services;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

    void sendPasswordResetEmail(String userEmail, String token);

}
