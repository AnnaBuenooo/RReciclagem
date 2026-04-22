package br.com.fiap.rreciclagem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF para APIs REST
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos de autenticação
                        .requestMatchers("/auth/**").permitAll()

                        // Endpoints públicos de consulta (ex: pontos de coleta)
                        .requestMatchers(HttpMethod.GET, "/api/pontos-coleta/**").permitAll()

                        // Endpoint de alerta de container (deve ser acessível por IoT, talvez com outra segurança - API Key)
                        // Por simplicidade, vamos permitir, mas em prod isso teria uma camada de segurança de máquina-para-máquina
                        .requestMatchers(HttpMethod.PUT, "/api/containers/**").permitAll()

                        // Endpoint de Admin (demonstrando restrição de ROLE)
                        .requestMatchers("/api/notificacoes/admin/**").hasRole("ADMIN")

                        // Todos os outros endpoints exigem autenticação
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sessão stateless
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT

        return http.build();
    }
}