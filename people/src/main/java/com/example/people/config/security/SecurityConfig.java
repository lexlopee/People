package com.example.people.config.security;

import com.example.people.config.jwt.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Actuator
                        .requestMatchers("/actuator/**").permitAll()

                        // Auth
                        .requestMatchers("/api/auth/**").permitAll()

                        // Imagenes subidas
                        .requestMatchers("/uploads/**").permitAll()

                        // Categorias: GET publico, POST solo administrador y creador
                        .requestMatchers(HttpMethod.GET, "/api/categorias").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categorias")
                        .hasAnyAuthority("ROLE_administrador", "ROLE_creador")

                        // Campanas: GET publico
                        .requestMatchers(HttpMethod.GET, "/api/campanias").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/campanias/**").permitAll()

                        // Crear campana y subir imagen: creador o administrador
                        // Usamos hasAnyAuthority con ROLE_ prefix para evitar problemas
                        .requestMatchers(HttpMethod.POST, "/api/campanias/crear")
                        .hasAnyAuthority("ROLE_administrador", "ROLE_creador")
                        .requestMatchers(HttpMethod.POST, "/api/campanias/*/imagen")
                        .hasAnyAuthority("ROLE_administrador", "ROLE_creador")

                        // Resto autenticado
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:5173"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}