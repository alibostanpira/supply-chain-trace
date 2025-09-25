package dev.abpira.sct.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Manufacturer endpoints
                        .requestMatchers(HttpMethod.POST, "/api/products").hasRole("MANUFACTURER")

                        // Logistics endpoints
                        .requestMatchers(HttpMethod.POST, "/api/movement").hasRole("LOGISTICS")

                        // Auditor endpoints
                        .requestMatchers(HttpMethod.GET, "/api/products/*").hasRole("AUDITOR")
                        .requestMatchers(HttpMethod.GET, "/api/movement/*").hasRole("AUDITOR")

                        // Read-only access for all authenticated users
                        .requestMatchers(HttpMethod.GET, "/api/products").authenticated()

                        // Secure everything else
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                        .disable()
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails manufacturer = User.withUsername("manufacturer")
                .password("{noop}manufacturer123")
                .roles("MANUFACTURER")
                .build();

        UserDetails logistics = User.withUsername("logistics")
                .password("{noop}logistics123")
                .roles("LOGISTICS")
                .build();

        UserDetails auditor = User.withUsername("auditor")
                .password("{noop}auditor123")
                .roles("AUDITOR")
                .build();

        return new InMemoryUserDetailsManager(manufacturer, logistics, auditor);
    }
}
