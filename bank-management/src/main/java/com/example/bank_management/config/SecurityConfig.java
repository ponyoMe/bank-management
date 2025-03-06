package com.example.bank_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/styles.css/**", "/js/**", "/google-icon.png").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // now matches "ROLE_ADMIN"
                        .requestMatchers("/dashboard").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process-login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                                response.sendRedirect("/my_accounts/");
                            }
                        })

                        .failureUrl("/auth/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


