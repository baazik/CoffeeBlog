package cz.hocuspocus.coffeeblog.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoggedUserDTO {

    private String email;

    @NotNull(message = "Insert a password")
    @NotBlank(message = "Insert a password")
    @Size(min = 6, message = "The password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Insert a password")
    @NotBlank(message = "Insert a password")
    @Size(min = 6, message = "The password must be at least 6 characters long")
    private String confirmPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
