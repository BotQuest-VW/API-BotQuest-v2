package com.botquest.BotQuestAPI.repositories;

import com.botquest.BotQuestAPI.models.ChamadoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChamadoRepository extends JpaRepository<ChamadoModel, UUID> {
}