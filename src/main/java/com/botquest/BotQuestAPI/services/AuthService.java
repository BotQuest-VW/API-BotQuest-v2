package com.botquest.BotQuestAPI.services;

import com.botquest.BotQuestAPI.repositories.UsuarioRepository; // Importa a classe UsuarioRepository para acessar os dados do usuário
import org.springframework.beans.factory.annotation.Autowired; // Importa a anotação @Autowired para injeção de dependências
import org.springframework.security.core.userdetails.UserDetails; // Importa a interface UserDetails do Spring Security
import org.springframework.security.core.userdetails.UserDetailsService; // Importa a interface UserDetailsService do Spring Security
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa a exceção UsernameNotFoundException do Spring Security
import org.springframework.stereotype.Service; // Importa a anotação @Service para marcar a classe como um serviço

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = usuarioRepository.findByEmail(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado!");
        }

        return usuario;
    }
}
