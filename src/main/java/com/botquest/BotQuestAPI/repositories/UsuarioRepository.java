package com.botquest.BotQuestAPI.repositories;

import com.botquest.BotQuestAPI.models.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, UUID> {
    UserDetails findByEmail(String email);
}