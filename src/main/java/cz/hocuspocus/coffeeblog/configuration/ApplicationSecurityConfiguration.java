package cz.hocuspocus.coffeeblog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationSecurityConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers("/articles/create", "/articles/edit/**", "/articles/delete/**")
                .authenticated()
                .requestMatchers(
                        "/styles/**", "/images/**", "/scripts/**", "/fonts/**",
                        "/articles", "/", "/skills", "/references", "/contact",
                        "/account/register"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/account/login") // Login URL
                .loginProcessingUrl("/account/login") // Login URL
                .defaultSuccessUrl("/articles", true) // Redirect after login
                .usernameParameter("email") // Login will be via email
                .permitAll() // `/account/login` will be allowed even for non logged users
                .and()
                .logout()
                .logoutUrl("/account/logout") // Logout URL
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
