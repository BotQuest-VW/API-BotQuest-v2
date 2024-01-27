package com.botquest.BotQuestAPI.controllers;

import com.botquest.BotQuestAPI.dtos.LoginDto;
import com.botquest.BotQuestAPI.dtos.TokenDto;
import com.botquest.BotQuestAPI.models.UsuarioModel;
import com.botquest.BotQuestAPI.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto dados) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((UsuarioModel) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new TokenDto(token));
    }
}

