package com.botquest.BotQuestAPI.repositories;

import com.botquest.BotQuestAPI.models.PerguntaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PerguntaRepository extends JpaRepository<PerguntaModel, UUID> {
}