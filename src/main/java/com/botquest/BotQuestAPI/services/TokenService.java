package com.botquest.BotQuestAPI.services;

import com.auth0.jwt.JWT; // Importa a classe JWT do Auth0 para lidar com tokens JWT
import com.auth0.jwt.algorithms.Algorithm; // Importa a classe Algorithm do Auth0 para escolher o algoritmo de assinatura JWT
import com.auth0.jwt.exceptions.JWTCreationException; // Importa a exceção JWTCreationException do Auth0 para lidar com erros na criação de tokens JWT
import com.botquest.BotQuestAPI.models.UsuarioModel; // Importa a classe UsuarioModel para acessar informações do usuário
import org.springframework.beans.factory.annotation.Value; // Importa a anotação @Value do Spring para acessar valores das propriedades
import org.springframework.stereotype.Service; // Importa a anotação @Service do Spring para marcar a classe como um serviço

import java.time.Instant; // Importa a classe Instant para lidar com data/hora
import java.time.LocalDateTime; // Importa a classe LocalDateTime para lidar com data/hora
import java.time.ZoneOffset; // Importa a classe ZoneOffset para definir o fuso horário

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(UsuarioModel usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("api-botquest")
                    .withSubject(usuario.getEmail())
                    .withClaim("nomeUsuario", usuario.getNome())
                    .withClaim("idUsuario", usuario.getId().toString())
                    .withExpiresAt(gerarValidadeToken())
                    .sign(algoritmo);


            return token;
        } catch(JWTCreationException exception) {
            throw new RuntimeException("Erro na criação do token: ", exception);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.require(algoritmo)
                    .withIssuer("api-botquest")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch(JWTCreationException exception) {
            return "";
        }
    }

    public Instant gerarValidadeToken() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
