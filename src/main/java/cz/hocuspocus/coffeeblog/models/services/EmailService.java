package cz.hocuspocus.coffeeblog.models.services;

public interface EmailService {

    public void sendEmail(String to, String subject, String text);

}
