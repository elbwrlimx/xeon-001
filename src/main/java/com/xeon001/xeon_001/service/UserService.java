package com.xeon001.xeon_001.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service // Diz pro Spring: "essa classe contém lógica de negócio"
public class UserService implements UserDetailsService {
    // UserDetailsService = interface obrigatória do Spring Security
    // Ela tem 1 método: loadUserByUsername() que DEVE ser implementado

    @Autowired // Spring vai injetar automaticamente o passwordEncoder aqui
    private PasswordEncoder passwordEncoder;
    
    // SIMULANDO UM BANCO DE DADOS em memória
    // Na vida real, isso seria uma tabela no MySQL, PostgreSQL, etc.
    private Map<String, String> users = new HashMap<>();
    // Formato: {"admin" -> "senhasCriptografadas", "user" -> "senhasCriptografadas"}

    // Método para criar usuários (só para teste)
    public void createUser(String username, String password) {
        // Criptografa a senha antes de guardar
        // "123456" vira algo como "$2a$10$N.zmdr9k7uOIW8aBz0xjVuBinh.."
        users.put(username, passwordEncoder.encode(password));
        System.out.println("Usuário criado: " + username);
    }
    
    // MÉTODO OBRIGATÓRIO: Spring Security chama esse método toda vez que alguém tenta fazer login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Se ainda não tem usuários, cria alguns para teste
        if (users.isEmpty()) {
            createUser("admin", "elber");    // Login: admin / Senha: elber  
            createUser("user", "password");   // Login: user  / Senha: password
            System.out.println("Usuários de teste criados!");
        }
        
        // Busca a senha criptografada do usuário
        String password = users.get(username);
        
        // Se não encontrou o usuário, lança exceção
        if (password == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        
        // Retorna um objeto User do Spring Security
        // Parâmetros: (username, password, authorities/permissões)
        // new ArrayList<>() = sem permissões especiais por enquanto
        return new User(username, password, new ArrayList<>());
    }
}