package cz.hocuspocus.coffeeblog.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactFormDTO {

    @NotBlank(message = "Please provide your name")
    private String name;

    @Email(message = "Please provide a valid email address")
    @NotBlank(message = "Please provide your email address")
    private String email;

    @NotBlank(message = "Please provide a subject")
    private String subject;

    @NotBlank(message = "Please provide your message")
    private String message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
