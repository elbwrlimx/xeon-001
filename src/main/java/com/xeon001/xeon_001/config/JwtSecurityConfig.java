package com.xeon001.xeon_001.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration      // Diz pro Spring: "essa classe tem configurações importantes"
@EnableWebSecurity  // Ativa a segurança web do Spring
public class JwtSecurityConfig {

    // BEAN 1: Codificador de senha
    @Bean  // @Bean = Spring vai criar e gerenciar esse objeto pra gente
    public PasswordEncoder passwordEncoder() {
        // BCrypt é um algoritmo super seguro pra criptografar senhas
        // Exemplo: "123456" vira algo como "$2a$10$abcd1234..."
        return new BCryptPasswordEncoder();
    }

    // BEAN 2: Gerenciador de autenticação (quem vai verificar se login tá certo)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        // Esse cara vai ser responsável por verificar username + senha
        return authConfig.getAuthenticationManager();
    }

    // BEAN 3: Configuração das regras de segurança (MAIS IMPORTANTE!)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF (Cross-Site Request Forgery)
            // Como vamos usar JWT, não precisamos dessa proteção
            .csrf(csrf -> csrf.disable())
            
            // AQUI É ONDE DEFINIMOS AS REGRAS: quem pode acessar o quê
            .authorizeHttpRequests(authz -> authz
                // ROTAS PÚBLICAS (liberadas para todos, sem precisar de login)
                .requestMatchers("/teste").permitAll()          // Seu endpoint original
                .requestMatchers("/public/**").permitAll()      // Tudo que começar com /public/
                .requestMatchers("/auth/**").permitAll()        // Tudo que começar com /auth/ (login, registro)
                
                // ROTAS PRIVADAS (só com token JWT válido)
                .requestMatchers("/private/**").authenticated() // Tudo que começar com /private/
                
                // TODAS AS OUTRAS ROTAS (por enquanto liberadas, depois mudamos)
                .anyRequest().permitAll()  // Qualquer outra rota = liberada
            )
            
            // Configuração de sessão: STATELESS
            // Isso significa: não vamos guardar nada no servidor sobre o usuário
            // Tudo vai estar no token JWT que o cliente manda
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build(); // Constrói e retorna a configuração pronta
    }
}
